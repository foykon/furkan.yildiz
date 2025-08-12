import { useRef } from "react";
import { Carousel, Button, Tag, Typography } from "antd";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import "./hero.css";

const { Title, Paragraph } = Typography;

export default function HeroSlider({ items = [] }) {
  const sliderRef = useRef(null);

  // relative URL -> absolute
  const abs = (u) => {
    if (!u) return null;
    if (/^https?:\/\//i.test(u)) return u;
    const base = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/+$/,'');
    return `${base}${u.startsWith("/") ? "" : "/"}${u}`;
  };

  return (
    <div className="hero">
      <Carousel ref={sliderRef} autoplay dots draggable>
        {items.map((m) => {
          const bg = abs(m.imageUrl || m.posterUrl || m.backdropUrl) || "https://via.placeholder.com/1200x500?text=Movie";
          return (
            <div key={m.id} className="hero-slide">
              <img className="hero-bg" src={bg} alt="" />
              <div className="hero-overlay">
                <div className="hero-meta">
                  <Tag color="cyan-inverse">HD</Tag>
                  {m.contentRating && <Tag className="cert-tag">{m.contentRating}</Tag>}
                  {m.duration ? <span>• {m.duration} min</span> : null}
                  {m.genres?.length ? <span>• {m.genres.map(g=>g.name).join(", ")}</span> : null}
                </div>
                <Title level={1} className="hero-title">{m.title}</Title>
                {m.description && <Paragraph ellipsis={{ rows: 2 }} className="hero-desc">{m.description}</Paragraph>}
                <div className="hero-cta">
                  <Button type="primary" size="large">Watch Now</Button>
                </div>
              </div>
            </div>
          );
        })}
      </Carousel>

      <Button className="hero-nav prev" type="text" icon={<LeftOutlined />} onClick={()=>sliderRef.current?.prev()} />
      <Button className="hero-nav next" type="text" icon={<RightOutlined />} onClick={()=>sliderRef.current?.next()} />
    </div>
  );
}
