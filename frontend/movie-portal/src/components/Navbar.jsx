import { useEffect, useState } from "react";
import { Layout, Dropdown, Input, Avatar, Button,
         Segmented, Select, Slider, InputNumber, Space } from "antd";
import { UserOutlined, LogoutOutlined, SearchOutlined, FilterOutlined } from "@ant-design/icons";
import { Link, useNavigate } from "react-router-dom";
import { api, endpoints } from "../lib/api";
import { logout } from "../auth/auth";
import "./navbar.css";

const { Header } = Layout;

function MegaDropdown({ title, items, onSelect }) {
  return (
    <Dropdown
      trigger={["click"]}
      arrow={false}
      placement="bottomLeft"
      dropdownRender={() => (
        <div className="mega">
          {items.map((x) => (
            <a key={x.id} className="mega-item" onClick={() => onSelect?.(x)}>
              {x.name}
            </a>
          ))}
        </div>
      )}
    >
      <a className="nav-link" onClick={(e) => e.preventDefault()}>{title}</a>
    </Dropdown>
  );
}

function FiltersPanel({ genres, countries, directors, defaultTitle, onApply, onClose }) {
  const [title, setTitle] = useState(defaultTitle || "");
  const [genreId, setGenreId] = useState();
  const [countryId, setCountryId] = useState();
  const [directorId, setDirectorId] = useState();
  const [minYear, setMinYear] = useState();
  const [maxYear, setMaxYear] = useState();
  const [rating, setRating] = useState(0);
  const [sort, setSort] = useState("title,asc");

  const apply = () => {
    const qs = new URLSearchParams();
    if (title?.trim()) qs.set("title", title.trim());
    if (genreId) qs.set("genreId", String(genreId));
    if (countryId) qs.set("countryId", String(countryId));
    if (directorId) qs.set("directorId", String(directorId));
    if (minYear) qs.set("minYear", String(minYear));
    if (maxYear) qs.set("maxYear", String(maxYear));
    if (rating > 0) qs.set("minRating", String(rating));
    qs.set("sort", sort);
    qs.set("page", "1");
    qs.set("size", "28");
    onApply?.(qs);
    onClose?.();
  };

  return (
    <div className="filters-pop">
      <div className="row"><Input placeholder="Title contains…" value={title} onChange={(e)=>setTitle(e.target.value)} allowClear /></div>
      <div className="row two">
        <Select value={genreId} onChange={setGenreId} placeholder="Genre" allowClear options={genres.map(g => ({ value:g.id, label:g.name }))} />
        <Select value={countryId} onChange={setCountryId} placeholder="Country" allowClear options={countries.map(c => ({ value:c.id, label:c.name }))} />
      </div>
      <div className="row">
        <Select value={directorId} onChange={setDirectorId} placeholder="Director" allowClear showSearch optionFilterProp="label"
                options={directors.map(d => ({ value:d.id, label:d.name }))} />
      </div>
      <div className="row two">
        <InputNumber value={minYear} onChange={setMinYear} placeholder="Min year" style={{ width:"100%" }} />
        <InputNumber value={maxYear} onChange={setMaxYear} placeholder="Max year" style={{ width:"100%" }} />
      </div>
      <div className="row"><div className="label">Min rating</div><Slider min={0} max={10} step={0.1} value={rating} onChange={setRating} /></div>
      <div className="row">
        <div className="label">Sort</div>
        <Segmented value={sort} onChange={setSort} options={[
          { label:"Title A→Z", value:"title,asc" },
          { label:"Year ↓",    value:"year,desc" },
          { label:"Rating ↓",  value:"rating,desc" },
        ]}/>
      </div>
      <div className="row actions"><Space><Button onClick={()=>{
        setTitle(defaultTitle||""); setGenreId(); setCountryId(); setDirectorId(); setMinYear(); setMaxYear(); setRating(0); setSort("title,asc");
      }}>Reset</Button><Button type="primary" onClick={apply}>Apply</Button></Space></div>
    </div>
  );
}

export default function Navbar(){
  const nav = useNavigate();
  const [genres, setGenres] = useState([]);
  const [countries, setCountries] = useState([]);
  const [directors, setDirectors] = useState([]);
  const [search, setSearch] = useState("");
  const [filtersOpen, setFiltersOpen] = useState(false);

  useEffect(() => {
    const params = { pageable: { page: 0, size: 200, sort: ["name,asc"] } };
    Promise.all([
      api.get(endpoints.genres.list,    { params }),
      api.get(endpoints.countries.list, { params }),
      api.get(endpoints.directors.list, { params }),
    ]).then(([g,c,d]) => {
      setGenres(g.data?.data || []);
      setCountries(c.data?.data || []);
      setDirectors(d.data?.data || []);
    }).catch(console.error);
  }, []);

  const onSearch = (value) => {
    const q = value?.trim();
    if (q) nav(`/browse?title=${encodeURIComponent(q)}&page=1&size=28`);
  };
  const applyFilters = (qs) => nav(`/browse?${qs.toString()}`);

  const profileMenuItems = [
    { key: "profile", icon: <UserOutlined />,  label: "Edit profile" },
    { type: "divider" },
    { key: "logout",  icon: <LogoutOutlined />, label: "Logout" },
  ];

  return (
    <Header className="site-header">
      <div className="container header-wrap">
        <Link to="/" className="brand"><span className="brand-badge">Furki</span></Link>

        <nav className="nav-left">
          <Link to="/" className="nav-link">Home</Link>
          <Link to="/browse?page=1&size=28" className="nav-link">All Movies</Link>

          <MegaDropdown title="Genres"    items={genres}    onSelect={(x)=>nav(`/browse?genreId=${x.id}&page=1&size=28`)} />
          <MegaDropdown title="Country"   items={countries} onSelect={(x)=>nav(`/browse?countryId=${x.id}&page=1&size=28`)} />
          <MegaDropdown title="Directors" items={directors} onSelect={(x)=>nav(`/browse?directorId=${x.id}&page=1&size=28`)} />

          <Dropdown trigger={["click"]} arrow={false} menu={{ items:[
            { key: "watch",    label: <Link to="/my/watch">Watch</Link> },
            { key: "favorite", label: <Link to="/my/favorite">Favorite</Link> },
          ]}}>
            <a className="nav-link" onClick={(e)=>e.preventDefault()}>My List</a>
          </Dropdown>
        </nav>

        <div className="header-right">
          <Input.Search
            className="search"
            placeholder="Search"
            value={search}
            onChange={(e)=>setSearch(e.target.value)}
            onSearch={onSearch}
            allowClear
            enterButton={<SearchOutlined />}
          />

          <Dropdown
            open={filtersOpen}
            onOpenChange={setFiltersOpen}
            trigger={["click"]}
            arrow={false}
            dropdownRender={() => (
              <FiltersPanel
                genres={genres}
                countries={countries}
                directors={directors}
                defaultTitle={search}
                onApply={applyFilters}
                onClose={()=>setFiltersOpen(false)}
              />
            )}
          >
            <Button className="filters-btn" icon={<FilterOutlined />} />
          </Dropdown>

          <Dropdown
            trigger={["click"]}
            arrow={false}
            menu={{
              items: profileMenuItems,
              onClick: async ({ key }) => {
                if (key === "profile") return nav("/profile");
                if (key === "logout") { try { await logout(); } finally { nav("/login", { replace: true }); } }
              },
            }}
          >
            <Button type="text" className="avatar-btn" icon={<Avatar size="small" icon={<UserOutlined />} />} />
          </Dropdown>
        </div>
      </div>
    </Header>
  );
}
