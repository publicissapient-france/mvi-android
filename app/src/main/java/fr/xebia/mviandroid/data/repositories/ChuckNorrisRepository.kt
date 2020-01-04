package fr.xebia.mviandroid.data.repositories

import com.squareup.moshi.JsonReader
import fr.xebia.mviandroid.data.api.IChuckNorrisService
import fr.xebia.mviandroid.domain.abstractions.IChuckNorrisRepository
import fr.xebia.mviandroid.domain.entities.Fact
import fr.xebia.mviandroid.domain.entities.FactsCategory
import fr.xebia.mviandroid.domain.exceptions.GenericNetworkException
import org.json.JSONException
import java.io.IOException

class ChuckNorrisRepository(private val api: IChuckNorrisService) : IChuckNorrisRepository {

    @Throws(GenericNetworkException::class)
    override suspend fun getRandomFact(category: String?): Fact {
        try {
            val call  = if(category != null){
                api.getRandomJoke(category)
            }else {
                api.getRandomJoke()
            }
            println(call.request().url())
            call.execute().body()?.apply {
                return this
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        throw GenericNetworkException()
    }

    @Throws(GenericNetworkException::class)
    override suspend fun getCategories(): List<FactsCategory> {
        try {
            api.getCategories().execute()
                .body()?.source()
                ?.apply {
                    val reader = JsonReader.of(this)
                    return (reader.readJsonValue() as List<String>)
                        .map { FactsCategory(it) }

                }

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        throw GenericNetworkException()
    }
}