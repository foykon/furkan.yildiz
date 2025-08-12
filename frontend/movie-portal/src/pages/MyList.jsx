import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Typography, Segmented, Select, Pagination, Spin } from "antd";
import { api, endpoints } from "../lib/api";
import MovieCard from "../components/MovieCard.jsx";
import "./grid.css";

const { Title } = Typography;

export default function MyList() {
  const { type: rawType } = useParams();
  const nav = useNavigate();
  const type = (rawType || "watch").toUpperCase();
  const validType = type === "WATCH" || type === "FAVORITE" ? type : "WATCH";

  const [items, setItems] = useState([]);
  const [total, setTotal] = useState(0);
  const [page, setPage]   = useState(1);
  const [size, setSize]   = useState(28);
  const [loading, setLoading] = useState(false);

  useEffect(() => { setPage(1); }, [validType]);

  const params = useMemo(() => ({
    type: validType,
    filter: { q: "" },                     // spec: filter zorunlu
    pageable: { page: page - 1, size, sort: ["addedAt,desc"] },
  }), [validType, page, size]);

  useEffect(() => {
    setLoading(true);
    api.get(endpoints.lists.me, { params })
      .then(res => {
        // v1: data = array, totalElements üstte
        const data = res.data?.data ?? [];
        setItems(data);
        setTotal(res.data?.totalElements ?? data.length);
      })
      .finally(() => setLoading(false));
  }, [JSON.stringify(params)]);

  return (
    <div className="container">
      <div className="grid-head">
        <Title level={2} style={{ margin: 0 }}>My List</Title>
        <div className="grid-actions">
          <Segmented
            value={validType}
            options={[{ label: "Watch", value: "WATCH" }, { label: "Favorite", value: "FAVORITE" }]}
            onChange={(v) => nav(`/my/${String(v).toLowerCase()}`, { replace: true })}
          />
          <Select
            value={size}
            style={{ width: 120 }}
            options={[14, 21, 28, 35, 42].map(n => ({ label: `${n}/page`, value: n }))}
            onChange={(v) => { setSize(v); setPage(1); }}
          />
        </div>
      </div>

      {loading ? <Spin /> : (
        <>
          <div className="grid-7">
            {items.map((it) => (
              <MovieCard
                key={`${it.type}-${it.movieId}`}
                // ListItemResponse alanları
                movie={{ id: it.movieId, title: it.title, imageUrl: it.posterUrl }}
              />
            ))}
          </div>

          <div className="grid-pagination">
            <Pagination
              current={page}
              pageSize={size}
              total={total}
              showSizeChanger={false}
              onChange={(p) => setPage(p)}
            />
          </div>

          {!items.length && <div style={{ padding: 16, opacity: .7 }}>No items</div>}
        </>
      )}
    </div>
  );
}
