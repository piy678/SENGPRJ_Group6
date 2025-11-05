
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
export default defineConfig({
 plugins: [react({ fastRefresh: true })],
  base: '/',                    
  server: { port: 5173, strictPort: true },
  build: { outDir: 'dist' }
});
