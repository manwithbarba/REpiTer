# REpiTer (Registros Epidemiológicos en Terreno)

<p align="center">
  <img src="android/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="200" alt="REpiTer Logo">
</p>

REpiTer es una aplicación Android pensada modularmente y diseñada para la recolección de datos epidemiológicos en campo. Desarrollada para funcionar en entornos sin conectividad, prioriza la integridad del dato, la georreferenciación  y la interoperabilidad mediante estándares internacionales.

## 🚀 Características Principales

- **📋 Metadatos de Sesión Globales**: Gestión del contexto de trabajo (Día, Responsable, Institución) al inicio de cada jornada, eliminando la selección manual en cada encuesta.
- **🏥 Jerarquía Nacional con Filtros (REFES Corporizado)**: Selección jerárquica por Provincia y Municipio, con filtrado inteligente por **Sector** (Estatal, Privado, Mutual) para agilizar la búsqueda en más de 36.000 centros.
- **⚙️ Motor Dinámico de Formularios**: Los cuestionarios se generan dinámicamente a partir de configuraciones JSON, lo que permite añadir nuevas encuestas sin modificar el código fuente.
- **📍 Georreferenciación Automática**: Captura coordenadas GPS de alta precisión en el momento exacto del guardado.
- **📡 Interoperabilidad HL7 FHIR**: Exportación en formato FHIR QuestionnaireResponse (JSON).
- **💾 Exportación Flexible**: Generación de archivos CSV enriquecidos con metadatos de sesión (incluyendo Sector) y JSON para integración técnica.
- **📵 Offline-First**: Almacenamiento local mediante base de datos Room v4, garantizando que no se pierda información.

## 📋 Módulos de Relevamiento Incluidos

- **Encuesta Permanente de Hogares (EPH)**: Versión completa basada en variables de INDEC.
- **Factores de Riesgo (ENFR) 2018**: Versión adaptada para relevamientos de salud cardiovascular.
- **Consumo de Sustancias (ENPreCoSP)**: Módulo adaptado para el relevamiento de prevalencia de consumo.
- **Control de Vacunación**: Módulo basado íntegramente en el Calendario Nacional de Vacunación de Argentina.
- **Salud Mental**: Incluye las escalas validadas **PHQ-9** y **GAD-7**.

## 📲 Instalación

Para instalar REpiTer en dispositivos Android, descarga siempre la última versión desde la sección oficial:

👉 **[Descargar Último APK (Release v1.1.0-beta)](https://github.com/manwithbarba/REpiTer/releases/tag/v1.1.0-beta)**

### ⚠️ Importante

A partir de la versión **v1.1.0-beta**, se ha implementado el **Flujo de Sesión Obligatorio**. Al iniciar la app, deberás configurar la fecha del relevamiento y seleccionar tu establecimiento mediante la nueva jerarquía REFES. Esto asegura que todos los registros del día queden correctamente vinculados institucionalmente.

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Persistencia**: Room Database (SQLite)
- **Localización**: Google Play Services (FusedLocationProvider)
- **Estándares**: HL7 FHIR v4.0.1

## 📄 Créditos y Licencia

- **Autor**: Julián Sánchez Viamonte, Facultad de Medicina Universidad Nacional de Mar del Plata
- **Uso y Distribución**: Este software es de **distribución libre**. Se autoriza su uso, copia y redistribución, siempre que se mantenga la cita al autor original y se mencione a **REpiTer** como la fuente del desarrollo.
