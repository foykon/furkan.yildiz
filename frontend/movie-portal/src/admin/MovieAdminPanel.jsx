import { useEffect, useMemo, useState } from "react";
import { Row, Col, Card, Table, Input, Button, Form, DatePicker, InputNumber, Select, Space, message, Popconfirm, Divider, List } from "antd";
import dayjs from "dayjs";
import { api, endpoints } from "../lib/api";

const STATUS_OPTS = ["RELEASED","UPCOMING","CANCELED"].map(v => ({ label: v, value: v }));
const CONTENT_RATING_OPTS = ["G","PG","PG13","R","NC17"].map(v => ({ label: v, value: v }));

export default function MovieAdminPanel() {
  // sol liste
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loadingList, setLoadingList] = useState(false);

  // sağ form
  const [form] = Form.useForm();
  const [mode, setMode] = useState("create");
  const [selectedId, setSelectedId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [loadingOne, setLoadingOne] = useState(false);

  // referanslar
  const [directors, setDirectors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [languages, setLanguages] = useState([]);
  const [countries, setCountries] = useState([]);

  // CAST
  const [cast, setCast] = useState([]);
  const [loadingCast, setLoadingCast] = useState(false);
  const [actors, setActors] = useState([]);
  const [castForm] = Form.useForm();
  const [savingCast, setSavingCast] = useState(false);
  const [editingCastId, setEditingCastId] = useState(null);

  const listParams = useMemo(() => ({
    "filter.title": q?.trim() || undefined,
    "pageable.page": page - 1,
    "pageable.size": pageSize,
    "pageable.sort": ["releaseDate,desc","id,desc"],
  }), [q, page, pageSize]);

  // referans data
  useEffect(() => {
    const params = { "pageable.page": 0, "pageable.size": 500, "pageable.sort": "name,asc" };
    Promise.all([
      api.get(endpoints.directors.list, { params }),
      api.get(endpoints.genres.list, { params }),
      api.get(endpoints.languages.list, { params }),
      api.get(endpoints.countries.list, { params }),
      api.get(endpoints.actors.list, { params }),
    ]).then(([d,g,l,c,a]) => {
      setDirectors(d.data?.data || []);
      setGenres(g.data?.data || []);
      setLanguages(l.data?.data || []);
      setCountries(c.data?.data || []);
      setActors(a.data?.data || []);
    }).catch(()=>{});
  }, []);

  // liste
  useEffect(() => {
    let alive = true;
    setLoadingList(true);
    api.get(endpoints.movies.search, { params: listParams })
      .then(res => {
        if (!alive) return;
        const data = res?.data?.data ?? [];
        const totalElements = res?.data?.totalElements ?? res?.data?.data?.totalElements ?? res?.data?.total ?? 0;
        setRows(data);
        setTotal(totalElements ?? data.length);
      })
      .finally(() => alive && setLoadingList(false));
    return () => { alive = false; };
  }, [JSON.stringify(listParams)]);

  const toInitialForm = (m) => ({
    title: m?.title,
    description: m?.description,
    releaseDate: m?.releaseDate ? dayjs(m.releaseDate) : null,
    duration: m?.duration,
    imageUrl: m?.imageUrl,
    rating: m?.rating,
    status: m?.status,
    contentRating: m?.contentRating,
    directorId: m?.directorId,
    genreIds: (m?.genres || []).map(x => x.id),
    languageIds: (m?.languages || []).map(x => x.id),
    countryIds: (m?.countries || []).map(x => x.id),
  });

  const loadOne = async (id) => {
    setLoadingOne(true);
    try {
      const { data } = await api.get(endpoints.movies.byId(id));
      const m = data?.data;
      form.setFieldsValue(toInitialForm(m));
      // cast
      loadCast(id);
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

  const onSelect = (id) => {
    setSelectedId(id);
    setMode("edit");
    form.resetFields();
    loadOne(id).catch(()=>{});
  };

  const onAddNew = () => {
    setSelectedId(null);
    setMode("create");
    form.resetFields();
    setCast([]);
  };

  const submit = async (vals) => {
    setSaving(true);
    try {
      const body = {
        title: vals.title,
        description: vals.description,
        releaseDate: vals.releaseDate ? vals.releaseDate.format("YYYY-MM-DD") : null,
        duration: vals.duration,
        imageUrl: vals.imageUrl,
        rating: vals.rating,
        status: vals.status,
        contentRating: vals.contentRating,
        directorId: vals.directorId,
        genreIds: vals.genreIds || [],
        languageIds: vals.languageIds || [],
        countryIds: vals.countryIds || [],
        cast: undefined, // cast ayrı yönetiliyor
      };

      if (mode === "create") {
        const { data } = await api.post(endpoints.movies.create, body);
        const newId = data?.data?.id;
        message.success("Movie created");
        if (newId) onSelect(newId); else onAddNew();
        // listeyi tazele
        setPage(1);
        setQ(q => q);
      } else {
        await api.put(endpoints.movies.update(selectedId), body);
        message.success("Saved");
        setQ(q => q);
      }
    } catch (e) {
      message.error(e?.response?.data?.message || e.message);
    } finally {
      setSaving(false);
    }
  };

  const removeMovie = async () => {
    if (!selectedId) return;
    await api.delete(endpoints.movies.delete(selectedId));
    message.success("Deleted");
    onAddNew();
    setQ(q => q);
  };

  // CAST ops
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
      message.error(e?.response?.data?.message || e.message);
    } finally {
      setSavingCast(false);
    }
  };

  const deleteCast = async (castId) => {
    if (!selectedId) return;
    await api.delete(endpoints.movies.cast.delete(selectedId, castId));
    message.success("Cast deleted");
    await loadCast(selectedId);
  };

  const startEditCast = (row) => {
    setEditingCastId(row.id ?? row.castId);
    castForm.setFieldsValue({
      actorId: row.actorId,
      roleName: row.roleName,
      castOrder: row.castOrder,
    });
  };

  return (
    <Row gutter={16}>
      <Col xs={24} md={10} lg={9}>
        <Card
          title="Movies"
          extra={<a onClick={onAddNew}>Add Movie</a>}
        >
          <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
            <Input.Search
              placeholder="Search by title..."
              allowClear
              value={q}
              onChange={(e) => { setQ(e.target.value); setPage(1); }}
              onSearch={(v) => { setQ(v); setPage(1); }}
            />
          </div>

          <Table
            rowKey="id"
            loading={loadingList}
            size="small"
            columns={[
              { title: "Title", dataIndex: "title" },
              { title: "Release", dataIndex: "releaseDate", width: 110 },
              { title: "Rating", dataIndex: "rating", width: 90 },
              { title: "Status", dataIndex: "status", width: 120 },
            ]}
            dataSource={rows}
            onRow={(record) => ({ onClick: () => onSelect(record.id) })}
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

      <Col xs={24} md={14} lg={15}>
        <Card title={mode === "create" ? "Create Movie" : selectedId ? `Edit Movie #${selectedId}` : "Select a movie"}>
          <Form layout="vertical" form={form} onFinish={submit} requiredMark={false}>
            <Row gutter={12}>
              <Col xs={24} md={16}>
                <Form.Item label="Title" name="title" rules={[{ required: true, message: "Required" }]}>
                  <Input placeholder="Movie title" />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Release Date" name="releaseDate" rules={[{ required: true, message: "Required" }]}>
                  <DatePicker style={{ width: "100%" }} />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item label="Description" name="description">
              <Input.TextArea rows={4} placeholder="Description" />
            </Form.Item>

            <Row gutter={12}>
              <Col xs={24} md={8}>
                <Form.Item label="Duration (min)" name="duration" rules={[{ type: "number", min: 1, max: 600 }]}>
                  <InputNumber style={{ width: "100%" }} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Rating (0-10)" name="rating" rules={[{ type: "number", min: 0, max: 10 }]}>
                  <InputNumber step={0.1} style={{ width: "100%" }} />
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
                <Form.Item label="Status" name="status" rules={[{ required: true, message: "Required" }]}>
                  <Select options={STATUS_OPTS} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Content Rating" name="contentRating">
                  <Select allowClear options={CONTENT_RATING_OPTS} />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Director" name="directorId">
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
                <Form.Item label="Genres" name="genreIds">
                  <Select
                    mode="multiple"
                    allowClear
                    options={genres.map(g => ({ value: g.id, label: g.name }))}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Languages" name="languageIds">
                  <Select
                    mode="multiple"
                    allowClear
                    options={languages.map(l => ({ value: l.id, label: l.name }))}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={8}>
                <Form.Item label="Countries" name="countryIds">
                  <Select
                    mode="multiple"
                    allowClear
                    options={countries.map(c => ({ value: c.id, label: c.name }))}
                  />
                </Form.Item>
              </Col>
            </Row>

            <Space style={{ marginTop: 8 }}>
              <Button type="primary" htmlType="submit" loading={saving}>
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

          {/* CAST */}
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
                            <a onClick={() => startEditCast(it)}>Edit</a>,
                            <Popconfirm title="Remove cast?" onConfirm={() => deleteCast(it.id ?? it.castId)}>
                              <a style={{ color: "#ff4d4f" }}>Delete</a>
                            </Popconfirm>
                          ]}
                        >
                          <List.Item.Meta
                            title={it.actorName}
                            description={
                              <>
                                {it.roleName ? `${it.roleName}` : ""}{it.roleName && typeof it.castOrder === "number" ? " • " : ""}
                                {typeof it.castOrder === "number" ? `#${it.castOrder}` : ""}
                              </>
                            }
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
                        <Button
                          onClick={() => { setEditingCastId(null); castForm.resetFields(); }}
                        >
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
