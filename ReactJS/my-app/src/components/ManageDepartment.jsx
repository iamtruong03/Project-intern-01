import React, { useState, useEffect } from "react";

const ManageDepartment = () => {
  const [departments, setDepartments] = useState([]);

  const fetchUserDepartmentInfo = async () => {
    try {
      const response = await fetch("/user/my-department", {
        method: "GET",
        credentials: "include",
      });
      const data = await response.json();
      setDepartments(data.subDepartments);
    } catch (error) {
      console.error("Lỗi khi fetch API:", error);
    }
  };

  useEffect(() => {
    fetchUserDepartmentInfo();
  }, []);

  return (
    <div className="manage-department">
      <h2>Quản Lý Phòng Ban</h2>
      <button onClick={fetchUserDepartmentInfo}>Xem thông tin</button>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên Phòng Ban</th>
            <th>Thuộc Phòng Ban</th>
            <th>Nhân Sự</th>
          </tr>
        </thead>
        <tbody>
          {departments.map((dept) => (
            <tr key={dept.subDepartmentId}>
              <td>{dept.subDepartmentId}</td>
              <td>{dept.subDepartmentName}</td>
              <td>{dept.parentDepartmentName || "Không có"}</td>
              <td>{dept.userCount || 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ManageDepartment;