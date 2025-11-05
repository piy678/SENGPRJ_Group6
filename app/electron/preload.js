
const { contextBridge } = require('electron');
const apiBase = process.env.VITE_API || 'http://localhost:8080';
contextBridge.exposeInMainWorld('config', { apiBase });
