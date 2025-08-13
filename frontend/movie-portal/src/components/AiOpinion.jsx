// src/components/AiOpinion.jsx
import { useEffect, useRef, useState } from "react";
import { Card, Segmented, Switch, Button, Typography, Space, message } from "antd";
import { RobotOutlined, ThunderboltOutlined, StopOutlined, ReloadOutlined } from "@ant-design/icons";
import { api, endpoints } from "../lib/api";
import { getToken } from "../auth/auth";

const { Paragraph, Text } = Typography;

export default function AiOpinion({ movieId, defaultTone = "neutral", showTitle = true }) {
  const [tone, setTone] = useState(defaultTone);
  const [streaming, setStreaming] = useState(true);
  const [loading, setLoading] = useState(false);
  const [text, setText] = useState("");
  const abortRef = useRef(null);

  useEffect(() => () => { // unmount -> akışı durdur
    if (abortRef.current) abortRef.current.abort();
  }, []);

  const startStream = async () => {
    setLoading(true);
    setText("");
    const ctrl = new AbortController();
    abortRef.current = ctrl;

    try {
      const base = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/+$/,'');
      const qs = new URLSearchParams();
      if (tone) qs.set("tone", tone);

      const resp = await fetch(`${base}${endpoints.ai.stream(movieId)}?${qs.toString()}`, {
        method: "GET",
        headers: { Authorization: `Bearer ${getToken()}` },
        credentials: "include",
        signal: ctrl.signal,
      });

      if (!resp.ok || !resp.body) throw new Error(`AI stream HTTP ${resp.status}`);

      const reader = resp.body.getReader();
      const decoder = new TextDecoder();

      while (true) {
        const { value, done } = await reader.read();
        if (done) break;
        const chunk = decoder.decode(value, { stream: true });

        // Basit SSE ayrıştırma: "data: ..." satırlarını topla
        chunk.split(/\r?\n/).forEach((line) => {
          if (line.startsWith("data:")) {
            const data = line.slice(5).trim();
            if (data && data !== "[DONE]") {
              setText((prev) => prev + (prev && !/\s$/.test(prev) ? " " : "") + data);
            }
          }
        });
      }
    } catch (e) {
      if (e.name !== "AbortError") {
        console.error(e);
        message.error("AI akışı başlatılamadı, tek seferlik yanıt deneniyor.");
        await oneShot(); // stream patlarsa fallback
      }
    } finally {
      setLoading(false);
      abortRef.current = null;
    }
  };

  const oneShot = async () => {
    setLoading(true);
    setText("");
    try {
      const { data } = await api.post(endpoints.ai.comment(movieId), { tone });
      const comment = data?.data?.comment || "";
      setText(comment);
    } catch (e) {
      console.error(e);
      message.error(e?.response?.data?.message || "AI yanıtı alınamadı");
    } finally {
      setLoading(false);
    }
  };

  const handleGenerate = () => {
    if (streaming) startStream();
    else oneShot();
  };

  const handleStop = () => {
    if (abortRef.current) abortRef.current.abort();
    abortRef.current = null;
    setLoading(false);
  };

  return (
    <Card
      title={showTitle ? <Space><RobotOutlined /> <span>AI Yorumu</span></Space> : null}
      extra={
        <Space size="small">
          <Text type="secondary">Canlı</Text>
          <Switch size="small" checked={streaming} onChange={setStreaming} />
        </Space>
      }
    >
      <Space direction="vertical" style={{ width: "100%" }} size="middle">
        <Space wrap>
          <Segmented
            value={tone}
            onChange={setTone}
            options={[
              { label: "Düz", value: "neutral" },
              { label: "Samimi", value: "friendly" },
              { label: "Eleştirel", value: "critical" },
              { label: "Espiri", value: "funny" },
            ]}
          />
          <Button
            type="primary"
            icon={<ThunderboltOutlined />}
            loading={loading}
            onClick={handleGenerate}
          >
            {loading ? "Üretiliyor…" : "AI’den Yorum Al"}
          </Button>
          <Button icon={<ReloadOutlined />} disabled={loading} onClick={() => setText("")}>
            Temizle
          </Button>
          {loading && (
            <Button danger icon={<StopOutlined />} onClick={handleStop}>
              Durdur
            </Button>
          )}
        </Space>

        <Paragraph
          style={{
            minHeight: 80,
            whiteSpace: "pre-wrap",
            background: "#0b0b0b0d",
            borderRadius: 8,
            padding: 12,
            marginBottom: 0,
          }}
        >
          {text || <Text type="secondary">AI yorumunu almak için “AI’den Yorum Al”a tıkla.</Text>}
        </Paragraph>
      </Space>
    </Card>
  );
}
