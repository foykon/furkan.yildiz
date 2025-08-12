import { useEffect, useMemo, useRef, useState } from "react";
import { Typography, Button } from "antd";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import { api, endpoints } from "../lib/api";
import MovieCard from "./MovieCard.jsx";
import "./row.css";

const { Title } = Typography;

export default function RowSection({ title, filter = {}, sort = "releaseDate,desc", size = 7, transform, cardWidth = 240 }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const scroller = useRef(null);

  const sorts = useMemo(() => (Array.isArray(sort) ? sort : [sort]).filter(Boolean), [sort]);

  const params = useMemo(() => {
    const dottedFilter = Object.fromEntries(Object.entries(filter || {}).map(([k, v]) => [`filter.${k}`, v]));
    return {
      "pageable.page": 0,
      "pageable.size": size,
      "pageable.sort": sorts,
      ...dottedFilter,
      page: 0,
      size,
      sort: sorts,
      ...filter,
    };
  }, [JSON.stringify(filter), JSON.stringify(sorts), size]);

  useEffect(() => {
    let alive = true;
    setLoading(true);
    api.get(endpoints.movies.search, { params })
      .then(res => {
        if (!alive) return;
        let data = res.data?.data || [];

        if (sorts.some(s => String(s).toLowerCase() === "releasedate,desc")) {
          data = data.slice().sort((a, b) => {
            const ax = a.releaseDate ? Date.parse(a.releaseDate) : 0;
            const bx = b.releaseDate ? Date.parse(b.releaseDate) : 0;
            if (bx !== ax) return bx - ax;
            return (b.id ?? 0) - (a.id ?? 0);
          });
        } else if (sorts.some(s => String(s).toLowerCase() === "rating,desc")) {
          data = data.slice().sort((a, b) => {
            const ar = a.rating ?? -1, br = b.rating ?? -1;
            if (br !== ar) return br - ar;
            const ax = a.releaseDate ? Date.parse(a.releaseDate) : 0;
            const bx = b.releaseDate ? Date.parse(b.releaseDate) : 0;
            return bx - ax;
          });
        }

        if (typeof transform === "function") data = transform(data);
        setItems(data.slice(0, size));
      })
      .finally(() => alive && setLoading(false));
    return () => { alive = false; };
  }, [JSON.stringify(params)]);

  // <<< wheel: passive:false ile manuel bağla
  useEffect(() => {
    const el = scroller.current;
    if (!el) return;
    const onWheel = (e) => {
      // dikey scroll'u yatay scroller'a çevir
      if (Math.abs(e.deltaY) > Math.abs(e.deltaX)) {
        e.preventDefault();
        el.scrollBy({ left: e.deltaY, behavior: "smooth" });
      }
    };
    el.addEventListener("wheel", onWheel, { passive: false });
    return () => el.removeEventListener("wheel", onWheel);
  }, []);

  const scrollBy = (dir) => {
    const el = scroller.current; if (!el) return;
    const step = (cardWidth + 16) * 3;
    el.scrollBy({ left: dir * step, behavior: "smooth" });
  };

  return (
    <div className="row-section" style={{ "--card-w": `${cardWidth}px` }}>
      <div className="row-head">
        <Title level={3} style={{ margin: 0 }}>{title}</Title>
        <div className="row-actions">
          <Button type="text" icon={<LeftOutlined />} onClick={() => scrollBy(-1)} />
          <Button type="text" icon={<RightOutlined />} onClick={() => scrollBy(1)} />
        </div>
      </div>

      <div
        className="row-scroller"
        ref={scroller}
        // onWheel KALDIRILDI!
      >
        {items.map(m => <MovieCard key={m.id} movie={m} />)}
        {!loading && items.length === 0 && <div style={{ padding: 16, opacity: .7 }}>No items</div>}
      </div>
    </div>
  );
}
