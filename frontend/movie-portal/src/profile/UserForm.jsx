import { useEffect, useState } from "react";
import { Alert, Button, Form, Input, Select, Switch, Space, Skeleton, message, Popconfirm } from "antd";
import { api, endpoints } from "../lib/api";

export default function UserForm({ userId, isAdmin=false, mode="edit", onSaved, onCreated, onDeleted }){
  const [form] = Form.useForm();
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  // Roles (admin ise)
  useEffect(() => {
    if (!isAdmin) return;
    api.get(endpoints.roles.list)
      .then(res => setRoles(res.data?.data || []))
      .catch(()=>{});
  }, [isAdmin]);

  // Edit modunda kullanıcı detayını çek
  useEffect(() => {
    if (mode !== "edit" || !userId) {
      form.resetFields();
      if (mode === "create") {
        form.setFieldsValue({ roles: ["ROLE_USER"], enabled: true, locked: false });
      }
      return;
    }
    setLoading(true); setError(null);
    api.get(endpoints.users.get(userId))
      .then(res => {
        const u = res.data?.data;
        form.setFieldsValue({
          username: u?.username,
          email: u?.email,
          password: "",
          enabled: u?.enabled,
          locked: u?.locked,
          roles: (u?.roles || []).map(r => r.name),
        });
      })
      .catch((e) => setError(e?.response?.data?.message || e.message))
      .finally(() => setLoading(false));
  }, [userId, mode]);

  const submit = async (vals) => {
    setSaving(true); setError(null);
    try{
      if (mode === "create") {
        // create: username, email, password, roles
        const body = {
          username: vals.username,
          email: vals.email,
          password: vals.password,
          roles: vals.roles || ["ROLE_USER"],
        };
        const { data } = await api.post(endpoints.users.create, body);
        onCreated?.(data?.data);
      } else {
        // edit (patch)
        if (!userId) return;
        const body = {
          username: vals.username,
          email: vals.email,
        };
        if (vals.password) body.password = vals.password;
        if (isAdmin) {
          body.enabled = !!vals.enabled;
          body.locked = !!vals.locked;
          body.roles = vals.roles || [];
        }
        const { data } = await api.patch(endpoints.users.patch(userId), body);
        onSaved?.(data?.data);
      }
    } catch(e){
      setError(e?.response?.data?.message || e.message);
    } finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!userId) return;
    await api.delete(endpoints.users.delete(userId));
    onDeleted?.(userId);
  };

  if (mode === "edit" && !userId) return <Alert type="info" message="Kullanıcı seçin." />;

  return (
    <>
      {error && <Alert type="error" showIcon message="Hata" description={error} style={{ marginBottom: 12 }} />}
      {loading ? <Skeleton active /> : (
        <Form layout="vertical" form={form} onFinish={submit} requiredMark={false}>
          <Form.Item label="Kullanıcı Adı" name="username" rules={[{ required: true, message: "Zorunlu" }]}>
            <Input placeholder="username" />
          </Form.Item>
          <Form.Item label="Email" name="email" rules={[{ type: "email", message: "Geçersiz email" }]}>
            <Input placeholder="email@example.com" />
          </Form.Item>

          <Form.Item
            label={mode === "create" ? "Şifre" : "Yeni Şifre"}
            name="password"
            rules={mode === "create" ? [{ required: true, message: "Zorunlu" }] : []}
            tooltip={mode === "create" ? undefined : "Boş bırakırsan şifre değişmez"}
          >
            <Input.Password placeholder="••••••••" />
          </Form.Item>

          {isAdmin && (
            <>
              {mode === "edit" && (
                <>
                  <Form.Item label="Enabled" name="enabled" valuePropName="checked">
                    <Switch />
                  </Form.Item>
                  <Form.Item label="Locked" name="locked" valuePropName="checked">
                    <Switch />
                  </Form.Item>
                </>
              )}
              <Form.Item label="Roles" name="roles">
                <Select
                  mode="multiple"
                  placeholder="Seçiniz"
                  options={roles.map(r => ({ value: r.name, label: r.name }))}
                />
              </Form.Item>
            </>
          )}

          <Space>
            <Button type="primary" htmlType="submit" loading={saving}>
              {mode === "create" ? "Create" : "Save"}
            </Button>
            <Button htmlType="button" onClick={() => form.resetFields()}>Reset</Button>

            {isAdmin && mode === "edit" && userId && (
              <Popconfirm title="Delete user?" okButtonProps={{ danger: true }} onConfirm={handleDelete}>
                <Button danger>Delete</Button>
              </Popconfirm>
            )}
          </Space>
        </Form>
      )}
    </>
  );
}
