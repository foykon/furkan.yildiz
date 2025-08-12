import { useEffect } from "react";
import { logout } from "../auth/auth";
import { useNavigate } from "react-router-dom";

export default function Logout(){
  const nav = useNavigate();
  useEffect(() => {
    (async () => {
      try { await logout(); } finally { nav("/login", { replace: true }); }
    })();
  }, []);
  return null;
}
