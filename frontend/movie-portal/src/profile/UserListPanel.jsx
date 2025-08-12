import { useEffect, useMemo, useState } from "react";
import { Input, Table, Button } from "antd";
import { api, endpoints } from "../lib/api";

export default function UserListPanel({ onSelect, selectedId, onAddNew, onDeleted, reloadKey }) {
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);         
  const [pageSize, setPageSize] = useState(12);

  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const params = useMemo(() => ({
    "filter.username": q?.trim() || undefined,  
    "pageable.page": page - 1,
    "pageable.size": pageSize,
    "pageable.sort": "username,asc",
    username: q?.trim() || undefined,            
  }), [q, page, pageSize, reloadKey]);

  useEffect(() => {
    let alive = true;
    (async () => {
      setLoading(true);
      try {
        const res = await api.get(endpoints.users.search, { params });
        if (!alive) return;
        setRows(res?.data?.data ?? []);
        setTotal(res?.data?.totalElements ?? 0);
      } finally {
        alive && setLoading(false);
      }
    })();
    return () => { alive = false; };
  }, [JSON.stringify(params)]);

  const columns = [
    { title: "Username", dataIndex: "username" },
    { title: "Email", dataIndex: "email" },
    { title: "Enabled", dataIndex: "enabled", width: 90, render: (v) => (v ? "Yes" : "No") },
    { title: "Locked",  dataIndex: "locked",  width: 80, render: (v) => (v ? "Yes" : "No") },
  ];

  return (
    <>
      <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <Input.Search
          placeholder="Search username"
          allowClear
          value={q}
          onChange={(e) => { setQ(e.target.value); setPage(1); }}
          onSearch={(v) => { setQ(v); setPage(1); }}
        />
        <Button type="primary" onClick={() => onAddNew?.()}>Add User</Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        size="small"
        columns={columns}
        dataSource={rows}
        onRow={(record) => ({ onClick: () => onSelect?.(record.id) })}
        pagination={{
          current: page,
          pageSize,
          total,
          showSizeChanger: true,
          pageSizeOptions: ["1","5","10","20","50","100"],
          onChange: (p, s) => {
            if (s !== pageSize) {
              setPageSize(s);
              setPage(1);
            } else {
              setPage(p);
            }
          },
        }}
        rowClassName={(record) =>
          (record.id === selectedId ? "ant-table-row-selected " : "") + "row-clickable"
        }
        tableLayout="fixed"
        scroll={{ y: 360 }}
        locale={{ emptyText: "No users found" }}
      />
    </>
  );
}
