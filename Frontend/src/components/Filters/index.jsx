import React from "react";

const Filters = ({
  selectedParty,
  handlePartySelection,
  parties,
  selectedState,
  handleStateSelection,
  states,
  brazilGeoJson,
  calculateStateCenter,
}) => {
  return (
    <div className="filters" style={{ display: "flex", justifyContent: "center", gap: "20px", marginBottom: "30px" }}>
      <label className="labels">
        Partido:
        <select value={selectedParty} onChange={(e) => handlePartySelection(e.target.value)}>
          <option value="">All</option>
          {parties.map((p) => (
            <option key={p} value={p}>
              {p}
            </option>
          ))}
        </select>
      </label>
      <label className="labels">
        Estado:
        <select
          value={selectedState}
          onChange={(e) => {
            const sigla = e.target.value;
            if (sigla === "") {
              handleStateSelection("", [-14.235, -51.9253]);
            } else {
              const estado = brazilGeoJson.features.find((f) => f.properties.sigla === sigla);
              if (estado) {
                const centerCoords = calculateStateCenter(estado);
                handleStateSelection(sigla, centerCoords);
              } else {
                handleStateSelection("", [-14.235, -51.9253]);
              }
            }
          }}
        >
          <option value="">All</option>
          {states.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </label>
    </div>
  );
};

export default Filters;