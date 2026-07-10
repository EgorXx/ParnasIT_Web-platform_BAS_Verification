import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  MapContainer,
  TileLayer,
  Polygon,
} from "react-leaflet";

type Point = {
  lat: number;
  lng: number;
};

type Zone = {
  name: string;
  coordinates: Point[];
};

export default function ZoneViewPage() {
  const { id } = useParams();

  const [zone, setZone] = useState<Zone | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadZone();
  }, []);

  const loadZone = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/zones/${id}`
      );

      if (!response.ok) {
        throw new Error("Ошибка загрузки");
      }

      const data: Zone = await response.json();

      setZone(data);
    } catch (error) {
      console.error(error);
      alert("Не удалось загрузить зону");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Загрузка...</div>;
  }

  if (!zone) {
    return <div>Зона не найдена</div>;
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
        center={[
          zone.coordinates[0].lat,
          zone.coordinates[0].lng,
        ]}
        zoom={10}
        style={{
          width: "100%",
          height: "100%",
        }}
      >
        <TileLayer
          url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="© OpenStreetMap"
        />

        <Polygon
          positions={zone.coordinates.map((point) => [
            point.lat,
            point.lng,
          ])}
        />
      </MapContainer>

      <div
        style={{
          position: "absolute",
          top: 20,
          left: 20,
          zIndex: 1000,
          background: "#fff",
          padding: 20,
          borderRadius: 10,
        }}
      >
        <h3>{zone.name}</h3>
      </div>
    </div>
  );
}