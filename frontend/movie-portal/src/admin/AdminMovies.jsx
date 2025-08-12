import { useEffect, useMemo, useState } from "react";
import { Row, Col, Card, Table, Input, Button, Space, message, Popconfirm, Select } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { api, endpoints } from "../lib/api";
import MovieForm from "./MovieForm.jsx";
import CastPanel from "./CastPanel.jsx";

export default function AdminMovies() {
  const [q, setQ] = useState("");
  const [genreId, setGenreId] = useState();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(12);
  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const [selectedId, setSelectedId] = useState(null);
  const [mode, setMode] = useState("create"); // create | edit

  const [genres, setGenres] = useState([]);

  useEffect(() => {
    // drop-down için türler
    const params = { "pageable.page": 0, "pageable.size": 200, "pageable.sort": "name,asc" };
    api.get(endpoints.genres.list, { params }).then(res => setGenres(res?.data?.data || [])).catch(()=>{});
  }, []);

  const listParams = useMemo(() => ({
    // dotted (Spring için garanti)
    "filter.title": q?.trim() || undefined,
    "filter.genreId": genreId || undefined,
    "pageable.page": page - 1,
    "pageable.size": pageSize,
    "pageable.sort": "releaseDate,desc",
    // düz (bazı backend’lerde fallback)
    title: q?.trim() || undefined,
    genreId: genreId || undefined,
    page: page - 1,
    size: pageSize,
    sort: "releaseDate,desc",
  }), [q, genreId, page, pageSize]);

  useEffect(() => {
    let alive = true;
    setLoading(true);
    api.get(endpoints.movies.search, { params: listParams })
      .then(res => {
        if (!alive) return;
        const data = res?.data?.data || [];
        setRows(data);
        setTotal(res?.data?.totalElements ?? data.length);
      })
      .finally(() => alive && setLoading(false));
    return () => { alive = false; };
  }, [JSON.stringify(listParams)]);

  const columns = [
    { title: "Title", dataIndex: "title", ellipsis: true },
    { title: "Year",  dataIndex: "releaseDate", width: 90, render: v => v ? new Date(v).getFullYear() : "-" },
    { title: "Rating", dataIndex: "rating", width: 90 },
    {
      title: "Actions",
      width: 160,
      render: (_, r) => (
        <Space>
          <Button size="small" onClick={() => { setSelectedId(r.id); setMode("edit"); }}>Edit</Button>
          <Popconfirm
            title="Delete movie?"
            onConfirm={() => deleteMovie(r.id)}
          >
            <Button size="small" danger>Delete</Button>
          </Popconfirm>
        </Space>
      )
    },
  ];

  const deleteMovie = async (id) => {
    try {
      await api.delete(endpoints.movies.delete(id));
      message.success("Deleted");
      // refresh
      setSelectedId((sid) => (sid === id ? null : sid));
      // sayfa başına kayma olmasın diye sadece tekrar yükle
      const res = await api.get(endpoints.movies.search, { params: listParams });
      const data = res?.data?.data || [];
      setRows(data);
      setTotal(res?.data?.totalElements ?? data.length);
      if (mode === "edit" && selectedId === id) setMode("create");
    } catch (e) {
      message.error(e?.response?.data?.message || "Delete failed");
    }
  };

  return (
    <Row gutter={16}>
      <Col xs={24} lg={10} xl={9}>
        <Card
          title="Movies"
          extra={
            <Button
              icon={<PlusOutlined />}
              type="primary"
              onClick={() => { setSelectedId(null); setMode("create"); }}
            >
              Add Movie
            </Button>
          }
        >
          <Space style={{ marginBottom: 12 }} wrap>
            <Input.Search
              placeholder="Search title"
              allowClear
              value={q}
              onChange={(e)=>{ setQ(e.target.value); setPage(1); }}
              onSearch={(v)=>{ setQ(v); setPage(1); }}
              style={{ minWidth: 200 }}
            />
            <Select
              allowClear
              placeholder="Genre filter"
              value={genreId}
              onChange={(v)=>{ setGenreId(v); setPage(1); }}
              options={genres.map(g => ({ value: g.id, label: g.name }))}
              style={{ minWidth: 180 }}
            />
          </Space>

          <Table
            rowKey="id"
            loading={loading}
            size="small"
            columns={columns}
            dataSource={rows}
            pagination={{
              current: page,
              pageSize,
              total,
              showSizeChanger: true,
              pageSizeOptions: ["6","12","20","50","100"],
              onChange: (p, s) => {
                if (s !== pageSize) { setPageSize(s); setPage(1); }
                else setPage(p);
              },
            }}
            onRow={(record) => ({
              onClick: () => { setSelectedId(record.id); setMode("edit"); },
            })}
          />
        </Card>
      </Col>

      <Col xs={24} lg={14} xl={15}>
        <Card
          title={mode === "create" ? "Create Movie" : selectedId ? `Edit Movie #${selectedId}` : "Select a movie"}
        >
          <MovieForm
            mode={mode}
            movieId={selectedId}
            onCreated={(m) => { message.success("Movie created"); setSelectedId(m.id); setMode("edit"); }}
            onSaved={() => message.success("Saved")}
          />

          {mode === "edit" && selectedId && (
            <div style={{ marginTop: 24 }}>
              <CastPanel movieId={selectedId} />
            </div>
          )}
        </Card>
      </Col>
    </Row>
  );
}
