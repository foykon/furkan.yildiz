// src/components/Comments.jsx
import { useEffect, useMemo, useState } from "react";
import { List, Avatar, Input, Button, Space, Popconfirm, Pagination, message } from "antd";
import { api, endpoints } from "../lib/api";
import { getPrincipal } from "../auth/auth"; // <-- ekledik

const { TextArea } = Input;

function formatDate(dt) {
  try {
    const d = new Date(dt);
    return new Intl.DateTimeFormat(undefined, {
      year:"numeric", month:"short", day:"2-digit",
      hour:"2-digit", minute:"2-digit"
    }).format(d);
  } catch { return dt; }
}

export default function Comments({ movieId }) {
  // --- ADMIN KONTROLÜ ---
  const principal = useMemo(() => getPrincipal(), []);
  const isAdmin = !!principal?.isAdmin
    || (principal?.roles || []).some(r => String(r?.name || r).toUpperCase().includes("ADMIN"));

  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [page, setPage]   = useState(1);
  const [size, setSize]   = useState(10);
  const [loading, setLoading] = useState(false);

  const [newText, setNewText] = useState("");
  const [saving, setSaving] = useState(false);

  const [editingId, setEditingId] = useState(null);
  const [editingText, setEditingText] = useState("");
  const [updating, setUpdating] = useState(false);

  const sort = useMemo(() => ["createdAt,desc"], []);

  useEffect(() => {
    let alive = true;
    if (!movieId) return;
    setLoading(true);
    api.get(endpoints.comments.list(movieId), { params: { page: page - 1, size, sort } })
      .then(res => {
        if (!alive) return;
        const data = res?.data?.data ?? [];
        const totalElements = res?.data?.totalElements ?? data.length;
        setRows(data);
        setTotal(totalElements);
      })
      .finally(() => alive && setLoading(false));
    return () => { alive = false; };
  }, [movieId, page, size, sort]);

  const submitNew = async () => {
    const content = newText.trim();
    if (!content) return;
    setSaving(true);
    try {
      const res = await api.post(endpoints.comments.add(movieId), { content });
      const created = res?.data?.data;
      message.success("Comment added");
      setNewText("");
      if (page === 1) {
        setRows(prev => [created, ...prev].slice(0, size));
        setTotal(t => t + 1);
      } else {
        setPage(1);
      }
    } catch (e) {
      message.error(e?.response?.data?.message || "Failed to add comment");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (c) => { setEditingId(c.id); setEditingText(c.content || ""); };
  const cancelEdit = () => { setEditingId(null); setEditingText(""); };

  const saveEdit = async () => {
    const content = editingText.trim();
    if (!content || !editingId) return;
    setUpdating(true);
    try {
      const res = await api.patch(endpoints.comments.update(movieId, editingId), { content });
      const updated = res?.data?.data;
      setRows(prev => prev.map(x => x.id === editingId ? updated : x));
      message.success("Updated");
      cancelEdit();
    } catch (e) {
      message.error(e?.response?.data?.message || "Update failed");
    } finally {
      setUpdating(false);
    }
  };

  const remove = async (id) => {
    try {
      await api.delete(endpoints.comments.delete(movieId, id));
      setRows(prev => prev.filter(x => x.id !== id));
      setTotal(t => Math.max(0, t - 1));
      message.success("Deleted");
      if (rows.length === 1 && page > 1) setPage(p => p - 1);
    } catch (e) {
      message.error(e?.response?.data?.message || "Delete failed");
    }
  };

  return (
    <div className="comments-box">
      {/* Yeni yorum */}
      <div className="new-comment">
        <TextArea
          value={newText}
          onChange={(e)=>setNewText(e.target.value)}
          rows={3}
          maxLength={1000}
          placeholder="Write a comment…"
          showCount
        />
        <div style={{ marginTop: 8 }}>
          <Button type="primary" onClick={submitNew} loading={saving} disabled={!newText.trim()}>
            Send
          </Button>
        </div>
      </div>

      {/* Liste */}
      <List
        loading={loading}
        dataSource={rows}
        locale={{ emptyText: "No comments yet" }}
        renderItem={(item) => {
          const isEditing = editingId === item.id;
          const canDelete = item.mine || isAdmin; // <-- admin herkesinkini silebilir
          return (
            <List.Item
              key={item.id}
              actions={[
                // Edit: sadece sahibi düzenleyebilsin
                ...(item.mine && !isEditing ? [
                  <Button key="edit" type="link" onClick={()=>startEdit(item)}>Edit</Button>
                ] : []),
                // Delete: sahibi VEYA admin
                ...(canDelete ? [
                  <Popconfirm
                    key="del"
                    title="Delete this comment?"
                    okButtonProps={{ danger: true }}
                    onConfirm={()=>remove(item.id)}
                  >
                    <Button type="link" danger>Delete</Button>
                  </Popconfirm>
                ] : [])
              ]}
            >
              <List.Item.Meta
                avatar={<Avatar>{(item.username || "?").slice(0,1).toUpperCase()}</Avatar>}
                title={
                  <div style={{ display:"flex", gap:8, alignItems:"baseline", flexWrap:"wrap" }}>
                    <strong>{item.username || "Anonymous"}</strong>
                    <span style={{ opacity:.7, fontSize:12 }} title={item.createdAt}>
                      {formatDate(item.createdAt)}
                    </span>
                  </div>
                }
                description={
                  isEditing ? (
                    <div>
                      <TextArea
                        value={editingText}
                        onChange={(e)=>setEditingText(e.target.value)}
                        rows={3}
                        maxLength={1000}
                        showCount
                      />
                      <Space style={{ marginTop: 8 }}>
                        <Button type="primary" onClick={saveEdit} loading={updating} disabled={!editingText.trim()}>
                          Save
                        </Button>
                        <Button onClick={cancelEdit}>Cancel</Button>
                      </Space>
                    </div>
                  ) : (
                    <div style={{ whiteSpace:"pre-wrap" }}>{item.content}</div>
                  )
                }
              />
            </List.Item>
          );
        }}
      />

      {/* Sayfalama */}
      <div style={{ display:"flex", justifyContent:"center", marginTop: 12 }}>
        <Pagination
          current={page}
          pageSize={size}
          total={total}
          showSizeChanger
          pageSizeOptions={["5","10","20","50"]}
          onChange={(p, s) => { setPage(1); setSize(s); setPage(p); }}
        />
      </div>
    </div>
  );
}
