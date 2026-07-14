import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  MapContainer,
  Marker,
  Polygon,
  Polyline,
  TileLayer,
} from "react-leaflet";
import L from "leaflet";

type Point = {
  lat: number;
  lng: number;
};

type RouteStatus = "SUBMITTED" | "APPROVED" | "REJECTED";

type Route = {
  id: string;
  name: string;
  userFullName: string;
  status: RouteStatus;
  createdAt: string;
  points: Point[];
};

type Zone = {
  id: string;
  name: string;
  coordinates: Point[];
  createdAt: string;
};

const markerIcon = new L.Icon({
  iconUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",

  iconRetinaUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",

  shadowUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",

  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

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

export default function RoutesViewApprovePage() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const [route, setRoute] = useState<Route | null>(null);
  const [zones, setZones] = useState<Zone[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRouteAndZones = async () => {
      try {
        const [routesResponse, zonesResponse] = await Promise.all([
          fetch("http://localhost:8080/api/admin/routes/pending", {
            headers: getAuthHeaders(),
          }),
          fetch("http://localhost:8080/api/zones", {
            headers: getAuthHeaders(),
          }),
        ]);

        if (!routesResponse.ok) {
          throw new Error(
            `Ошибка загрузки маршрута: ${routesResponse.status}`
          );
        }

        if (!zonesResponse.ok) {
          throw new Error(
            `Ошибка загрузки зон: ${zonesResponse.status}`
          );
        }

        const routesData: Route[] = await routesResponse.json();
        const zonesData: Zone[] = await zonesResponse.json();

        const currentRoute =
          routesData.find((routeItem) => routeItem.id === id) ?? null;

        setRoute(currentRoute);
        setZones(zonesData);
      } catch (error) {
        console.error(error);
        setRoute(null);
        setZones([]);
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchRouteAndZones();
    } else {
      setLoading(false);
      setRoute(null);
      setZones([]);
    }
  }, [id]);

  const linePositions = useMemo(
    () =>
      route?.points.map(
        (point) =>
          [
            point.lat,
            point.lng,
          ] as [number, number]
      ) ?? [],
    [route]
  );

  const zonePolygons = useMemo(
    () =>
      zones.map((zone) => ({
        ...zone,
        positions: zone.coordinates.map(
          (point) =>
            [
              point.lat,
              point.lng,
            ] as [number, number]
        ),
      })),
    [zones]
  );

  if (loading) {
    return <div>Загрузка...</div>;
  }

  if (!route) {
    return <div>Маршрут не найден</div>;
  }

  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        position: "relative",
      }}
    >
      <MapContainer
        center={
          linePositions.length > 0
            ? linePositions[0]
            : [55.7558, 37.6173]
        }
        zoom={10}
        style={{
          width: "100%",
          height: "100%",
        }}
      >
        <TileLayer
          attribution="© OpenStreetMap contributors"
          url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {zonePolygons.map((zone) => (
          <Polygon
            key={zone.id}
            positions={zone.positions}
            pathOptions={{
              color: "#dc3545",
              fillColor: "#dc3545",
              fillOpacity: 0.25,
              weight: 2,
            }}
          />
        ))}

        {linePositions.length > 1 && (
          <Polyline
            positions={linePositions}
            color="black"
            weight={3}
          />
        )}

        {route.points.map((point, index) => (
          <Marker
            key={index}
            position={[
              point.lat,
              point.lng,
            ]}
            icon={markerIcon}
          />
        ))}
      </MapContainer>

      <div
        style={{
          position: "absolute",
          top: 20,
          left: 50,
          zIndex: 1000,
          background: "#fff",
          padding: "12px 16px",
          borderRadius: 8,
          boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
        }}
      >
        <div
          style={{
            fontWeight: 700,
            marginBottom: 4,
          }}
        >
          {route.name}
        </div>

        <div>
          Запретных зон: {zones.length}
        </div>
      </div>

      <button
        onClick={() => navigate("/admin/routes/pending")}
        style={{
          position: "absolute",
          bottom: "20px",
          left: "50%",
          transform: "translate(-50%, -50%)",
          zIndex: 1000,
          padding: "12px 24px",
          borderRadius: 8,
          border: "1px solid #d21951",
          background: "#fff",
          color: "#d21951",
          cursor: "pointer",
          fontSize: "16px",
        }}
      >
        К списку
      </button>
    </div>
  );
}