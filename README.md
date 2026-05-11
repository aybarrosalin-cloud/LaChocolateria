# CHOCOLATERIA

Sistema de gestión empresarial para una chocolatería, desarrollado como aplicación de escritorio con JavaFX y conexión a base de datos SQL Server.

---

## Tecnologías utilizadas

- **Java 21**
- **JavaFX 21** — interfaz gráfica de escritorio
- **Maven** — gestión de dependencias y build
- **SQL Server** — base de datos
- **jpackage** — generación de instalador para Windows

---

## Funcionalidades del sistema

### Autenticación y seguridad
- Pantalla de login con validación de credenciales
- Sistema de roles y permisos por departamento — cada usuario solo ve y accede a lo que le corresponde según su rol
- Alerta automática para usuarios inactivos al intentar iniciar sesión
- Cifrado de contraseñas

### Pantalla de inicio
- Diseño visual completo con banner y logo de la empresa
- Menú lateral completamente funcional con navegación entre módulos
- Accesos rápidos organizados con FlowPane para las funciones más usadas
- Foto de perfil y nombre del usuario autenticado visibles en todas las pantallas
- Navegación fluida sin congelamiento de pantalla gracias al uso de hilos de fondo (Task)

### Gestión de usuarios
- Pantalla para crear, editar y administrar cuentas de usuario
- Asignación de roles por departamento
- Control de acceso según el rol asignado
- Carga dinámica de imagen de perfil personalizada para cada usuario

### Gestión de clientes
- Registro de nuevos clientes
- Consulta y búsqueda de clientes existentes
- Historial de órdenes por cliente

### Gestión de empleados
- Registro completo de empleados
- Consulta de empleados activos

### Gestión de productos
- Registro de productos del catálogo
- Consulta y visualización de inventario

### Gestión de suplidores
- Registro y seguimiento de suplidores o proveedores
- Consulta de suplidores registrados

### Órdenes al proveedor
- Creación de órdenes de compra a suplidores
- Seguimiento del estado de cada orden
- Recepción de mercancía contra orden de proveedor

### Órdenes de cliente
- Registro de órdenes realizadas por clientes
- Detalle de productos por orden
- Consulta de órdenes activas e historial

### Producción
- Solicitudes de producción con detalle de productos a fabricar
- Órdenes de producción internas
- Consulta del estado de producción

### Envíos
- Pantalla de gestión de envíos a clientes
- Registro y seguimiento de cada despacho
- Consulta de envíos realizados

### Reclamos
- Registro de reclamos de clientes
- Gestión y seguimiento del estado de cada reclamo
- Consulta del historial de reclamos

### Maquinaria
- Registro de maquinaria de la empresa
- Control de mantenimiento de maquinaria
- Historial de mantenimientos realizados

### Pagos
- Registro de pagos de ventas
- Registro de pagos de compras a proveedores
- Consulta de pagos realizados

### Módulo de consultas
- Pantallas de consulta separadas por módulo con tabla de resultados
- Scroll horizontal en tablas para visualizar todos los datos cómodamente
- Consultas disponibles: clientes, empleados, envíos, maquinaria, mantenimiento, órdenes de cliente, órdenes de producción, órdenes de proveedor, pagos de compra, pagos de venta, productos, recepción, reclamos, solicitudes de producción, suplidores, usuarios

---

## Contribuidoras

| Contribuidora | Trabajo realizado |
|---|---|
| **Rosalin** | Estructura base del proyecto, pantalla de inicio y menú lateral, sistema de roles y permisos, gestión de usuarios, foto de perfil dinámica, accesos rápidos, alerta de usuario inactivo, navegación sin freeze, módulo de consultas, instalador Windows, correcciones de BOM y controladores |
| **Yara** | Vista de productos, pantalla de suplidor, maquinaria, envíos, reclamos, orden al proveedor, orden de cliente, recepción, solicitud de producción, organización de carpetas, consultas terminadas |

---

## Instalación

1. Clonar el repositorio
2. Abrir con IntelliJ IDEA
3. Configurar la conexión a SQL Server en el archivo de base de datos
4. Ejecutar con Maven: `mvn clean javafx:run`

Para generar el instalador de Windows ejecutar `crear-instalador.bat`
