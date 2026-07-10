import { Routes, Route } from "react-router-dom";

import RoutePage from "./pages/RoutePage";
import ListPage from "./pages/ListPage";
import RouteViewPage from "./pages/RouteViewPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";
import ZonePage from "./pages/ZonePage";
import ZoneCreatePage from "./pages/ZoneCreatePage";
import ZoneViewPage from "./pages/ZoneViewPage";
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



        <Route
          path="/zones"
          element={
            <AdminRoute>
              <ZonePage />
            </AdminRoute>
          }
        />


        <Route
          path="/zones/create"
          element={
            <AdminRoute>
              <ZoneCreatePage />
            </AdminRoute>
          }
        />


        <Route
          path="/zones/:id"
          element={
            <AdminRoute>
              <ZoneViewPage />
            </AdminRoute>
          }
        />

      </Route>


    </Routes>
  );
}