import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  MapContainer,
  Marker,
  Polyline,
  TileLayer,
} from "react-leaflet";
import L from "leaflet";

type Point = {
  lat: number;
  lng: number;
};

type Route = {
  id: number;
  name: string;
  points: Point[];
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

export default function RouteViewPage() {
    const navigate = useNavigate();
  const { id } = useParams();

  const [route, setRoute] = useState<Route | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getCookie("token");

    const fetchRoute = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/routes/${id}`,
          {
            headers: {
              Authorization: `Bearer ${decodeURIComponent(token ?? "")}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error(
            `Ошибка загрузки маршрута: ${response.status}`
          );
        }

        const data: Route = await response.json();

        setRoute(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchRoute();
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

<button
  onClick={() => navigate("/list")}
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