import { useEffect, useMemo, useState } from "react";
import { Tooltip, message } from "antd";
import { EyeOutlined, EyeFilled, HeartOutlined, HeartFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { api, endpoints } from "../lib/api";
import { getCurrentUserId } from "../auth/auth";
import "./movie-card.css";

export default function MovieCard({ movie }) {
  const nav = useNavigate();

  const abs = (u) => {
    if (!u) return null;
    if (/^https?:\/\//i.test(u)) return u;
    const base = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/+$/,'');
    return `${base}${u.startsWith("/") ? "" : "/"}${u}`;
  };

  const poster = abs(movie.posterUrl || movie.imageUrl) || "https://via.placeholder.com/300x450?text=Poster";
  const title  = movie.title || "Untitled";

  const rating = useMemo(() => {
    const pick = [movie.rating, movie.voteAverage, movie.imdbRating]
      .map(v => (typeof v === "string" ? parseFloat(v) : v))
      .find(v => Number.isFinite(v));
    return Number.isFinite(pick) ? pick.toFixed(1) : null;
  }, [movie]);

  const [userId, setUserId] = useState(null);
  const [inWatch, setInWatch] = useState(false);
  const [inFav, setInFav] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => { (async () => setUserId(await getCurrentUserId()))(); }, []);

  useEffect(() => {
    if (!userId || !movie?.id) return;
    const q = (type) =>
      api.get(endpoints.lists.contains(userId), { params: { movieId: movie.id, type } })
         .then(r => !!r.data?.data).catch(() => false);
    Promise.all([q("WATCH"), q("FAVORITE")]).then(([w,f]) => { setInWatch(w); setInFav(f); });
  }, [userId, movie?.id]);

  const toggle = async (type, e) => {
    e?.stopPropagation(); // butona basınca kartın tıklaması çalışmasın
    if (!userId) return;
    setLoading(true);
    try{
      const isOn = type === "WATCH" ? inWatch : inFav;
      if (isOn) {
        await api.delete(endpoints.lists.remove(userId), { params: { movieId: movie.id, type } });
      } else {
        await api.post(endpoints.lists.add(userId), { movieId: movie.id, type });
      }
      if (type === "WATCH") setInWatch(!isOn); else setInFav(!isOn);
    }catch(e){
      message.error(e?.response?.data?.message || "İşlem başarısız");
    }finally{ setLoading(false); }
  };

  const goDetail = () => {
    if (movie?.id) nav(`/movie/${movie.id}`);
  };

  return (
    <div className="movie-card" onClick={goDetail} role="button" tabIndex={0}
         onKeyDown={(e)=> e.key === "Enter" && goDetail()} style={{ cursor: "pointer" }}>
      <img src={poster} alt={title} className="movie-poster" loading="lazy" />

      <div className="movie-badges">
        {rating && <span className="badge rating">{rating}</span>}
        {movie.contentRating && <span className="badge cert">{movie.contentRating}</span>}
      </div>

      <div className="movie-overlay">
        <div className="movie-actions">
          <Tooltip title={inWatch ? "Remove from Watchlist" : "Add to Watchlist"}>
            <button className={`action ${inWatch ? "active" : ""}`} disabled={loading} onClick={(e) => toggle("WATCH", e)}>
              {inWatch ? <EyeFilled /> : <EyeOutlined />}
            </button>
          </Tooltip>
          <Tooltip title={inFav ? "Remove from Favorites" : "Add to Favorites"}>
            <button className={`action ${inFav ? "active" : ""}`} disabled={loading} onClick={(e) => toggle("FAVORITE", e)}>
              {inFav ? <HeartFilled /> : <HeartOutlined />}
            </button>
          </Tooltip>
        </div>
        <div className="movie-title">{title}</div>
      </div>
    </div>
  );
}
