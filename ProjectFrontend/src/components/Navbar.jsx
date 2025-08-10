import { useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import api from "../lib/api";
import "./navbar.css";

function useOutsideClose(ref, onClose) {
  useEffect(() => {
    function handler(e) { if (ref.current && !ref.current.contains(e.target)) onClose?.(); }
    document.addEventListener("click", handler);
    return () => document.removeEventListener("click", handler);
  }, [ref, onClose]);
}

export default function Navbar() {
  const navigate = useNavigate();
  const { search } = useLocation();

  // Search (URL senkron)
  const [q, setQ] = useState(new URLSearchParams(search).get("q") || "");
  useEffect(() => { setQ(new URLSearchParams(search).get("q") || ""); }, [search]);

  // Desktop dropdown state
  const [open, setOpen] = useState(null); // "genres" | "countries" | "directors" | "mylist" | null
  const genresRef = useRef(null);
  const countriesRef = useRef(null);
  const directorsRef = useRef(null);
  const mylistRef = useRef(null);
  useOutsideClose(genresRef, () => open==="genres" && setOpen(null));
  useOutsideClose(countriesRef, () => open==="countries" && setOpen(null));
  useOutsideClose(directorsRef, () => open==="directors" && setOpen(null));
  useOutsideClose(mylistRef, () => open==="mylist" && setOpen(null));

  // Sözlükler
  const [genres, setGenres] = useState([]);
  const [countries, setCountries] = useState([]);
  const [directors, setDirectors] = useState([]);
  const [loadingKey, setLoadingKey] = useState(null);

  async function fetchList(key) {
    try {
      setLoadingKey(key);
      if (key === "genres" && genres.length === 0) {
        const { data } = await api.get("/genres", { params: { page: 0, size: 200 } });
        setGenres(data?.data || []);
      }
      if (key === "countries" && countries.length === 0) {
        const { data } = await api.get("/countries", { params: { page: 0, size: 200 } });
        setCountries(data?.data || []);
      }
      if (key === "directors" && directors.length === 0) {
        const { data } = await api.get("/directors", { params: { page: 0, size: 200 } });
        setDirectors(data?.data || []);
      }
    } finally { setLoadingKey(null); }
  }

  function goHome() { navigate("/movies"); }
  function goFilter(param, id) {
    const p = new URLSearchParams();
    p.set(param, String(id));
    if (q) p.set("q", q);
    navigate(`/movies?${p.toString()}`);
    setOpen(null);
    setMobileOpen(false);
  }
  function goTo(path) {
    navigate(path);
    setOpen(null);
    setMobileOpen(false);
  }

  const token = localStorage.getItem("access_token") || sessionStorage.getItem("access_token");
  const logout = () => {
    localStorage.removeItem("access_token"); localStorage.removeItem("refresh_token");
    sessionStorage.removeItem("access_token"); sessionStorage.removeItem("refresh_token");
    navigate("/login", { replace: true });
  };

  // Search submit
  function onSearch(e) {
    e.preventDefault();
    const p = new URLSearchParams();
    if (q) p.set("q", q);
    navigate(`/movies?${p.toString()}`);
  }

  // MOBILE SHEET
  const [mobileOpen, setMobileOpen] = useState(false);
  const sheetRef = useRef(null);
  useOutsideClose(sheetRef, () => setMobileOpen(false));
  async function openSheet() {
    setMobileOpen(true);
    if (!genres.length) await fetchList("genres");
    if (!countries.length) await fetchList("countries");
    if (!directors.length) await fetchList("directors");
  }

  return (
    <nav className="nav-root">
      <div className="nav-inner">
        {/* Sol */}
        <div className="left">
          <button className="brand" onClick={goHome}>FMOVIES</button>
          <button className="linklike desktop-only" onClick={goHome}>Home</button>

          {/* Genres */}
          <div className="dropdown desktop-only" ref={genresRef}>
            <button className="linklike" onClick={async()=>{ setOpen(open==="genres"?null:"genres"); await fetchList("genres"); }}>
              Genres
            </button>
            {open === "genres" && (
              <div className="menu grid">
                {loadingKey === "genres" && <div className="menu-item muted">Loading…</div>}
                {genres.map(g => (
                  <button key={g.id} className="menu-item" onClick={() => goFilter("genreId", g.id)}>{g.name}</button>
                ))}
                {genres.length === 0 && loadingKey !== "genres" && <div className="menu-item muted">No data</div>}
              </div>
            )}
          </div>

          {/* Country */}
          <div className="dropdown desktop-only" ref={countriesRef}>
            <button className="linklike" onClick={async()=>{ setOpen(open==="countries"?null:"countries"); await fetchList("countries"); }}>
              Country
            </button>
            {open === "countries" && (
              <div className="menu grid">
                {loadingKey === "countries" && <div className="menu-item muted">Loading…</div>}
                {countries.map(c => (
                  <button key={c.id} className="menu-item" onClick={() => goFilter("countryId", c.id)}>{c.name}</button>
                ))}
                {countries.length === 0 && loadingKey !== "countries" && <div className="menu-item muted">No data</div>}
              </div>
            )}
          </div>

          {/* Director */}
          <div className="dropdown desktop-only" ref={directorsRef}>
            <button className="linklike" onClick={async()=>{ setOpen(open==="directors"?null:"directors"); await fetchList("directors"); }}>
              Director
            </button>
            {open === "directors" && (
              <div className="menu grid">
                {loadingKey === "directors" && <div className="menu-item muted">Loading…</div>}
                {directors.map(d => (
                  <button key={d.id} className="menu-item" onClick={() => goFilter("directorId", d.id)}>{d.name}</button>
                ))}
                {directors.length === 0 && loadingKey !== "directors" && <div className="menu-item muted">No data</div>}
              </div>
            )}
          </div>

          {/* My List */}
          <div className="dropdown desktop-only" ref={mylistRef}>
            <button className="linklike" onClick={()=> setOpen(open==="mylist" ? null : "mylist")}>
              MyLists
            </button>
            {open === "mylist" && (
              <div className="menu column">
                <button className="menu-item" onClick={()=>goTo("/watchlist")}>Watchlist</button>
                <button className="menu-item" onClick={()=>goTo("/favorites")}>Favorite list</button>
              </div>
            )}
          </div>
        </div>

        {/* Orta: Search (desktop) */}
        <form className="search desktop-only" onSubmit={onSearch}>
          <input placeholder="Search" value={q} onChange={(e)=>setQ(e.target.value)} />
          <button type="submit" aria-label="Search">
            <svg width="18" height="18" viewBox="0 0 24 24"><path d="M10 4a6 6 0 104.472 10.027l4.25 4.25 1.414-1.414-4.25-4.25A6 6 0 0010 4zm-4 6a4 4 0 118 0 4 4 0 01-8 0z"/></svg>
          </button>
        </form>

        {/* Sağ */}
        <div className="right">
          <Link className="profile-btn desktop-only" to="/profile">Profile</Link>
          <button className="logout desktop-only" onClick={logout} disabled={!token}>Logout</button>
          <button className="hamburger mobile-only" onClick={openSheet} aria-label="Open menu">
            <span></span><span></span><span></span>
          </button>
        </div>
      </div>

      {/* Mobil drawer */}
      <div className={`sheet ${mobileOpen ? "open" : ""}`}>
        <div className="sheet-overlay" onClick={()=>setMobileOpen(false)} />
        <div className="sheet-panel" ref={sheetRef}>
          <div className="sheet-header">
            <button className="brand small" onClick={()=>{ setMobileOpen(false); goHome(); }}>FMOVIES</button>
            <button className="close" onClick={()=>setMobileOpen(false)} aria-label="Close">✕</button>
          </div>

          <form className="sheet-search" onSubmit={(e)=>{ onSearch(e); setMobileOpen(false); }}>
            <input placeholder="Search" value={q} onChange={(e)=>setQ(e.target.value)} />
            <button type="submit">Search</button>
          </form>

          <div className="sheet-section">
            <button className="sheet-link" onClick={()=>{ goHome(); setMobileOpen(false); }}>Home</button>
          </div>

          <div className="sheet-section">
            <div className="sheet-title">My List</div>
            <div className="sheet-grid">
              <button className="sheet-item" onClick={()=>goTo("/watchlist")}>Watchlist</button>
              <button className="sheet-item" onClick={()=>goTo("/favorites")}>Favorite list</button>
            </div>
          </div>

          <div className="sheet-section">
            <div className="sheet-title">Genres</div>
            <div className="sheet-grid">
              {genres.length === 0 && <div className="muted">No data</div>}
              {genres.map(g => (
                <button key={g.id} className="sheet-item" onClick={()=>goFilter("genreId", g.id)}>{g.name}</button>
              ))}
            </div>
          </div>

          <div className="sheet-section">
            <div className="sheet-title">Country</div>
            <div className="sheet-grid">
              {countries.length === 0 && <div className="muted">No data</div>}
              {countries.map(c => (
                <button key={c.id} className="sheet-item" onClick={()=>goFilter("countryId", c.id)}>{c.name}</button>
              ))}
            </div>
          </div>

          <div className="sheet-section">
            <div className="sheet-title">Director</div>
            <div className="sheet-grid">
              {directors.length === 0 && <div className="muted">No data</div>}
              {directors.map(d => (
                <button key={d.id} className="sheet-item" onClick={()=>goFilter("directorId", d.id)}>{d.name}</button>
              ))}
            </div>
          </div>

          <div className="sheet-footer">
            <Link to="/profile" onClick={()=>setMobileOpen(false)} className="profile-btn">Profile</Link>
            <button className="logout" onClick={()=>{ setMobileOpen(false); logout(); }} disabled={!token}>Logout</button>
          </div>
        </div>
      </div>
    </nav>
  );
}
