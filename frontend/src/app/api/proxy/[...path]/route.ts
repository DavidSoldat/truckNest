import { NextRequest, NextResponse } from 'next/server';
import { getIronSession } from 'iron-session';
import { sessionOptions, SessionData } from '@/lib/session';

async function handler(
  request: NextRequest,
  { params }: { params: Promise<{ path: string[] }> },
) {
  const res = NextResponse.next();
  const session = await getIronSession<SessionData>(
    request,
    res,
    sessionOptions,
  );

  if (!session.accessToken) {
    return NextResponse.json({ message: 'Unauthorized' }, { status: 401 });
  }

  const isExpiringSoon = session.expiresAt - Date.now() < 30_000;

  if (isExpiringSoon) {
    const refreshRes = await fetch(
      new URL('/api/auth/refresh', request.url).toString(),
      {
        method: 'POST',
        headers: { cookie: request.headers.get('cookie') || '' },
      },
    );
    if (!refreshRes.ok) {
      return NextResponse.json({ message: 'Session expired' }, { status: 401 });
    }
  }

  const { path } = await params;
  const pathString = path.join('/');
  const url = `${process.env.API_URL}/api/v1/${pathString}${request.nextUrl.search}`;

  const body =
    request.method !== 'GET' && request.method !== 'HEAD'
      ? await request.text()
      : undefined;

  const springResponse = await fetch(url, {
    method: request.method,
    headers: {
      Authorization: `Bearer ${session.accessToken}`,
      'Content-Type': 'application/json',
    },
    body,
  });

  const data = await springResponse.text();

  return new NextResponse(data, {
    status: springResponse.status,
    headers: { 'Content-Type': 'application/json' },
  });
}

export const GET = handler;
export const POST = handler;
export const PUT = handler;
export const PATCH = handler;
export const DELETE = handler;
