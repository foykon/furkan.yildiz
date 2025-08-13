// src/pages/MovieDetail.jsx
import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Comments from "../components/Comment.jsx";
import RowSection from "../components/RowSection.jsx";
import AiOpinion from "../components/AiOpinion.jsx"; // 👈 EKLENDİ
import {
  Row, Col, Typography, Tag, Space, Button, Tooltip,
  Skeleton, Divider, List, Card, Rate, message
} from "antd";
import {
  EyeOutlined, EyeFilled, HeartOutlined, HeartFilled, ArrowLeftOutlined
} from "@ant-design/icons";
import { api, endpoints } from "../lib/api";
import { getCurrentUserId } from "../auth/auth";
import "./movie-detail.css";

const { Title, Text, Paragraph } = Typography;

export default function MovieDetail() {
  const { id } = useParams();
  const nav = useNavigate();

  const [movie, setMovie] = useState(null);
  const [cast, setCast] = useState([]);
  const [loadingMovie, setLoadingMovie] = useState(true);
  const [loadingCast, setLoadingCast] = useState(true);

  const [userId, setUserId] = useState(null);
  const [inWatch, setInWatch] = useState(false);
  const [inFav, setInFav] = useState(false);
  const [toggling, setToggling] = useState(false);

  // Similar için state
  const [similarGenreId, setSimilarGenreId] = useState(null);

  // helpers
  const abs = (u) => {
    if (!u) return null;
    if (/^https?:\/\//i.test(u)) return u;
    const base = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/+$/,'');
    return `${base}${u.startsWith("/") ? "" : "/"}${u}`;
  };

  // movie detail
  useEffect(() => {
    let alive = true;
    setLoadingMovie(true);

    const url =
      endpoints?.movies?.byId?.(id) ||
      endpoints?.movies?.get?.(id) ||
      `/api/v1/movies/${id}`;

    api.get(url)
      .then(res => { if (alive) setMovie(res?.data?.data || null); })
      .finally(() => { alive && setLoadingMovie(false); });

    return () => { alive = false; };
  }, [id]);

  // similar genre id
  useEffect(() => {
    if (movie?.genres?.length) {
      setSimilarGenreId(movie.genres[0]?.id ?? null);
    } else {
      setSimilarGenreId(null);
    }
  }, [movie?.genres]);

  // cast
  useEffect(() => {
    let alive = true;
    setLoadingCast(true);

    api.get(`/api/v1/movies/${id}/cast`)
      .then(res => {
        if (!alive) return;
        const list = res?.data?.data || [];
        list.sort((a,b) => (a.castOrder ?? 0) - (b.castOrder ?? 0));
        setCast(list);
      })
      .finally(() => { alive && setLoadingCast(false); });

    return () => { alive = false; };
  }, [id]);

  // auth + contains
  useEffect(() => { (async () => setUserId(await getCurrentUserId()))(); }, []);
  useEffect(() => {
    if (!userId || !id) return;
    const contains = (type) =>
      api.get(
        endpoints?.lists?.contains?.(userId) || `/api/v1/lists/${userId}/contains`,
        { params: { movieId: id, type } }
      ).then(r => !!r.data?.data).catch(() => false);

    Promise.all([contains("WATCH"), contains("FAVORITE")]).then(([w,f]) => {
      setInWatch(w); setInFav(f);
    });
  }, [userId, id]);

  // rating
  const rating = useMemo(() => {
    const src = movie || {};
    const pick = [src.rating, src.voteAverage, src.imdbRating, src.tmdbRating, src.tmdbVoteAverage]
      .map(v => (typeof v === "string" ? parseFloat(v) : v))
      .find(v => Number.isFinite(v));
    return Number.isFinite(pick) ? Number(pick) : null;
  }, [movie]);

  // ui helpers
  const poster = abs(movie?.imageUrl);
  const year = useMemo(() => movie?.releaseDate ? new Date(movie.releaseDate).getFullYear() : null, [movie]);
  const durationMin = movie?.duration;
  const contentRating = movie?.contentRating;
  const status = movie?.status;

  const toggle = async (type) => {
    if (!userId) return;
    setToggling(true);
    try {
      const isOn = type === "WATCH" ? inWatch : inFav;
      if (isOn) {
        await api.delete(
          endpoints?.lists?.remove?.(userId) || `/api/v1/lists/${userId}`,
          { params: { movieId: id, type } }
        );
      } else {
        await api.post(
          endpoints?.lists?.add?.(userId) || `/api/v1/lists/${userId}`,
          { movieId: Number(id), type }
        );
      }
      if (type === "WATCH") setInWatch(!isOn); else setInFav(!isOn);
    } catch (e) {
      message.error(e?.response?.data?.message || "İşlem başarısız");
    } finally {
      setToggling(false);
    }
  };

  return (
    <div className="movie-detail">
      <div className="hero" style={{ backgroundImage: `url(${poster || ""})` }}>
        <div className="hero-overlay" />
        <div className="container">
          <Button className="back-btn" icon={<ArrowLeftOutlined />} onClick={() => nav(-1)}>
            Back
          </Button>

          {loadingMovie ? (
            <Skeleton active paragraph={{ rows: 6 }} />
          ) : movie ? (
            <Row gutter={[24, 24]}>
              <Col xs={24} md={8} lg={6}>
                <div className="poster-wrap">
                  <img src={poster || "https://via.placeholder.com/600x900?text=Poster"} alt={movie.title} />
                </div>
              </Col>
              <Col xs={24} md={16} lg={18}>
                <Title level={2} className="title">{movie.title}</Title>

                <Space size={[8, 8]} wrap className="meta">
                  {year && <Tag>{year}</Tag>}
                  {Number.isFinite(durationMin) && <Tag>{durationMin} min</Tag>}
                  {contentRating && <Tag color="geekblue">{contentRating}</Tag>}
                  {status && <Tag color="blue">{status}</Tag>}
                  {Number.isFinite(rating) && (
                    <Space>
                      <Rate allowHalf disabled value={rating / 2} />
                      <Text className="rating-text">{rating.toFixed(1)}/10</Text>
                    </Space>
                  )}
                </Space>

                <Space size="middle" className="cta" wrap>
                  <Tooltip title={inWatch ? "Remove from Watchlist" : "Add to Watchlist"}>
                    <Button
                      type={inWatch ? "primary" : "default"}
                      icon={inWatch ? <EyeFilled /> : <EyeOutlined />}
                      loading={toggling}
                      onClick={() => toggle("WATCH")}
                    >
                      {inWatch ? "Watching" : "Watchlist"}
                    </Button>
                  </Tooltip>

                  <Tooltip title={inFav ? "Remove from Favorites" : "Add to Favorites"}>
                    <Button
                      type={inFav ? "primary" : "default"}
                      icon={inFav ? <HeartFilled /> : <HeartOutlined />}
                      loading={toggling}
                      onClick={() => toggle("FAVORITE")}
                    >
                      {inFav ? "Favorited" : "Favorite"}
                    </Button>
                  </Tooltip>
                </Space>

                <Divider />

                <Row gutter={[16, 12]} className="quick-info">
                  <Col xs={24} md={12}>
                    <Text type="secondary">Director</Text>
                    <div>{movie.directorName || "-"}</div>
                  </Col>
                  <Col xs={24} md={12}>
                    <Text type="secondary">Genres</Text>
                    <div className="chips">
                      {(movie.genres || []).map(g => <Tag key={g.id}>{g.name}</Tag>)}
                    </div>
                  </Col>
                  <Col xs={24} md={12}>
                    <Text type="secondary">Languages</Text>
                    <div className="chips">
                      {(movie.languages || []).map(l => <Tag key={l.id}>{l.name}</Tag>)}
                    </div>
                  </Col>
                  <Col xs={24} md={12}>
                    <Text type="secondary">Countries</Text>
                    <div className="chips">
                      {(movie.countries || []).map(c => <Tag key={c.id}>{c.name}</Tag>)}
                    </div>
                  </Col>
                </Row>

                <Divider />

                <Title level={4}>Overview</Title>
                <Paragraph className="overview">{movie.description || "No description."}</Paragraph>

                {/* ---------- AI Opinion section (buraya gömüldü) ---------- */}
                <Divider />
                <Title level={4}>AI Yorumu</Title>
                <AiOpinion movieId={Number(id)} />
                {/* --------------------------------------------------------- */}
              </Col>
            </Row>
          ) : (
            <Text type="danger">Movie not found.</Text>
          )}
        </div>
      </div>

      <div className="container section">
        <Title level={4}>Cast</Title>
        {loadingCast ? (
          <Skeleton active paragraph={{ rows: 4 }} />
        ) : (
          <List
            grid={{ gutter: 16, xs: 2, sm: 3, md: 4, lg: 6, xl: 8 }}
            dataSource={cast}
            locale={{ emptyText: "No cast" }}
            renderItem={(it) => (
              <List.Item>
                <Card size="small" className="cast-card">
                  <div className="cast-name">{it.actorName}</div>
                  {it.roleName && <div className="cast-role">{it.roleName}</div>}
                </Card>
              </List.Item>
            )}
          />
        )}
      </div>

      <div className="container section">
        <Title level={4}>Trailer / Video</Title>
        <div className="video-box">
          <div className="video-placeholder">Trailer coming soon</div>
        </div>
      </div>

      {similarGenreId && (
        <div className="container section">
          <Title level={4}>Similar Movies</Title>
          <RowSection
            title=""
            filter={{ genreId: similarGenreId }}
            sort={["rating,desc", "releaseDate,desc"]}
            size={14}
            cardWidth={240}
            transform={(data) => data.filter(x => x.id !== movie?.id)}
          />
        </div>
      )}

      <div className="container section">
        <Title level={4}>Comments</Title>
        <Card>
          <section className="movie-section">
            <h3 className="sec-title">Comments</h3>
            <Comments movieId={id} />
          </section>
        </Card>
      </div>
    </div>
  );
}
