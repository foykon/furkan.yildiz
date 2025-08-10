import api from '../lib/api';

export async function searchMovies({ title }, { page = 0, size = 10, sort = 'releaseDate,desc' } = {}) {
  const params = { page, size, sort };
  if (title) params.title = title;
  const { data } = await api.get('/movies', { params });
  return data;
}

export async function getMovie(id) {
  const { data } = await api.get(`/movies/${id}`);
  return data?.data || data; // MovieResponse
}

export async function updateMovie(id, payload) {
  const { data } = await api.put(`/movies/${id}`, payload);
  return data?.data || data; // MovieResponse
}

// sözlükler
export async function listDirectors() {
  const { data } = await api.get('/directors', { params: { page: 0, size: 200 } });
  return data?.data || [];
}
export async function listGenres() {
  const { data } = await api.get('/genres', { params: { page: 0, size: 200 } });
  return data?.data || [];
}
export async function listLanguages() {
  const { data } = await api.get('/languages', { params: { page: 0, size: 200 } });
  return data?.data || [];
}
export async function listCountries() {
  const { data } = await api.get('/countries', { params: { page: 0, size: 200 } });
  return data?.data || [];
}
