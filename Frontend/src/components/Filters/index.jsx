import React from "react";
import "./Filters.css";

const Filters = ({
  selectedParty,
  handlePartySelection,
  parties,
  selectedState,
  handleStateSelection,
  states,
  brazilGeoJson,
  calculateStateCenter
}) => {
  return (
    <div className="filters-container">
      <div className="filter-group">
        <label htmlFor="partido-select">Partido:</label>
        <select
          id="partido-select"
          value={selectedParty}
          onChange={(e) => handlePartySelection(e.target.value)}
        >
          <option value="">Todos</option>
          {parties.map((party) => (
            <option key={party} value={party}>
              {party}
            </option>
          ))}
        </select>
      </div>

      <div className="filter-group">
        <label htmlFor="estado-select">Estado:</label>
        <select
          id="estado-select"
          value={selectedState}
          onChange={(e) => {
            const sigla = e.target.value;
            if (sigla === "") {
              handleStateSelection("", null);
            } else {
              const estado = brazilGeoJson.features.find(
                (f) => f.properties.sigla === sigla
              );
              if (estado) {
                const center = calculateStateCenter(estado);
                handleStateSelection(sigla, { lat: center[0], lng: center[1] });
              }
            }
          }}
        >
          <option value="">Todos</option>
          {states.map((state) => (
            <option key={state} value={state}>
              {state}
            </option>
          ))}
        </select>
      </div>
      
      <div className="filter-badge">
        {selectedParty && (
          <span className="badge party-badge">
            Partido: {selectedParty}
            <button 
              onClick={() => handlePartySelection("")}
              className="badge-clear"
            >
              ×
            </button>
          </span>
        )}
        
        {selectedState && (
          <span className="badge state-badge">
            Estado: {selectedState}
            <button 
              onClick={() => handleStateSelection("", null)}
              className="badge-clear"
            >
              ×
            </button>
          </span>
        )}
      </div>
    </div>
  );
};

export default Filters;
