
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card } from '../components/UI.jsx';
const rows = [
  { title:'Understanding requirements analysis', dep:'No dependencies', status:'Achieved' },
  { title:'Creating Gherkin specifications', dep:'Understanding requirements analysis', status:'Partially' },
  { title:'Develop low-fidelity prototypes', dep:'Understanding requirements analysis', status:'Unrated' },
  { title:'Applying Test-Driven Development', dep:'Creating Gherkin specifications', status:'Unrated' },
  { title:'Implement backend with Spring Boot', dep:'Applying Test-Driven Development', status:'Unrated' }
];
export default function StudentProgress() {
  const nav = useNavigate();
  return (
    <div>
      <div className="row" style={{ justifyContent:'space-between', alignItems:'center', marginBottom:12 }}>
        <div style={{ fontWeight:700 }}>Progress - Software Engineering Projekt</div>
        <Button className="back" onClick={()=>nav(-1)}>Back</Button>
      </div>
      <Card>
        <div className="muted" style={{ marginBottom:12 }}>Progress Overview</div>
        <div className="row" style={{ alignItems:'center', gap:16 }}>
          <div className="muted">Total LEOs: 15</div>
          <div>Achieved: <b>2</b></div>
          <div>Partially: <b>1</b></div>
          <div>Not achieved: <b>0</b></div>
          <div>Unrated: <b>12</b></div>
        </div>
      </Card>
      <div className="spacer"></div>
      <Card>
        <div style={{ fontWeight:600, marginBottom:8 }}>Recommended Next Steps</div>
        <ul>
          <li>You can start creating mockups in Figma right away!</li>
          <li>First, focus on improving your Gherkin skills.</li>
        </ul>
      </Card>
      <div className="spacer"></div>
      <Card>
        <table>
          <thead>
            <tr><th>All LEOs</th><th>Depends on</th><th>Status</th></tr>
          </thead>
          <tbody>
            {rows.map((r,i)=>(
              <tr key={i}><td>{r.title}</td><td>{r.dep}</td><td>{r.status}</td></tr>
            ))}
          </tbody>
        </table>
      </Card>
    </div>
  );
}
