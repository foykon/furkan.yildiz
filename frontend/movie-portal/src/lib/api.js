import axios from "axios";
import qs from "qs";
import { getToken, setToken } from "../auth/auth";

const BASE_URL = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/+$/,"");

// ---- ENDPOINTS (v1) ----
export const endpoints = {
  auth: {
    login:  "/api/v1/auth/login",
    refresh:"/api/v1/auth/refresh",
    logout: "/api/v1/auth/logout",
  },
  ai: {
    comment: (movieId) => `/api/v1/movies/${movieId}/ai/comment`,
    stream:  (movieId) => `/api/v1/movies/${movieId}/ai/comment/stream`,
    clearCache: `/api/v1/ai/cache/comments`,
  },
  movies: {
    search: "/api/v1/movies",
    create: "/api/v1/movies",
    byId:   (id) => `/api/v1/movies/${id}`,
    update: (id) => `/api/v1/movies/${id}`,
    delete: (id) => `/api/v1/movies/${id}`,
    cast: {
      list:   (movieId)         => `/api/v1/movies/${movieId}/cast`,              // GET (list_1)
      add:    (movieId)         => `/api/v1/movies/${movieId}/cast`,              // POST (add)
      update: (movieId, castId) => `/api/v1/movies/${movieId}/cast/${castId}`,    // PUT  (update)
      delete: (movieId, castId) => `/api/v1/movies/${movieId}/cast/${castId}`,    // DELETE(delete)
    },
  },
  comments: {
    list:   (movieId)       => `/api/v1/movies/${movieId}/comments`,
    add:    (movieId)       => `/api/v1/movies/${movieId}/comments`,
    update: (movieId, id)   => `/api/v1/movies/${movieId}/comments/${id}`,
    delete: (movieId, id)   => `/api/v1/movies/${movieId}/comments/${id}`,
  },
  genres:    { list: "/api/v1/genres" },
  countries: { list: "/api/v1/countries" },
  directors: { list: "/api/v1/directors" },
  actors:    { list: "/api/v1/actors" },
  languages: { list: "/api/v1/languages" },
  lists: {
    me:       "/api/v1/lists/me",
    add:      (userId) => `/api/v1/lists/${userId}`,
    remove:   (userId) => `/api/v1/lists/${userId}`,
    contains: (userId) => `/api/v1/lists/${userId}/contains`,
    reorder:  (userId) => `/api/v1/lists/${userId}/reorder`,
    clear:    (userId) => `/api/v1/lists/${userId}/clear`,
  },
  users: {
    search: "/api/v1/users",
    get:    (id) => `/api/v1/users/${id}`,
    patch:  (id) => `/api/v1/users/${id}`,
    create: "/api/v1/users",
    delete: (id) => `/api/v1/users/${id}`,
  },
  roles: { list: "/api/v1/roles" },
};


// ---- axios ----
export const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  paramsSerializer: (params) =>
    qs.stringify(params, { indices: false, allowDots: true }),
});

// whitelist
const AUTH_WHITELIST = [
  endpoints.auth.login,
  endpoints.auth.refresh,
  endpoints.auth.logout,
];

// add token
api.interceptors.request.use((config) => {
  const token = getToken();
  if (token && !AUTH_WHITELIST.includes(config.url)) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// refresh
let isRefreshing = false;
let waiters = [];
const raw = axios.create({ baseURL: BASE_URL, withCredentials: true });

async function doRefresh() {
  const res = await raw.post(endpoints.auth.refresh);
  const newToken = res?.data?.data?.token;
  if (!newToken) throw new Error("No token on refresh");
  setToken(newToken, true);
  return newToken;
}
function notifyWaiters(token) {
  waiters.forEach((r) => r(token));
  waiters = [];
}

api.interceptors.response.use(
  (r) => r,
  async (err) => {
    const { response, config } = err || {};
    const status = response?.status;
    const url = config?.url;
    if (!config || AUTH_WHITELIST.includes(url)) return Promise.reject(err);

    if ((status === 401 || status === 403) && !config._retry) {
      config._retry = true;
      try {
        if (!isRefreshing) {
          isRefreshing = true;
          const token = await doRefresh();
          isRefreshing = false;
          notifyWaiters(token);
        } else {
          await new Promise((resolve) => waiters.push(resolve));
        }
        const token = getToken();
        config.headers = config.headers || {};
        config.headers.Authorization = `Bearer ${token}`;
        return api.request(config);
      } catch (e) {
        isRefreshing = false;
        waiters = [];
        setToken(null, true);
        if (window.location.pathname !== "/login") window.location.assign("/login");
        return Promise.reject(e);
      }
    }
    return Promise.reject(err);
  }
);
