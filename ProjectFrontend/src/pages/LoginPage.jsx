import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../lib/api';
import './login.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const [username, setU] = useState('');
  const [password, setP] = useState('');
  const [showPw, setShowPw] = useState(false);
  const [remember, setRemember] = useState(true); // remember me varsayılan açık
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const valid = username.trim().length >= 3 && password.length >= 6;

  async function onSubmit(e) {
    e.preventDefault();
    if (!valid || loading) return;
    setError(null); setLoading(true);
    try {
      // Backend: POST /api/auth/login -> { success, data: { token, user, ... } }
      const { data } = await api.post('/api/auth/login', { username, password });
      const resp = data?.data || data;
      const token = resp?.token;
      if (!token) throw new Error('Token alınamadı');

      if (remember) {
        localStorage.setItem('access_token', token);
        if (resp?.refreshToken) localStorage.setItem('refresh_token', resp.refreshToken);
      } else {
        sessionStorage.setItem('access_token', token);
        if (resp?.refreshToken) sessionStorage.setItem('refresh_token', resp.refreshToken);
      }

      // Giriş başarılı → film listesine
      navigate('/movies', { replace: true });
    } catch (err) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Login failed';
      setError(msg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <div className="brand">🎬 Movie Portal</div>

        <h1 className="title">Hoş geldin</h1>
        <p className="subtitle">Devam etmek için hesabına giriş yap.</p>

        <form className="form" onSubmit={onSubmit}>
          <label className="field">
            <span>Kullanıcı adı</span>
            <input
              type="text"
              placeholder="furkan"
              value={username}
              onChange={(e)=>setU(e.target.value)}
              autoComplete="username"
              autoFocus
              required
            />
          </label>

          <label className="field">
            <span>Şifre</span>
            <div className="pw">
              <input
                type={showPw ? 'text' : 'password'}
                placeholder="••••••••"
                value={password}
                onChange={(e)=>setP(e.target.value)}
                autoComplete="current-password"
                required
              />
              <button
                type="button"
                className="pw-toggle"
                onClick={()=>setShowPw(s=>!s)}
                aria-label={showPw ? 'Şifreyi gizle' : 'Şifreyi göster'}
                title={showPw ? 'Şifreyi gizle' : 'Şifreyi göster'}
              >
                {showPw ? '🙈' : '👁️'}
              </button>
            </div>
          </label>

          <div className="row">
            <label className="check">
              <input
                type="checkbox"
                checked={remember}
                onChange={(e)=>setRemember(e.target.checked)}
              />
              <span>Beni hatırla</span>
            </label>
            {/* gelecekte şifre sıfırlama eklenecekse: <button className="link" type="button">Şifremi unuttum</button> */}
          </div>

          {error && <div className="error">{error}</div>}

          <button className="submit" type="submit" disabled={!valid || loading}>
            {loading ? 'Giriş yapılıyor…' : 'Giriş yap'}
          </button>

          <p className="hint">
            Min: kullanıcı adı 3+, şifre 6+ karakter. Hata alırsan CORS/endpoint’i kontrol et.
          </p>
        </form>
      </div>
    </div>
  );
}
