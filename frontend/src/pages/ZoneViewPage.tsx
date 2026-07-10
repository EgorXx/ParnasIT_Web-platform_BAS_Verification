import {
  useEffect,
  useState,
} from "react";

import {
  useNavigate,
  useParams,
} from "react-router-dom";

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
  id: number;
  name: string;
  coordinates: Point[];
};


export default function ZoneViewPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [zone, setZone] = useState<Zone | null>(null);


  useEffect(() => {
    if (!id) {
      return;
    }


    const loadZone = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/zones/${id}`
        );


        if (!response.ok) {
          throw new Error("Ошибка загрузки зоны");
        }


        const data: Zone = await response.json();

        console.log("Loaded zone:", data);

        setZone(data);

      } catch (error) {
        console.error(error);
      }
    };


    loadZone();

  }, [id]);


  if (!zone) {
    return <div>Загрузка...</div>;
  }


  if (!zone.coordinates || zone.coordinates.length === 0) {
    return <div>У зоны нет координат</div>;
  }


  const polygonPoints = zone.coordinates.map(
    (point) => [
      point.lat,
      point.lng,
    ] as [number, number]
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
        center={polygonPoints[0]}
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
          positions={polygonPoints}
        />

      </MapContainer>


      <div
        style={{
          position: "absolute",
          top: 20,
          left: 20,
          zIndex: 1000,
          background: "white",
          padding: 20,
          borderRadius: 10,
        }}
      >

        <h2>
          {zone.name}
        </h2>


        <button
          onClick={() => navigate("/zones")}
          style={{
            padding: "10px 20px",
          }}
        >
          Назад
        </button>

      </div>

    </div>
  );
}