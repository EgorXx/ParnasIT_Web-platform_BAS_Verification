import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

type RouteStatus = "SUBMITTED" | "APPROVED" | "REJECTED";

type RoutePoint = {
  lat: number;
  lng: number;
};

type Route = {
  id: string;
  name: string;
  userFullName: string;
  status: RouteStatus;
  autoCheckResult: boolean;
  createdAt: string;
  points: RoutePoint[];
};

type RouteBlock = {
  key: string;
  title: string;
  routes: Route[];
};

const getCookie = (name: string) => {
  return document.cookie
    .split("; ")
    .find((row) => row.startsWith(`${name}=`))
    ?.split("=")[1];
};

const getAuthHeaders = () => {
  const token = getCookie("token");

  return {
    Authorization: `Bearer ${decodeURIComponent(token ?? "")}`,
  };
};

export default function RoutesApprovePage() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [processingId, setProcessingId] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRoutes = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/api/admin/routes",
          {
            headers: getAuthHeaders(),
          }
        );

        if (!response.ok) {
          throw new Error(`Ошибка загрузки маршрутов: ${response.status}`);
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

  const routeBlocks = useMemo<RouteBlock[]>(() => {
    return [
      {
        key: "submitted",
        title: "На рассмотрении",
        routes: routes.filter((route) => route.status === "SUBMITTED"),
      },
      {
        key: "approved-auto",
        title: "Автоматически одобренные",
        routes: routes.filter(
          (route) =>
            route.status === "APPROVED" && route.autoCheckResult === true
        ),
      },
      {
        key: "approved-manual",
        title: "Одобренные вручную",
        routes: routes.filter(
          (route) =>
            route.status === "APPROVED" && route.autoCheckResult !== true
        ),
      },
      {
        key: "rejected",
        title: "Отклоненные",
        routes: routes.filter((route) => route.status === "REJECTED"),
      },
    ];
  }, [routes]);

  const handleRouteAction = async (
    id: string,
    action: "approve" | "reject"
  ) => {
    setProcessingId(id);

    try {
      const response = await fetch(
        `http://localhost:8080/api/admin/routes/${id}/${action}`,
        {
          method: "POST",
          headers: getAuthHeaders(),
        }
      );

      if (!response.ok) {
        throw new Error(`Ошибка обработки маршрута: ${response.status}`);
      }

      const nextStatus: RouteStatus =
        action === "approve" ? "APPROVED" : "REJECTED";

      setRoutes((prev) =>
        prev.map((route) =>
          route.id === id
            ? {
                ...route,
                status: nextStatus,
                autoCheckResult: false,
              }
            : route
        )
      );
    } catch (error) {
      console.error(error);
      alert("Не удалось обработать маршрут");
    } finally {
      setProcessingId(null);
    }
  };

  return (
    <div
      style={{
        padding: "20px",
        minHeight: "100vh",
        background: "#f0f2f5",
      }}
    >
      {loading ? (
        <div>Загрузка...</div>
      ) : (
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: "24px",
          }}
        >
          {routeBlocks.map((block) => (
            <section key={block.key}>
              <h2 style={{ marginBottom: "12px" }}>{block.title}</h2>

              {block.routes.length === 0 ? (
                <div>Заявок нет</div>
              ) : (
                <div
                  style={{
                    display: "flex",
                    gap: "12px",
                    flexWrap: "wrap",
                  }}
                >
                  {block.routes.map((route) => (
                    <div
                      key={route.id}
                      style={{
                        padding: "12px 24px",
                        borderRadius: "8px",
                        background: "#fff",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "flex-start",
                        gap: "10px",
                      }}
                    >
                      <span><strong>{route.name}</strong></span>
                      <span>{route.userFullName}</span>

                      <div
                        style={{
                          display: "flex",
                          gap: "8px",
                          flexWrap: "wrap",
                        }}
                      >
                        <button
                          onClick={() =>
                            navigate(`/admin/pending/route/${route.id}`)
                          }
                          style={{
                            padding: "8px 12px",
                            borderRadius: "6px",
                            border: "none",
                            background: "#6c757d",
                            color: "#fff",
                            cursor: "pointer",
                          }}
                        >
                          Посмотреть
                        </button>

                        {route.status === "SUBMITTED" && (
                          <>
                            <button
                              onClick={() =>
                                handleRouteAction(route.id, "approve")
                              }
                              disabled={processingId === route.id}
                              style={{
                                padding: "8px 12px",
                                borderRadius: "6px",
                                border: "none",
                                background: "#28a745",
                                color: "#fff",
                                cursor:
                                  processingId === route.id
                                    ? "not-allowed"
                                    : "pointer",
                                opacity: processingId === route.id ? 0.6 : 1,
                              }}
                            >
                              Одобрить
                            </button>

                            <button
                              onClick={() =>
                                handleRouteAction(route.id, "reject")
                              }
                              disabled={processingId === route.id}
                              style={{
                                padding: "8px 12px",
                                borderRadius: "6px",
                                border: "none",
                                background: "#dc3545",
                                color: "#fff",
                                cursor:
                                  processingId === route.id
                                    ? "not-allowed"
                                    : "pointer",
                                opacity: processingId === route.id ? 0.6 : 1,
                              }}
                            >
                              Отклонить
                            </button>
                          </>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </section>
          ))}
        </div>
      )}

      {!loading && routes.length === 0 && <div>Маршрутов нет</div>}
    </div>
  );
}