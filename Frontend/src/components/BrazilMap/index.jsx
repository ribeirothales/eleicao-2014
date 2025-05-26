import { useRef, useState, useEffect } from "react";
import { MapContainer, TileLayer, GeoJSON } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { fetchStatesData, fetchMunicipalitiesByState } from "../../data/api";
import Filters from "../Filters";
import './MapLoading.css';


const parties = ["PSB", "PSDB", "PT", "PSOL", "PSC", "PV"];
const states = [
  "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA",
  "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", 
  "RS", "RO", "RR", "SC", "SE", "SP", "TO"
];
const LoadingOverlay = () => (
  <div className="loading-overlay">
    <div className="loading-spinner"></div>
    <p className="loading-text">Carregando mapa...</p>
  </div>
);


const MapBrazil = () => {
  const mapRef = useRef(null);
  const [selectedState, setSelectedState] = useState("");
  const [selectedParty, setSelectedParty] = useState("");
  const [selectedMunicipios, setSelectedMunicipios] = useState([]);
  const [selectedPartyVotesByState, setSelectedPartyVotesByState] = useState([]);
  const [brazilGeoJson, setBrazilGeoJson] = useState({ type: "FeatureCollection", features: [] });
  const [loading, setLoading] = useState(true);
  const [loadingMunicipalities, setLoadingMunicipalities] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        const statesData = await fetchStatesData();
        setBrazilGeoJson(statesData);
      } catch (error) {
        console.error("Erro ao carregar dados:", error);
      } finally {
        setLoading(false);
      }
    };
    
    loadData();
  }, []);

  useEffect(() => {
    const loadMunicipalities = async () => {
      if (selectedState) {
        try {
          setLoadingMunicipalities(true);
          const municipalitiesData = await fetchMunicipalitiesByState(selectedState);
          
          const filteredMunicipios = selectedParty 
            ? municipalitiesData.features.filter(m => 
                m.properties.votos.some(v => v.partido === selectedParty))
            : municipalitiesData.features;
          
          setSelectedMunicipios(filteredMunicipios);
        } catch (error) {
          console.error("Erro ao carregar municípios:", error);
          setSelectedMunicipios([]);
        } finally {
          setLoadingMunicipalities(false);
        }
      } else {
        setSelectedMunicipios([]);
      }
    };

    loadMunicipalities();
  }, [selectedState, selectedParty]);

  const handleStateSelection = (sigla, latlng) => {
    setSelectedMunicipios([]);
    setSelectedState(sigla);

    if (mapRef.current) {
      if (sigla === "") {
        mapRef.current.setView([-14.235, -51.9253], 4, { animate: true });
      } else {
        mapRef.current.setView(latlng, 6, { animate: true });
      }
    }
  };

  const handlePartySelection = (party) => {
    setSelectedState("");
    setSelectedMunicipios([]);
    setSelectedParty(party);

    if (mapRef.current) {
      mapRef.current.setView([-14.235, -51.9253], 4, { animate: true });
    }

    if (party) {
      const filteredStatesByParty = brazilGeoJson.features.map((state) => {
        const matchedVotos = state.properties.votos.reduce((acc, voto) => {
          if (voto.partido === party) {
            acc.push(voto);
          }
          return acc;
        }, []);
        
        return {
          ...state,
          properties: {
            ...state.properties,
            votos: matchedVotos,
          },
        };
      });
      setSelectedPartyVotesByState(filteredStatesByParty);
    } else {
      setSelectedPartyVotesByState([]);
    }
  };

  const getColor = (percentage) => {
    if (percentage > 60) return "#800026";
    if (percentage > 40) return "#BD0026";
    if (percentage > 25) return "#E31A1C";
    if (percentage > 10) return "#FC4E2A";
    if (percentage > 5) return "#FD8D3C";

    if (percentage > 2) return "#800026";
    if (percentage > 1) return "#BD0026";
    if (percentage > 0.5) return "#E31A1C";
    if (percentage > 0.2) return "#FC4E2A";
    if (percentage > 0.1) return "#FD8D3C";
    return "#FFEDA0";
  };

  const stateStyle = (feature) => {
    const votos = feature.properties.votos;
    
    if (!votos || votos.length === 0) {
      return {
        fillColor: "#aaaaaa",
        fillOpacity: 0.5,
        color: "white",
        weight: 1,
      };
    }
    
    const percentage = votos[0].porcentagem;
    return {
      fillColor: getColor(percentage),
      fillOpacity: 0.7,
      color: "white",
      weight: 1,
    };
  };

  const onEachFeature = (feature, layer) => {
    if (feature.properties) {
      const { id, nome, sigla, votos } = feature.properties;
      
      const topVotos = votos 
        ? votos.sort((a, b) => b.quantidade - a.quantidade).slice(0, 3)
        : [];
      
      const votosInfo = topVotos.map(voto => 
        `${voto.candidato} (${voto.partido}): ${voto.quantidade} votos (${voto.porcentagem}%)`
      ).join("<br>");
      
      const tooltipContent = `<b>${nome} (${sigla}, ID: ${id})</b><br>${votosInfo || 'Sem dados'}`;
      
      // tooltip indo pra baixo em RR e AP
      const direction = (sigla === 'RR' || sigla === 'AP') ? 'bottom' : 'top';
      
      layer.bindTooltip(tooltipContent, {
        permanent: false,
        direction: direction, 
        opacity: 0.9
      });
      
      layer.on({
        mouseover: (e) => {
          if (!selectedParty) {
            e.target.setStyle({ fillOpacity: 0.7 });
          }
        },
        mouseout: (e) => {
          if (!selectedParty) {
            e.target.setStyle({ fillOpacity: 0.5 });
          }
        },
        click: (e) => handleStateSelection(sigla, e.latlng)
      });
    }
  };

  const onEachMunicipio = (feature, layer) => {
    if (feature.properties) {
      const { nome } = feature.properties;
      layer.bindTooltip(`<b>${nome}</b>`, { permanent: false, direction: "top", opacity: 0.9 });
    }
  };

  const calculateStateCenter = (estado) => {
    try {
      if (estado.geometry.type === "Polygon") {
        const coordinates = estado.geometry.coordinates[0];
        let lat = 0, lng = 0;
        coordinates.forEach(coord => {
          lat += coord[1];
          lng += coord[0];
        });
        return [lat / coordinates.length, lng / coordinates.length];
      } else if (estado.geometry.type === "MultiPolygon") {
        const coordinates = estado.geometry.coordinates[0][0];
        let lat = 0, lng = 0;
        coordinates.forEach(coord => {
          lat += coord[1];
          lng += coord[0];
        });
        return [lat / coordinates.length, lng / coordinates.length];
      }
    } catch (e) {
      console.error("Erro ao calcular centro do estado:", e);
    }
    return [-14.235, -51.9253];
  };

  if (loading) {
    return <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '50vh',
      fontSize: '1.2rem'
    }}></div>;
  }

  return (
    <div>
      <Filters
        selectedParty={selectedParty}
        handlePartySelection={handlePartySelection}
        parties={parties}
        selectedState={selectedState}
        handleStateSelection={handleStateSelection}
        states={states}
        brazilGeoJson={brazilGeoJson}
        calculateStateCenter={calculateStateCenter}
      />
      <div style={{ height: "calc(80vh - 100px)", width: "100%", position: 'relative' }}>
        {loadingMunicipalities && (
          <div style={{
            position: 'absolute',
            top: '10px',
            left: '50%',
            transform: 'translateX(-50%)',
            zIndex: 1000,
            backgroundColor: 'rgba(255,255,255,0.8)',
            padding: '5px 10px',
            borderRadius: '5px'
          }}>
            Carregando municípios...
          </div>
        )}
        <MapContainer 
          ref={mapRef} 
          center={[-14.235, -51.9253]} 
          zoom={4} 
          style={{ height: "100%", width: "100%", background: "#ddd" }}
          maxBounds={[
            [-33.75, -73.99],
            [5.27, -34.79],
          ]}
          maxBoundsViscosity={1.0}
        >
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
          
          {!selectedParty && (
            <GeoJSON 
              key={`base-${selectedParty}-${selectedState}`}
              data={brazilGeoJson}
              style={() => ({
                fillColor: "#007bff",
                fillOpacity: 0.5,
                color: "white",
                weight: 1
              })}
              onEachFeature={onEachFeature}
            />
          )}

          {selectedState && selectedMunicipios.length > 0 && (
            <GeoJSON 
              key={`municipios-${selectedState}-${selectedParty}`}
              data={{ type: "FeatureCollection", features: selectedMunicipios }}
              style={() => ({ fillColor: "#ff7f00", fillOpacity: 0.5, color: "white", weight: 1 })} 
              onEachFeature={onEachMunicipio}
            />
          )}

          {selectedParty && selectedPartyVotesByState.length > 0 && (
            <GeoJSON 
              key={`party-${selectedParty}`}
              data={{ type: "FeatureCollection", features: selectedPartyVotesByState }}
              style={stateStyle} 
              onEachFeature={onEachFeature}
            />
          )}
        </MapContainer>
      </div>
    </div>
  );
};

export default MapBrazil;