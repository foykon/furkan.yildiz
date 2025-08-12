import { useEffect, useState } from "react";
import { Form, Input, DatePicker, InputNumber, Select, Button, Space, Skeleton, Alert } from "antd";
import dayjs from "dayjs";
import { api, endpoints } from "../lib/api";

const STATUS_OPTS = [
  { value: "RELEASED", label: "RELEASED" },
  { value: "UPCOMING", label: "UPCOMING" },
  { value: "CANCELED", label: "CANCELED" },
];
const CONTENT_RATING_OPTS = ["G","PG","PG13","R","NC17"].map(x => ({ value: x, label: x }));

export default function MovieForm({ movieId, mode="create", onCreated, onSaved }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const [directors, setDirectors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [languages, setLanguages] = useState([]);
  const [countries, setCountries] = useState([]);

  useEffect(() => {
    const params = { "pageable.page": 0, "pageable.size": 200, "pageable.sort": "name,asc" };
    Promise.all([
      api.get(endpoints.directors.list, { params }),
      api.get(endpoints.genres.list, { params }),
      api.get(endpoints.languages.list, { params }),
      api.get(endpoints.countries.list, { params }),
    ]).then(([d,g,l,c]) => {
      setDirectors(d?.data?.data || []);
      setGenres(g?.data?.data || []);
      setLanguages(l?.data?.data || []);
      setCountries(c?.data?.data || []);
    }).catch(()=>{});
  }, []);

  useEffect(() => {
    if (mode !== "edit" || !movieId) {
      form.resetFields();
      return;
    }
    let alive = true;
    setLoading(true); setError(null);
    api.get(endpoints.movies.byId(movieId))
      .then(res => {
        if (!alive) return;
        const m = res?.data?.data;
        form.setFieldsValue({
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
      })
      .catch(e => setError(e?.response?.data?.message || e.message))
      .finally(() => setLoading(false));

    return () => { alive = false; };
  }, [movieId, mode]);

  const submit = async (vals) => {
    setSaving(true); setError(null);
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
        cast: [], // formdan yönetmiyoruz (CastPanel ayrı)
      };

      if (mode === "create") {
        const { data } = await api.post(endpoints.movies.create, body);
        onCreated?.(data?.data);
        form.resetFields();
      } else {
        await api.put(endpoints.movies.update(movieId), body);
        onSaved?.();
      }
    } catch (e) {
      setError(e?.response?.data?.message || e.message);
    } finally {
      setSaving(false);
    }
  };

  if (mode === "edit" && !movieId) return null;

  return (
    <>
      {error && <Alert type="error" showIcon style={{ marginBottom: 12 }} message="Error" description={error} />}
      {loading ? <Skeleton active /> : (
        <Form form={form} layout="vertical" onFinish={submit} requiredMark={false}>
          <Form.Item name="title" label="Title" rules={[{ required: true, message: "Required" }]}>
            <Input placeholder="Movie title" />
          </Form.Item>

          <Form.Item name="description" label="Description">
            <Input.TextArea rows={4} placeholder="Short description" />
          </Form.Item>

          <Form.Item name="releaseDate" label="Release Date">
            <DatePicker style={{ width: "100%" }} />
          </Form.Item>

          <Form.Item name="duration" label="Duration (min)">
            <InputNumber min={1} max={600} style={{ width: "100%" }} />
          </Form.Item>

          <Form.Item name="imageUrl" label="Image URL">
            <Input placeholder="https://..." />
          </Form.Item>

          <Form.Item name="rating" label="Rating (0-10)">
            <InputNumber min={0} max={10} step={0.1} style={{ width: "100%" }} />
          </Form.Item>

          <Form.Item name="status" label="Status">
            <Select options={STATUS_OPTS} allowClear />
          </Form.Item>

          <Form.Item name="contentRating" label="Content Rating">
            <Select options={CONTENT_RATING_OPTS} allowClear />
          </Form.Item>

          <Form.Item name="directorId" label="Director">
            <Select
              showSearch
              optionFilterProp="label"
              options={directors.map(d => ({ value: d.id, label: d.name }))}
              allowClear
            />
          </Form.Item>

          <Form.Item name="genreIds" label="Genres">
            <Select
              mode="multiple"
              options={genres.map(g => ({ value: g.id, label: g.name }))}
              allowClear
            />
          </Form.Item>

          <Form.Item name="languageIds" label="Languages">
            <Select
              mode="multiple"
              options={languages.map(l => ({ value: l.id, label: l.name }))}
              allowClear
            />
          </Form.Item>

          <Form.Item name="countryIds" label="Countries">
            <Select
              mode="multiple"
              options={countries.map(c => ({ value: c.id, label: c.name }))}
              allowClear
            />
          </Form.Item>

          <Space>
            <Button type="primary" htmlType="submit" loading={saving}>
              {mode === "create" ? "Create" : "Save"}
            </Button>
            <Button htmlType="button" onClick={() => form.resetFields()}>Reset</Button>
          </Space>
        </Form>
      )}
    </>
  );
}
