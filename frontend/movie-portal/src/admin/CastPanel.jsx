import { useEffect, useMemo, useState } from "react";
import { Card, Table, Button, Modal, Form, Select, Input, InputNumber, Space, message } from "antd";
import { api, endpoints } from "../lib/api";

export default function CastPanel({ movieId }) {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);

  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null); // { id, actorId, roleName, castOrder }
  const [actors, setActors] = useState([]);

  const listActors = async (q) => {
    const params = {
      "q": q || undefined,
      "pageable.page": 0,
      "pageable.size": 200,
      "pageable.sort": "name,asc",
    };
    const res = await api.get(endpoints.actors.list, { params });
    setActors(res?.data?.data || []);
  };

  const load = useMemo(() => async () => {
    setLoading(true);
    try {
      const res = await api.get(endpoints.movies.cast.list(movieId));
      const data = res?.data?.data || [];
      data.sort((a,b) => (a.castOrder ?? 0) - (b.castOrder ?? 0));
      setRows(data);
    } finally {
      setLoading(false);
    }
  }, [movieId]);

  useEffect(() => { load(); }, [load]);
  useEffect(() => { listActors().catch(()=>{}); }, []);

  const columns = [
    { title: "Actor", dataIndex: "actorName" },
    { title: "Role",  dataIndex: "roleName" },
    { title: "Order", dataIndex: "castOrder", width: 90 },
    {
      title: "Actions",
      width: 160,
      render: (_, r) => (
        <Space>
          <Button size="small" onClick={() => { setEditing(r); setOpen(true); }}>Edit</Button>
          <Button size="small" danger onClick={() => remove(r)}>Delete</Button>
        </Space>
      )
    },
  ];

  const remove = async (row) => {
    try {
      await api.delete(endpoints.movies.cast.delete(movieId, row.id));
      message.success("Deleted");
      load();
    } catch(e) {
      message.error(e?.response?.data?.message || "Delete failed");
    }
  };

  const openCreate = () => { setEditing(null); setOpen(true); };

  return (
    <Card
      title="Cast"
      extra={<Button onClick={openCreate}>Add</Button>}
      size="small"
    >
      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={rows}
        pagination={false}
        size="small"
      />

      {open && (
        <CastModal
          open={open}
          onClose={() => setOpen(false)}
          movieId={movieId}
          editing={editing}
          actors={actors}
          onSearchActors={listActors}
          onDone={() => { setOpen(false); load(); }}
        />
      )}
    </Card>
  );
}

function CastModal({ open, onClose, movieId, editing, actors, onSearchActors, onDone }) {
  const [form] = Form.useForm();
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (editing) {
      form.setFieldsValue({
        actorId: editing.actorId,
        roleName: editing.roleName,
        castOrder: editing.castOrder ?? 0,
      });
    } else {
      form.resetFields();
      form.setFieldsValue({ castOrder: 0 });
    }
  }, [editing]);

  const submit = async (vals) => {
    setSaving(true);
    try{
      const body = { actorId: vals.actorId, roleName: vals.roleName, castOrder: vals.castOrder ?? 0 };
      if (editing?.id) {
        await api.put(endpoints.movies.cast.update(movieId, editing.id), body);
      } else {
        await api.post(endpoints.movies.cast.add(movieId), body);
      }
      onDone?.();
    } catch(e) {
      message.error(e?.response?.data?.message || "Save failed");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal
      open={open}
      onCancel={onClose}
      title={editing ? "Edit Cast Item" : "Add Cast Item"}
      okText="Save"
      onOk={() => form.submit()}
      confirmLoading={saving}
      destroyOnClose
    >
      <Form layout="vertical" form={form} onFinish={submit}>
        <Form.Item name="actorId" label="Actor" rules={[{ required: true, message: "Required" }]}>
          <Select
            showSearch
            optionFilterProp="label"
            onSearch={(val) => onSearchActors?.(val)}
            filterOption={false}
            options={actors.map(a => ({ value: a.id, label: a.name }))}
          />
        </Form.Item>
        <Form.Item name="roleName" label="Role name" rules={[{ required: true, message: "Required" }]}>
          <Input placeholder="e.g. John Wick" />
        </Form.Item>
        <Form.Item name="castOrder" label="Order">
          <InputNumber min={0} max={1000} style={{ width: "100%" }} />
        </Form.Item>
      </Form>
    </Modal>
  );
}
