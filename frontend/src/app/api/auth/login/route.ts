import { NextRequest, NextResponse } from 'next/server';
import { getIronSession } from 'iron-session';
import { decodeJwt } from 'jose';
import { sessionOptions, SessionData } from '@/lib/session';

export async function POST(request: NextRequest) {
  const { email, password } = await request.json();

  const params = new URLSearchParams();
  params.append('grant_type', 'password');
  params.append('client_id', process.env.KEYCLOAK_CLIENT_ID!);
  params.append('username', email);
  params.append('password', password);

  const keycloakResponse = await fetch(
    `${process.env.KEYCLOAK_URL}/realms/${process.env.KEYCLOAK_REALM}/protocol/openid-connect/token`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params,
    },
  );

  if (!keycloakResponse.ok) {
    return NextResponse.json(
      { message: 'Invalid email or password' },
      { status: 401 },
    );
  }

  const tokens = await keycloakResponse.json();

  const payload = decodeJwt(tokens.access_token);

  const res = NextResponse.json({
    companyId: payload.company_id,
    email: payload.email,
  });

  const session = await getIronSession<SessionData>(
    request,
    res,
    sessionOptions,
  );
  session.accessToken = tokens.access_token;
  session.refreshToken = tokens.refresh_token;
  session.companyId = payload.company_id as string;
  session.email = payload.email as string;
  session.expiresAt = (payload.exp as number) * 1000;
  await session.save();

  return res;
}
