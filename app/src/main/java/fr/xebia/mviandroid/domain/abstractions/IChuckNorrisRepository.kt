package fr.xebia.mviandroid.domain.abstractions

import fr.xebia.mviandroid.domain.entities.Fact
import fr.xebia.mviandroid.domain.entities.FactsCategory
import fr.xebia.mviandroid.domain.exceptions.GenericNetworkException

interface IChuckNorrisRepository {

    @Throws(GenericNetworkException::class)
    suspend fun getRandomFact(category: String? = null): Fact

    @Throws(GenericNetworkException::class)
    suspend fun getCategories(): List<FactsCategory>
}