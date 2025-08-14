import { useEffect } from "react";
import { Card, Form, Input, Button, Typography, message } from "antd";
import { KeyOutlined, LockOutlined } from "@ant-design/icons";
import { useLocation, useNavigate, Link } from "react-router-dom";
import { api, endpoints } from "../lib/api";

const { Title, Text } = Typography;

export default function ResetPassword(){
  const [form] = Form.useForm();
  const loc = useLocation();
  const nav = useNavigate();

  useEffect(() => {
    const sp = new URLSearchParams(loc.search);
    const t = sp.get("token");
    if (t) form.setFieldsValue({ token: t });
  }, [loc.search]);

  const onFinish = async (vals) => {
    if (vals.newPassword !== vals.confirm) {
      form.setFields([{ name:"confirm", errors:["Parolalar uyuşmuyor"] }]);
      return;
    }
    try {
      await api.post(endpoints.auth.resetPassword, {
        token: vals.token,
        newPassword: vals.newPassword,
      });
      message.success("Parolanız güncellendi. Şimdi giriş yapabilirsiniz.");
      nav("/login");
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "İşlem başarısız");
    }
  };

  return (
    <div className="auth-wrap">
      <Card style={{ width: 420 }}>
        <Title level={3} style={{ marginTop: 0 }}>Yeni Şifre Belirle</Title>
        <Text type="secondary">E-postadaki bağlantıdan geldiyseniz token alanı otomatik dolar.</Text>

        <Form layout="vertical" form={form} onFinish={onFinish} style={{ marginTop: 16 }}>
          <Form.Item
            name="token"
            label="Token / Kod"
            rules={[{ required: true, message: "E-postadaki token/kodu yapıştırın" }]}
          >
            <Input prefix={<KeyOutlined />} placeholder="reset token" />
          </Form.Item>

          <Form.Item
            name="newPassword"
            label="Yeni Şifre"
            rules={[{ required: true, message: "Yeni şifreyi giriniz" }, { min: 8, message: "En az 8 karakter" }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Yeni şifre" />
          </Form.Item>

          <Form.Item
            name="confirm"
            label="Yeni Şifre (Tekrar)"
            dependencies={["newPassword"]}
            rules={[{ required: true, message: "Tekrar giriniz" }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Yeni şifre (tekrar)" />
          </Form.Item>

          <Button type="primary" htmlType="submit" block>Şifreyi Güncelle</Button>

          <div style={{ marginTop: 12 }}>
            <Link to="/login">Girişe dön</Link>
          </div>
        </Form>
      </Card>
    </div>
  );
}
