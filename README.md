# CHOCOLATERIA

App de escritorio para manejar todo lo de una chocolatería. Hecha en Java con JavaFX y una base de datos en SQL Server.

---

## Con qué está hecha

- Java 21
- JavaFX 21
- Maven
- SQL Server
- jpackage (para el instalador de Windows)

---

## Qué puede hacer

**Login y acceso**
- Pantalla de login
- Cada usuario tiene un rol y solo ve lo que le toca
- Si un usuario está inactivo le sale una alerta
- Las contraseñas van cifradas

**Pantalla de inicio**
- Menú lateral para moverse entre módulos
- Accesos rápidos a lo más usado
- Muestra el nombre y la foto del usuario que entró
- La navegación no se congela

**Usuarios**
- Crear y editar usuarios
- Asignarles rol y departamento
- Cada quien tiene su foto de perfil

**Clientes**
- Registrar clientes nuevos
- Ver y buscar los que ya están guardados

**Empleados**
- Registrar empleados
- Consultar la lista

**Productos**
- Registrar productos
- Ver el inventario

**Suplidores**
- Agregar suplidores
- Consultar los registrados

**Órdenes al proveedor**
- Hacer órdenes de compra
- Recibir la mercancía cuando llega

**Órdenes de cliente**
- Registrar lo que pide cada cliente
- Ver el historial de órdenes

**Producción**
- Solicitudes de lo que se va a fabricar
- Órdenes internas de producción

**Envíos**
- Registrar cada despacho
- Ver el historial de envíos

**Reclamos**
- Anotar los reclamos de clientes
- Darles seguimiento

**Maquinaria**
- Registrar las máquinas
- Llevar el control de mantenimientos

**Pagos**
- Pagos de ventas
- Pagos de compras a proveedores

**Consultas**
- Una pantalla de consulta por cada módulo
- Las tablas tienen scroll horizontal para ver todo bien

---

## Quiénes lo hicieron

**Rosalin** — estructura del proyecto, pantalla de inicio, menú, sistema de roles, gestión de usuarios, foto de perfil, accesos rápidos, alerta de inactivos, navegación fluida, módulo de consultas, instalador Windows, correcciones varias

**Yara** — productos, suplidores, maquinaria, envíos, reclamos, orden al proveedor, orden de cliente, recepción, producción, organización de carpetas, consultas

---

## Cómo correrla

1. Clonar el repo
2. Abrirlo en IntelliJ IDEA
3. Configurar la conexión a SQL Server
4. Correr con Maven: `mvn clean javafx:run`

Para el instalador de Windows: ejecutar `crear-instalador.bat`
