import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { api, endpoints } from "../lib/api";
import { List, Tag, Pagination, Typography } from "antd";
const { Text } = Typography;

export default function Search(){
  const [sp, setSp] = useSearchParams();
  const [data, setData] = useState({ data: [], totalElements: 0, totalPages: 0, currentPage: 0, pageSize: 12 });
  const [loading, setLoading] = useState(false);

  const page = Number(sp.get("page") || 0);
  const size = Number(sp.get("size") || 12);
  const q = sp.get("q") || "";
  const genreId = sp.get("genreId");
  const countryId = sp.get("countryId");
  const directorId = sp.get("directorId");

  const params = useMemo(() => {
    const filter = {};
    if (q) filter.title = q;
    if (genreId) filter.genreId = Number(genreId);
    if (countryId) filter.countryId = Number(countryId);
    if (directorId) filter.directorId = Number(directorId);
    const pageable = { page, size, sort: ["title,asc"] };
    return { filter, pageable };
  }, [q, genreId, countryId, directorId, page, size]);

  useEffect(() => {
    setLoading(true);
    api.get(endpoints.movies.search, { params })
      .then(res => setData(res.data))
      .finally(()=> setLoading(false));
  }, [JSON.stringify(params)]);

  const onPage = (next) => {
    sp.set("page", String(next - 1)); // antd 1-based, backend 0-based
    setSp(sp, { replace: true });
  };

  return (
    <div>
      <h2>Search</h2>
      <div style={{ marginBottom: 12 }}>
        {q && <Tag color="cyan">q: {q}</Tag>}
        {genreId && <Tag color="geekblue">genreId: {genreId}</Tag>}
        {countryId && <Tag color="purple">countryId: {countryId}</Tag>}
        {directorId && <Tag color="gold">directorId: {directorId}</Tag>}
      </div>

      <List
        loading={loading}
        bordered
        dataSource={data.data}
        renderItem={(m) => (
          <List.Item>
            <div style={{ display:"grid" }}>
              <Text strong>{m.title}</Text>
              <Text type="secondary">{m.releaseDate || "-"} • {m.status || "-"} • {m.contentRating || "-"}</Text>
            </div>
          </List.Item>
        )}
      />

      <div style={{ display:"flex", justifyContent:"flex-end", marginTop: 12 }}>
        <Pagination
          current={data.currentPage + 1}
          pageSize={data.pageSize}
          total={data.totalElements}
          onChange={onPage}
          showSizeChanger={false}
        />
      </div>
    </div>
  );
}
