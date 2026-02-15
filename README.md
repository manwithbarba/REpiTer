# REpiTer (Registros Epidemiológicos en Terreno)

<p align="center">
  <img src="android/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="200" alt="REpiTer Logo">
</p>

REpiTer es una aplicación Android pensada modularmente y diseñada para la recolección de datos epidemiológicos en campo. Desarrollada para funcionar en entornos sin conectividad, prioriza la integridad del dato, la georreferenciación  y la interoperabilidad mediante estándares internacionales.

## 🚀 Características Principales

- **⚙️ Motor Dinámico de Formularios**: Los cuestionarios se generan dinámicamente a partir de configuraciones JSON, lo que permite añadir nuevas encuestas sin modificar el código fuente.
- **📍 Georreferenciación Automática**: Captura coordenadas GPS de alta precisión en el momento exacto del guardado de cada registro.
- **📡 Interoperabilidad HL7 FHIR**: Exportación de datos en formato FHIR QuestionnaireResponse (JSON) para integración directa con sistemas de Historia Clínica Electrónica.
- **💾 Exportación Flexible**: Generación de archivos CSV para análisis estadístico (Excel, R, Python) y JSON para integración técnica.
- **📵 Offline-First**: Almacenamiento local mediante base de datos Room, garantizando que no se pierda información en zonas sin cobertura.
- **🛡️ Validación de Identidad**: Validación local de DNI y campos obligatorios de identidad para asegurar la trazabilidad del dato.

## 📋 Módulos de Relevamiento Incluidos

- **Encuesta Permanente de Hogares (EPH)**: Versión completa basada en variables de INDEC.
- **Factores de Riesgo (ENFR) 2018**: Versión adaptada para relevamientos de salud cardiovascular.
- **Consumo de Sustancias (ENPreCoSP)**: Módulo adaptado para el relevamiento de prevalencia de consumo.
- **Control de Vacunación**: Módulo basado íntegramente en el Calendario Nacional de Vacunación de Argentina.
- **Salud Mental**: Incluye las escalas validadas **PHQ-9** (Depresión) y **GAD-7** (Ansiedad).

## 📲 Instalación

Para instalar REpiTer en dispositivos Android, descarga siempre la última versión desde la sección oficial:

👉 **[Descargar Último APK (Release)](https://github.com/manwithbarba/REpiTer/releases/tag/v1.0.3-beta)**

### ⚠️ Importante

A partir de la versión **v1.0.3-beta**, se han incluido mejoras críticas en la selección de opciones (Sí/No) y la edición de campos de texto. Si tienes versiones anteriores, te recomendamos desinstalarlas primero para asegurar una experiencia limpia.

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Persistencia**: Room Database (SQLite)
- **Localización**: Google Play Services (FusedLocationProvider)
- **Estándares**: HL7 FHIR v4.0.1

## 📄 Créditos y Licencia

- **Autor**: Julián Sánchez Viamonte, Facultad de Medicina Universidad Nacional de Mar del Plata
- **Uso y Distribución**: Este software es de **distribución libre**. Se autoriza su uso, copia y redistribución, siempre que se mantenga la cita al autor original y se mencione a **REpiTer** como la fuente del desarrollo.

---
*Desarrollado para el fortalecimiento de la vigilancia epidemiológica y la salud pública digital.*
