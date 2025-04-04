import React from "react";
import Map from "./pages/Map/index";
import "./App.css";

function App() {
    return (
        <div className="container">
            <h1>Distribuição de votos por partido nas unidades político administrativas</h1>
            <Map />
        </div>

    );
}

export default App;