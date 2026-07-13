export default function AccessDeniedPage() {
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
          textAlign: "center",
        }}
      >
        <h2>
          Недостаточно прав
        </h2>

        <p>
          Для доступа к этому разделу нужны права администратора.
        </p>
      </div>
    </div>
  );
}