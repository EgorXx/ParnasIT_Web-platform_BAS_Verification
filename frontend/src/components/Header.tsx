import { useNavigate } from "react-router-dom";

function getCookie(name) {
  return document.cookie
    .split("; ")
    .find((row) => row.startsWith(`${name}=`))
    ?.split("=")[1];
}

export default function Header() {
  const navigate = useNavigate();
  const role = getCookie("role");

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
      <button onClick={() => navigate("/")} style={buttonStyle}>
        Главная
      </button>

      <nav
        style={{
          display: "flex",
          gap: 15,
        }}
      >
        {!role && (
          <button onClick={() => navigate("/login")} style={buttonStyle}>
            Войти
          </button>
        )}

        {role === "USER" && (
          <button onClick={() => navigate("/routes")} style={buttonStyle}>
            Маршруты
          </button>
        )}

        {role === "ADMIN" && (
          <>
            <button onClick={() => navigate("/admin/pending ")} style={buttonStyle}>
              Маршруты на рассмотрение
            </button>

            <button onClick={() => navigate("/admin/zones")} style={buttonStyle}>
              Зоны
            </button>
          </>
        )}
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