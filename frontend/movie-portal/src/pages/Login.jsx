import { useEffect, useMemo, useState } from "react";
import {
  Card, Form, Input, Button, Checkbox, Typography, Alert, Modal, message
} from "antd";
import {
  UserOutlined, LockOutlined, SafetyOutlined, MailOutlined, KeyOutlined
} from "@ant-design/icons";
import { login } from "../auth/auth";
import { useLocation, useNavigate } from "react-router-dom";
import { api, endpoints } from "../lib/api";

const { Title, Text } = Typography;

export default function Login() {
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState(null);

  // forgot/reset modal state
  const [forgotOpen, setForgotOpen] = useState(false);
  const [resetOpen,  setResetOpen]  = useState(false);
  const [forgotLoading, setForgotLoading] = useState(false);
  const [resetLoading,  setResetLoading]  = useState(false);
  const [forgotForm] = Form.useForm();
  const [resetForm]  = Form.useForm();

  const nav = useNavigate();
  const loc = useLocation();
  const from = loc.state?.from?.pathname || "/";

  // URL ile gelen token varsa reset modalını otomatik aç
  useEffect(() => {
    const sp = new URLSearchParams(loc.search);
    const t = sp.get("token");
    if (t) {
      setResetOpen(true);
      resetForm.setFieldsValue({ token: t });
    }
  }, [loc.search]);

  const onFinish = async (values) => {
    const { username, password, remember } = values;
    setError(null); setLoading(true);
    try {
      await login({ username, password, remember });
      nav(from, { replace: true });
    } catch (e) {
      const msg = e?.response?.data?.message || e?.message || "Giriş başarısız";
      setError(msg);
    } finally { setLoading(false); }
  };

  // --- Forgot password submit ---
  const submitForgot = async (vals) => {
    setForgotLoading(true);
    try {
      await api.post(endpoints.auth.forgotPassword, { email: vals.email });
      message.success("Şifre sıfırlama bağlantısı e-postanıza gönderildi.");
      setForgotOpen(false);
      forgotForm.resetFields();
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "İşlem başarısız");
    } finally { setForgotLoading(false); }
  };

  // --- Reset password submit ---
  const submitReset = async (vals) => {
    if (vals.newPassword !== vals.confirm) {
      resetForm.setFields([{ name: "confirm", errors: ["Parolalar uyuşmuyor"] }]);
      return;
    }
    setResetLoading(true);
    try {
      await api.post(endpoints.auth.resetPassword, {
        token: vals.token,
        newPassword: vals.newPassword,
      });
      message.success("Parolanız güncellendi. Şimdi giriş yapabilirsiniz.");
      setResetOpen(false);
      resetForm.resetFields();
    } catch (e) {
      message.error(e?.response?.data?.message || e.message || "İşlem başarısız");
    } finally { setResetLoading(false); }
  };

  return (
    <div className="auth-wrap">
      <Card
        style={{ width: 420, background: "rgba(255,255,255,0.04)", backdropFilter:"blur(4px)" }}
        styles={{ body: { padding: 28 } }}
      >
        <div className="brand" style={{ marginBottom: 16 }}>
          <div className="brand-badge">Furki</div>
          <Title level={3} style={{ margin: 0 }}>Sign in</Title>
        </div>

        {error && (
          <Alert
            type="error"
            message="Giriş başarısız"
            description={error}
            showIcon
            style={{ marginBottom: 16 }}
          />
        )}

        <Form layout="vertical" initialValues={{ remember: true }} onFinish={onFinish} requiredMark={false}>
          <Form.Item
            label="Kullanıcı Adı"
            name="username"
            rules={[{ required: true, message: "Kullanıcı adını giriniz" }]}
          >
            <Input size="large" prefix={<UserOutlined />} placeholder="username" autoFocus allowClear />
          </Form.Item>

          <Form.Item
            label="Şifre"
            name="password"
            rules={[{ required: true, message: "Şifreyi giriniz" }]}
          >
            <Input.Password size="large" prefix={<LockOutlined />} placeholder="••••••••" />
          </Form.Item>

          <div style={{ display:"flex", alignItems:"center", marginBottom:12 }}>
            <Form.Item name="remember" valuePropName="checked" noStyle>
              <Checkbox>Remember me</Checkbox>
            </Form.Item>

            <div style={{ marginLeft:"auto", display:"flex", gap:12 }}>
              <a style={{ color:"var(--teal)" }} onClick={(e)=>{ e.preventDefault(); setForgotOpen(true); }}>
                Forgot password?
              </a>
              <a style={{ color:"var(--teal)" }} onClick={(e)=>{ e.preventDefault(); setResetOpen(true); }}>
                I have a reset code
              </a>
            </div>
          </div>

          <Button
            type="primary"
            size="large"
            htmlType="submit"
            icon={<SafetyOutlined />}
            block
            loading={loading}
          >
            Sign In
          </Button>
        </Form>
      </Card>

      {/* ---- Forgot Password Modal ---- */}
      <Modal
        title="Şifre Sıfırlama"
        open={forgotOpen}
        onCancel={()=>setForgotOpen(false)}
        footer={null}
        destroyOnClose
      >
        <Form layout="vertical" form={forgotForm} onFinish={submitForgot}>
          <Form.Item
            label="E-posta"
            name="email"
            rules={[
              { required: true, message: "E-posta adresinizi giriniz" },
              { type: "email", message: "Geçerli bir e-posta giriniz" },
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="you@example.com" />
          </Form.Item>
          <Button type="primary" htmlType="submit" loading={forgotLoading} block>
            E-posta Gönder
          </Button>
          <Text type="secondary" style={{ display:"block", marginTop:8 }}>
            E-postadaki bağlantı ile şifre sıfırlama sayfasına/jetonuna ulaşırsınız.
          </Text>
        </Form>
      </Modal>

      {/* ---- Reset Password Modal ---- */}
      <Modal
        title="Yeni Şifre Belirle"
        open={resetOpen}
        onCancel={()=>setResetOpen(false)}
        footer={null}
        destroyOnClose
      >
        <Form layout="vertical" form={resetForm} onFinish={submitReset}>
          <Form.Item
            label="Token / Kod"
            name="token"
            rules={[{ required: true, message: "E-postadaki token/kodu yapıştırın" }]}
          >
            <Input prefix={<KeyOutlined />} placeholder="reset token" />
          </Form.Item>

          <Form.Item
            label="Yeni Şifre"
            name="newPassword"
            rules={[
              { required: true, message: "Yeni şifrenizi giriniz" },
              { min: 8, message: "En az 8 karakter olmalı" },
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Yeni şifre" />
          </Form.Item>

          <Form.Item
            label="Yeni Şifre (Tekrar)"
            name="confirm"
            dependencies={["newPassword"]}
            rules={[{ required: true, message: "Tekrar giriniz" }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Yeni şifre (tekrar)" />
          </Form.Item>

          <Button type="primary" htmlType="submit" loading={resetLoading} block>
            Şifreyi Güncelle
          </Button>
        </Form>
      </Modal>
    </div>
  );
}
