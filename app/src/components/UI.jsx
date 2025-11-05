
import React from 'react';
export const Button = ({ children, variant='default', ...rest }) => {
  const cls = ['btn'];
  if (variant==='primary') cls.push('primary');
  if (variant==='danger') cls.push('danger');
  if (variant==='ghost') cls.push('ghost');
  return <button className={cls.join(' ')} {...rest}>{children}</button>;
};
export const Card = ({ children, style }) => <div className="card" style={style}>{children}</div>;
export const Field = ({ label, children }) => (
  <div style={{ marginBottom: 12 }}>
    <div className="muted" style={{ marginBottom: 6 }}>{label}</div>
    {children}
  </div>
);
