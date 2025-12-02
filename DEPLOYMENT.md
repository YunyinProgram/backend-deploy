# 🚀 Deployment a Render

## Configuración Automática con render.yaml

Este proyecto está configurado para desplegarse automáticamente en Render usando Docker.

### Pasos para Desplegar:

#### 1. **Preparar el Repositorio**
   - Asegúrate de que todos los cambios estén en la rama `develop` (o cambia la rama en `render.yaml`)
   - Push al repositorio de GitHub

#### 2. **Crear el Servicio en Render**
   
   **Opción A: Usando render.yaml (Recomendado)**
   - Ve a [Render Dashboard](https://dashboard.render.com/)
   - Click en "New +" → "Blueprint"
   - Conecta tu repositorio de GitHub
   - Render detectará automáticamente el `render.yaml` y configurará todo

   **Opción B: Configuración Manual**
   - Ve a [Render Dashboard](https://dashboard.render.com/)
   - Click en "New +" → "Web Service"
   - Conecta tu repositorio de GitHub
   - Configura:
     - **Name**: `mindora-backend`
     - **Environment**: `Docker`
     - **Branch**: `develop`
     - **Plan**: Free

#### 3. **Configurar Variables de Entorno**
   
   En la sección "Environment" del servicio, agrega:

   | Variable | Valor |
   |----------|-------|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `MINDORA_MYSQL_PWD` | `[tu-password-de-azure]` |
   | `TZ` | `America/Lima` |

   ⚠️ **IMPORTANTE**: `MINDORA_MYSQL_PWD` debe ser la contraseña de tu base de datos Azure.

#### 4. **Desplegar**
   - Render automáticamente:
     - Construirá la imagen Docker
     - Compilará el proyecto Maven
     - Desplegará la aplicación
   - El proceso toma aproximadamente 5-10 minutos en el primer deploy

#### 5. **Verificar el Deployment**
   - Una vez desplegado, Render te dará una URL: `https://mindora-backend.onrender.com`
   - Verifica el health check: `https://mindora-backend.onrender.com/actuator/health`
   - Accede al Swagger: `https://mindora-backend.onrender.com/swagger-ui/index.html`

---

## 🔧 Configuración Técnica

### Dockerfile Multi-Stage
- **Stage 1 (Builder)**: Compila el proyecto con Maven
- **Stage 2 (Runtime)**: Imagen ligera con JRE para ejecutar la app

### Health Check
- Endpoint: `/actuator/health`
- Render verifica cada 30 segundos que la app esté funcionando

### Recursos (Plan Free)
- RAM: 512 MB
- CPU: Compartida
- La app se duerme después de 15 minutos de inactividad
- El primer request después de dormir toma ~30 segundos

---

## 🐛 Troubleshooting

### El build falla con "Permission denied"
✅ **Solucionado**: El Dockerfile incluye `chmod +x mvnw`

### El build falla con "Could not find package.json"
✅ **Solucionado**: Usando Docker elimina la necesidad de Node.js

### Error de conexión a la base de datos
- Verifica que `MINDORA_MYSQL_PWD` esté configurado correctamente
- Verifica que Azure MySQL permita conexiones desde la IP de Render
- En Azure Portal → MySQL → "Connection security" → Habilita "Allow access to Azure services"

### El servicio se muestra como "unhealthy"
- Verifica los logs en Render Dashboard
- Asegúrate de que la base de datos esté accesible
- El health check puede tardar hasta 60 segundos en el primer inicio

---

## 📝 Notas Importantes

1. **Plan Free de Render**:
   - La app se duerme después de 15 min de inactividad
   - 750 horas/mes gratis (suficiente para un proyecto)
   - Si necesitas que esté siempre activa, upgrade a plan Starter ($7/mes)

2. **Base de Datos Azure**:
   - Asegúrate de tener el firewall configurado para permitir conexiones externas
   - Considera usar Azure Database for MySQL - Flexible Server para mejor compatibilidad

3. **CORS**:
   - Ya está configurado en `SecurityConfig.java` para permitir `http://localhost:4200`
   - Actualiza para incluir tu frontend en producción: `https://tu-frontend.vercel.app`

---

## 🔄 Redespliegue Automático

Render redesplega automáticamente cuando:
- Haces push a la rama configurada (`develop`)
- Puedes hacer deploy manual desde el Dashboard

---

## 📊 Monitoreo

- **Logs**: Disponibles en tiempo real en Render Dashboard
- **Métricas**: CPU, RAM, requests/segundo
- **Health**: `/actuator/health` monitoreado cada 30 segundos

