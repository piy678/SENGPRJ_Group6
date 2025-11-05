
import React, { useState } from 'react';
import { Button, Card, Field } from '../../../components/UI.jsx';
export default function CreateGraph() {
  const [title, setTitle] = useState('Understand requirements analysis');
  const [desc, setDesc] = useState('Basic concepts of requirements analysis');
  const [dep, setDep] = useState('');
  const options = ['No dependencies','Understanding requirements analysis','Create Gherkin specifications'];
  return (
    <div className="grid" style={{ gridTemplateColumns:'1fr 1fr' }}>
      <Card>
        <div style={{ fontWeight:600, marginBottom:8 }}>Existing LEOs</div>
        <ol style={{ lineHeight:1.9, marginTop:8 }}>
          <li>Understand requirements analysis</li>
          <li>Create Gherkin specifications</li>
          <li>Develop low-fidelity prototypes</li>
          <li>Apply test-driven development</li>
          <li>Implement the backend with Spring Boot</li>
        </ol>
        <div className="spacer"></div>
        <Button variant="primary">Add new LEO</Button>
      </Card>
      <Card>
        <div style={{ fontWeight:600, marginBottom:8 }}>LEO Editor</div>
        <Field label="LEO Title">
          <input className="input" value={title} onChange={e=>setTitle(e.target.value)} />
        </Field>
        <Field label="Description">
          <input className="input" value={desc} onChange={e=>setDesc(e.target.value)} />
        </Field>
        <Field label="Dependencies (assumes)">
          <select className="select" value={dep} onChange={e=>setDep(e.target.value)}>
            {options.map(o=><option key={o} value={o}>{o}</option>)}
          </select>
        </Field>
        <Field label="">
          <textarea className="textarea" placeholder="Notes…" rows="4"></textarea>
        </Field>
        <Button variant="primary">Save LEO</Button>
      </Card>
    </div>
  );
}
