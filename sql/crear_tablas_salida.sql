-- ============================================================
-- Script: crear_tablas_salida.sql
-- Motor: SQL Server
-- Descripcion: Crea las 4 tablas faltantes para las pantallas
--              de Salida de Materiales y Salida de Productos.
-- ============================================================

-- ----------------------------------------
-- 1. SALIDA DE MATERIALES (maestro)
-- ----------------------------------------
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbl_salida_materiales' AND xtype='U')
CREATE TABLE tbl_salida_materiales (
    id_salida       INT           NOT NULL IDENTITY(1,1) PRIMARY KEY,
    id_solicitud    INT           NULL,
    fecha_salida    DATE          NOT NULL,
    responsable     VARCHAR(150)  NULL,
    observaciones   VARCHAR(500)  NULL
);

-- ----------------------------------------
-- 2. SALIDA DE MATERIALES (detalle)
-- ----------------------------------------
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbl_salida_materiales_detalle' AND xtype='U')
CREATE TABLE tbl_salida_materiales_detalle (
    id_detalle      INT           NOT NULL IDENTITY(1,1) PRIMARY KEY,
    id_salida       INT           NOT NULL,
    codigo_producto VARCHAR(50)   NOT NULL,
    producto        VARCHAR(200)  NOT NULL,
    cantidad        INT           NOT NULL,
    unidad_medida   VARCHAR(50)   NULL,
    FOREIGN KEY (id_salida) REFERENCES tbl_salida_materiales(id_salida) ON DELETE CASCADE
);

-- ----------------------------------------
-- 3. SALIDA DE PRODUCTOS (maestro)
-- ----------------------------------------
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbl_salida_productos' AND xtype='U')
CREATE TABLE tbl_salida_productos (
    id_salida         INT           NOT NULL IDENTITY(1,1) PRIMARY KEY,
    id_orden_cliente  INT           NULL,
    cliente           VARCHAR(200)  NULL,
    fecha_salida      DATE          NOT NULL,
    responsable       VARCHAR(150)  NULL,
    observaciones     VARCHAR(500)  NULL,
    total             DECIMAL(18,2) NULL DEFAULT 0
);

-- ----------------------------------------
-- 4. SALIDA DE PRODUCTOS (detalle)
-- ----------------------------------------
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbl_salida_productos_detalle' AND xtype='U')
CREATE TABLE tbl_salida_productos_detalle (
    id_detalle       INT           NOT NULL IDENTITY(1,1) PRIMARY KEY,
    id_salida        INT           NOT NULL,
    codigo_producto  VARCHAR(50)   NOT NULL,
    producto         VARCHAR(200)  NOT NULL,
    cantidad         INT           NOT NULL,
    precio_unitario  DECIMAL(18,2) NULL DEFAULT 0,
    total            DECIMAL(18,2) NULL DEFAULT 0,
    FOREIGN KEY (id_salida) REFERENCES tbl_salida_productos(id_salida) ON DELETE CASCADE
);
