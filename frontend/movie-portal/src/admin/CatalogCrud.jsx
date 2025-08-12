import { useEffect, useMemo, useState } from "react";
import { Row, Col, Card, Table, Input, Button, Form, Space, message, Popconfirm, Skeleton } from "antd";
import { api } from "../lib/api";

export default function CatalogCrud({ title, columns, fields, endpoints, sortField = "name" }) {
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(12);

  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const [form] = Form.useForm();
  const [mode, setMode] = useState("create"); // "create" | "edit"
  const [selectedId, setSelectedId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [initialLoading, setInitialLoading] = useState(false);

  const params = useMemo(() => ({
    q: q?.trim() || undefined,
    "pageable.page": page - 1,
    "pageable.size": pageSize,
    "pageable.sort": `${sortField},asc`,
  }), [q, page, pageSize, sortField]);

  useEffect(() => {
    let alive = true;
    setLoading(true);
    api.get(endpoints.list, { params })
      .then(res => {
        if (!alive) return;
        const data = res?.data?.data ?? [];
        const totalElements = res?.data?.totalElements ?? res?.data?.data?.totalElements ?? res?.data?.total ?? 0;
        setRows(data);
        setTotal(totalElements ?? data.length);
      })
      .finally(() => alive && setLoading(false));
    return () => { alive = false; };
  }, [JSON.stringify(params), endpoints.list]);

  const loadOne = async (id) => {
    setInitialLoading(true);
    try {
      const { data } = await api.get(endpoints.get(id));
      const rec = data?.data;
      form.setFieldsValue(rec || {});
    } finally {
      setInitialLoading(false);
    }
  };

  const onSelect = (id) => {
    setSelectedId(id);
    setMode("edit");
    loadOne(id).catch(() => {});
  };

  const onAddNew = () => {
    setSelectedId(null);
    setMode("create");
    form.resetFields();
  };

  const submit = async (vals) => {
    setSaving(true);
    try {
      if (mode === "create") {
        await api.post(endpoints.create, vals);
        message.success("Created");
        onAddNew();
        // refresh list
        setPage(1);
        setQ(q => q); // trigger
      } else {
        await api.put(endpoints.update(selectedId), vals);
        message.success("Saved");
        // refresh list
        setQ(q => q); // trigger
      }
    } catch (e) {
      message.error(e?.response?.data?.message || e.message);
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async () => {
    if (!selectedId) return;
    await api.delete(endpoints.delete(selectedId));
    message.success("Deleted");
    onAddNew();
    setQ(q => q); // trigger refresh
  };

  return (
    <Row gutter={16}>
      <Col xs={24} md={10} lg={9}>
        <Card
          title={title}
          extra={<a onClick={onAddNew}>Add New</a>}
        >
          <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
            <Input.Search
              placeholder="Search..."
              allowClear
              value={q}
              onChange={(e) => { setQ(e.target.value); setPage(1); }}
              onSearch={(v) => { setQ(v); setPage(1); }}
            />
          </div>

          <Table
            rowKey="id"
            loading={loading}
            size="small"
            columns={columns}
            dataSource={rows}
            onRow={(record) => ({ onClick: () => onSelect(record.id) })}
            pagination={{
              current: page,
              pageSize,
              total,
              showSizeChanger: true,
              pageSizeOptions: ["5", "10", "12", "20", "50", "100"],
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
            locale={{ emptyText: "No data" }}
          />
        </Card>
      </Col>

      <Col xs={24} md={14} lg={15}>
        <Card title={mode === "create" ? `Create ${title.slice(0,-1)}` : selectedId ? `Edit #${selectedId}` : "Select a record"}>
          {initialLoading ? <Skeleton active /> : (
            <Form layout="vertical" form={form} onFinish={submit} requiredMark={false}>
              {fields.map(f => (
                <Form.Item
                  key={f.name}
                  label={f.label}
                  name={f.name}
                  rules={f.required ? [{ required: true, message: "Required" }] : []}
                >
                  <Input placeholder={f.label} />
                </Form.Item>
              ))}

              <Space>
                <Button type="primary" htmlType="submit" loading={saving}>
                  {mode === "create" ? "Create" : "Save"}
                </Button>
                <Button htmlType="button" onClick={() => form.resetFields()}>Reset</Button>
                {mode === "edit" && selectedId && (
                  <Popconfirm title="Delete?" okButtonProps={{ danger: true }} onConfirm={onDelete}>
                    <Button danger>Delete</Button>
                  </Popconfirm>
                )}
              </Space>
            </Form>
          )}
        </Card>
      </Col>
    </Row>
  );
}
