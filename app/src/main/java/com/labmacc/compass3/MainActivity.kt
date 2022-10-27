package com.labmacc.compass3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var view : MyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = MyView(this)
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        //Register the rotation vector sensor to the listener
        val sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sm.registerListener(
            view,
            sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        val sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //Unregister as the app is pausing, so no compass is displayed
        sm.unregisterListener(view,
            sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR))
    }

}