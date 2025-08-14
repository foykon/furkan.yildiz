import { useEffect, useMemo, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { Card, Select, Pagination, Empty, Space, Tag, Button } from "antd";
import { api, endpoints } from "../lib/api";
import MovieCard from "../components/MovieCard.jsx";
import "./browse.css";

const PAGE_SIZE_OPTIONS = [7, 14, 21, 28, 35, 49];

export default function Browse() {
  const [sp, setSp] = useSearchParams();
  const nav = useNavigate();

  // URL paramları (UI)
  const page   = Number(sp.get("page") || 1);     // 1-based
  const size   = Number(sp.get("size") || 28);
  const title  = sp.get("title") || "";
  const genreId    = sp.get("genreId");
  const countryId  = sp.get("countryId");
  const directorId = sp.get("directorId");

  // Yıl / tarih / rating
  const minYear         = sp.get("minYear");            // eski link / UI’den gelebilir
  const maxYear         = sp.get("maxYear");
  const releaseDateFrom = sp.get("releaseDateFrom");    // yeni anahtar
  const releaseDateTo   = sp.get("releaseDateTo");
  const minRating       = sp.get("minRating");
  const maxRating       = sp.get("maxRating");

  // Sort (çoklu destek)
  const sortsFromUrl = sp.getAll("sort");
  const sorts = sortsFromUrl.length ? sortsFromUrl : ["releaseDate,desc"];

  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const yearToDate = (y, end = false) =>
    y ? `${String(y).padStart(4, "0")}${end ? "-12-31" : "-01-01"}` : undefined;

  // UI filter objesi (etiketlerde gösterim amaçlı)
  const filter = useMemo(() => {
    const f = {};
    if (title)      f.title = title;
    if (genreId)    f.genreId = Number(genreId);
    if (countryId)  f.countryId = Number(countryId);
    if (directorId) f.directorId = Number(directorId);

    // Yıl → tarih (URL’de releaseDateFrom/To varsa onları kullan; yoksa minYear/maxYear’den türet)
    f.releaseDateFrom = releaseDateFrom || yearToDate(minYear, false);
    f.releaseDateTo   = releaseDateTo   || yearToDate(maxYear, true);

    if (minRating) f.minRating = minRating;
    if (maxRating) f.maxRating = maxRating;
    return f;
  }, [title, genreId, countryId, directorId, releaseDateFrom, releaseDateTo, minYear, maxYear, minRating, maxRating]);

  // API paramları (eski çalışır davranışı korumak için hem dotted hem düz)
  const params = useMemo(() => {
    const dotted = Object.fromEntries(
      Object.entries(filter)
        .filter(([, v]) => v !== undefined && v !== null && v !== "")
        .map(([k, v]) => [`filter.${k}`, v])
    );

    // sort normalizasyonu: year → releaseDate (UI'dan böyle gelebilir)
    const normalizedSorts = (sorts || []).map(s =>
      s.startsWith("year,") ? s.replace(/^year,/, "releaseDate,") : s
    );
    const finalSorts = normalizedSorts.length ? normalizedSorts : ["releaseDate,desc"];

    return {
      // SPRING uyumlu (asıl kullanılan)
      "pageable.page": Math.max(0, page - 1),
      "pageable.size": Math.max(1, size),
      "pageable.sort": finalSorts,

      ...dotted,

      // Geriden uyumluluk: bazı yerlere düz kopyalar gidiyorsa kırılmasın
      page: Math.max(0, page - 1),
      size,
      sort: finalSorts,
      ...filter,
    };
  }, [page, size, JSON.stringify(filter), JSON.stringify(sorts)]);

  useEffect(() => {
    let alive = true;
    setLoading(true);

    api.get(endpoints.movies.search, {
      params,
      paramsSerializer: {
        // key=val&key=val (array’ler için tekrarlı key) — Spring için doğru format
        serialize: (p) => {
          const usp = new URLSearchParams();
          Object.entries(p).forEach(([k, v]) => {
            if (Array.isArray(v)) v.forEach(it => usp.append(k, String(it)));
            else if (v !== undefined && v !== null && v !== "") usp.append(k, String(v));
          });
          return usp.toString();
        }
      }
    })
      .then(res => {
        if (!alive) return;
        setRows(res?.data?.data || []);
        setTotal(res?.data?.totalElements ?? 0);
      })
      .catch(console.error)
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
          {filter.title && <Tag>title: {filter.title}</Tag>}
          {filter.genreId && <Tag>genreId: {filter.genreId}</Tag>}
          {filter.countryId && <Tag>countryId: {filter.countryId}</Tag>}
          {filter.directorId && <Tag>directorId: {filter.directorId}</Tag>}
          {(filter.releaseDateFrom || filter.releaseDateTo) && (
            <Tag>
              year: {filter.releaseDateFrom?.slice(0,4) || "…"}–{filter.releaseDateTo?.slice(0,4) || "…"}
            </Tag>
          )}
          {filter.minRating && <Tag>minRating: {filter.minRating}</Tag>}
          {(filter.title || filter.genreId || filter.countryId || filter.directorId || filter.releaseDateFrom || filter.releaseDateTo || filter.minRating) && (
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
