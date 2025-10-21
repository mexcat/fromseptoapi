# json-to-api

API en Spring Boot (Java 17) para recibir un JSON (`/jsonToApi`) y orquestar las llamadas a Estate Manager.

## Docker
```bash
# Construir imagen
docker build -t jsontoapi:local .

# Ejecutar (mapeando config local)
docker run --rm -p 3000:3000   -v "$PWD/config:/app/config"   -e APP_LOCAL_CONFIG=/app/config/local.json   --name jsontoapi jsontoapi:local
```

## Probar
```bash
curl -s http://localhost:3000/jsonToApi   -H "Content-Type: application/json"   -d @samples/sample-ok.json | jq .
```

## docker-compose
```bash
docker compose up --build
```

## Azure Pipelines (Maven + Docker)
Archivo `azure-pipelines.yml` incluido con pasos de build y docker build/push (ajusta variables del ACR).
