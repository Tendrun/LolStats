import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import eslintPlugin from "@nabla/vite-plugin-eslint";
import tailwindcss from '@tailwindcss/vite'
import tsconfigPaths from 'vite-tsconfig-paths'


// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), eslintPlugin(), tailwindcss(), tsconfigPaths()],
  server: {
    host: '127.0.0.1',
    port: 5000,
    strictPort: true,
  },
  preview: {
    host: '127.0.0.1',
    port: 5000,
    strictPort: true,
  },
})
