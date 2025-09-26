# GRUPO 5

Proyecto de Estructura de Datos desarrollo de aplicacion para gestion de tiquetes para banco

Integrantes:
- Handel Enriquez
- Stef Gomez
- Josué Grijalba



## Estructura del Proyecto

```
sistemabancario/
├── src/
│   └── main/
│       ├── java/main/
│       │   ├── Main.java                    # Clase principal
│       │   ├── config/
│       │   │   └── Configuracion.java       # Módulo 0: Configuración
│       │   ├── model/
│       │   │   ├── Cliente.java             # Modelo de cliente
│       │   │   ├── Tiquete.java             # Modelo de tiquete
│       │   │   ├── Caja.java                # Modelo de caja bancaria
│       │   │   └── Banco.java               # Modelo del banco
│       │   ├── structures/
│       │   │   ├── ColaPrioridad.java       # Cola de prioridad personalizada
│       │   │   ├── Grafo.java               # Grafo para complementarios
│       │   │   └── Nodo.java                # Nodo para estructuras enlazadas
│       │   ├── services/
│       │   │   ├── TiqueteService.java      # Módulos 1.1, 1.2, 1.3
│       │   │   ├── ReporteService.java      # Módulo 1.4: Reportes
│       │   │   ├── WebService.java          # Consulta tipo de cambio
│       │   │   └── LoginService.java        # Sistema de autenticación
│       │   ├── persistence/
│       │   │   ├── ArchivoManager.java      # Manejo de archivos
│       │   │   └── DatabaseManager.java     # Base de datos histórica
│       │   └── ui/
│       │       └── InterfazPrincipal.java   # Interfaz con JOptionPane
│       └── resources/data/
│           ├── prod.txt                     # Configuración del sistema
│           ├── colas_temp.txt              # Estado temporal de colas
│           ├── historico.txt               # Histórico de transacciones
│           └── complementarios.txt         # Productos complementarios
├── target/                                  # Archivos compilados (generado)
├── pom.xml                                 # Configuración Maven
└── README.md                               # Este archivo
```
