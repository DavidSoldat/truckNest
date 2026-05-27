import axios from 'axios';
import { toast } from 'sonner';

const api = axios.create({
  baseURL: '/api/proxy',
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshRes = await axios.post('/api/auth/refresh');
        if (refreshRes.status === 200) {
          return api(originalRequest);
        }
      } catch {
        toast.error('Your session has expired. Please sign in again.');
        setTimeout(() => {
          window.location.href = '/login';
        }, 1500);
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  },
);

export default api;
