# json-to-api

API en Spring Boot (Java 17) para recibir un JSON (`/jsonToApi`) y orquestar las llamadas a Estate Manager.

- Puerto: **3000**
- Respuesta estándar: `{"status":"success|fail","error":"descripción del error"}`
- Base URL configurable en `config/local.json` (o variable `APP_BASE_URL`)
- Credenciales **NO** se registran en logs.

## Flujo (A → D)
1. **A**: Buscar `channel` en `ChannelData` de `local.json`. Si no existe ⇒ `fail` `channel no encontrado`.
2. `getFolder`: `GET /emapi/dms/terminals/criterias/?category=0&precise=false&recursive=true&signature={channelData.folder}`
3. **B**: Normaliza `terminal_code` (agrega `A` si no la trae). `GET /emapi/dms/terminals/signature/{terminalSignature}`.
4. **C-1**: Si no existe ⇒ `POST /emapi/dms/terminals` (usa `folderData.id`).
5. **C-2**: Si existe pero no está en carpeta ⇒ `POST /emapi/dms/terminals/{terminalId}/parent/{folderId}`.
6. **D**: `PUT /emapi/pms/terminals/{terminalId}/{channelData.template}/{channelData.version}` con body generado desde `jsonData` y `config/param-mapping.json`.

## Variables/Configuración
- `config/local.json`: credenciales, baseUrl, ChannelData.
- `config/param-mapping.json`: mapeo `jsonData` → parámetros para el paso D. Ajustable.

## Ejecutar localmente
```bash
# Compilar
mvn -q -DskipTests package

# Ejecutar
java -jar target/json-to-api-0.1.0.jar
```

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
