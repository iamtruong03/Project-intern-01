import React, { useState, useEffect } from "react";

const ManageAccount = () => {
  const [accounts, setAccounts] = useState([]);

  const loadAccounts = async () => {
    try {
      const response = await fetch("/user/sub-users");
      const data = await response.json();
      setAccounts(data);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu cá nhân:", error);
    }
  };

  useEffect(() => {
    loadAccounts();
  }, []);

  return (
    <div className="manage-account">
      <h2>Quản Lý Tài Khoản</h2>
      <button onClick={loadAccounts}>Xem thông tin</button>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên đầy đủ</th>
            <th>Tên đăng nhập</th>
            <th>Mật khẩu</th>
            <th>Địa chỉ</th>
          </tr>
        </thead>
        <tbody>
          {accounts.map((account) => (
            <tr key={account.id}>
              <td>{account.id}</td>
              <td>{account.fullName}</td>
              <td>{account.userName}</td>
              <td>{account.password}</td>
              <td>{account.address}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ManageAccount;