import React, { useState } from "react";

const Login = ({ setCurrentSection }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const response = await fetch("/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userName: username, password }),
      });
      const data = await response.json();
      if (data.success) {
        sessionStorage.setItem("userId", data.userId);
        setCurrentSection("manageJob");
      } else {
        alert(data.message);
      }
    } catch (error) {
      console.error("Lỗi đăng nhập:", error);
      alert("Lỗi đăng nhập. Vui lòng kiểm tra lại thông tin.");
    }
  };

  return (
    <div className="login-form">
      <h2>Đăng Nhập</h2>
      <input
        type="text"
        placeholder="Tên đăng nhập"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Mật khẩu"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>Đăng nhập</button>
    </div>
  );
};

export default Login;