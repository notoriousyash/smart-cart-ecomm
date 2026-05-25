import Cookies from 'js-cookie';

const API_BASE_URL = 'http://localhost:8080/api';

export const fetchApi = async (endpoint: string, options: RequestInit = {}) => {
  const token = Cookies.get('token');
  
  const headers = new Headers(options.headers || {});
  headers.set('Content-Type', 'application/json');
  
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || `API Error: ${response.status}`);
  }

  // Handle empty responses
  const text = await response.text();
  return text ? JSON.parse(text) : {};
};
