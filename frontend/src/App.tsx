import { Routes, Route } from "react-router-dom";

import RoutePage from "./pages/RoutePage";
import ListPage from "./pages/ListPage";
import RouteViewPage from "./pages/RouteViewPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";


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
        element={
          <ProtectedRoute>
            <RoutePage />
          </ProtectedRoute>
        }
      />


      <Route
        path="/list"
        element={
          <ProtectedRoute>
            <ListPage />
          </ProtectedRoute>
        }
      />


      <Route
        path="/route/:id"
        element={
          <ProtectedRoute>
            <RouteViewPage />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}