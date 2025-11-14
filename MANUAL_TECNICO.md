# Manual Técnico – Sistema de Parqueos 2025

## 1. Datos Generales del Proyecto

- *Proyecto:* Sistema de Control de Parqueos 2025
- *Autor:* Miguel Fernando Tziquín Marroquín
- *Carnet:* (colocar tu carnet)
- *Curso:* Programación 2
- *Catedrático:* Ing. José Gabriel Linares de León
- *Universidad:* Universidad Mariano Gálvez de Guatemala
- *Fecha:* 2025

## Tecnologías Utilizadas

- *Lenguaje:* Java JDK 22
- *IDE:* Apache NetBeans 22
- *Base de Datos:* SQL Server 2019 / 2022
- *Motor JDBC:* mssql-jdbc-13.2.1.jre11.jar
- *Paradigma:* Programación Orientada a Objetos (POO)
- *Arquitectura:* Modelo + Vista (UI) + DAO
- *Formato de documentos:* Markdown (.md)
## 2. Arquitectura del Sistema

El sistema está desarrollado con arquitectura *Modelo – Vista – DAO*, dividiendo el proyecto en capas:

---

### 📌 2.1 Paquetes Principales

#### *1) proyectorparqueo.ui*
Contiene todas las interfaces gráficas (formularios) desarrolladas con Swing:

- FrmIngreso.java → Registro de vehículos.
- FrmReingreso.java → Reingreso para vehículos con plan FLAT.
- FrmSalida.java → Registrar salida y generar recibo.
- FrmHistorialSalidas.java → Vista de historial general.
- FrmReportes.java → Reportes por fecha.
- menuPrincipal.java → Menú principal del sistema.

---

#### *2) proyectorparqueo.model*
Contiene toda la lógica del sistema:

- vehiculo.java → Clase modelo para un vehículo.
- Area.java → Representa un área del parqueo.
- ReciboSalida.java → Modelo del recibo generado al salir.
- controlParqueo.java → Lógica principal del manejo del parqueo.
- DatosApp.java → Manejo global del estado y conexión con SQL.

---

#### *3) DAO (Acceso a Datos / SQL Server)*

- ConexionSQL.java → Cadena de conexión y configuración del JDBC.
- VehiculoDAO.java → Inserta, lista y elimina vehículos en SQL.
- ReciboSalidaDAO.java → Inserta y obtiene recibos desde SQL.

Estas clases implementan consultas SQL mediante PreparedStatement.

---

### 📌 2.2 Librerías Externas Utilizadas

- mssql-jdbc-13.2.1.jre11.jar
  - Permite conectar Java con SQL Server.
  - Debe estar agregado en:
    
    Project → Libraries → Add JAR/Folder
    

---

### 📌 2.3 Flujo General del Sistema

1. El usuario registra un vehículo desde la interfaz.
2. Se valida el área, capacidad y plan.
3. Se guarda en memoria (DatosApp) y en SQL Server (VehiculoDAO).
4. Cuando sale, se genera un ReciboSalida.
5. Este recibo se guarda en SQL para reportes.
6. Al reiniciar el programa, todo se reconstruye desde SQL (cargarDesdeSQL).

## 3. Diagrama de Clases (UML)

El siguiente diagrama UML describe la arquitectura lógica del sistema Parqueo 2025, 
mostrando las clases principales del módulo de control de parqueo, el manejo de datos, 
los modelos y las clases DAO conectadas a SQL Server.

```mermaid
classDiagram

class vehiculo {
    -String placa
    -String propietario
    -String tipoVehiculo
    -String tipoPlan
    -boolean planActivo
    -LocalDateTime horaIngreso
    -String rol
    -String area
}

class Area {
    -String id
    -String nombre
    -int capacidad
    -int ocupados
    -String tipoPermitido
}

class ReciboSalida {
    -vehiculo vehiculo
    -LocalDateTime horaSalida
    -long minutos
    -long horas
    -double total
    -String nota
}

class controlParqueo {
    -List~vehiculo~ vehiculosAdentro
    +registrarVehiculo(vehiculo)
    +buscarPorPlaca(String)
    +eliminarVehiculo(String)
}

class DatosApp {
    +PARQUEO : controlParqueo
    +HISTORIAL_SALIDAS : List~ReciboSalida~
    +PENDIENTES_FLAT : Map~String,LocalDateTime~
    +cargarDesdeSQL()
    +registrarSalida(String)
}

class VehiculoDAO {
    +insertar(vehiculo)
    +listarTodos() : List~vehiculo~
    +eliminarPorPlaca(String)
}

class ReciboSalidaDAO {
    +insertar(ReciboSalida)
    +listarTodos() : List~ReciboSalida~
    +listarPorRango(LocalDateTime, LocalDateTime)
}

class ConexionSQL {
    +getConnection() : Connection
}

vehiculo --> Area : perteneceA
controlParqueo --> vehiculo : contiene
DatosApp --> controlParqueo : usa
DatosApp --> vehiculo : registra
DatosApp --> ReciboSalida : genera
VehiculoDAO --> vehiculo : CRUD
ReciboSalidaDAO --> ReciboSalida : CRUD
VehiculoDAO --> ConexionSQL : usa
ReciboSalidaDAO --> ConexionSQL : usa

### 4.1 Clase ConexionSQL
Esta clase administra la conexión del programa Java con SQL Server.

*Responsabilidades:*
- Define la URL de conexión a la instancia SQLEXPRESS.
- Guarda usuario y contraseña del login creado en SQL Server.
- Expone el método getConnection() para que todas las clases DAO puedan conectarse.

*Método principal*
```java
public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASS);
}

USO: Tosas las clases DAO usan estas conexiones para consultar, insertar o borrar datos 
## ### *4.2 Clase vehiculo (Modelo)*

```markdown
### 4.2 Clase vehiculo
Representa un vehículo dentro del sistema. Es parte del *modelo*.

*Atributos principales:*
- placa
- propietario
- tipoVehiculo (CARRO/MOTO)
- tipoPlan (PLAN FLAT / TARIFA VARIABLE)
- planActivo
- horaIngreso
- rol (Estudiante/Catedrático)
- área asignada

*Propósito:*  
Modelar un vehículo que está dentro del parqueo o que fue registrado en una salida.

### 4.3 Clase Area
Representa un área física dentro del parqueo (Motos, Estudiantes, Catedráticos).

*Atributos:*
- nombre
- capacidad máxima
- ocupación actual
- tipo permitido

*Responsabilidad:*  
Llevar el control de cuántos vehículos hay dentro de cada área.

### 4.4 Clase controlParqueo
Administra todos los vehículos que están dentro del parqueo.

*Funciones clave:*
- registrarVehiculo() agrega un vehículo al parqueo.
- buscarPorPlaca() localiza un vehículo activo.
- eliminarVehiculo() lo marca como fuera del parqueo.

*Relación:*  
Es utilizado por DatosApp para procesar ingresos y salidas.


### 4.5 Clase DatosApp
Es el núcleo del sistema donde se integran TODAS las funciones.

*Contiene:*
- PARQUEO → instancia única de controlParqueo
- HISTORIAL_SALIDAS → lista con todos los recibos (solo en memoria)
- PENDIENTES_FLAT → controla ventana de 2 horas de reingreso
- Métodos principales:
  - cargarDesdeSQL()
  - registrarSalida()
  - ultimaSalidaDe()

*Responsabilidad:*  
Coordinar la lógica del parqueo entre Java y SQL Server

### 4.6 Clase ReciboSalida
Modelo que representa un recibo generado al momento de registrar una salida.

*Incluye:*
- el vehículo
- hora de salida
- minutos y horas consumidas
- total cobrado
- nota del plan FLAT

*Uso:*  
Se usa para reportes, historial y exportación a CSV.

### 4.7 Clase VehiculoDAO
Capa encargada de comunicar Java con SQL Server para la tabla Vehiculo.

*Funciones:*
- insertar() → registra un vehículo en SQL.
- listarTodos() → carga vehículos guardados.
- eliminarPorPlaca() → elimina un vehículo al salir.

*Importante:*  
Es parte de la capa DAO (Data Access Object).


### 4.8 Clase ReciboSalidaDAO
Administra el acceso a la tabla ReciboSalida.

*Funciones:*
- Insertar recibo de salida.
- Cargar todos los recibos al abrir el programa.
- Filtrar recibos por rango de fechas (para el reporte del período).


### 4.9 Formularios Swing
Los formularios conforman la capa de interfaz visual.

*Cada formulario hace lo siguiente:*
- Llama a métodos de DatosApp.
- Muestra datos en tablas (JTable).
- Permite exportar a CSV.
- Filtrar por fechas.
- Mostrar totales por:
  - Estudiantes
  - Catedráticos
  - Motos
  - Tipo de plan


##5. Diseño y Explicación de la Base de Datos (SQL Server)

A continuación se describe la estructura de la base de datos Parqueo2025, 
detallando sus tablas, columnas, tipos de datos, relaciones y reglas de funcionamiento.


### 5.1 Nombre de la Base de Datos

Parqueo2025

Esta base almacena:

Vehículos actualmente dentro del parqueo

Historial de salidas

Datos necesarios para reportes y análisis



### 5.2 Tabla: Vehiculo

Esta tabla contiene todos los vehículos que están actualmente dentro del parqueo.
Cuando un vehículo sale, se elimina de esta tabla y pasa a ReciboSalida.

Estructura:

Columna	Tipo	Descripción

placa   	NVARCHAR(20) PK	Identificador único del vehículo
propietario	NVARCHAR(100)	Nombre del dueño
tipoVehiculo	NVARCHAR(20)	CARRO o MOTO
tipoPlan	NVARCHAR(20)	PLAN FLAT o TARIFA VARIABLE
planActivo	BIT     	true/false
horaIngreso	DATETIME2	Fecha y hora de entrada
rol     	NVARCHAR(20)	ESTUDIANTE o CATEDRATICO
area    	NVARCHAR(20)	MOTOS / ESTUDIANTES / CATEDRATICOS

Comportamiento de la Tabla

Se INSERTA cuando un vehículo ingresa.

Se ELIMINA cuando un vehículo sale.

Siempre representa solo los vehículos activos adentro del parqueo.



---

### 5.3 Tabla: ReciboSalida

Esta tabla almacena todas las salidas realizadas en la historia del sistema.

Estructura:

Columna	Tipo	Descripción

idRecibo	INT IDENTITY PK	Identificador único del recibo
placa    	NVARCHAR(20)	Placa del vehículo
propietario	NVARCHAR(100)	Nombre del dueño
tipoVehiculo	NVARCHAR(20)	CARRO/MOTO
tipoPlan	NVARCHAR(20)	FLAT/VARIABLE
horaIngreso	DATETIME2	Fecha/hora de entrada
horaSalida	DATETIME2	Fecha/hora de salida
minutos 	INT	        Tiempo total
horas   	INT     	Horas cobradas
total   	DECIMAL(10,2)	Monto total pagado
nota    	NVARCHAR(255)	Observaciones del plan FLAT

Comportamiento de la Tabla

Cada salida crea una fila nueva.

No se elimina nada de esta tabla.

Es usada para:

Historial

Reportes

Exportación CSV

Cálculo de totales por período



### 5.4 Relaciones Entre Tablas

Este sistema usa una estrategia simple:

❗ No existen relaciones FK entre Vehiculo y ReciboSalida

¿Por qué?
Porque la placa puede repetirse muchas veces en el historial a lo largo del año.

Cada salida es un evento único.

Por diseño:

Vehiculo = estado actual

ReciboSalida = historial completo


Esto simplifica las operaciones y evita problemas al eliminar vehículos.



### 5.5 Justificación del Modelo

Simplicidad

El parqueo opera en tiempo real, por lo que tener estados activos y un historial separado agiliza todo.

 Eficiencia

Insertar, borrar y consultar placas es instantáneo.

Seguridad

La tabla ReciboSalida conserva todo como evidencia o auditoría.



### 5.6 Sentencias SQL del Proyecto

Estas son las sentencias utilizadas para crear la BD y las tablas.

Crear Base de Datos

CREATE DATABASE Parqueo2025;
GO

Crear Tabla Vehiculo

CREATE TABLE Vehiculo (
    placa          NVARCHAR(20) PRIMARY KEY,
    propietario    NVARCHAR(100) NOT NULL,
    tipoVehiculo   NVARCHAR(20)  NOT NULL,
    tipoPlan       NVARCHAR(20)  NOT NULL,
    planActivo     BIT           NOT NULL,
    horaIngreso    DATETIME2     NOT NULL,
    rol            NVARCHAR(20)  NOT NULL,
    area           NVARCHAR(20)  NOT NULL
);

Crear Tabla ReciboSalida

CREATE TABLE ReciboSalida (
    idRecibo   INT IDENTITY(1,1) PRIMARY KEY,
    placa          NVARCHAR(20)  NOT NULL,
    propietario    NVARCHAR(100) NOT NULL,
    tipoVehiculo   NVARCHAR(20)  NOT NULL,
    tipoPlan       NVARCHAR(20)  NOT NULL,
    horaIngreso    DATETIME2     NOT NULL,
    horaSalida     DATETIME2     NOT NULL,
    minutos        INT           NOT NULL,
    horas          INT           NOT NULL,
    total          DECIMAL(10,2) NOT NULL,
    nota           NVARCHAR(255) NULL
);


### 5.7 Configuración del Login SQL Server

### Login necesario para la conexión JAVA → SQL Server

1. Crear login en master:
   ```sql
   CREATE LOGIN parqueoUser WITH PASSWORD = 'Parqueo123*';

2. Crear usuario dentro de la BD:

USE Parqueo2025;
CREATE USER parqueoUser FOR LOGIN parqueoUser;


3. Otorgar permisos máximos:

ALTER ROLE db_owner ADD MEMBER parqueoUser;


##DIAGRAMA UML

## 6. Diagrama UML del Sistema

A continuación se presenta el diagrama de clases que describe la estructura interna del sistema
 de parqueos. Este diagrama permite visualizar la relación entre las clases, los métodos principales 
y la interacción entre las capas del sistema (lógica de negocio vs. acceso a datos).

[Diagrama UML](https://github.com/MiguelFernando2/proyectoParqueo/blob/master/img/uml/WhatsApp%20Image%202025-11-14%20at%2010.57.41%20AM.jpeg)


6.1. Clases Principales

El sistema está dividido en cuatro grandes bloques:

A) Gestión del Parqueo

vehiculo

Area

controlParqueo

DatosApp


B) Manejo de BD (DAO)

VehiculoDAO

ReciboSalidaDAO


C) Entidades de Registro

ReciboSalida


D) Formularios (UI)

(No se incluyen en UML porque pertenecen a interfaz gráfica y no a lógica de negocio.)



6.3. Explicación Profesional del UML

Vehiculo

Representa un vehículo dentro del sistema.
Contiene información esencial: placa, propietario, tipo, plan, rol, área y fecha de ingreso.


---

Area

Modela cada zona del parqueo:

MOTOS

ESTUDIANTES

CATEDRATICOS


Controla capacidad y ocupación.


---

controlParqueo

Es el núcleo del negocio.
Permite agregar, buscar y eliminar vehículos en memoria.



DatosApp

Clase estática que une todo el sistema.
Maneja:

Control del parqueo

Historial de salidas

Pendientes FLAT

Cargar datos desde SQL

Registrar salida



VehiculoDAO / ReciboSalidaDAO

Acceso a la base de datos SQL Server.
Implementan operaciones CRUD para persistencia.

ReciboSalida

Representa un recibo con los datos del cobro o salida.
