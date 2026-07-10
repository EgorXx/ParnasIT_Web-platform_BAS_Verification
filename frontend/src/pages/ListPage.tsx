import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type Route = {
  id: number;
  name: string;
};

export default function ListPage() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRoutes = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/api/routes"
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
      onClick={() => navigate("/route")}
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
            onClick={() => navigate(`/route/${route.id}`)}
            style={{
              padding: "12px 24px",
              borderRadius: "8px",
              border: "1px solid #d21951",
              background: "#fff",
              color: "#d21951",
              cursor: "pointer",
            }}
          >
            {route.name}
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