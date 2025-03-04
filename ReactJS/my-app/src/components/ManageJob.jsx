import React, { useState } from "react";

const ManageJob = () => {
  const [jobs, setJobs] = useState([]);

  const getMyJobs = async () => {
    try {
      const response = await fetch("/user/my-jobs");
      const data = await response.json();
      setJobs(data);
    } catch (error) {
      console.error("Lỗi khi lấy dữ liệu công việc:", error);
    }
  };

  return (
    <div className="manage-job">
      <h2>Quản Lý Công Việc</h2>
      <button onClick={getMyJobs}>Xem Công Việc</button>
      <ul>
        {jobs.map((job) => (
          <li key={job.id}>
            {job.name} - {job.deadline}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ManageJob;