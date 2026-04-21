-- ============================================================
-- Script: Insertar usuarios de prueba por rol
-- Base de datos: lachoco_prog
-- IMPORTANTE: Ejecutar DESPUES de sql_agregar_departamento_usuario.sql
-- Contrasenas en texto plano (cambiarlas despues del primer login)
-- ============================================================

-- Rol 1: Administrador  → Departamento: Administración (id=19)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('admin', 'Admin123!', '', 'Administrador', 'Activo', 19);

-- Rol 2: Gerente General  → Departamento: Supervisión (id=23)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('gerente', 'Gerente123!', '', 'Gerente General', 'Activo', 23);

-- Rol 3: Auditor  → Departamento: Auditoría (id=25)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('auditor', 'Auditor123!', '', 'Auditor', 'Activo', 25);

-- Rol 4: Encargado de Finanzas  → Departamento: Finanzas (id=9)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('finanzas', 'Finanzas123!', '', 'Encargado de Finanzas', 'Activo', 9);

-- Rol 5: Encargado de RRHH  → Departamento: Recursos Humanos (id=10)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('rrhh', 'Rrhh123!', '', 'Encargado de RRHH', 'Activo', 10);

-- Rol 6: Vendedor  → Departamento: Ventas (id=2)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('vendedor', 'Vendedor123!', '', 'Vendedor', 'Activo', 2);

-- Rol 7: Encargado de Producción  → Departamento: Producción (id=1)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('produccion', 'Produccion123!', '', 'Encargado de Producción', 'Activo', 1);

-- Rol 8: Operario de Empaque  → Departamento: Empaque (id=6)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('empaque', 'Empaque123!', '', 'Operario de Empaque', 'Activo', 6);

-- Rol 9: Inspector de Calidad  → Departamento: Calidad (id=5)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('calidad', 'Calidad123!', '', 'Inspector de Calidad', 'Activo', 5);

-- Rol 10: Encargado de Almacén  → Departamento: Almacén (id=3)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('almacen', 'Almacen123!', '', 'Encargado de Almacén', 'Activo', 3);

-- Rol 11: Encargado de Compras  → Departamento: Compras (id=4)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('compras', 'Compras123!', '', 'Encargado de Compras', 'Activo', 4);

-- Rol 12: Encargado de Logística  → Departamento: Logística (id=12)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('logistica', 'Logistica123!', '', 'Encargado de Logística', 'Activo', 12);

-- Rol 13: Técnico de Mantenimiento  → Departamento: Mantenimiento (id=11)
INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado, id_departamento)
VALUES ('mantenimiento', 'Mantenim123!', '', 'Técnico de Mantenimiento', 'Activo', 11);

-- ============================================================
-- Verificar todos los usuarios insertados
-- ============================================================
SELECT u.id_usuario, u.usuario, u.rol, d.nombre AS departamento, u.estado
FROM   tbl_usuario u
LEFT JOIN tbl_departamento d ON u.id_departamento = d.id_departamento
ORDER BY u.id_usuario;
