import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card } from '../components/UI.jsx';

export default function StudentDashboard() {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Back wie Browser-Back; Fallback auf Landing/Login
    if (window.history.length > 1) navigate(-1);
    else navigate('/');
  };

  return (
    <div>
      <div className="row" style={{ justifyContent:'space-between', alignItems:'center', marginBottom:12 }}>
        <div style={{ fontWeight:700 }}>Student Dashboard - Anna</div>
        <Button variant="danger" onClick={handleLogout}>Logout</Button>
      </div>

      <Card>
        <div className="muted">
          Welcome, Anna!<br/>Here you can track your progress in all enrolled courses.
        </div>
      </Card>

      <div className="spacer"></div>

      <Card>
        <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center' }}>
          <div>
            <div style={{ fontWeight:600 }}>Software Engineering Projekt</div>
            <div className="muted">Progress: 5 of 15 LEOs achieved (33%)</div>
          </div>
          <Button variant="primary" onClick={() => navigate('/student/progress')}>view details</Button>
        </div>
      </Card>
    </div>
  );
}
