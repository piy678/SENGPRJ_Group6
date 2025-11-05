
import React, { useState } from 'react';
import { Button, Card } from '../../../components/UI.jsx';
const students = ['Anna Müller','Max Schmidt','Lisa Weber'];
const leos = [
  { title: 'Understand requirements analysis', dep:'No dependencies' },
  { title: 'Create Gherkin specifications', dep:'Understanding requirements analysis' },
  { title: 'Develop low-fidelity prototypes', dep:'Understanding requirements analysis' },
];
export default function ConductAssessments() {
  const [currentStudent, setCurrentStudent] = useState(students[0]);
  return (
    <div>
      <div className="row" style={{ gap:8 }}>
        {students.map(s => (
          <Button key={s} className={currentStudent===s?'primary':''} onClick={()=>setCurrentStudent(s)}>{s}</Button>
        ))}
      </div>
      <div className="spacer"></div>
      <Card>
        <div className="muted" style={{ marginBottom:8 }}>{currentStudent} — LEO Ratings</div>
        {leos.map((l,i)=>(
          <div key={i} style={{ padding:'10px 0', borderBottom:'1px solid var(--border)' }}>
            <div style={{ fontWeight:600 }}>{i+1}. {l.title}</div>
            <div className="muted" style={{ marginBottom:6 }}>Depending on: {l.dep}</div>
            <div className="row">
              {['Not Achieved','Partially achieved','Achieved','Unrated'].map(r=>(
                <label key={r} style={{ display:'flex', alignItems:'center', gap:6 }}>
                  <input type="radio" name={`r${i}`} /> {r}
                </label>
              ))}
            </div>
          </div>
        ))}
        <div className="spacer"></div>
        <Button variant="primary">Save Review</Button>
      </Card>
    </div>
  );
}
