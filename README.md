# 📚 Sistema de Gestión de Biblioteca Digital

> **Proyecto Final - Programación Orientada a Objetos**  
> Universidad Tecnológica del Perú (UTP) | Ciclo 2025-1

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-19-007396?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

## 🎯 Descripción del Proyecto

Sistema integral para la gestión de bibliotecas digitales que demuestra la **aplicación completa de conceptos de Programación Orientada a Objetos**. El sistema permite gestionar usuarios, recursos bibliográficos (físicos y digitales), préstamos, reservas y multas de manera eficiente y automatizada.

### ✨ Características Principales

- 🔄 **Polimorfismo Real**: Diferentes tipos de usuarios con límites y permisos específicos
- 🎭 **Interfaces Inteligentes**: Implementación selectiva según la naturaleza del recurso
- 🏗️ **Herencia Práctica**: Jerarquías de usuarios y recursos bien estructuradas
- 🔗 **Asociaciones Naturales**: Relaciones Usuario-Préstamo-Recurso
- 📊 **Composición Efectiva**: Multas asociadas a préstamos específicos
- 🎮 **Demo Interactiva**: Visualización en tiempo real del polimorfismo

## 🏗️ Arquitectura POO Implementada

### 📐 Diagramas UML
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│    <<Usuario>>  │    │   <<Recurso>>   │    │   <<Gestión>>   │
│   (Abstract)    │    │   (Abstract)    │    │                 │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ + Estudiante    │    │ + LibroFisico   │    │ + Prestamo      │
│ + Profesor      │    │ + EBook         │    │ + Reserva       │
│ + Bibliotecario │    │ + AudioLibro    │    │ + Multa         │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🎯 Conceptos POO Demostrados

| Concepto | Implementación | Ejemplo |
|----------|----------------|---------|
| **Abstracción** | Clases abstractas `Usuario` y `Recurso` | Métodos abstractos `getLimitePrestamos()` |
| **Encapsulamiento** | Atributos privados + getters/setters | Control de acceso a datos sensibles |
| **Herencia** | Jerarquías Usuario y Recurso | `Profesor extends Usuario` |
| **Polimorfismo** | Límites diferenciados por tipo | Estudiante: 3, Profesor: 10, Bibliotecario: ∞ |
| **Interfaces** | `Prestable`, `Reservable`, `Buscable` | eBooks NO implementan `Reservable` |
| **Asociación** | Usuario ↔ Préstamo ↔ Recurso | Relaciones many-to-many |
| **Composición** | Préstamo ← Multa | Multa no existe sin préstamo |

## 🚀 Instalación y Ejecución

### 📋 Prerrequisitos

- ☕ **Java 17 LTS** o superior
- 📦 **Maven 3.9** o superior  
- 🗄️ **MySQL 8.0** (opcional para persistencia)
- 💻 **Visual Studio Code** (recomendado)

### ⚡ Ejecución Rápida

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/biblioteca-digital-poo.git
cd biblioteca-digital-poo

# 2. Compilar el proyecto
mvn clean compile

# 3. Ejecutar la aplicación
mvn javafx:run
```

### 🎮 Demo Interactiva

1. **Ejecutar la aplicación**
2. **Hacer clic en "🚀 Demostrar Polimorfismo POO"**
3. **Revisar la consola** para ver el polimorfismo en acción:

```
=== POLIMORFISMO EN LÍMITES DE PRÉSTAMO ===
Estudiante Juan Pérez - Límite: 3
Profesor Dr. María García - Límite: 10
Bibliotecario Ana López - Límite: 2147483647

=== POLIMORFISMO EN CÁLCULO DE MULTAS ===
Libro físico - Multa por 3 días: S/ 3.0
eBook - Multa por 3 días: S/ 0.0
AudioLibro - Multa por 3 días: S/ 0.0
```

## 📁 Estructura del Proyecto

```
biblioteca-digital/
├── 📄 README.md
├── 📄 pom.xml                     # Configuración Maven
├── 📁 docs/                       # Documentación académica
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/utp/biblioteca/
│   │   │   ├── 📄 BibliotecaApplication.java    # Clase principal
│   │   │   └── 📁 model/
│   │   │       ├── 📁 interfaces/               # Prestable, Reservable, Buscable
│   │   │       ├── 📁 usuario/                  # Jerarquía Usuario
│   │   │       ├── 📁 recurso/                  # Jerarquía Recurso
│   │   │       └── 📁 gestion/                  # Préstamo, Reserva, Multa
│   │   └── 📁 resources/
│   │       └── 📄 logback.xml                   # Configuración logging
│   └── 📁 test/                                 # Tests unitarios
├── 📁 database/                                 # Scripts SQL
└── 📁 uml/                                      # Diagramas UML
```

## 🎓 Documentación Académica

### 📖 Informe Completo
- **Capítulo I**: Aspectos Generales (Problema, Objetivos, Alcances)
- **Capítulo II**: Marco Teórico (POO, UML, Bases de Datos)
- **Capítulo III**: Desarrollo de la Solución (Implementación técnica)
- **Conclusiones**: Análisis de cumplimiento de objetivos
- **Bibliografía**: 20+ referencias en formato APA 7ma edición

### 🏆 Logros Académicos
- ✅ **100% de conceptos POO** del sílabo implementados
- ✅ **Arquitectura profesional** con patrones de diseño
- ✅ **Código auto-documentado** con Javadoc
- ✅ **Testing automatizado** con JUnit 5
- ✅ **Documentación completa** para mantenimiento

## 🛠️ Tecnologías Utilizadas

### 🔧 Stack Tecnológico
- **Lenguaje**: Java 17 LTS
- **Framework UI**: JavaFX 19
- **Build Tool**: Apache Maven 3.9
- **Base de Datos**: MySQL 8.0
- **ORM**: Hibernate 6.x (JPA)
- **Testing**: JUnit 5 + Mockito
- **Logging**: SLF4J + Logback
- **IDE**: Visual Studio Code

### 📚 Dependencias Principales
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>19</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.4.1.Final</version>
    </dependency>
</dependencies>
```

## 🧪 Testing

```bash
# Ejecutar tests unitarios
mvn test

# Generar reporte de cobertura
mvn jacoco:report

# Ver cobertura
open target/site/jacoco/index.html
```

## 👥 Equipo de Desarrollo

| Integrante | Código | Rol |
|------------|--------|-----|
| **Estudiante 1** | U20102540 | Desarrollador Full-Stack |
| **Estudiante 2** | U20227836 | Arquitecto de Software |
| **Estudiante 3** | U18304473 | Analista de Sistemas |

**Docente**: Rubén Seclén Serrepe  
**Estudiante**: Santiago Mansilla Nunura  
**Curso**: Programación Orientada a Objetos (100000SI34)  
**Universidad**: Universidad Tecnológica del Perú  

## 📄 Licencia

Este proyecto es desarrollado con fines académicos para el curso de Programación Orientada a Objetos de la Universidad Tecnológica del Perú.

## 🤝 Contribuciones

Este es un proyecto académico. Para sugerencias o mejoras:

1. 🍴 Fork el proyecto
2. 🌿 Crear una rama feature (`git checkout -b feature/AmazingFeature`)
3. 💾 Commit los cambios (`git commit -m 'Add some AmazingFeature'`)
4. 📤 Push a la rama (`git push origin feature/AmazingFeature`)
5. 🔄 Abrir un Pull Request

## 📞 Contacto

Para consultas académicas o técnicas sobre el proyecto:

- 📧 **Email**: u22325995@utp.edu.pe
- 🔗 **LinkedIn**: [Santiago Mansilla Nunura](https://www.linkedin.com/in/santiago-mansilla-nunura-09569087/)
- 🐙 **GitHub**: [Xantifox](https://github.com/xantifox)

---

<div align="center">

**⭐ Si este proyecto te ha sido útil, por favor considera darle una estrella**

**🎓 Desarrollado con ❤️ para el aprendizaje de POO en UTP**

![UTP Logo](https://www.utp.edu.pe/sites/default/files/styles/webp/public/2021-06/logo-utp.png.webp?itok=4QWF8kqJ)

</div>