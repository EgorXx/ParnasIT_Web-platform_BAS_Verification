import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type Zone = {
  id: number;
  name: string;
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

const getZonesFromResponse = (data: unknown): Zone[] => {
  if (Array.isArray(data)) {
    return data;
  }

  if (
    data &&
    typeof data === "object" &&
    "zones" in data &&
    Array.isArray(data.zones)
  ) {
    return data.zones;
  }

  if (
    data &&
    typeof data === "object" &&
    "data" in data &&
    Array.isArray(data.data)
  ) {
    return data.data;
  }

  return [];
};

export default function ZonePage() {
  const [zones, setZones] = useState<Zone[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/zones", {
      headers: getAuthHeaders(),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Ошибка загрузки зон: ${response.status}`);
        }

        return response.json();
      })
      .then((data) => {
        setZones(getZonesFromResponse(data));
      })
      .catch((error) => {
        console.error(error);
        setZones([]);
      });
  }, []);

  return (
    <div
      style={{
        padding: 20,
        background: "#f0f2f5",
        minHeight: "100vh",
      }}
    >
      <button
        onClick={() => navigate("/admin/zones/new")}
        style={{
          padding: 12,
          background: "#d21951",
          color: "#fff",
          border: "none",
          borderRadius: 8,
          cursor: "pointer",
        }}
      >
        Создать зону
      </button>

      {zones.map((zone) => (
        <div
          key={zone.id}
          style={{
            marginTop: 15,
            background: "#fff",
            padding: 15,
            borderRadius: 8,
          }}
        >
          {zone.name}
        </div>
      ))}
    </div>
  );
}