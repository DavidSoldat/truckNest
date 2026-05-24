import axios from 'axios';

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
        window.location.href = '/login';
        return Promise.reject(error);
      }
    }

    if (error.response?.status === 401 && originalRequest._retry) {
      window.location.href = '/login';
    }

    return Promise.reject(error);
  },
);

export default api;
