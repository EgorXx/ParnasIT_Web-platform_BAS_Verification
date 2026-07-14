import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type RouteStatus = "SUBMITTED" | "APPROVED" | "REJECTED";

type Route = {
  id: string;
  name: string;
  status: RouteStatus;
};

const getCookie = (name: string) => {
  return document.cookie
    .split("; ")
    .find((row) => row.startsWith(`${name}=`))
    ?.split("=")[1];
};

const getStatusBackground = (status: RouteStatus) => {
  switch (status) {
    case "SUBMITTED":
      return "#fff3cd";
    case "APPROVED":
      return "#d4edda";
    case "REJECTED":
      return "#f8d7da";
    default:
      return "#fff";
  }
};

const getStatusColor = (status: RouteStatus) => {
  switch (status) {
    case "SUBMITTED":
      return "#856404";
    case "APPROVED":
      return "#155724";
    case "REJECTED":
      return "#721c24";
    default:
      return "#d21951";
  }
};

export default function ListPage() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRoutes = async () => {
      const token = getCookie("token");

      try {
        const response = await fetch(
          "http://localhost:8080/api/routes",
          {
            headers: {
              Authorization: `Bearer ${decodeURIComponent(token ?? "")}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error(
            `Ошибка загрузки маршрутов: ${response.status}`
          );
        }

        const data: Route[] = await response.json();

        setRoutes(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchRoutes();
  }, []);

  return (
    <div
      style={{
        padding: "20px",
        minHeight: "100vh",
        background: "#f0f2f5",
      }}
    >
      <button
        onClick={() => navigate("/routes/new")}
        style={{
          marginBottom: "20px",
          padding: "12px 24px",
          borderRadius: "8px",
          border: "none",
          background: "#d21951",
          color: "#fff",
          cursor: "pointer",
          fontSize: "16px",
        }}
      >
        Создать маршрут
      </button>

      {loading ? (
        <div>Загрузка...</div>
      ) : (
        <div
          style={{
            display: "flex",
            gap: "12px",
            marginBottom: "20px",
            flexWrap: "wrap",
          }}
        >
          {routes.map((route) => (
            <button
              key={route.id}
              onClick={() => navigate(`/routes/${route.id}`)}
              style={{
                padding: "12px 24px",
                borderRadius: "8px",
                border: `1px solid ${getStatusColor(route.status)}`,
                background: getStatusBackground(route.status),
                color: getStatusColor(route.status),
                cursor: "pointer",
                display: "flex",
                flexDirection: "column",
                alignItems: "flex-start",
                gap: "6px",
              }}
            >
              <span>{route.name}</span>

              <span
                style={{
                  fontSize: "12px",
                  fontWeight: 700,
                }}
              >
                {route.status}
              </span>
            </button>
          ))}
        </div>
      )}

      {!loading && routes.length === 0 && (
        <div>Маршрутов нет</div>
      )}
    </div>
  );
}