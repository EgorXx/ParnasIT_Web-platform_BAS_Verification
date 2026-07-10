import { useNavigate } from "react-router-dom";

export default function Header() {
  const navigate = useNavigate();

  return (
    <header
      style={{
        height: 70,
        background: "#fff",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 30px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
      }}
    >
      <h2
        style={{
          cursor: "pointer",
          color: "#d21951",
          margin: 0,
        }}
        onClick={() => navigate("/")}
      >
        Route Service
      </h2>


      <nav
        style={{
          display: "flex",
          gap: 15,
        }}
      >
        <button
          onClick={() => navigate("/")}
          style={buttonStyle}
        >
          Главная
        </button>

        <button
          onClick={() => navigate("/list")}
          style={buttonStyle}
        >
          Маршруты
        </button>

        <button
          onClick={() => navigate("/zones")}
          style={buttonStyle}
        >
          Зоны
        </button>
      </nav>
    </header>
  );
}


const buttonStyle = {
  padding: "10px 18px",
  borderRadius: 8,
  border: "1px solid #d21951",
  background: "#fff",
  color: "#d21951",
  cursor: "pointer",
};