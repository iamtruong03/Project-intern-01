import React, { useState } from "react";
import Sidebar from "./components/Sidebar";
import Login from "./components/Login";
import ManageJob from "./components/ManageJob";
import ManageDepartment from "./components/ManageDepartment";
import Statistics from "./components/Statistics";
import ManageAccount from "./components/ManageAccount";
import "./App.css";

const App = () => {
  const [currentSection, setCurrentSection] = useState("login");

  const renderSection = () => {
    switch (currentSection) {
      case "login":
        return <Login setCurrentSection={setCurrentSection} />;
      case "manageJob":
        return <ManageJob />;
      case "manageDepartment":
        return <ManageDepartment />;
      case "statistics":
        return <Statistics />;
      case "manageAccount":
        return <ManageAccount />;
      default:
        return <Login setCurrentSection={setCurrentSection} />;
    }
  };

  return (
    <div className="app-container">
      <Sidebar setCurrentSection={setCurrentSection} />
      <div className="main-content">{renderSection()}</div>
    </div>
  );
};

export default App;