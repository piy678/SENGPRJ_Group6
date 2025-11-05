import React from 'react';
import { useNavigate } from 'react-router-dom';            // <— hinzufügen
import { Button, Card } from '../../../components/UI.jsx';


export default function ManageCourses() {
  const navigate = useNavigate();                          // <— hinzufügen

  return (
    <div>
      <Card>
        <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center' }}>
          <div>
            <div style={{ fontWeight:600 }}>Software Engineering Project</div>
            <div className="muted">Students: 12 — LEOs: 15</div>
          </div>
          <div className="row">
            <Button className="ghost">Manage LEO Graph</Button>
            <Button className="ghost">Reviews</Button>
          </div>
        </div>
      </Card>

      <div className="spacer"></div>

      {}
      <Button variant="primary" onClick={() => navigate('/teacher/courses/new')}>
        Create new courses
      </Button>
    </div>
  );
}
