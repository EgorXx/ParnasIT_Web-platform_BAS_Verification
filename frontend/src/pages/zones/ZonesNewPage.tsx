import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  MapContainer,
  TileLayer,
  Polygon,
  Marker,
  useMapEvents,
} from "react-leaflet";
import L from "leaflet";


type Point = {
  lat: number;
  lng: number;
};

const getCookie = (name: string) => {
  return document.cookie
    .split("; ")
    .find((row) => row.startsWith(`${name}=`))
    ?.split("=")[1];
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
  setPoints,
  drawing,
}: {
  setPoints: React.Dispatch<React.SetStateAction<Point[]>>;
  drawing: boolean;
}) {

  useMapEvents({

    click(e) {

      if (!drawing) {
        return;
      }


      setPoints(prev => [
        ...prev,
        {
          lat: e.latlng.lat,
          lng: e.latlng.lng,
        },
      ]);

    },

  });


  return null;
}



export default function ZoneCreatePage() {

  const navigate = useNavigate();


  const [name, setName] = useState("");

  const [points, setPoints] = useState<Point[]>([]);

  const [polygonCreated, setPolygonCreated] = useState(false);



  const createPolygon = () => {

    if (points.length < 3) {
      alert("Нужно поставить минимум 3 точки");
      return;
    }


    setPolygonCreated(true);

  };



  const saveZone = async () => {


    if (!name.trim()) {
      alert("Введите название зоны");
      return;
    }


    if (!polygonCreated) {
      alert("Сначала создайте полигон");
      return;
    }



const coordinates = [
  ...points.map((point) => ({
    lat: point.lat,
    lng: point.lng,
  })),

  {
    lat: points[0].lat,
    lng: points[0].lng,
  },
];



    const zone = {

      name,

      coordinates,

    };



    try {

      console.log("➡️ Отправка запроса создания зоны");

      console.log("URL:", "http://localhost:8080/api/zones");

      console.log("Method:", "POST");

      console.log("Headers:", {
        "Content-Type": "application/json",
      });

      console.log(
        "Body:",
        JSON.stringify(zone, null, 2)
      );


      const token = getCookie("token");
      const response = await fetch(
        "http://localhost:8080/api/zones",
        {

          method: "POST",

          headers: {

            "Content-Type": "application/json",
            Authorization: `Bearer ${decodeURIComponent(token ?? "")}`
          },


          body: JSON.stringify(zone),

        }
      );



      console.log("⬅️ Ответ сервера");

      console.log("Status:", response.status);

      console.log("OK:", response.ok);



      const responseText = await response.text();

      console.log(
        "Response body:",
        responseText
      );



      if (!response.ok) {

        throw new Error(
          `Ошибка создания зоны: ${response.status}`
        );

      }



      navigate("/admin/zones");


    } catch(error) {

      console.error(
        "❌ Ошибка запроса:",
        error
      );

      alert(
        "Не удалось создать зону"
      );

    }

  };



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

          url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"

          attribution="© OpenStreetMap"

        />



        <MapClickHandler

          setPoints={setPoints}

          drawing={!polygonCreated}

        />




        {

          points.map((point, index) => (

            <Marker

              key={index}

              position={[

                point.lat,

                point.lng,

              ]}

              icon={markerIcon}

            />

          ))

        }




        {

          polygonCreated && (

            <Polygon

              positions={

                points.map(point => [

                  point.lat,

                  point.lng,

                ])

              }

            />

          )

        }



      </MapContainer>





      <div

        style={{

          position: "absolute",

          top: 20,

          left: 50,

          zIndex: 1000,

          background: "#fff",

          padding: 20,

          borderRadius: 10,

        }}

      >



        <input

          placeholder="Название зоны"

          value={name}

          onChange={
            e => setName(e.target.value)
          }

          style={{

            padding: 10,

            marginBottom: 10,

            display: "block",

          }}

        />





        {!polygonCreated && (

          <button

            onClick={createPolygon}

            style={{

              padding: "10px 20px",

              background: "#333",

              color: "#fff",

              border: "none",

              borderRadius: 8,

              marginBottom: 10,

            }}

          >

            Создать полигон

          </button>

        )}






        {polygonCreated && (

          <button

            onClick={saveZone}

            style={{

              padding: "10px 20px",

              background: "#d21951",

              color: "#fff",

              border: "none",

              borderRadius: 8,

            }}

          >

            Сохранить зону

          </button>

        )}



      </div>



    </div>

  );

}