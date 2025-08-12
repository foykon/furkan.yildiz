import { useEffect, useMemo, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { Card, Select, Pagination, Empty, Space, Tag, Button } from "antd";
import { api, endpoints } from "../lib/api";
import MovieCard from "../components/MovieCard.jsx";
import "./browse.css";

const PAGE_SIZE_OPTIONS = [7, 14, 21, 28, 35, 49]; // 7'şer kat

export default function Browse() {
  const [sp, setSp] = useSearchParams();
  const nav = useNavigate();

  const page   = Number(sp.get("page") || 1);            // antd 1-based
  const size   = Number(sp.get("size") || 28);           // varsayılan 4 satır (4*7)
  const title  = sp.get("title") || "";
  const genreId    = sp.get("genreId");
  const countryId  = sp.get("countryId");
  const directorId = sp.get("directorId");

  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  // filter objemiz
  const filter = useMemo(() => {
    const f = {};
    if (title) f.title = title;
    if (genreId) f.genreId = Number(genreId);
    if (countryId) f.countryId = Number(countryId);
    if (directorId) f.directorId = Number(directorId);
    return f;
  }, [title, genreId, countryId, directorId]);

  // dotted + flat param (server hangisini severse)
  const params = useMemo(() => {
    const dotted = Object.fromEntries(Object.entries(filter).map(([k,v]) => [`filter.${k}`, v]));
    const sorts = ["releaseDate,desc"]; // default: en yeni
    return {
      // dotted pageable
      "pageable.page": page - 1,
      "pageable.size": size,
      "pageable.sort": sorts,
      ...dotted,
      // flat fallback
      page: page - 1,
      size,
      sort: sorts,
      ...filter,
    };
  }, [page, size, JSON.stringify(filter)]);

  useEffect(() => {
    let alive = true;
    setLoading(true);
    api.get(endpoints.movies.search, { params })
      .then(res => {
        if (!alive) return;
        setRows(res?.data?.data || []);
        setTotal(res?.data?.totalElements ?? 0);
      })
      .finally(() => alive && setLoading(false));
    return () => { alive = false; };
  }, [JSON.stringify(params)]);

  const changeSize = (newSize) => {
    sp.set("size", String(newSize));
    sp.set("page", "1");
    setSp(sp, { replace: true });
  };
  const changePage = (p) => {
    sp.set("page", String(p));
    setSp(sp, { replace: true });
  };

  const clearFilters = () => {
    const keep = ["page","size"];
    const next = new URLSearchParams();
    keep.forEach(k => sp.get(k) && next.set(k, sp.get(k)));
    nav({ pathname: "/browse", search: `?${next.toString()}` });
  };

  return (
    <div className="browse-page">
      <Card className="browse-toolbar" bordered={false}>
        <Space wrap>
          <Tag color="cyan-inverse">All Movies</Tag>
          {title && <Tag>title: {title}</Tag>}
          {genreId && <Tag>genreId: {genreId}</Tag>}
          {countryId && <Tag>countryId: {countryId}</Tag>}
          {directorId && <Tag>directorId: {directorId}</Tag>}
          {(title || genreId || countryId || directorId) && (
            <Button onClick={clearFilters}>Clear filters</Button>
          )}
        </Space>

        <div className="toolbar-right">
          <span style={{ opacity:.8, marginRight: 8 }}>Items per page:</span>
          <Select
            value={size}
            onChange={changeSize}
            options={PAGE_SIZE_OPTIONS.map(v => ({ value: v, label: v }))}
            style={{ width: 100 }}
          />
        </div>
      </Card>

      <div className="grid7">
        {rows.map(m => <MovieCard key={m.id} movie={m} />)}
        {!loading && rows.length === 0 && <Empty style={{ gridColumn: "1 / -1" }} description="No movies" />}
      </div>

      <div className="pager">
        <Pagination
          current={page}
          pageSize={size}
          total={total}
          onChange={changePage}
          showSizeChanger={false}
        />
      </div>
    </div>
  );
}
