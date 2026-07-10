import { Routes, Route } from "react-router-dom";

import RoutePage from "./pages/RoutePage";
import ListPage from "./pages/ListPage";
import RouteViewPage from "./pages/RouteViewPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";


export default function App() {
  return (
    <Routes>

      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        path="/register"
        element={<RegisterPage />}
      />

      <Route
        path="/route"
        element={<RoutePage />}
      />

      <Route
        path="/list"
        element={<ListPage />}
      />

      <Route
        path="/route/:id"
        element={<RouteViewPage />}
      />

    </Routes>
  );
}