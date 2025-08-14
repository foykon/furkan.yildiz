import { useState } from "react";
import { Card, Form, Input, Button, Typography, message } from "antd";
import { MailOutlined } from "@ant-design/icons";
import { api, endpoints } from "../lib/api";
import { Link } from "react-router-dom";

const { Title, Text } = Typography;

export default function ForgotPassword(){
  const [loading, setLoading] = useState(false);

  const onFinish = async ({ email }) => {
    setLoading(true);
    try{
      await api.post(endpoints.auth.forgotPassword, { email });
      message.success("Sıfırlama bağlantısı e-postanıza gönderildi.");
    }catch(e){
      message.error(e?.response?.data?.message || e.message || "Gönderim başarısız");
    }finally{
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrap">
      <Card style={{ width: 420 }}>
        <Title level={3} style={{ marginTop: 0 }}>Şifre Sıfırlama</Title>
        <Text type="secondary">E-postana sıfırlama bağlantısı göndereceğiz.</Text>
        <Form layout="vertical" onFinish={onFinish} style={{ marginTop: 16 }}>
          <Form.Item
            name="email"
            label="E-posta"
            rules={[{ required: true, message: "E-posta giriniz" }, { type:"email", message:"Geçerli bir e-posta giriniz" }]}
          >
            <Input prefix={<MailOutlined />} placeholder="you@example.com" />
          </Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>Gönder</Button>
          <div style={{ marginTop: 12 }}>
            <Link to="/login">Girişe dön</Link>
          </div>
        </Form>
      </Card>
    </div>
  );
}
