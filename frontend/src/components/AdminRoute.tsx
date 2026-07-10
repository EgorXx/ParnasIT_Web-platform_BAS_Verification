import Cookies from "js-cookie";
import { Navigate } from "react-router-dom";
import AccessDeniedPage from "../pages/AccessDeniedPage";


export default function AdminRoute({
  children,
}: {
  children: React.ReactNode;
}) {

  const role = Cookies.get("role");


  if (role !== "ADMIN") {
    return <AccessDeniedPage />;
  }


  return children;
}