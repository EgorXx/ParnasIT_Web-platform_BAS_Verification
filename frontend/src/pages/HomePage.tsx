import { useNavigate } from "react-router-dom";


export default function HomePage() {
  const navigate = useNavigate();

  return (
    <div
      style={{
        minHeight: "100vh",
        background: "#f0f2f5",
      }}
    >


      <main
        style={{
          padding: 40,
        }}
      >

        <div
          style={{
            background: "#fff",
            padding: 40,
            borderRadius: 12,
            maxWidth: 900,
          }}
        >

          <h1>
            Управление маршрутами и зонами
          </h1>


          <p>
            Сервис позволяет создавать маршруты,
            определять географические зоны,
            просматривать их на карте.
          </p>


          <div
            style={{
              display: "flex",
              gap: 15,
              marginTop: 30,
              flexWrap: "wrap",
            }}
          >

            <button
              onClick={() => navigate("/list")}
              style={mainButton}
            >
              Маршруты
            </button>


            <button
              onClick={() => navigate("/zones")}
              style={mainButton}
            >
              Зоны
            </button>


          </div>

        </div>

      </main>

    </div>
  );
}


const mainButton = {
  padding: "14px 25px",
  background: "#d21951",
  color: "#fff",
  border: "none",
  borderRadius: 8,
  cursor: "pointer",
  fontSize: 16,
};