import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { ConfigProvider, theme } from "antd";
import App from "./App.jsx";
import "antd/dist/reset.css";
import "./styles.css";

const teal = "#00BCD4";
const dark = "#1F232B";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
    token: {
      colorPrimary: "#00BCD4",
      colorBgLayout: "#1F232B",
      colorTextBase: "#FFFFFF",
      borderRadius: 10,
      fontSize: 14,
    },
      }}
    >
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </ConfigProvider>
  </React.StrictMode>
);
