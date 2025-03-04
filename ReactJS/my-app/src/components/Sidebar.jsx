import React from "react";

const Sidebar = ({ setCurrentSection }) => {
  return (
    <div className="sidebar">
      <h2>Trang Chủ</h2>
      <ul>
        <li onClick={() => setCurrentSection("login")}>Đăng Nhập</li>
        <li onClick={() => setCurrentSection("manageJob")}>Quản Lý Công Việc</li>
        <li onClick={() => setCurrentSection("manageDepartment")}>Quản Lý Phòng Ban</li>
        <li onClick={() => setCurrentSection("statistics")}>Thống Kê</li>
        <li onClick={() => setCurrentSection("manageAccount")}>Quản Lý Tài Khoản</li>
      </ul>
    </div>
  );
};

export default Sidebar;