# Proyecto de Automatización Mobile - QA Innovation Lab 📱

Este repositorio contiene el marco de trabajo (framework) para la automatización de pruebas móviles en **Android** e **iOS** utilizando **Serenity BDD**, **Cucumber**, **Java 8**, **Appium Server 2.x** y **Appium Inspector**.

* **Repositorio:** [https://github.com/jbenitgu/QAInnovationLab](https://github.com/jbenitgu/QAInnovationLab)
* **Rama:** `automation-mobile`
* **Aplicación de Prueba (Demo App):** Sauce Labs Mobile Sample App (versión 2.7.1)

---

## 📌 Tabla de Contenidos

1. [Información General del Proyecto](#información-general-del-proyecto)
2. [Estructura del Proyecto y Capas](#estructura-del-proyecto-y-capas)
3. [Flujo de Automatización (Paso a Paso)](#flujo-de-automatización-paso-a-paso)
4. [Información de la Aplicación (APK / IPA)](#información-de-la-aplicación-apk--ipa)
5. [Requisitos Previos y Configuración de Appium 2.x](#requisitos-previos-y-configuración-de-appium-2x)
6. [Comandos Principales para Diagnóstico y ADB](#comandos-principales-para-diagnóstico-y-adb)
7. [Configuración de Appium Inspector](#configuración-de-appium-inspector)
8. [Ejecución de Pruebas y Monitoreo de Logs](#ejecución-de-pruebas-y-monitoreo-de-logs)
9. [Generación de Reportes](#generación-de-reportes)

---

## ℹ️ Información General del Proyecto

El proyecto está diseñado bajo la arquitectura BDD (Behavior Driven Development) integrando:
* **Serenity BDD (v2.4.51):** Facilita la gestión del ciclo de vida del WebDriver/AppiumDriver y genera reportes detallados.
* **Cucumber (v6 / Serenity Cucumber):** Permite redactar las historias de usuario y escenarios de prueba en formato Gherkin (`.feature`).
* **Appium Server 2.x:** Motor de automatización para aplicaciones nativas e híbridas en Android e iOS.
* **JUnit 4 & AssertJ:** Framework de ejecución e inspección de aserciones.

---

## 📁 Estructura del Proyecto y Capas

La estructura sigue un diseño modularizado (Clean Code / Page Object Model adaptado a Serenity) para mantener una clara separación de responsabilidades:

```text
QAInnovationLab/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── nttdata/
│       │           ├── runners/
│       │           │   └── CucumberTestSuite.java
│       │           ├── stepsdefinitions/
│       │           │   └── SauceLabsStepDef.java
│       │           ├── steps/
│       │           │   └── SuaceLabsLoginSteps.java
│       │           └── screens/
│       │               ├── LoginScreen.java
│       │               └── SauceLoginScreen.java
│       └── resources/
│           ├── features/
│           │   └── SauceLabs.feature
│           └── serenity.conf / serenity.properties
└── pom.xml
```

### 🔍 Descripción de las Capas:

1. **Features (`src/test/resources/features/`)**
   * Contiene los archivos `.feature` escritos en sintaxis Gherkin (`Given`, `When`, `Then`, `And`).
   * **Ejemplo:** `SauceLabs.feature` define los escenarios de prueba funcional (`@Login1`, `@Login2`, `@LoginTest`).

2. **Runners (`com.nttdata.runners`)**
   * Clases ejecutoras anotadas con `@RunWith(CucumberWithSerenity.class)`.
   * **Archivo:** `CucumberTestSuite.java`
   * Mapea la ruta de los `.feature`, la ubicación del `glue` (Step Definitions) y los `tags` a ejecutar.

3. **Step Definitions (`com.nttdata.stepsdefinitions`)**
   * Conecta las sentencias Gherkin del `.feature` con código Java usando anotaciones de Cucumber (`@Given`, `@When`, `@Then`).
   * **Archivo:** `SauceLabsStepDef.java`
   * Instancia a las clases de la capa `steps` mediante `@Steps`. No interactúa directamente con los elementos de pantalla.

4. **Steps (`com.nttdata.steps`)**
   * Lógica de negocio/orquestación intermedia entre el Step Definition y las Pantallas.
   * **Archivo:** `SuaceLabsLoginSteps.java`
   * Agrupa llamados a acciones de la pantalla y realiza validaciones/aserciones intermedias.

5. **Screens (`com.nttdata.screens`)**
   * Representación de los componentes de la interfaz de usuario (Screen Object Pattern).
   * **Archivos:** `SauceLoginScreen.java`, `LoginScreen.java`
   * Hereda de `PageObject` de Serenity. Contiene localizadores (`@AndroidFindBy`, `@iOSXCUITFindBy`, `By`) y métodos de bajo nivel para interactuar con los elementos (`click()`, `sendKeys()`, esperas explícitas).

---

## 🔄 Flujo de Automatización (Paso a Paso)

El flujo de trabajo estándar para automatizar un nuevo caso de prueba en este proyecto sigue una arquitectura descendente:

```text
[ 1. Runner ] ──(Inicia ejecución)──> [ 2. Feature ]
                                             │
                                             ▼
[ 5. Screen ] <──(Bajo Nivel)── [ 4. Step ] <──(Negocio)── [ 3. Step Definition ]
```

### 1. Definición en el Feature (`SauceLabs.feature`)
Se escribe el escenario en lenguaje natural utilizando Gherkin.
```gherkin
@Login2
Scenario: Login 2 Ok
  Given ingreso al aplicativo de SauceLabs
  When ingreso el usuario "standard_user"
  And ingreso la clave "secret_sauce"
  And hago clic en LOGIN
  Then valido el login OK
```

### 2. Mapeo en el Step Definition (`SauceLabsStepDef.java`)
Se vinculan las frases del `.feature` con métodos Java mediante `@Given`, `@When`, `@Then` y se delega la ejecución a la clase de la capa `steps` anotada con `@Steps`.
```java
@Steps
SuaceLabsLoginSteps login;

@When("ingreso el usuario {string}")
public void ingresoElUsuario(String usuario) {
    login.ingresoElUsuario(usuario);
}
```

### 3. Orquestación en la Capa Steps (`SuaceLabsLoginSteps.java`)
La clase de `steps` coordina las acciones necesarias llamando a los métodos de la pantalla (`Screen`).
```java
public class SuaceLabsLoginSteps {

    SauceLoginScreen login;

    public void ingresoElUsuario(String usuario) {
        login.ingresarUsuario(usuario);
    }
}
```

### 4. Interacción en la Capa Screen (`SauceLoginScreen.java` / `LoginScreen.java`)
Hereda de `PageObject` de Serenity y utiliza mapeos de Appium (`@AndroidFindBy`) para interactuar con los elementos nativos de la aplicación.
```java
public class LoginScreen extends PageObject {

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc="Close"]")
    private WebElement btnClose;

    public void clickClose() {
        WebDriverWait wait = new WebDriverWait(getDriver(), 20);
        wait.until(ExpectedConditions.elementToBeClickable(btnClose));
        btnClose.click();
    }
}
```

---

## 📱 Información de la Aplicación (APK / IPA)

Para las pruebas se utiliza la app oficial de demostración **Sauce Labs Sample App**:

* **Nombre de la App:** Sauce Labs Mobile Sample App (v2.7.1)
* **Descarga Android (.apk):** [Android.SauceLabs.Mobile.Sample.app.2.7.1.apk](https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk)
* **Descarga iOS Simulator (.app.zip):** [iOS.Simulator.SauceLabs.Mobile.Sample.app.2.7.1.zip](https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/iOS.Simulator.SauceLabs.Mobile.Sample.app.2.7.1.zip)
* **Descarga iOS Real Device (.ipa):** [iOS.RealDevice.SauceLabs.Mobile.Sample.app.2.7.1.ipa](https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/iOS.RealDevice.SauceLabs.Mobile.Sample.app.2.7.1.ipa)

### Credenciales de Prueba por Defecto:
* **Usuario:** `standard_user`
* **Contraseña:** `secret_sauce`

---

## ⚙️ Requisitos Previos y Configuración de Appium 2.x

### 1. Requisitos del Entorno
* **Java JDK:** 1.8 (Java 8)
* **Apache Maven:** 3.6+
* **Node.js:** >= 16.x
* **Android Studio & SDK:** Variables de entorno `ANDROID_HOME` configuradas y agregadas al `PATH` (`platform-tools`, `tools`, `emulator`).
* **Appium Server:** 2.x (`npm install -g appium`)
* **Controlador uiautomator2:** (`appium driver install uiautomator2`)
* **Controlador xcuitest (para iOS/Mac):** (`appium driver install xcuitest`)

---

## 🛠️ Comandos Principales para Diagnóstico y ADB

### 1. Verificación del Entorno con Appium Doctor
Permite verificar el estado de instalación de las dependencias requeridas.

```bash
# Instalación global de Appium Doctor (CLI v2+)
npm install -g @appium/doctor

# Verificación para Android
appium-doctor --android

# Verificación para iOS (en macOS)
appium-doctor --ios
```

### 2. Comandos ADB Útiles (Android Debug Bridge)

```bash
# Listar emuladores o dispositivos físicos conectados
adb devices

# Obtener package y activity de la app activa en pantalla
adb shell dumpsys window | grep -E 'mCurrentFocus|mFocusedApp'

# Instalar el APK en el dispositivo conectado
adb install ruta/a/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk

# Desinstalar la aplicación del dispositivo
adb uninstall com.swaglabsmobileapp

# Iniciar servidor Appium 2.x
appium
```

---

## 🕵️ Configuración de Appium Inspector

Para inspeccionar elementos y obtener localizadores Xpath / Accessibility ID en Appium 2.x:

1. Abrir **Appium Inspector**.
2. Parámetros de servidor:
   * **Remote Host:** `127.0.0.1` (o `localhost`)
   * **Remote Port:** `4723`
   * **Remote Path:** `/` *(En Appium 2.x el path es `/` y no `/wd/hub`)*.
3. **Desired Capabilities (JSON Representation)**:

```json
{
  "platformName": "Android",
  "appium:automationName": "UiAutomator2",
  "appium:deviceName": "emulator-5554",
  "appium:app": "C:/ruta/a/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk",
  "appium:appPackage": "com.swaglabsmobileapp",
  "appium:appActivity": "com.swaglabsmobileapp.MainActivity",
  "appium:ensureCleanPackage": true
}
```

---

## 🚀 Ejecución de Pruebas y Monitoreo de Logs

### 1. Ejecución desde el Runner
Se puede ejecutar directamente la clase `CucumberTestSuite.java` mediante el IDE (Right click -> **Run 'CucumberTestSuite'**).

### 2. Ejecución vía Comandos Maven (Terminal)

```bash
# Ejecutar la suite completa definida en el Runner
mvn clean verify

# Ejecutar por Tag específico (definido en los .feature)
mvn clean verify -Dtags="@LoginTest"

# Ejecutar un único escenario
mvn clean verify -Dtags="@Login1"

# Ejecutar pasando parámetros del sistema
mvn clean verify -Dwebdriver.base.url="" -Dtags="@Login2"
```

### 🔍 ¿Dónde Validar la Ejecución y Diagnosticar Fallas?

1. **Terminal / Logs de Maven (`Console Output`):**
   * Muestra la progresión de los escenarios de Cucumber.
   * Indica qué pasos fueron ejecutados, omitidos (`skipped`) o fallidos (`failed`).
   * Presenta las excepciones de Java y aserciones fallidas (ej. `AssertionError`, `NoSuchElementException`).

2. **Logs del Servidor de Appium (`Appium Server Console`):**
   * Muestra la comunicación en tiempo real entre el framework (Cliente HTTP W3C) y el dispositivo móvil.
   * Permite verificar si la sesión se crea correctamente (`POST /session`).
   * Sirve para depurar errores de búsqueda de elementos (ej. `findElement` timeouts, `UiAutomator2` crashes).

3. **Logs del Dispositivo (`adb logcat`):**
   * Para revisar fallas internas o cierres inesperados de la aplicación Android durante la ejecución.
   ```bash
   adb logcat *:E
   ```

---

## 📊 Generación de Reportes

Al finalizar la ejecución con Maven, Serenity BDD genera un reporte interactivo con capturas de pantalla de cada interacción:

* **Ruta del reporte HTML:** `target/site/serenity/index.html`

### Abrir el reporte en navegador:
```bash
# En macOS / Linux
open target/site/serenity/index.html

# En Windows (CMD / PowerShell)
start target/site/serenity/index.html
```
