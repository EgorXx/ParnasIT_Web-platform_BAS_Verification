import { Routes, Route } from "react-router-dom";
import RoutePage from "./pages/RoutePage";
import ListPage from "./pages/ListPage";
import RouteViewPage from "./pages/RouteViewPage";

export default function App() {
  return (
    <Routes>
      <Route path="/route" element={<RoutePage />} />
      <Route path="/list" element={<ListPage />} />
      <Route path="/route/:id" element={<RouteViewPage />} />
    </Routes>
  );
}