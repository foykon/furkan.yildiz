import { api, endpoints } from "../lib/api";

const TOKEN_KEY = "mp_access_token";
let __currentUserId;

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY);
}

export function setToken(token, remember) {
  // temizle
  localStorage.removeItem(TOKEN_KEY);
  sessionStorage.removeItem(TOKEN_KEY);
  if (!token) return;
  (remember ? localStorage : sessionStorage).setItem(TOKEN_KEY, token);
}

export async function login({ username, password, remember }) {
  const { data } = await api.post(endpoints.auth.login, { username, password });
  const token = data?.data?.token;
  if (!token) throw new Error(data?.message || "Token alınamadı");
  setToken(token, remember);
  return data.data;
}

export async function logout() {
  try { await api.post(endpoints.auth.logout); } catch {}
  setToken(null, true);
}

function parseJwt(token){
  try{
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const json = decodeURIComponent(
      atob(base64).split("").map(c => "%"+("00"+c.charCodeAt(0).toString(16)).slice(-2)).join("")
    );
    return JSON.parse(json);
  }catch{ return null; }
}

export function getPrincipal(){
  const t = getToken();
  if(!t) return null;
  const p = parseJwt(t) || {};
  const roles = p.roles || p.authorities || p.scopes || [];
  const username = p.username || p.sub || p.user_name || null;
  const userId = p.userId || p.uid || p.id || null;
  const arr = Array.isArray(roles) ? roles : [];
  const isAdmin = arr.includes("ROLE_ADMIN");
  return { username, userId, roles: arr, isAdmin };
}

export async function getCurrentUserId() {
  if (__currentUserId) return __currentUserId;
  const p = getPrincipal();
  if (p?.userId) { __currentUserId = p.userId; return __currentUserId; }
  if (!p?.username) return null;
  const params = { "filter.username": p.username, "pageable.page": 0, "pageable.size": 1 };
  const { data } = await api.get(endpoints.users.search, { params });
  __currentUserId = data?.data?.[0]?.id ?? null;
  return __currentUserId;
}
