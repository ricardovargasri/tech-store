#!/usr/bin/env bash
set -e

# ------------------------------------------------------------
# start-all.sh – arrancar entorno de desarrollo TechStore (sin Docker Compose)
# ------------------------------------------------------------
# 1) Iniciar Keycloak en modo desarrollo (background)
# 2) Esperar a que Keycloak esté disponible
# 3) Lanzar la API Spring Boot
# ------------------------------------------------------------

# --- 1) Iniciar Keycloak ---
KEYCLOAK_HOME="$HOME/Documentos/keycloak-26.6.1"
KEYCLOAK_SCRIPT="$KEYCLOAK_HOME/bin/kc.sh"

if [ ! -x "$KEYCLOAK_SCRIPT" ]; then
  echo "ERROR: No se encontró el script de Keycloak en $KEYCLOAK_SCRIPT" >&2
  exit 1
fi

echo "[+] Arrancando Keycloak (modo dev) en background..."
"$KEYCLOAK_SCRIPT" start-dev &
KEYCLOAK_PID=$!

# --- 2) Esperar a que Keycloak esté listo ---
echo -n "[+] Esperando a que Keycloak responda"
for i in {1..30}; do
  if curl -s http://localhost:8080 > /dev/null; then
    echo " ✅"
    break
  fi
  echo -n "."
  sleep 2
  if [ $i -eq 30 ]; then
    echo "\nERROR: Keycloak no respondió después de 60 segundos."
    kill $KEYCLOAK_PID || true
    exit 1
  fi
done

# --- 3) Lanzar la API Spring Boot ---
echo "[+] Iniciando la aplicación Spring Boot..."
./mvnw spring-boot:run

# Cuando la aplicación termine, matar Keycloak
die "--- Entorno detenido ---"
kill $KEYCLOAK_PID || true
