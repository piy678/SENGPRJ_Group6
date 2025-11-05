
import React from 'react';
import { Outlet } from 'react-router-dom';
export default function App() {
  return (
    <div>
      <div className="topbar"><div className="title">LEO Assessment Tool</div></div>
      <div className="shell"><Outlet /></div>
    </div>
  );
}


/*
export default function App() {
  return (
    <div style={{ padding: 24, fontFamily: "ui-sans-serif, system-ui" }}>
      <h1> Electron + Vite + React läuft</h1>
      <p>Wenn du das siehst, sind Pfade & Dev-Server korrekt.</p>
    </div>
  );
}
*/