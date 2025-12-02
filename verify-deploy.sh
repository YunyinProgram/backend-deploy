#!/bin/bash

echo "🔍 Verificando configuración para Render..."
echo ""

# Verificar que existen archivos necesarios
echo "✅ Verificando archivos..."
files=("Dockerfile" "render.yaml" "pom.xml" "src/main/resources/application-prod.properties")
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✓ $file existe"
    else
        echo "   ✗ $file NO EXISTE"
        exit 1
    fi
done
echo ""

# Verificar que MINDORA_MYSQL_PWD esté configurado
echo "✅ Verificando variables de entorno..."
if [ -z "$MINDORA_MYSQL_PWD" ]; then
    echo "   ⚠️  MINDORA_MYSQL_PWD no está configurado (necesario para Render)"
    echo "      En Render, debes configurarlo en Environment Variables"
else
    echo "   ✓ MINDORA_MYSQL_PWD está configurado"
fi
echo ""

# Test de Docker build local
echo "🐳 ¿Deseas probar el build de Docker localmente? (s/n)"
read -r response
if [[ "$response" =~ ^[Ss]$ ]]; then
    echo "   Construyendo imagen Docker..."
    docker build -t mindora-backend:test . || {
        echo "   ✗ Error en Docker build"
        exit 1
    }
    echo "   ✓ Docker build exitoso"
    echo ""
    echo "   Para probar localmente, ejecuta:"
    echo "   docker run -p 8080:8080 -e MINDORA_MYSQL_PWD=tu_password -e SPRING_PROFILES_ACTIVE=prod mindora-backend:test"
fi
echo ""

echo "✅ Verificación completa. Listo para desplegar a Render!"
echo ""
echo "📝 Próximos pasos:"
echo "   1. Push a GitHub: git push origin develop"
echo "   2. Ve a Render Dashboard: https://dashboard.render.com/"
echo "   3. Crea un nuevo Blueprint y conecta tu repositorio"
echo "   4. Configura MINDORA_MYSQL_PWD en Environment Variables"
echo ""

