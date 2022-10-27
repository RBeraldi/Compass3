package com.labmacc.compass3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.util.Log
import android.view.Display
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withRotation
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.math.PI
import kotlin.math.atan2

const val TAG = "MYDEBUG"
const val TAG2 = "POST"
class MyView(context: Context?) : View(context), SensorEventListener2 {

    var size = 2f  //Absolute size of the compass in inches
    val a = 0.5f //Low-pass filter parameter, higher is smoother

    var mLastRotationVector = FloatArray(3) //The last value of the rotation vector
    var mRotationMatrix = FloatArray(9)
    var yaw = 0f
    var compass : Bitmap
    var webApi : PostOrientation




    init {
        size*=160*resources.displayMetrics.density
        val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Log.i(TAG,""+resources.displayMetrics.density)
        //Read .svg compass
        compass = ResourcesCompat.getDrawable(resources,R.drawable.compass,
            null)?.
        toBitmap(size.toInt(),size.toInt())!!
        webApi = WebApi().retrofit.create(PostOrientation::class.java)

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i(TAG,"drawing"+System.currentTimeMillis())
        with(canvas) {
            drawColor(Color.YELLOW)
            withRotation (-yaw,width/2f,height/2f) {
                drawBitmap(compass, (width - size) / 2f, (height - size) / 2f, null)
            }
        }
    }


    //Implementation of Event Listener Interface
    override fun onSensorChanged(p0: SensorEvent?) {

        mLastRotationVector = p0?.values?.clone()!! //Get last rotation vector

        Log.i(TAG,""+mLastRotationVector[0]+""+mLastRotationVector[1]+" "+mLastRotationVector[2])

        //Compute the rotation matrix from the rotation vector
        SensorManager.getRotationMatrixFromVector(mRotationMatrix,mLastRotationVector)

        //Calculate the yaw angle, see slides of the lesson——
        yaw = a*yaw+(1-a)* atan2(mRotationMatrix[1],mRotationMatrix[4]) *180f/ PI.toFloat()
        invalidate()

        GlobalScope.launch(IO) {
            //Make a post
            val a = async {  webApi.doPost(""+System.currentTimeMillis(),yaw) }
            Log.i(TAG2,"REPLY FROM SERVER: "+a.await().body())
        }




    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        TODO("Not yet implemented")
    }

    override fun onFlushCompleted(p0: Sensor?) {
        //      TODO("Not yet implemented")
    }


}