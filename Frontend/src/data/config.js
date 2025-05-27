const isDev = import.meta.env.DEV;

export const API_BASE_URL = isDev
  ? 'http://localhost:8080'
  : 'https://backend-nnsh.onrender.com';
