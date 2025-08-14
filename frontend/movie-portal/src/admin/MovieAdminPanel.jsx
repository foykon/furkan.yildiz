// src/admin/MovieAdminPanel.jsx
import { useEffect, useMemo, useState } from "react";
import {
  Row, Col, Card, Table, Input, Button, Form, DatePicker, InputNumber,
  Select, Space, message, Popconfirm, Divider, List, Typography, Tag
} from "antd";
import dayjs from "dayjs";
import { api, endpoints } from "../lib/api";

const { Text } = Typography;
const STATUS_OPTS = ["RELEASED","UPCOMING","CANCELED"].map(v => ({ label: v, value: v }));
const CONTENT_RATING_OPTS = ["G","PG","PG13","R","NC17"].map(v => ({ label: v, value: v }));

export default function MovieAdminPanel() {
  // --------- SOL: LİSTE DURUMLARI ---------
  const [q, setQ] = useState("");
  const [statusFilter, setStatusFilter] = useState();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loadingList, setLoadingList] = useState(false);

  // --------- SAĞ: FORM DURUMLARI ---------
  const [form] = Form.useForm();
  const [mode, setMode] = useState("create"); // create | edit
  const [selectedId, setSelectedId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [loadingOne, setLoadingOne] = useState(false);

  // --------- REFERANSLAR ---------
  const [directors, setDirectors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [languages, setLanguages] = useState([]);
  const [countries, setCountries] = useState([]);
  const [actors, setActors] = useState([]);

  // --------- CAST ---------
  const [cast, setCast] = useState([]);
  const [loadingCast, setLoadingCast] = useState(false);
  const [castForm] = Form.useForm();
  const [savingCast, setSavingCast] = useState(false);
  const [editingCastId, setEditingCastId] = useState(null);

  // --------- HELPERS ----------
  const listParams = useMemo(() => {
    const p = {
      "pageable.page": page - 1,
      "pageable.size": pageSize,
      "pageable.sort": ["releaseDate,desc", "id,desc"],
    };
    if (q?.trim()) p["filter.title"] = q.trim();
    if (statusFilter) p["filter.status"] = statusFilter;
    return p;
  }, [q, statusFilter, page, pageSize]);

  const toFormValues = (m) => ({
    title: m?.title ?? undefined,
    description: m?.description ?? undefined,
    releaseDate: m?.releaseDate ? dayjs(m.releaseDate) : null,
    duration: m?.duration ?? undefined,
    imageUrl: m?.imageUrl ?? undefined,
    rating: m?.rating ?? undefined,
    status: m?.status ?? undefined,
    contentRating: m?.contentRating ?? undefined,
    directorId: m?.directorId ?? undefined,
    genreIds: (m?.genres || []).map(x => x.id),
    languageIds: (m?.languages || []).map(x => x.id),
    countryIds: (m?.countries || []).map(x => x.id),
  });

  const toRequestBody = (vals) => ({
    title: vals.title,
    description: vals.description,
    releaseDate: vals.releaseDate ? vals.releaseDate.format("YYYY-MM-DD") : null,
    duration: typeof vals.duration === "number" ? vals.duration : (vals.duration ? Number(vals.duration) : null),
    imageUrl: vals.imageUrl,
    rating: typeof vals.rating === "number" ? vals.rating : (vals.rating ? Number(vals.rating) : null),
    status: vals.status,
    contentRating: vals.contentRating || null,
    directorId: vals.directorId ? Number(vals.directorId) : null,
    genreIds: (vals.genreIds || []).map(Number),
    languageIds: (vals.languageIds || []).map(Number),
    countryIds: (vals.countryIds || []).map(Number),
    // cast ayrı uçtan yönetiliyor
  });

  // --------- REFERANS LİSTELERİ ---------
  useEffect(() => {
    const params = { "pageable.page": 0, "pageable.size": 500, "pageable.sort": "name,asc" };
    Promise.all([
      api.get(endpoints.directors.list, { params }),
      api.get(endpoints.genres.list,    { params }),
      api.get(endpoints.languages.list, { params }),
      api.get(endpoints.countries.list, { params }),
      api.get(endpoints.actors.list,    { params }),
    ])
      .then(([d,g,l,c,a]) => {
        setDirectors(d.data?.data || []);
        setGenres(g.data?.data || []);
        setLanguages(l.data?.data || []);
        setCountries(c.data?.data || []);
        setActors(a.data?.data || []);
      })
      .catch(() => {});
  }, []);

  // --------- MOVIE LİSTE ---------
  useEffect(() => {
    let alive = true;
    setLoadingList(true);
    api.get(endpoints.movies.search, { params: listParams })
      .then(res => {
        if (!alive) return;
        const data = res?.data?.data ?? [];
        const totalElements =
          res?.data?.totalElements ??
          res?.data?.data?.totalElements ??
          0;
        setRows(data);
        setTotal(totalElements || data.length);
      })
      .finally(() => alive && setLoadingList(false));
    return () => { alive = false; };
  }, [JSON.stringify(listParams)]);

  // --------- TEK KAYIT + CAST ---------
  const loadOne = async (id) => {
    setLoadingOne(true);
    try {
      const { data } = await api.get(endpoints.movies.byId(id));
      const m = data?.data;
      form.setFieldsValue(toFormValues(m));
      await loadCast(id);
    } finally {
      setLoadingOne(false);
    }
  };

  const loadCast = async (movieId) => {
    setLoadingCast(true);
    try {
      const { data } = await api.get(endpoints.movies.cast.list(movieId));
      const list = data?.data || [];
      list.sort((a,b) => (a.castOrder ?? 0) - (b.castOrder ?? 0));
      setCast(list);
    } finally {
      setLoadingCast(false);
    }
  };

  // --------- SEÇİM / YENİ EKLE ---------
  const onSelect = (id) => {
    setSelectedId(id);
    setMode("edit");
    form.resetFields();
    loadOne(id).catch(() => {});
  };

  const onAddNew = () => {
    setSelectedId(null);
    setMode("create");
    form.resetFields();
    setCast([]);
    setEditingCastId(null);
    castForm.resetFields();
  };

  // --------- CREATE/UPDATE ---------
  const onSubmit = async (vals) => {
    setSaving(true);
    try {
      const body = toRequestBody(vals);
      console.debug("[MOVIE] submit body:", mode, selectedId, body);

      if (mode === "create") {
        const { data } = await api.post(endpoints.movies.create, body);
        console.debug("[MOVIE] POST /create OK", data);
        const newId = data?.data?.id;
        message.success("Movie created");
        if (newId) {
          onSelect(newId);
          setPage(1);
          setQ(_ => _);
        } else {
          onAddNew();
        }
      } else if (selectedId) {
        await api.put(endpoints.movies.update(selectedId), body);
        console.debug("[MOVIE] PUT /update OK");
        message.success("Saved");
        setQ(_ => _);
      }
    } catch (e) {
      console.error("[MOVIE] save error:", e);
      message.error(e?.response?.data?.message || e.message || "Save failed");
    } finally {
      setSaving(false);
    }
  };

  // onFinish bazen hiç tetiklenmiyormuş gibi hissedildiği durumlar için
  const handleSaveClick = async () => {
    try {
      const vals = await form.validateFields();
      await onSubmit(vals);
    } catch (err) {
      if (err?.errorFields?.length) {
        message.error(err.errorFields[0]?.errors?.[0] || "Please check the form");
        form.scrollToField(err.errorFields[0].name);
      }
    }
  };

  const removeMovie = async () => {
    if (!selectedId) return;
    try {
      await api.delete(endpoints.movies.delete(selectedId));
      message.success("Deleted");
      onAddNew();
      setQ(_ => _);
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "Delete failed");
    }
  };

  // --------- CAST CRUD ---------
  const submitCast = async (vals) => {
    if (!selectedId) return;
    setSavingCast(true);
    try {
      const body = {
        actorId: vals.actorId,
        roleName: vals.roleName,
        castOrder: vals.castOrder ?? 0,
      };
      if (editingCastId) {
        await api.put(endpoints.movies.cast.update(selectedId, editingCastId), body);
        message.success("Cast updated");
      } else {
        await api.post(endpoints.movies.cast.add(selectedId), body);
        message.success("Cast added");
      }
      castForm.resetFields();
      setEditingCastId(null);
      await loadCast(selectedId);
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "Cast save failed");
    } finally {
      setSavingCast(false);
    }
  };

  const deleteCast = async (castId) => {
    if (!selectedId) return;
    try {
      await api.delete(endpoints.movies.cast.delete(selectedId, castId));
      message.success("Cast deleted");
      await loadCast(selectedId);
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "Cast delete failed");
    }
  };

  const startEditCast = (row) => {
    const cid = row.id ?? row.castId;
    setEditingCastId(cid);
    castForm.setFieldsValue({
      actorId: row.actorId,
      roleName: row.roleName,
      castOrder: row.castOrder,
    });
  };

  return (
    <Row gutter={16}>
      {/* --------- SOL: MOVIE LİSTE --------- */}
      <Col xs={24} md={10} lg={9}>
        <Card
          title="Movies"
          extra={<Button type="link" onClick={onAddNew}>Add Movie</Button>}
        >
          <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
            <Input.Search
              placeholder="Search by title..."
              allowClear
              value={q}
              onChange={(e) => { setQ(e.target.value); setPage(1); }}
              onSearch={(v) => { setQ(v); setPage(1); }}
            />
            <Select
              allowClear
              placeholder="Status"
              style={{ width: 140 }}
              value={statusFilter}
              onChange={(v) => { setStatusFilter(v); setPage(1); }}
              options={[{label:"All", value: undefined}, ...STATUS_OPTS].filter(x => x.value !== undefined)}
            />
          </div>

          <Table
            rowKey="id"
            loading={loadingList}
            size="small"
            columns={[
              { title: "Title", dataIndex: "title", ellipsis: true },
              { title: "Release", dataIndex: "releaseDate", width: 110 },
              { title: "Rating", dataIndex: "rating", width: 90 },
              {
                title: "Status", dataIndex: "status", width: 120,
                render: (s) => s ? <Tag color={s==="RELEASED"?"green":s==="UPCOMING"?"blue":"red"}>{s}</Tag> : "-"
              },
            ]}
            dataSource={rows}
            onRow={(record) => ({
              onClick: () => onSelect(record.id),
              style: { cursor: "pointer" }
            })}
            pagination={{
              current: page,
              pageSize,
              total,
              showSizeChanger: true,
              pageSizeOptions: ["5","10","20","50","100"],
              onChange: (p, s) => {
                if (s !== pageSize) { setPageSize(s); setPage(1); }
                else { setPage(p); }
              },
            }}
            rowClassName={(record) =>
              (record.id === selectedId ? "ant-table-row-selected " : "") + "row-clickable"
            }
            tableLayout="fixed"
            scroll={{ y: 360 }}
            locale={{ emptyText: "No movies" }}
          />
        </Card>
      </Col>

      {/* --------- SAĞ: FORM + CAST --------- */}
      <Col xs={24} md={14} lg={15}>
        <Card
          loading={loadingOne && mode==="edit"}
          title={mode === "create" ? "Create Movie" : selectedId ? `Edit Movie #${selectedId}` : "Select a movie"}
        >
          <Form
            layout="vertical"
            form={form}
            requiredMark={false}
          >
            <Row gutter={12}>
              <Col xs={24} md={16}>
                <Form.Item
                  label="Title"
                  name="title"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Input placeholder="Movie title" />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Release Date"
                  name="releaseDate"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <DatePicker style={{ width: "100%" }} />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item label="Description" name="description">
              <Input.TextArea rows={4} placeholder="Description" />
            </Form.Item>

            <Row gutter={12}>
              <Col xs={24} md={8}>
                <Form.Item label="Duration (min)" name="duration">
                  <InputNumber min={1} max={600} style={{ width: "100%" }} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Rating (0-10)" name="rating">
                  <InputNumber min={0} max={10} step={0.1} style={{ width: "100%" }} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Image URL" name="imageUrl">
                  <Input placeholder="/images/poster.jpg or https://..." />
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={12}>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Status"
                  name="status"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Select options={STATUS_OPTS} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Content Rating" name="contentRating">
                  <Select allowClear options={CONTENT_RATING_OPTS} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Director"
                  name="directorId"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Select
                    showSearch
                    allowClear
                    optionFilterProp="label"
                    options={directors.map(d => ({ value: d.id, label: d.name }))}
                  />
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={12}>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Genres"
                  name="genreIds"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Select
                    mode="multiple"
                    allowClear
                    options={genres.map(g => ({ value: g.id, label: g.name }))}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Languages"
                  name="languageIds"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Select
                    mode="multiple"
                    allowClear
                    options={languages.map(l => ({ value: l.id, label: l.name }))}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Countries"
                  name="countryIds"
                  rules={[{ required: true, message: "Required" }]}
                >
                  <Select
                    mode="multiple"
                    allowClear
                    options={countries.map(c => ({ value: c.id, label: c.name }))}
                  />
                </Form.Item>
              </Col>
            </Row>

            <Space style={{ marginTop: 8 }}>
              <Button
                type="primary"
                loading={saving}
                onClick={handleSaveClick}
              >
                {mode === "create" ? "Create" : "Save"}
              </Button>
              <Button htmlType="button" onClick={() => form.resetFields()}>Reset</Button>
              {mode === "edit" && selectedId && (
                <Popconfirm title="Delete movie?" okButtonProps={{ danger: true }} onConfirm={removeMovie}>
                  <Button danger>Delete</Button>
                </Popconfirm>
              )}
            </Space>
          </Form>

          {/* ---- CAST PANEL (sadece edit modunda) ---- */}
          {mode === "edit" && selectedId && (
            <>
              <Divider />
              <Row gutter={12}>
                <Col xs={24} md={12}>
                  <Card title="Cast" size="small" loading={loadingCast}>
                    <List
                      dataSource={cast}
                      locale={{ emptyText: "No cast" }}
                      renderItem={(it) => (
                        <List.Item
                          actions={[
                            <Button type="link" onClick={() => startEditCast(it)}>Edit</Button>,
                            <Popconfirm title="Remove cast?" onConfirm={() => deleteCast(it.id ?? it.castId)}>
                              <Button type="link" danger>Delete</Button>
                            </Popconfirm>
                          ]}
                        >
                          <List.Item.Meta
                            title={<Space split={<Text type="secondary">•</Text>}>
                              <Text strong>{it.actorName}</Text>
                              {it.roleName && <Text>{it.roleName}</Text>}
                            </Space>}
                            description={typeof it.castOrder === "number" ? `Order #${it.castOrder}` : ""}
                          />
                        </List.Item>
                      )}
                    />
                  </Card>
                </Col>
                <Col xs={24} md={12}>
                  <Card title={editingCastId ? "Edit Cast" : "Add Cast"} size="small">
                    <Form layout="vertical" form={castForm} onFinish={submitCast}>
                      <Form.Item label="Actor" name="actorId" rules={[{ required: true, message: "Required" }]}>
                        <Select
                          showSearch
                          optionFilterProp="label"
                          options={actors.map(a => ({ value: a.id, label: a.name }))}
                        />
                      </Form.Item>
                      <Form.Item label="Role Name" name="roleName" rules={[{ required: true, message: "Required" }]}>
                        <Input placeholder="e.g., Lead" />
                      </Form.Item>
                      <Form.Item label="Cast Order" name="castOrder">
                        <InputNumber min={0} style={{ width: "100%" }} />
                      </Form.Item>
                      <Space>
                        <Button type="primary" htmlType="submit" loading={savingCast}>
                          {editingCastId ? "Save" : "Add"}
                        </Button>
                        <Button onClick={() => { setEditingCastId(null); castForm.resetFields(); }}>
                          Clear
                        </Button>
                      </Space>
                    </Form>
                  </Card>
                </Col>
              </Row>
            </>
          )}
        </Card>
      </Col>
    </Row>
  );
}
