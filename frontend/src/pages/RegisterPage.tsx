import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");


  const register = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            fullName,
            email,
            password,
          }),
        }
      );


      if (!response.ok) {
        throw new Error("Ошибка регистрации");
      }


      const data = await response.json();


      Cookies.set(
        "token",
        data.token,
        {
          expires: 7,
        }
      );


      Cookies.set(
        "role",
        data.role,
        {
          expires: 7,
        }
      );


      navigate("/list");

    } catch (error) {
      console.error(error);
      alert("Не удалось зарегистрироваться");
    }
  };


  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "#f0f2f5",
      }}
    >
      <div
        style={{
          background: "#fff",
          padding: 30,
          borderRadius: 12,
          width: 320,
        }}
      >

        <h2>Регистрация</h2>


        <input
          placeholder="ФИО"
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          style={{
            width: "100%",
            marginBottom: 12,
            padding: 10,
          }}
        />


        <input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={{
            width: "100%",
            marginBottom: 12,
            padding: 10,
          }}
        />


        <input
          placeholder="Пароль"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={{
            width: "100%",
            marginBottom: 20,
            padding: 10,
          }}
        />


        <button
          onClick={register}
          style={{
            width: "100%",
            padding: 12,
            background: "#d21951",
            color: "#fff",
            border: "none",
            borderRadius: 8,
            cursor: "pointer",
          }}
        >
          Создать аккаунт
        </button>


        <button
          onClick={() => navigate("/login")}
          style={{
            marginTop: 10,
            width: "100%",
            padding: 12,
            background: "#fff",
            color: "#d21951",
            border: "1px solid #d21951",
            borderRadius: 8,
            cursor: "pointer",
          }}
        >
          Уже есть аккаунт
        </button>

      </div>
    </div>
  );
}