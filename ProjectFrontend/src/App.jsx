import { Routes, Route, Navigate, useLocation } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import MoviesPage from "./pages/MoviesPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
import Navbar from "./components/Navbar.jsx";
import "./components/navbar.css";
import WatchListPage from "./pages/WatchListPage.jsx";
import FavoriteListPage from "./pages/FavoriteListPage.jsx";

function isAuthed() {
  return !!(localStorage.getItem("access_token") || sessionStorage.getItem("access_token"));
}
function PrivateRoute({ children }) {
  return isAuthed() ? children : <Navigate to="/login" replace />;
}

export default function App() {
  const { pathname } = useLocation();
  const showNavbar = pathname !== "/login";

  return (
    <>
      {showNavbar && <Navbar />}
      <Routes>
        <Route path="/login" element={isAuthed() ? <Navigate to="/movies" replace /> : <LoginPage />} />
        <Route path="/movies" element={<PrivateRoute><MoviesPage /></PrivateRoute>} />
        <Route path="/watchlist" element={<PrivateRoute><WatchListPage /></PrivateRoute>} />
        <Route path="/favorites" element={<PrivateRoute><FavoriteListPage /></PrivateRoute>} />
        <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        <Route path="*" element={<Navigate to={isAuthed() ? "/movies" : "/login"} replace />} />
      </Routes>
    </>
  );
}
