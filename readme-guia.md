Proyecto: TechStore E-commerce Platform
Contexto de negocio
La empresa TechStore vende:
    • Computadores 
    • Monitores 
    • Componentes 
    • Accesorios 
Actualmente venden por WhatsApp y Excel.
Quieren migrar a una plataforma digital que permita:
    • Gestión de catálogo 
    • Compras online 
    • Control de inventario 
    • Gestión de pedidos 
La plataforma será usada inicialmente por unos 500 clientes registrados y se espera crecer.

Requerimientos funcionales
RF-001 Gestión de usuarios
El sistema debe permitir:
Cliente
    • Registro 
    • Inicio de sesión 
    • Actualización de perfil 
    • Cambio de contraseña 
Administrador
    • Crear usuarios 
    • Bloquear usuarios 
    • Cambiar roles 

RF-002 Gestión de productos
El administrador podrá:
    • Crear producto 
    • Editar producto 
    • Eliminar producto (borrado lógico) 
    • Activar/Inactivar producto 
Cada producto tendrá:
id
nombre
descripcion
precio
stock
categoria
marca
estado
fecha_creacion
fecha_actualizacion

RF-003 Gestión de categorías
Categorías jerárquicas:
Computadores
    ├─ Portátiles
    └─ Escritorio
Monitores
    ├─ Gaming
    └─ Oficina

RF-004 Búsqueda y filtros
El cliente podrá buscar por:
    • Nombre 
    • Categoría 
    • Marca 
    • Rango de precios 
La búsqueda debe soportar paginación.

RF-005 Carrito de compras
El cliente podrá:
    • Agregar producto 
    • Modificar cantidad 
    • Eliminar producto 
    • Vaciar carrito 

RF-006 Proceso de compra
Cuando el cliente confirme:
Validaciones
    • Usuario autenticado 
    • Stock suficiente 
    • Productos activos 

Resultado
Se genera:
Order
OrderItems

RF-007 Gestión de pedidos
Estados:
PENDING
PAID
SHIPPED
DELIVERED
CANCELLED

Requerimientos de seguridad
RS-001
Autenticación JWT.

RS-002
Roles:
ROLE_ADMIN
ROLE_CUSTOMER

RS-003
Endpoints administrativos protegidos.

Requerimientos de concurrencia
RC-001
Dos usuarios no pueden comprar el mismo stock simultáneamente.
Ejemplo:
Stock = 1
Usuario A compra
Usuario B compra
Solo uno debe completar la operación.

RC-002
Las operaciones de compra deben ejecutarse dentro de transacciones.

Requerimientos de base de datos
Motor:
👉 PostgreSQL

Entidades mínimas:
users
roles
products
categories
cart
cart_items
orders
order_items

Requerimientos de API
Seguir REST.
Ejemplos:
POST /api/auth/register
POST /api/auth/login
GET /api/products
GET /api/products/{id}
POST /api/cart/items
POST /api/orders
GET /api/orders/{id}

Requerimientos de rendimiento
RP-001
Consulta de productos:
GET /products
Debe responder en menos de:
300 ms
con:
10.000 productos

RP-002
La aplicación debe soportar:
100 usuarios concurrentes
realizando navegación y compras.

Requerimientos de cache
Usar:
👉 Redis
Cachear:
    • productos populares 
    • categorías 
TTL:
5 minutos

Requerimientos de contenedorización
Usar:
👉 Docker
Crear:
docker-compose
con:
    • aplicación 
    • PostgreSQL 
    • Redis 

Requerimientos de testing
Unitarios
Mínimo:
80%
de cobertura en servicios críticos.

Integración
Probar:
    • login 
    • creación de orden 
    • consulta de productos 

Requerimientos de observabilidad
Implementar:
    • logs estructurados 
    • manejo global de excepciones 

Requerimientos opcionales (nivel "te contrato")
Kafka
👉 Apache Kafka
Cuando se cree una orden:
OrderCreatedEvent
Publicar evento.

Consumidores:
    • inventario 
    • notificaciones 

Criterio de evaluación (como si yo fuera un líder técnico)
Si me muestras:
    • arquitectura limpia 
    • PostgreSQL 
    • Docker 
    • JWT 
    • Redis 
    • testing 
    • manejo de concurrencia 
    • documentación 
Te diría que estás construyendo un proyecto bastante más serio que la mayoría de portfolios junior que veo.
Y algo importante: no intentes hacerlo en una semana. Este es perfectamente un proyecto para 2–4 meses de trabajo constante mientras mantienes tu empleo como psicólogo. Ese nivel de profundidad vale mucho más que hacer tres proyectos superficiales.
Quality Assurance & Performance Requirements
Objetivo
Garantizar:
    • Correctitud funcional 
    • Seguridad 
    • Rendimiento 
    • Confiabilidad 
    • Escalabilidad básica 

🧪 Testing Funcional
Casos de prueba de autenticación
TC-001 Registro exitoso
Dado
    • Usuario no registrado 
Cuando
    • Envía datos válidos 
Entonces
    • Se crea usuario 
    • Retorna HTTP 201 

TC-002 Login exitoso
Dado
    • Usuario registrado 
Cuando
    • Ingresa credenciales válidas 
Entonces
    • Retorna JWT válido 

TC-003 Login fallido
Dado
    • Usuario registrado 
Cuando
    • Ingresa contraseña incorrecta 
Entonces
    • Retorna HTTP 401 

🛒 Testing de carrito
TC-004 Agregar producto
Validar:
    • producto existe 
    • cantidad válida 

TC-005 Producto inexistente
Esperado:
404 NOT FOUND

📦 Testing de órdenes
TC-006 Compra exitosa
Validar:
    • creación de orden 
    • descuento de inventario 
    • persistencia correcta 

TC-007 Stock insuficiente
Validar:
409 CONFLICT

🔒 Testing de Seguridad
TS-001 Endpoint protegido
Intentar acceder sin JWT.
Esperado:
401 Unauthorized

TS-002 Rol incorrecto
Cliente intenta:
POST /admin/products
Esperado:
403 Forbidden

TS-003 JWT inválido
Validar:
401 Unauthorized

⚡ Testing de Concurrencia
TC-008 Compra simultánea
Escenario:
Stock = 1
Dos usuarios compran al mismo tiempo.
Resultado esperado:
1 compra exitosa
1 compra rechazada

Implementación sugerida
Usar:
@Lock(LockModeType.PESSIMISTIC_WRITE)
o
@Version
(optimistic locking)

🧪 Unit Testing
Objetivo
Cobertura mínima:
80%
en:
    • Service Layer 
    • Reglas de negocio 

Casos mínimos
ProductService
    • crear producto 
    • actualizar producto 
    • eliminar producto 

OrderService
    • compra exitosa 
    • stock insuficiente 
    • producto inexistente 

🔗 Integration Testing
Utilizar:
@SpringBootTest
Validar:
    • login 
    • creación de órdenes 
    • consulta de productos 

🚀 API Performance Testing
Herramientas sugeridas:
    • JMeter 
    • K6 
    • Gatling 

Escenario 1
Consulta catálogo
Endpoint:
GET /products
Usuarios concurrentes:
100
Duración:
5 minutos
Objetivo:
Response time < 300 ms

Escenario 2
Consulta producto individual
Endpoint:
GET /products/{id}
Usuarios:
200
Objetivo:
Response time < 200 ms

🗄️ Database Performance Testing
Motor:
👉 PostgreSQL

Dataset mínimo
Generar:
10.000 productos
5.000 usuarios
50.000 órdenes

Pruebas requeridas
Query catálogo
Medir:
SELECT *
FROM products
LIMIT 20;

Query filtrada
SELECT *
FROM products
WHERE category_id = ?

Query de historial de órdenes
SELECT *
FROM orders
WHERE user_id = ?

Optimización requerida
Analizar:
EXPLAIN ANALYZE

Crear índices para:
products(category_id)
products(name)
orders(user_id)
orders(created_at)

🧠 Redis Cache Validation
Motor:
👉 Redis

Caso RC-001
Primera consulta:
GET /products/popular
Resultado esperado:
Cache MISS

Caso RC-002
Segunda consulta:
GET /products/popular
Resultado esperado:
Cache HIT

Métricas
Comparar:
Sin cache
Con cache
Medir:
    • tiempo promedio 
    • reducción de consultas SQL 

📊 Observabilidad
Implementar logs para:
Login
Usuario autenticado
Creación de orden
Orden creada
Error de stock
Stock insuficiente

🎯 Bonus (Nivel "backend serio")
Implementar dashboard de métricas con:
    • Spring Boot Actuator 
    • Prometheus 
    • Grafana 
Monitorear:
    • Requests por minuto 
    • Tiempo promedio de respuesta 
    • Errores HTTP 
    • Uso de memoria 
    • Conexiones activas 
