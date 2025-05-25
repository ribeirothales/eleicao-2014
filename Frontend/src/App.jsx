import React from "react";
import Map from "./pages/Map/index";
import "./App.css";

function App() {
    return (
        <div className="app-container">
            <header className="header">
                <h1>Distribuição de votos por partido nas unidades político administrativas</h1>
                <p>Visualize dados eleitorais de 2014 em um mapa interativo do Brasil</p>
            </header>
            
            <div className="map-container">
                {/* Efeito de glow ao redor do mapa */}
                <div className="map-glow-effect"></div>
                
                {/* Componente do mapa mantido intacto */}
                <Map />
            </div>
            
            <footer className="footer">
                <p>© {new Date().getFullYear()} | Dados eleitorais do Brasil - Eleições 2014</p>
            </footer>
        </div>
    );
}

export default App;
