
import React, { useState } from 'react';
import { Button, Card, Field } from '../../../components/UI.jsx';
const data = [
  { name:'Anna Müller', a:1, p:1, n:3, t:5, progress:20 },
  { name:'Lisa Weber', a:1, p:1, n:3, t:5, progress:20 },
  { name:'Max Schmidt', a:1, p:1, n:3, t:5, progress:20 },
];
export default function MonitorProgress() {
  const [search, setSearch] = useState('');
  const [min, setMin] = useState(0);
  const [sort, setSort] = useState('Name');
  const filtered = data
    .filter(d => d.name.toLowerCase().includes(search.toLowerCase()))
    .filter(d => d.progress >= min)
    .sort((a,b)=> sort==='Progress ⬆' ? a.progress-b.progress
                  : sort==='Progress ⬇' ? b.progress-a.progress
                  : a.name.localeCompare(b.name));
  return (
    <div>
      <div className="row" style={{ gap:12 }}>
        <Field label="Search"><input className="input" placeholder="Name" value={search} onChange={e=>setSearch(e.target.value)} /></Field>
        <Field label="Min. %"><input className="input" type="number" value={min} onChange={e=>setMin(Number(e.target.value)||0)} /></Field>
        <Field label="Sorting">
          <select className="select" value={sort} onChange={e=>setSort(e.target.value)}>
            <option>Name</option>
            <option>Progress ⬆</option>
            <option>Progress ⬇</option>
          </select>
        </Field>
        <div style={{ alignSelf:'flex-end' }}><Button variant="primary">Apply</Button></div>
      </div>
      <div className="spacer"></div>
      <Card>
        <table>
          <thead>
            <tr>
              <th>Name</th><th>Achieved</th><th>Partially</th><th>Not Assessed</th><th>Total</th><th>Progress</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((r,i)=>(
              <tr key={i}>
                <td>{r.name}</td><td>{r.a}</td><td>{r.p}</td><td>{r.n}</td><td>{r.t}</td>
                <td>{r.progress}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>
    </div>
  );
}
