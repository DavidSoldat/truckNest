import { NextRequest, NextResponse } from 'next/server';
import { getIronSession } from 'iron-session';
import { decodeJwt } from 'jose';
import { sessionOptions, SessionData } from '@/lib/session';

export async function POST(request: NextRequest) {
  const res = NextResponse.json({ success: true });
  const session = await getIronSession<SessionData>(
    request,
    res,
    sessionOptions,
  );

  if (!session.refreshToken) {
    const clearRes = NextResponse.json(
      { message: 'No session' },
      { status: 401 },
    );
    clearRes.cookies.delete('trucknest-session');
    return clearRes;
  }

  const params = new URLSearchParams();
  params.append('grant_type', 'refresh_token');
  params.append('client_id', process.env.KEYCLOAK_CLIENT_ID!);
  params.append('refresh_token', session.refreshToken);

  const keycloakResponse = await fetch(
    `${process.env.KEYCLOAK_URL}/realms/${process.env.KEYCLOAK_REALM}/protocol/openid-connect/token`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params,
    },
  );

  if (!keycloakResponse.ok) {
    const clearRes = NextResponse.json(
      { message: 'Session expired' },
      { status: 401 },
    );
    clearRes.cookies.delete('trucknest-session');
    return clearRes;
  }

  const tokens = await keycloakResponse.json();
  const payload = decodeJwt(tokens.access_token);

  session.accessToken = tokens.access_token;
  session.refreshToken = tokens.refresh_token;
  session.companyId = payload.company_id as string;
  session.expiresAt = (payload.exp as number) * 1000;
  await session.save();

  return res;
}
