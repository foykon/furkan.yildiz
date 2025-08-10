import { useEffect, useMemo, useState } from "react";
import { useLocation } from "react-router-dom";
import api from "../lib/api";

export default function MoviesPage() {
  const { search } = useLocation();
  const params = useMemo(() => new URLSearchParams(search), [search]);
  const q = params.get("q") || "";
  const genreId = params.get("genreId");
  const countryId = params.get("countryId");
  const directorId = params.get("directorId");

  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [items, setItems] = useState([]);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => { setPage(0); }, [q, genreId, countryId, directorId]);

  useEffect(() => {
    const load = async () => {
      setLoading(true); setError(null);
      try {
        // OpenAPI: MovieFilterRequest: title, directorId, genreId, languageId, countryId, ...
        const query = { page, size: 10, sort: "releaseDate,desc" };
        if (q) query.title = q;
        if (genreId) query.genreId = Number(genreId);
        if (countryId) query.countryId = Number(countryId);
        if (directorId) query.directorId = Number(directorId);

        const { data } = await api.get("/movies", { params: query });
        setItems(data?.data || []);
        setTotalPages(data?.totalPages || 1);
      } catch (e) {
        setError(e?.response?.data?.message || e.message || "Fetch failed");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [q, genreId, countryId, directorId, page]);

  return (
    <div style={{ maxWidth: 1100, margin: "16px auto", display: "grid", gap: 12, padding: "0 12px" }}>
      <div style={{ fontSize: 13, opacity: .8 }}>
        Filters → {q && `q="${q}" `}{genreId && `genreId=${genreId} `}{countryId && `countryId=${countryId} `}{directorId && `directorId=${directorId}`}
      </div>

      {loading && <p>Loading…</p>}
      {error && <p style={{ color: "crimson" }}>{error}</p>}

      <ul style={{ listStyle: "none", padding: 0, margin: 0, display: "grid", gap: 8 }}>
        {items.map(m => (
          <li key={m.id} style={{ padding: 12, border: "1px solid #ddd", borderRadius: 8 }}>
            <b>{m.title}</b> {m.releaseDate ? `(${m.releaseDate})` : ""} – <i>{m.directorName || "-"}</i>
          </li>
        ))}
        {!loading && items.length === 0 && <li>Sonuç yok.</li>}
      </ul>

      <div style={{ display: "flex", alignItems: "center", gap: 8, justifyContent: "center" }}>
        <button disabled={page===0} onClick={()=>setPage(p=>p-1)}>Prev</button>
        <span>Page {page+1} / {totalPages}</span>
        <button disabled={page+1 >= totalPages} onClick={()=>setPage(p=>p+1)}>Next</button>
      </div>
    </div>
  );
}
