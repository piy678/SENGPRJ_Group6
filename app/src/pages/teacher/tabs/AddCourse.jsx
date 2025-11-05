import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card, Field } from '../../../components/UI.jsx';


export default function AddCourse() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: '',
    students: '',
    leos: ''
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const onChange = (key) => (e) => {
    const v = e.target.value;
    setForm(f => ({ ...f, [key]: v }));
  };

  const onSave = async () => {
    setSaving(true);
    setError(null);
    try {
      const base = window.config?.apiBase || 'http://localhost:8080';
      // Adjust payload/endpoint to your backend model
      const res = await fetch(`${base}/courses`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: form.name,
          numberOfStudents: Number(form.students || 0),
          numberOfLeos: Number(form.leos || 0)
        })
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      // Go back to courses list (Teacher dashboard)
      navigate('/teacher');
    } catch (e) {
      setError('Could not save course. Check backend and try again.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <div className="row" style={{ justifyContent:'space-between', alignItems:'center', marginBottom:12 }}>
        <div style={{ fontWeight:700 }}>Teacher Dashboard</div>
        <Button variant="ghost" onClick={() => (window.history.length > 1 ? navigate(-1) : navigate('/teacher'))}>
          Back
        </Button>
      </div>

      <div style={{ display:'flex', justifyContent:'center' }}>
        <Card style={{ maxWidth: 520, width: '100%' }}>
          <div style={{ textAlign:'center', fontWeight:600, marginBottom:16 }}>Add new course</div>

          <Field label="Course Name">
            <input className="input" placeholder="Value" value={form.name} onChange={onChange('name')} />
          </Field>

          <Field label="Number of Students">
            <input className="input" placeholder="Value" inputMode="numeric"
                   value={form.students} onChange={onChange('students')} />
          </Field>

          <Field label="Number of LEOs">
            <textarea className="input" placeholder="Value"
                      value={form.leos} onChange={onChange('leos')} />
          </Field>

          {error ? <div className="muted" style={{ color:'#dc2626', marginBottom:10 }}>{error}</div> : null}

          <Button variant="primary" onClick={onSave} disabled={saving}>
            {saving ? 'Saving…' : 'Save'}
          </Button>
        </Card>
      </div>
    </div>
  );
}
