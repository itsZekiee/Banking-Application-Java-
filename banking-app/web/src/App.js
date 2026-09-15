import React from 'react';
import Navbar from './components/Navbar';
import DashboardPage from './pages/DashboardPage';
import './App.css';

function App() {
  return (
    <div className="App">
      <Navbar />
      <main>
        <DashboardPage />
      </main>
    </div>
  );
}

export default App;
