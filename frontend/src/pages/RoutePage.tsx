import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  MapContainer,
  Marker,
  Polyline,
  TileLayer,
  useMapEvents,
} from "react-leaflet";
import L from "leaflet";


type Point = {
  lat: number;
  lng: number;
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


function MapClickHandler({
  drawing,
  setPoints,
}: {
  drawing: boolean;
  setPoints: React.Dispatch<
    React.SetStateAction<Point[]>
  >;
}) {

  useMapEvents({
    click(event) {

      if (!drawing) {
        return;
      }


      setPoints((prev) => [
        ...prev,
        {
          lat: event.latlng.lat,
          lng: event.latlng.lng,
        },
      ]);
    },
  });


  return null;
}


export default function RoutePage() {

  const navigate = useNavigate();


  const [points, setPoints] = useState<Point[]>([]);
  const [drawing, setDrawing] = useState(true);



const saveDraft = async () => {
  const name = window.prompt("Введите имя черновика");

  if (!name) {
    return;
  }

  const route = {
    id: 0,
    name,
    points,
  };

  try {
    const response = await fetch(
      "http://localhost:8080/api/routes",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(route),
      }
    );

    if (!response.ok) {
      throw new Error(
        `Ошибка сохранения маршрута: ${response.status}`
      );
    }

    navigate("/list");
  } catch (error) {
    console.error(error);
    alert("Не удалось сохранить маршрут");
  }
};



  const linePositions = useMemo(
    () =>
      points.map(
        (point) =>
          [
            point.lat,
            point.lng,
          ] as [number, number]
      ),

    [points]
  );



  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        position: "relative",
      }}
    >

      <MapContainer
        center={[
          55.7558,
          37.6173,
        ]}
        zoom={6}
        style={{
          width: "100%",
          height: "100%",
        }}
      >

        <TileLayer
          attribution="© OpenStreetMap contributors"
          url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
        />


        <MapClickHandler
          drawing={drawing}
          setPoints={setPoints}
        />



        {linePositions.length > 1 && (
          <Polyline
            positions={linePositions}
            color="black"
            weight={3}
          />
        )}



        {points.map((point, index) => (
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
    bottom: 20,
    left: "50%",
    transform: "translateX(-50%)",
    display: "flex",
    gap: 12,
    zIndex: 1000,
  }}
>
  <button
    onClick={() => navigate("/list")}
    style={{
      padding: "12px 24px",
      borderRadius: 8,
      border: "1px solid #777",
      background: "#fff",
      color: "#333",
      cursor: "pointer",
    }}
  >
    К списку
  </button>

  <button
    onClick={saveDraft}
    style={{
      padding: "12px 24px",
      borderRadius: 8,
      border: "1px solid #d21951",
      background: "#fff",
      color: "#d21951",
      cursor: "pointer",
    }}
  >
    Сохранить черновик
  </button>

  <button
    onClick={() => setDrawing(false)}
    style={{
      padding: "12px 24px",
      borderRadius: 8,
      border: "none",
      background: "#d21951",
      color: "#fff",
      cursor: "pointer",
    }}
  >
    Завершить маршрут
  </button>
</div>

    </div>
  );
}