package com.labmacc.compass3
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface PostOrientation {
    @POST("/")
    suspend fun doPost(@Query("time") time:String, @Query("azimuth") azimuth:Float) : Response<String>
}

class WebApi {

    var retrofit : Retrofit
    init {
        val baseUrl = "Your URL"
         retrofit =
            Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // JSON converter to Kotlin object
            .build()
    }

}