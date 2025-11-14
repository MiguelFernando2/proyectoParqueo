
Manual de Usuario — Sistema de Parqueos 2025

1. Introducción

Este manual explica de manera sencilla cómo utilizar el sistema de control de parqueos.
Está pensado para usuarios comunes, sin necesidad de conocimientos técnicos o de programación.

El sistema permite:
Registrar vehículos.
Registrar salidas.
Controlar cupos por área.
Cargar datos desde archivo CSV.
Generar reportes y exportarlos.
Guardar y cargar datos desde SQL Server.


2. Pantalla Principal

Al abrir la aplicación aparecen los siguientes módulos:
Registro de Vehículos
Registro de Salidas
Reportes
Historial de Salidas
Opciones SQL (Cargar desde la BD)

Para ingresar a cada módulo solo debes hacer clic en los botones del menú.


3. Registrar un Vehículo

1. Abrir la ventana Registro Vehículos.

2. Completar los siguientes datos:
Placa
Propietario
Tipo (Moto / Carro)
Plan (Flat / Variable)
Rol (Estudiante / Catedrático)

3. Presionar Registrar.
Plan Flat
El sistema cobrará automáticamente:
Q25 para Motos
Q40 para Carros

El cobro aparece en pantalla.

Validaciones automáticas
El sistema no permitirá:
Placas duplicadas dentro del parqueo.
Registrar si el área está llena.


4. Registrar Salida

1. Abrir Registrar Salida.
2. Ingresar la placa.
3. El sistema calcula:
Tiempo dentro del parqueo
Monto a pagar (solo si es plan variable)
Estado del plan Flat

El sistema guarda el recibo en SQL automáticamente.


5. Cargar desde CSV

1. Abrir la ventana Carga CSV.
2. Seleccionar archivo o carpeta.

3. El sistema mostrará:
Registros aceptados
Rechazados
Duplicados
Sin cupo
Mal formados

Los datos válidos se cargan tanto en la aplicación como en SQL.


6. Reporte de Vehículos Activos

En la ventana Reportes se puede filtrar por:
Tipo (carro/moto)
Plan (flat/variable)
Rol
Área
Y ver:
Cupos ocupados
Cupos disponibles
Vehículos pendientes (plan flat dentro de 2 horas)

También se puede exportar a CSV con el botón “Exportar”.


7. Historial de Salidas

Aquí se muestran TODOS los recibos guardados en SQL.
Funciones:
Filtrar por rango de fechas
Total generado en el período
Totales por:
motos
estudiantes
catedráticos
flat
variable
Exportar a CSV


8. Conexión con SQL Server

El sistema permite guardar y cargar datos de SQL.
Cargar datos desde SQL

1. Menú → Cargar datos SQL
2. Un mensaje mostrará cuántos vehículos y recibos fueron cargados.



9. Reingresos Plan Flat

El usuario puede salir y regresar dentro de 2 horas sin perder el espacio.
Colores:
Amarillo → pendiente de reingreso
Verde/rojo/orange → según ocupación del área


10. Errores Comunes

Error	Motivo	Solución
“Área sin cupo”	Capacidad llena	Esperar un espacio
“Placa duplicada”	Vehículo ya está adentro	Registrar salida primero
“Error SQL”	No hay conexión	Verificar SQL Server encendido


11. Créditos

Sistema desarrollado por: Miguel Fernando Tziquín Marroquín
Curso: Programación II / Estadística II
Proyecto final 2025