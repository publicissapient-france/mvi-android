package fr.xebia.mviandroid.data.api

import fr.xebia.mviandroid.domain.entities.Fact
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IChuckNorrisService {

    @GET("jokes/random/")
    fun getRandomJoke(): Call<Fact>

    @GET("jokes/random/")
    fun getRandomJoke(@Query("category") category: String): Call<Fact>

    @GET("jokes/categories/")
    fun getCategories(): Call<ResponseBody>

    companion object Builder {
        fun newInstance(): IChuckNorrisService = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(IChuckNorrisService::class.java)
    }
}