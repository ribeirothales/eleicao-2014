import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        allowedHosts: ['frontend-d5nv.onrender.com', 'eleicao-2014.netlify.app']
    }
});
