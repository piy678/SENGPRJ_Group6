// Do not place shell commands inside JS source files; set environment variables in your shell instead.
// Optionally provide a runtime default if VITE_DEV_SERVER_URL is not already set:
//if (!process.env.VITE_DEV_SERVER_URL) {
 // process.env.VITE_DEV_SERVER_URL = 'http://localhost:5174';}

import { app, BrowserWindow } from 'electron';
import path from 'path';
import { fileURLToPath } from 'url';
//const { app, BrowserWindow } = require('electron');
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const isDev = !app.isPackaged;
let win;
function createWindow() {
  win = new BrowserWindow({
    width: 1280, height: 800,
    title: 'LEO Assessment',
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
       nodeIntegration: false,
    },
  });
  //const isDev = !app.isPackaged;
  if (isDev) {
    //win.loadURL('http://localhost:5173');
    const devUrl = process.env.VITE_DEV_SERVER_URL ?? 'http://localhost:5173';
    win.loadURL(devUrl);
    win.webContents.openDevTools({ mode: 'detach' });
  } else {
    // In electron/main.js
if (process.env.VITE_DEV_SERVER_URL) {
  win.loadURL(process.env.VITE_DEV_SERVER_URL);
} else {
  win.loadFile(path.join(__dirname, '..', 'dist', 'index.html'));
}
  }
    win.webContents.on('did-fail-load', (_e, code, desc, url) => {
    console.error('did-fail-load:', code, desc, url);
  });

}

app.whenReady().then(() => {
  createWindow();
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});
app.on('window-all-closed', () => { if (process.platform !== 'darwin') app.quit(); });
//app.on('activate', () => { if (BrowserWindow.getAllWindows().length === 0) createWindow(); });
