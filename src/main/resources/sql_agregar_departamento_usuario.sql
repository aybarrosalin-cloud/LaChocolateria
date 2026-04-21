-- ============================================================
-- Script: Agregar departamento a tbl_usuario
-- Ejecutar en: lachoco_prog
-- ============================================================

-- 1. Agregar columna id_departamento a tbl_usuario
ALTER TABLE tbl_usuario
    ADD id_departamento INT NULL;

-- 2. Llave foranea hacia tbl_departamento
ALTER TABLE tbl_usuario
    ADD CONSTRAINT FK_usuario_departamento
    FOREIGN KEY (id_departamento) REFERENCES tbl_departamento(id_departamento);

-- 3. Verificar resultado
SELECT u.id_usuario, u.usuario, u.rol, u.estado,
       u.id_departamento, d.nombre AS departamento
FROM   tbl_usuario u
LEFT JOIN tbl_departamento d ON u.id_departamento = d.id_departamento;
