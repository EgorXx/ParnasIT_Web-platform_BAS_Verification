import { Routes, Route } from "react-router-dom";

import RoutePage from "./pages/routes /RoutesNewPage";
import ListPage from "./pages/routes /RoutesListPage";
import RouteViewPage from "./pages/routes /RoutesViewPage";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";
import ZonePage from "./pages/zones/ZonesListPage";
import ZoneCreatePage from "./pages/zones/ZonesNewPage";
import AdminRoute from "./components/AdminRoute";
import HomePage from "./pages/HomePage";
import Layout from "./Layout";


export default function App() {
  return (
    <Routes>


      {/* Страницы без Header */}

      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        path="/register"
        element={<RegisterPage />}
      />



      {/* Все страницы с Header */}

      <Route element={<Layout />}>

        <Route
          path="/"
          element={<HomePage />}
        />


        <Route
          path="/routes/new"
          element={
            <ProtectedRoute>
              <RoutePage />
            </ProtectedRoute>
          }
        />


        <Route
          path="/routes"
          element={
            <ProtectedRoute>
              <ListPage />
            </ProtectedRoute>
          }
        />


        <Route
          path="/routes/:id"
          element={
            <ProtectedRoute>
              <RouteViewPage />
            </ProtectedRoute>
          }
        />



        <Route
          path="/admin/zones"
          element={
            <AdminRoute>
              <ZonePage />
            </AdminRoute>
          }
        />


        <Route
          path="/admin/zones/new"
          element={
            <AdminRoute>
              <ZoneCreatePage />
            </AdminRoute>
          }
        />

      </Route>


    </Routes>
  );
}