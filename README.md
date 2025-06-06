# Eleições Presidenciais de 2014 

##  Visão Geral

O backend do projeto tem como objetivo fornecer uma API RESTful para disponibilização de dados eleitorais agregados por estado e por município, incluindo informações de geolocalização (GeoJSON) para visualização em mapas. Esses dados foram extraídos de duas API's disponibilizadas pelo IBGE e de um json.

O frontend por sua vez vai receber esses dados passados pelo back e implementar em uma estrutura gráfica do mapa do Brasil, com opções interativas como hover com as informações do estado e dois selectors, permitindo filtrar o mapa por Estados ou por Partidos.

- Acesse aqui: (https://eleicao-2014.netlify.app/)

Tecnologias utilizadas:
- Java 21 - Maven
- Spring Boot
- MongoDB
- Jackson (leitura de arquivos JSON)
- React + Vite
- Leaflet (biblioteca de mapas)
- Axios

## Arquitetura Geral

A aplicação segue uma arquitetura baseada em microsserviços, de uma forma mais simples, mas dividida entre três componentes principais: o frontend (interface com o usuário feita com React), a API backend (servidor Spring Boot) e o banco de dados (MongoDB). Cada camada é separada e se comunica exclusivamente por meio de requisições HTTP e troca de dados em formato JSON.

![MicroServiços](https://i.imgur.com/CaiQNhy.png)

##  Arquitetura do Backend

A arquitetura segue o padrão MVC (Model-View-Controller), com as seguintes camadas:

- **Controller**: expõe os endpoints REST.
- **Service**: centraliza a lógica de negócio.
- **Repository**: abstração de acesso ao banco de dados MongoDB.
- **Model**: representa documentos MongoDB.
- **DTO**: usado para transportar dados entre camadas sem expor os modelos diretamente.
- **DataLoader**: executa o carregamento dos dados a partir de arquivos JSON ao iniciar a aplicação.

![Classes](https://i.imgur.com/DdQENTF.png)
![Classes](https://i.imgur.com/4C4CUTo.png)

##  Fluxo de Execução

1. Usuário faz requisição para a API (ex: /api/eleicao/2014/presidente/primeiro-turno/estados).
2. Controller encaminha para o Service.
3. Service determina os repositórios apropriados.
4. Repository executa consulta ao MongoDB.
5. Dados são retornados com votos e geoJson embutidos.

![Fluxos](https://i.imgur.com/ULJ8Zvx.png)


## Endpoints REST

GET `/api/eleicao/{ano}/{cargo}/{turno}/estados`
- Retorna a lista de estados com votos agregados e suas malhas geográficas (GeoJSON).


GET `/api/eleicao/{ano}/{cargo}/{turno}/estados/{estado}/municipios`
- Retorna a lista de municípios com votos agregados e GeoJSON do estado informado.
- {estado} pode ser sigla (RJ) ou código numérico (33)


## Banco de Dados MongoDB

Optei pelo MongoDB neste projeto por sua capacidade de lidar com dados sem estrutura rígida, como é o caso dos documentos JSON utilizados para representar estados e municípios com seus respectivos votos e malhas geográficas.

## Frontend

O frontend do projeto foi desenvolvido com React e utiliza a biblioteca Leaflet para renderização de mapas interativos. Seu objetivo principal é exibir visualmente os dados eleitorais retornados pela API, permitindo ao usuário explorar resultados por estado e por município.

### Funcionalidades:
- Mapa interativo com destaque de estados e municípios.
- Zoom automático ao clicar em um estado, revelando seus municípios.
- Tooltip em cada estado exibe os dados agregados daquele estado, incluindo os três candidatos mais votados (retirados do endpoint).
- Esquema de cores por partido: ao selecionar um partido, os estados ou municípios são coloridos com base na votação obtida por esse partido.
- Como são mais de 5000 mil municípos no Brasil, o site ficaria muito lento se toda vez que fosse aberto fosse necessário ler todos os municípios, então fiz uma implementação em que os dados dos municípios só são requisitados e carregados quando um estado específico é selecionado pelo usuário.

![Mapa](https://i.imgur.com/kZGR8CS.png)
![Mapa](https://i.imgur.com/HjaAK92.png)
![Mapa](https://i.imgur.com/5ecWiOi.png)

## Melhorias Futuras

- Implementação de testes
- Adicionar exigência de autenticação quando fizer requisição ao backend
- Utilizar redux a fim de gerenciar estados da aplicação no frontend
- Criar CI/CD para fazer o build e push da imagem para um repositório de imagens

## Portas utilizadas para acesso

Frontend
```
http://localhost:3000
```

Backend

```
8080
```

MongoDB

```
27017
```

## Acesse a aplicação

Windows
```
docker compose up -d
```

Linux

```
docker-compose up -d
```



