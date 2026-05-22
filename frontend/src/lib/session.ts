import { SessionOptions } from 'iron-session';

export interface SessionData {
  accessToken: string;
  refreshToken: string;
  companyId: string;
  email: string;
  expiresAt: number;
}

export const sessionOptions: SessionOptions = {
  password: process.env.SESSION_SECRET as string,
  cookieName: 'trucknest-session',
  cookieOptions: {
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'lax',
    maxAge: 60 * 60 * 24 * 7, // 7 days
    path: '/',
  },
};
