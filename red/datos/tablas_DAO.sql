set schema 'poo2024'

create table conexion_palma (
	equipo1 varchar(50) constraint equipo1_fk references equipos_palma(codigo),
	equipo2 varchar(50) constraint equipo2_fk references equipos_palma(codigo),
	tipo_cable varchar(50) constraint tipo_cable_fk references tipo_cable_palma(codigo),
	tipo_puerto1 varchar(50) constraint tipo_puerto1_fk references tipo_puerto_palma(codigo),
	tipo_puerto2 varchar(50) constraint tipo_puerto2_fk references tipo_puerto_palma(codigo)
)

ALTER TABLE conexion_palma
  ALTER COLUMN equipo1 SET NOT NULL,
  ALTER COLUMN equipo2 SET NOT NULL,
  ALTER COLUMN tipo_cable SET NOT NULL,
  ALTER COLUMN tipo_puerto1 SET NOT NULL,
  ALTER COLUMN tipo_puerto2 SET NOT NULL;


CREATE TABLE equipos_palma (
    codigo VARCHAR(50) PRIMARY KEY,
    descripcion VARCHAR(100),
    marca VARCHAR(100),
	modelo VARCHAR(100),
	tipo_equipo VARCHAR(20) constraint tipo_equipo_fk references tipo_equipo_palma(codigo),
	ubicacion VARCHAR(20),
	estado VARCHAR(10),
	info_puertos VARCHAR(300)
);

ALTER TABLE equipos_palma
  ADD CONSTRAINT ubicacion_fk FOREIGN KEY (ubicacion) REFERENCES ubicaciones_palma(codigo)
  ALTER COLUMN codigo SET NOT NULL,
  ALTER COLUMN tipo_equipo SET NOT NULL,
  ALTER COLUMN info_puertos SET NOT NULL;

CREATE TABLE direcciones_ip_palma (
	equipo_codigo varchar(30) constraint equipo_codigo_fk references equipos_palma(codigo),
	direccion_ip varchar(100)
)

ALTER TABLE direcciones_ip_palma
  ALTER COLUMN equipo_codigo SET NOT NULL;

CREATE TABLE tipo_equipo_palma (
    codigo varchar(30) PRIMARY KEY,
    descripcion VARCHAR(100) 
);

ALTER TABLE tipo_equipo_palma
  ALTER COLUMN codigo SET NOT NULL;



CREATE TABLE tipo_cable_palma (
    codigo varchar(30) PRIMARY KEY,
    descripcion VARCHAR(100), 
	velocidad int
);

ALTER TABLE tipo_cable_palma
  ALTER COLUMN codigo SET NOT NULL;

CREATE TABLE tipo_puerto_palma (
    codigo varchar(30) PRIMARY KEY,
    descripcion VARCHAR(100), 
	velocidad int
);

ALTER TABLE tipo_puerto_palma
  ALTER COLUMN codigo SET NOT NULL;

CREATE TABLE ubicaciones_palma (
    codigo varchar(30) PRIMARY KEY,
    descripcion VARCHAR(100) 
);
ALTER TABLE ubicaciones_palma
  ALTER COLUMN codigo SET NOT NULL;