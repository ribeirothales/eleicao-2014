export const fetchStatesData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/eleicao/2014/presidente/primeiro-turno/estados');
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const estados = await response.json();
      
      const stateData = {
        type: 'FeatureCollection',
        features: estados.map(estado => ({
          type: 'Feature',
          properties: {
            id: estado.id,
            nome: estado.nome,
            sigla: estado.estadoSigla || estado.sigla || 'ND',
            votos: estado.votos || []
          },
          geometry: estado.geoJson?.features?.[0]?.geometry || { 
            type: 'MultiPolygon', 
            coordinates: [] 
          }
        }))
      };
      
      return stateData;
      
    } catch (error) {
      console.error('Erro ao buscar dados dos estados:', error);
      return {
        type: "FeatureCollection",
        features: []
      };
    }
  };
  
  
  export const fetchMunicipalitiesByState = async (sigla) => {
    try {
      const response = await fetch(`http://localhost:8080/api/eleicao/2014/presidente/primeiro-turno/estados/${sigla}/municipios`);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const municipios = await response.json();
      
      return {
        type: 'FeatureCollection',
        features: municipios.map(municipio => ({
          type: 'Feature',
          properties: {
            id: municipio.id,
            nome: municipio.nome,
            sigla: sigla,
            votos: municipio.votos || []
          },
          geometry: municipio.geoJson?.features?.[0]?.geometry || { 
            type: 'MultiPolygon', 
            coordinates: [] 
          }
        }))
      };
    } catch (error) {
      console.error(`Erro ao buscar municípios para ${sigla}:`, error);
      return {
        type: "FeatureCollection",
        features: []
      };
    }
  };

  