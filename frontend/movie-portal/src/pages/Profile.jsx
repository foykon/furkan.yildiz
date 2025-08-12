import { useEffect, useState } from "react";
import { Card, Row, Col, message, Spin, Tabs } from "antd";
import { api, endpoints } from "../lib/api";
import { getPrincipal } from "../auth/auth";
import UserListPanel from "../profile/UserListPanel.jsx";
import UserForm from "../profile/UserForm.jsx";
import MovieAdminPanel from "../admin/MovieAdminPanel.jsx";
import CatalogCrud from "../admin/CatalogCrud.jsx";

export default function Profile() {
  const principal = getPrincipal();
  const isAdmin = !!principal?.isAdmin;

  const [selectedId, setSelectedId] = useState(null);
  const [mode, setMode] = useState("edit");
  const [loading, setLoading] = useState(false);
  const [reloadKey, setReloadKey] = useState(0);

  useEffect(() => {
    if (!isAdmin) {
      (async () => {
        setLoading(true);
        try {
          if (principal?.userId) {
            setSelectedId(principal.userId);
            setMode("edit");
            return;
          }
          const params = { "filter.username": principal?.username || "", "pageable.page": 0, "pageable.size": 1 };
          const { data } = await api.get(endpoints.users.search, { params });
          const first = data?.data?.[0];
          if (first) { setSelectedId(first.id); setMode("edit"); }
          else message.error("Kullanıcı bulunamadı.");
        } finally { setLoading(false); }
      })();
    }
  }, [isAdmin]);

  const refreshList = () => setReloadKey(k => k + 1);

  if (!isAdmin) {
    // Normal kullanıcı
    return (
      <div className="profile-page">
        <Spin spinning={loading}>
          <Row gutter={16}>
            <Col xs={24} md={14} lg={12} xl={10}>
              <Card title="My Profile">
                <UserForm
                  mode="edit"
                  userId={selectedId}
                  onSaved={() => message.success("Saved")}
                />
              </Card>
            </Col>
          </Row>
        </Spin>
      </div>
    );
  }

  // Admin görünümü (sekme bazlı)
  return (
    <div className="profile-page">
      <Tabs
        defaultActiveKey="users"
        items={[
          {
            key: "users",
            label: "Users",
            children: (
              <Row gutter={16}>
                <Col xs={24} md={9}>
                  <Card
                    className="users-card"
                    title="Users"
                    extra={<a onClick={() => { setSelectedId(null); setMode("create"); }}>Add User</a>}
                  >
                    <UserListPanel
                      reloadKey={reloadKey}
                      onAddNew={() => { setSelectedId(null); setMode("create"); }}
                      onSelect={(id) => { setSelectedId(id); setMode("edit"); }}
                      selectedId={selectedId}
                      onDeleted={(deletedId) => {
                        if (deletedId === selectedId) { setSelectedId(null); setMode("create"); }
                        refreshList();
                        message.success("User deleted");
                      }}
                    />
                  </Card>
                </Col>
                <Col xs={24} md={15}>
                  <Card
                    className="profile-right-card"
                    title={mode === "create" ? "Create User" : selectedId ? `Edit User #${selectedId}` : "Select a user"}
                  >
                    <UserForm
                      mode={mode}
                      userId={selectedId}
                      isAdmin
                      onCreated={(u) => { message.success("User created"); setSelectedId(u.id); setMode("edit"); refreshList(); }}
                      onSaved={() => { message.success("Saved"); refreshList(); }}
                      onDeleted={() => { message.success("User deleted"); setSelectedId(null); setMode("create"); refreshList(); }}
                    />
                  </Card>
                </Col>
              </Row>
            ),
          },
          {
            key: "movies",
            label: "Movies",
            children: <MovieAdminPanel />,
          },
          {
            key: "actors",
            label: "Actors",
            children: (
              <CatalogCrud
                title="Actors"
                sortField="name"
                columns={[
                  { title: "Name", dataIndex: "name" },
                  { title: "Nationality", dataIndex: "nationality" },
                ]}
                fields={[
                  { name: "name", label: "Name", required: true },
                  { name: "nationality", label: "Nationality" },
                ]}
                endpoints={{
                  list: endpoints.actors.list,
                  get: (id) => `/api/v1/actors/${id}`,
                  create: endpoints.actors.list,
                  update: (id) => `/api/v1/actors/${id}`,
                  delete: (id) => `/api/v1/actors/${id}`,
                }}
              />
            ),
          },
          {
            key: "directors",
            label: "Directors",
            children: (
              <CatalogCrud
                title="Directors"
                sortField="name"
                columns={[
                  { title: "Name", dataIndex: "name" },
                  { title: "Nationality", dataIndex: "nationality" },
                ]}
                fields={[
                  { name: "name", label: "Name", required: true },
                  { name: "nationality", label: "Nationality" },
                ]}
                endpoints={{
                  list: endpoints.directors.list,
                  get: (id) => `/api/v1/directors/${id}`,
                  create: endpoints.directors.list,
                  update: (id) => `/api/v1/directors/${id}`,
                  delete: (id) => `/api/v1/directors/${id}`,
                }}
              />
            ),
          },
          {
            key: "genres",
            label: "Genres",
            children: (
              <CatalogCrud
                title="Genres"
                sortField="name"
                columns={[{ title: "Name", dataIndex: "name" }]}
                fields={[{ name: "name", label: "Name", required: true }]}
                endpoints={{
                  list: endpoints.genres.list,
                  get: (id) => `/api/v1/genres/${id}`,
                  create: endpoints.genres.list,
                  update: (id) => `/api/v1/genres/${id}`,
                  delete: (id) => `/api/v1/genres/${id}`,
                }}
              />
            ),
          },
          {
            key: "languages",
            label: "Languages",
            children: (
              <CatalogCrud
                title="Languages"
                sortField="name"
                columns={[
                  { title: "Name", dataIndex: "name" },
                  { title: "ISO Code", dataIndex: "isoCode", width: 120 },
                ]}
                fields={[
                  { name: "name", label: "Name", required: true },
                  { name: "isoCode", label: "ISO Code" },
                ]}
                endpoints={{
                  list: endpoints.languages.list,
                  get: (id) => `/api/v1/languages/${id}`,
                  create: endpoints.languages.list,
                  update: (id) => `/api/v1/languages/${id}`,
                  delete: (id) => `/api/v1/languages/${id}`,
                }}
              />
            ),
          },
          {
            key: "countries",
            label: "Countries",
            children: (
              <CatalogCrud
                title="Countries"
                sortField="name"
                columns={[{ title: "Name", dataIndex: "name" }]}
                fields={[{ name: "name", label: "Name", required: true }]}
                endpoints={{
                  list: endpoints.countries.list,
                  get: (id) => `/api/v1/countries/${id}`,
                  create: endpoints.countries.list,
                  update: (id) => `/api/v1/countries/${id}`,
                  delete: (id) => `/api/v1/countries/${id}`,
                }}
              />
            ),
          },
        ]}
      />
    </div>
  );
}
