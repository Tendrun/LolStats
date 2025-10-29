import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import eslintPlugin from "@nabla/vite-plugin-eslint";
import tailwindcss from '@tailwindcss/vite'
import tsconfigPaths from 'vite-tsconfig-paths'

// Local-only Vite config: binds server and preview to 127.0.0.1 and uses a separate build output
export default defineConfig({
  plugins: [react(), eslintPlugin(), tailwindcss(), tsconfigPaths()],
  server: {
    // bind only to localhost to prevent external machines from accessing the dev server
    host: '127.0.0.1',
    // you can specify a port here if you want, e.g. port: 5173
    strictPort: false,
  },
  preview: {
    // preview also bound to localhost only
    host: '127.0.0.1',
  },
  build: {
    // use a separate output folder so local-only build artifacts don't mix with the normal build
    outDir: 'dist-local',
  },
})
