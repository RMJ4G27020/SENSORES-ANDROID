package com.example.sensoractivity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lightSensor: Sensor? = null
    private var proximitySensor: Sensor? = null
    
    private lateinit var tvAccelerometer: TextView
    private lateinit var tvLight: TextView
    private lateinit var tvProximity: TextView
    private lateinit var tvSensorList: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        initializeViews()
        
        // Initialize sensors
        initializeSensors()
        
        // Display available sensors
        displayAvailableSensors()
    }
    
    private fun initializeViews() {
        tvAccelerometer = findViewById(R.id.tvAccelerometer)
        tvLight = findViewById(R.id.tvLight)
        tvProximity = findViewById(R.id.tvProximity)
        tvSensorList = findViewById(R.id.tvSensorList)
    }
    
    private fun initializeSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        // Get sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        
        // Check sensor availability
        if (accelerometer == null) {
            Toast.makeText(this, "Acelerómetro no disponible", Toast.LENGTH_SHORT).show()
        }
        if (lightSensor == null) {
            Toast.makeText(this, "Sensor de luz no disponible", Toast.LENGTH_SHORT).show()
        }
        if (proximitySensor == null) {
            Toast.makeText(this, "Sensor de proximidad no disponible", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun displayAvailableSensors() {
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sb = StringBuilder()
        sb.append("Sensores disponibles:\n\n")
        
        sensorList.forEach { sensor ->
            sb.append("• ${sensor.name}\n")
            sb.append("  Tipo: ${sensor.type}\n")
            sb.append("  Fabricante: ${sensor.vendor}\n\n")
        }
        
        tvSensorList.text = sb.toString()
    }
    
    override fun onResume() {
        super.onResume()
        // Register sensor listeners
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Unregister sensor listeners to save battery
        sensorManager.unregisterListener(this)
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    tvAccelerometer.text = "Acelerómetro:\nX: ${"%.2f".format(x)} m/s²\n" +
                            "Y: ${"%.2f".format(y)} m/s²\nZ: ${"%.2f".format(z)} m/s²"
                }
                Sensor.TYPE_LIGHT -> {
                    val lux = it.values[0]
                    tvLight.text = "Sensor de Luz:\n${"%.2f".format(lux)} lux"
                }
                Sensor.TYPE_PROXIMITY -> {
                    val distance = it.values[0]
                    tvProximity.text = "Sensor de Proximidad:\n${"%.2f".format(distance)} cm"
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if needed
        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                // High accuracy
            }
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                // Medium accuracy
            }
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                // Low accuracy
            }
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                Toast.makeText(this, "Sensor poco confiable", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
