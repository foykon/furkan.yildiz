import { useEffect, useMemo, useState } from "react";
import { Table, Button, Modal, Form, Input, InputNumber, Space, Popconfirm, message, Typography } from "antd";
import { ArrowUpOutlined, ArrowDownOutlined, PlusOutlined, EditOutlined, DeleteOutlined, ReloadOutlined } from "@ant-design/icons";
import { api, endpoints } from "../../lib/api";
import ActorSelect from "../../components/ActorSelect.jsx";

const { Text } = Typography;

export default function MovieCastPanel({ movieId, onChanged, className }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const [openAdd, setOpenAdd] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [editing, setEditing] = useState(null);

  const [form] = Form.useForm();
  const [editForm] = Form.useForm();

  const normalized = useMemo(() => {
    return (items || [])
      .map((r, idx) => ({
        castId: r.id ?? r.castId,
        actorId: r.actorId,
        actorName: r.actorName,
        roleName: r.roleName,
        castOrder: Number.isFinite(r.castOrder) ? r.castOrder : (idx + 1),
      }))
      .sort((a, b) => (a.castOrder ?? 0) - (b.castOrder ?? 0));
  }, [items]);

  const load = async () => {
    if (!movieId) return;
    setLoading(true);
    try {
      const url = endpoints.movies.cast.list(movieId);
      const { data } = await api.get(url);
      setItems(data?.data ?? []);
    } catch (e) {
      message.error("Cast listesi alınamadı");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [movieId]);

  const notifyChanged = () => { onChanged?.(); };

  const onAdd = async () => {
    try {
      const vals = await form.validateFields();
      const body = {
        actorId: vals.actorId,
        roleName: vals.roleName?.trim(),
        castOrder: vals.castOrder ?? undefined,
      };
      await api.post(endpoints.movies.cast.add(movieId), body);
      message.success("Cast eklendi");
      setOpenAdd(false);
      form.resetFields();
      await load();
      notifyChanged();
    } catch (e) {
      if (e?.errorFields) return; // form
      message.error(e?.response?.data?.message || "Ekleme başarısız");
    }
  };

  const openEditModal = (row) => {
    setEditing(row);
    editForm.setFieldsValue({
      actorId: row.actorId,
      roleName: row.roleName,
      castOrder: row.castOrder,
    });
    setOpenEdit(true);
  };

  const onSaveEdit = async () => {
    try {
      const vals = await editForm.validateFields();
      const body = {
        actorId: vals.actorId,
        roleName: vals.roleName?.trim(),
        castOrder: vals.castOrder ?? undefined,
      };
      await api.put(endpoints.movies.cast.update(movieId, editing.castId), body);
      message.success("Cast güncellendi");
      setOpenEdit(false);
      setEditing(null);
      await load();
      notifyChanged();
    } catch (e) {
      if (e?.errorFields) return;
      message.error(e?.response?.data?.message || "Güncelleme başarısız");
    }
  };

  const onDelete = async (row) => {
    try {
      await api.delete(endpoints.movies.cast.delete(movieId, row.castId));
      message.success("Silindi");
      await load();
      notifyChanged();
    } catch (e) {
      message.error(e?.response?.data?.message || "Silme başarısız");
    }
  };

  const swapOrder = async (a, b) => {
    if (!a || !b) return;
    try {
      const bodyA = { actorId: a.actorId, roleName: a.roleName, castOrder: b.castOrder ?? 0 };
      const bodyB = { actorId: b.actorId, roleName: b.roleName, castOrder: a.castOrder ?? 0 };
      await Promise.all([
        api.put(endpoints.movies.cast.update(movieId, a.castId), bodyA),
        api.put(endpoints.movies.cast.update(movieId, b.castId), bodyB),
      ]);
      await load();
      notifyChanged();
    } catch (e) {
      message.error("Sıralama başarısız");
    }
  };

  const columns = [
    {
      title: "#",
      dataIndex: "castOrder",
      width: 70,
      render: (v) => <Text type="secondary">{v ?? "-"}</Text>,
      sorter: (a, b) => (a.castOrder ?? 0) - (b.castOrder ?? 0),
    },
    { title: "Actor", dataIndex: "actorName" },
    { title: "Role", dataIndex: "roleName" },
    {
      title: "Actions",
      key: "actions",
      width: 220,
      render: (_, row, idx) => {
        const prev = normalized[idx - 1];
        const next = normalized[idx + 1];
        return (
          <Space>
            <Button size="small" icon={<ArrowUpOutlined />} disabled={!prev} onClick={() => swapOrder(row, prev)} />
            <Button size="small" icon={<ArrowDownOutlined />} disabled={!next} onClick={() => swapOrder(row, next)} />
            <Button size="small" icon={<EditOutlined />} onClick={() => openEditModal(row)}>Edit</Button>
            <Popconfirm title="Silinsin mi?" onConfirm={() => onDelete(row)}>
              <Button size="small" danger icon={<DeleteOutlined />}>Delete</Button>
            </Popconfirm>
          </Space>
        );
      }
    }
  ];

  return (
    <div className={className}>
      <Space style={{ marginBottom: 12 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setOpenAdd(true)}>
          Add Cast
        </Button>
        <Button icon={<ReloadOutlined />} onClick={load}>Refresh</Button>
      </Space>

      <Table
        rowKey={(r) => r.castId}
        loading={loading}
        dataSource={normalized}
        columns={columns}
        size="middle"
        pagination={false}
      />

      {/* Add Modal */}
      <Modal
        title="Add Cast"
        open={openAdd}
        onCancel={() => setOpenAdd(false)}
        onOk={onAdd}
        okText="Add"
        destroyOnClose
      >
        <Form form={form} layout="vertical">
          <Form.Item name="actorId" label="Actor" rules={[{ required: true, message: "Actor seçiniz" }]}>
            <ActorSelect />
          </Form.Item>
          <Form.Item name="roleName" label="Role Name" rules={[{ required: true, message: "Role giriniz" }]}>
            <Input maxLength={100} />
          </Form.Item>
          <Form.Item name="castOrder" label="Order (optional)">
            <InputNumber min={0} max={1000} style={{ width: "100%" }} />
          </Form.Item>
        </Form>
      </Modal>

      {/* Edit Modal */}
      <Modal
        title="Edit Cast"
        open={openEdit}
        onCancel={() => { setOpenEdit(false); setEditing(null); }}
        onOk={onSaveEdit}
        okText="Save"
        destroyOnClose
      >
        <Form form={editForm} layout="vertical">
          <Form.Item name="actorId" label="Actor" rules={[{ required: true, message: "Actor seçiniz" }]}>
            <ActorSelect />
          </Form.Item>
          <Form.Item name="roleName" label="Role Name" rules={[{ required: true, message: "Role giriniz" }]}>
            <Input maxLength={100} />
          </Form.Item>
          <Form.Item name="castOrder" label="Order (optional)">
            <InputNumber min={0} max={1000} style={{ width: "100%" }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
