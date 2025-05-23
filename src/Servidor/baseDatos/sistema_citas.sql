DROP DATABASE IF EXISTS sistema_medico;
CREATE DATABASE sistema_medico;
USE sistema_medico;


CREATE TABLE Medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY
    , nombre VARCHAR(100) NOT NULL
    , especialidad VARCHAR(50) NOT NULL
    , cedula VARCHAR(50) NOT NULL
    , correo VARCHAR(100) NOT NULL
);

CREATE TABLE Paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY
    , nombre VARCHAR(100) NOT NULL
    , curp VARCHAR(18) NOT NULL
	, telefono VARCHAR(20)
    , correo VARCHAR(100)
);

CREATE TABLE Cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY
    , fecha DATE NOT NULL
    , hora TIME NOT NULL
    , motivo VARCHAR(200) NOT NULL
	, id_medico INT NOT NULL
    , id_paciente INT NOT NULL
    , FOREIGN KEY (id_medico) REFERENCES Medico(id_medico) ON DELETE CASCADE
    , FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente) ON DELETE CASCADE
);