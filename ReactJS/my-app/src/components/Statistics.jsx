import React, { useState } from "react";
import { Chart } from "chart.js/auto";

const Statistics = () => {
  const [statistics, setStatistics] = useState({});

  const loadStatistics = async () => {
    try {
      const response = await fetch("/user/userStatistics");
      const data = await response.json();
      setStatistics(data);
      renderChart(data);
    } catch (error) {
      console.error("Lỗi khi lấy dữ liệu:", error);
    }
  };

  const renderChart = (data) => {
    const ctx = document.getElementById("statisticChart").getContext("2d");
    new Chart(ctx, {
      type: "pie",
      data: {
        labels: Object.keys(data),
        datasets: [
          {
            data: Object.values(data),
            backgroundColor: ["#F44336", "#4CAF50", "#FF9800"],
          },
        ],
      },
    });
  };

  return (
    <div className="statistics">
      <h2>Thống Kê</h2>
      <button onClick={loadStatistics}>Xem thống kê</button>
      <canvas id="statisticChart" width="400" height="400"></canvas>
    </div>
  );
};

export default Statistics;