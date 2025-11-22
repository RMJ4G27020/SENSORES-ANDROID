# SensorActivity - Implementación Avanzada de Sensores en Android

## 1. Resumen del Proyecto
Este proyecto constituye una demostración técnica del **Android Sensor Framework** (`android.hardware`), implementado utilizando **Kotlin**. La aplicación proporciona una interfaz en tiempo real para la monitorización de datos de hardware, enfocándose específicamente en el Acelerómetro, el Sensor de Luz Ambiental y el Sensor de Proximidad. Adicionalmente, implementa un mecanismo de introspección de hardware para enumerar todos los módulos de sensores disponibles en el dispositivo anfitrión.

## 2. Arquitectura Técnica

### 2.1 Componentes Principales
La aplicación se construye sobre la arquitectura estándar de Android, aprovechando los siguientes servicios del sistema:

*   **SensorManager**: El servicio de sistema central (`Context.SENSOR_SERVICE`) que actúa como mediador para el acceso al hardware de sensores del dispositivo.
*   **SensorEventListener**: Una implementación de interfaz responsable de recibir notificaciones asíncronas desde el hardware del sensor.
*   **Sensor**: La representación orientada a objetos de un sensor de hardware específico.

### 2.2 Gestión del Ciclo de Vida y Eficiencia
Para garantizar un rendimiento óptimo de la batería y la asignación de recursos, la aplicación se adhiere estrictamente al Ciclo de Vida de la Actividad:

| Estado | Método del Ciclo de Vida | Acción del SensorManager | Impacto en Recursos |
| :--- | :--- | :--- | :--- |
| **Activo / Visible** | `onResume()` | `registerListener()` | **Alto**: El hardware se activa y envía eventos continuamente. |
| **Pausado / Oculto** | `onPause()` | `unregisterListener()` | **Nulo**: El hardware se desactiva para prevenir el "drenaje de batería". |

> **Nota Crítica**: No desregistrar los sensores en `onPause()` es una de las causas más comunes de consumo excesivo de batería en Android.

## 3. Especificaciones de Sensores Implementados

La siguiente tabla detalla la configuración técnica y el mapeo de datos para cada sensor integrado en la aplicación:

| Sensor | Constante Android | Unidad de Medida | Mapeo de Datos (`SensorEvent.values`) | Descripción Técnica |
| :--- | :--- | :---: | :--- | :--- |
| **Acelerómetro** | `TYPE_ACCELEROMETER` | $m/s^2$ | `[0]`: Eje X (Lateral)<br>`[1]`: Eje Y (Vertical)<br>`[2]`: Eje Z (Profundidad) | Mide la aceleración aplicada al dispositivo. Incluye la fuerza de gravedad ($g \approx 9.81 m/s^2$). Útil para detectar movimiento y orientación. |
| **Luz Ambiental** | `TYPE_LIGHT` | Lux ($lx$) | `[0]`: Nivel de Iluminancia | Fotómetro que mide la intensidad de la luz incidente. El rango varía drásticamente entre interiores (~500lx) y exteriores (>10,000lx). |
| **Proximidad** | `TYPE_PROXIMITY` | $cm$ | `[0]`: Distancia al objeto | Sensor IR típicamente ubicado cerca del auricular. En muchos dispositivos actúa como un interruptor binario (Cerca: 0cm / Lejos: 5cm). |

### 3.1 Diagrama de Flujo de Datos
```mermaid
graph TD
    HW[Hardware del Sensor] -->|Señal Cruda| HAL[Hardware Abstraction Layer]
    HAL -->|Datos Procesados| SM[SensorManager System Service]
    SM -->|SensorEvent| App[MainActivity]
    App -->|onSensorChanged()| Logic{Lógica de UI}
    Logic -->|TYPE_ACCELEROMETER| UI_Acc[Actualizar TextView Acelerómetro]
    Logic -->|TYPE_LIGHT| UI_Light[Actualizar TextView Luz]
    Logic -->|TYPE_PROXIMITY| UI_Prox[Actualizar TextView Proximidad]
```

### 3.2 Introspección de Hardware
La aplicación consulta al `SensorManager` mediante `Sensor.TYPE_ALL` para recuperar una `List<Sensor>`. Itera a través de esta colección para extraer y visualizar detalles de bajo nivel.
*   **Nombre**: El identificador de cadena del hardware.
*   **Tipo**: La constante entera que representa la categoría del sensor.
*   **Fabricante**: El proveedor del hardware del sensor.

## 4. Detalles de Implementación

### Clase: `MainActivity`
*   **Herencia**: Extiende `AppCompatActivity` e implementa `SensorEventListener`.
*   **Métodos Clave**:
    *   `initializeSensors()`: Instanciación segura de objetos sensor con verificaciones de nulidad para prevenir cierres inesperados (crashes) en dispositivos que carecen de hardware específico.
    *   `onSensorChanged(event: SensorEvent?)`: El bucle de callback de alta frecuencia donde los datos son recibidos y despachados al hilo de UI (UI Thread).
    *   `onAccuracyChanged()`: Método stub para manejar cambios en el estado de calibración del sensor.

### Layout: `activity_main.xml`
*   **Estructura**: Un `ScrollView` conteniendo un `LinearLayout` asegura que la interfaz permanezca accesible en dispositivos con varios tamaños de pantalla.
*   **Componentes**: Contenedores `CardView` se utilizan para segregar visualmente los flujos de datos, proporcionando una interfaz limpia y compatible con Material Design.

## 5. Requisitos y Configuración
*   **SDK Mínimo**: API 24 (Android 7.0 Nougat).
*   **SDK Objetivo**: API 34 (Android 14).
*   **Hardware**: Se recomienda encarecidamente un dispositivo Android físico. Los emuladores de Android tienen soporte de sensores limitado o simulado que puede no reflejar el comportamiento del mundo real.

---
*Generado para el Módulo Educativo de Actividad de Sensores.*
