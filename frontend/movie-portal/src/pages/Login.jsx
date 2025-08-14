import { useState } from "react";
import { Card, Form, Input, Button, Checkbox, Typography, Alert } from "antd";
import { UserOutlined, LockOutlined, SafetyOutlined } from "@ant-design/icons";
import { login } from "../auth/auth";
import { useLocation, useNavigate } from "react-router-dom";

const { Title, Text } = Typography;

export default function Login(){
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState(null);
  const nav = useNavigate();
  const loc = useLocation();
  const from = loc.state?.from?.pathname || "/";

  const onFinish = async (values) => {
    const { username, password, remember } = values;
    setError(null); setLoading(true);
    try{
      await login({ username, password, remember });
      nav(from, { replace: true });
    }catch(e){
      const msg = e?.response?.data?.message || e?.message || "Giriş başarısız";
      setError(msg);
    }finally{ setLoading(false); }
  };

  return (
    <div className="auth-wrap">
      <Card style={{ width: 420, background: "rgba(255,255,255,0.04)", backdropFilter:"blur(4px)" }} styles={{ body: { padding: 28 } }}>
        <div className="brand" style={{ marginBottom: 16 }}>
          <div className="brand-badge">Furki</div>
          <Title level={3} style={{ margin: 0 }}>Sign in</Title>
        </div>
        {error && <Alert type="error" message="Giriş başarısız" description={error} showIcon style={{ marginBottom: 16 }} />}
        <Form layout="vertical" initialValues={{ remember: true }} onFinish={onFinish} requiredMark={false}>
          <Form.Item label="Kullanıcı Adı" name="username" rules={[{ required: true, message: "Kullanıcı adını giriniz" }]} >
            <Input size="large" prefix={<UserOutlined />} placeholder="username" autoFocus allowClear />
          </Form.Item>
          <Form.Item label="Şifre" name="password" rules={[{ required: true, message: "Şifreyi giriniz" }]} >
            <Input.Password size="large" prefix={<LockOutlined />} placeholder="••••••••" />
          </Form.Item>
          <div style={{ display:"flex", alignItems:"center", marginBottom:12 }}>
            <Form.Item name="remember" valuePropName="checked" noStyle>
              <Checkbox>Remember me</Checkbox>
            </Form.Item>
            <a style={{ marginLeft:"auto", color:"var(--teal)" }} onClick={(e)=>e.preventDefault()}>Forgot password?</a>
          </div>
          <Button type="primary" size="large" htmlType="submit" icon={<SafetyOutlined />} block loading={loading}>Sign In</Button>
        </Form>
      </Card>
    </div>
  );
}
