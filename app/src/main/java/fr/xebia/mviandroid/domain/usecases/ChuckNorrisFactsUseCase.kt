package fr.xebia.mviandroid.domain.usecases

import fr.xebia.mviandroid.domain.utils.Either
import fr.xebia.mviandroid.domain.abstractions.IChuckNorrisRepository
import fr.xebia.mviandroid.domain.entities.Fact
import fr.xebia.mviandroid.domain.entities.FactsCategory
import fr.xebia.mviandroid.domain.exceptions.GenericNetworkException

class ChuckNorrisFactsUseCase(private val repository: IChuckNorrisRepository) {

    suspend fun getRandom(category: String? = null): Either<Fact, GenericNetworkException> {
        return try {
            Either.Success(repository.getRandomFact(category))
        } catch (exception: GenericNetworkException) {
            Either.Failure(exception)
        }
    }

    suspend fun getCategories(): Either<List<FactsCategory>, GenericNetworkException> {
        return try {
            Either.Success(repository.getCategories())
        } catch (exception: GenericNetworkException) {
            Either.Failure(exception)
        }
    }
}