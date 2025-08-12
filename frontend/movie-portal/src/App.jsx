import { Routes, Route, Navigate, useLocation, Outlet } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Search from "./pages/Search.jsx";
import Profile from "./pages/Profile.jsx";
import MyList from "./pages/MyList.jsx";
import Browse from "./pages/Browse.jsx";

import Navbar from "./components/Navbar.jsx";
import { getToken } from "./auth/auth";
import Logout from "./pages/Logout.jsx";
import MovieDetail from "./pages/MovieDetail.jsx";

function Protected() {
  const token = getToken();
  const loc = useLocation();
  if (!token) return <Navigate to="/login" replace state={{ from: loc }} />;
  return <Outlet />;
}

function Shell() {
  return (
    <>
      <Navbar />
      <div style={{ padding: 24, maxWidth: 1200, margin: "0 auto" }}>
        <Outlet />
      </div>
    </>
  );
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route element={<Protected />}>
        <Route element={<Shell />}>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<Search />} />
          <Route path="/profile" element={<Profile />} />
           <Route path="/logout" element={<Logout />} />
           <Route path="/browse" element={<Browse/>} />
           <Route path="/movie/:id" element={<MovieDetail />} />
          <Route path="/my/:type" element={<MyList />} /> {/* type=watch|favorite */}
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
