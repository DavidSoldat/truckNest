import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface AuthState {
  companyId: string | null;
  email: string | null;
  isAuthenticated: boolean;
  setAuth: (companyId: string, email: string) => void;
  clearAuth: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      companyId: null,
      email: null,
      isAuthenticated: false,
      setAuth: (companyId, email) =>
        set({ companyId, email, isAuthenticated: true }),
      clearAuth: () =>
        set({ companyId: null, email: null, isAuthenticated: false }),
    }),
    { name: 'trucknest-ui' },
  ),
);
