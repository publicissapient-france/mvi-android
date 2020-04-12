package fr.xebia.mviandroid.android.chucksfacts

import android.content.Context
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.xebia.mviandroid.data.api.IChuckNorrisService
import fr.xebia.mviandroid.data.repositories.ChuckNorrisRepository
import fr.xebia.mviandroid.domain.entities.Fact
import fr.xebia.mviandroid.domain.entities.FactsCategory
import fr.xebia.mviandroid.domain.exceptions.GenericNetworkException
import fr.xebia.mviandroid.domain.usecases.ChuckNorrisFactsUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

class NonNullMutableLiveData<T> @UiThread constructor(initialValue: T) : MutableLiveData<T>() {

    init {
        value = initialValue
    }

    override fun getValue(): T {
        return super.getValue()!!
    }
}

class MainViewModel : ViewModel(), IModel<State, UserIntent>, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default

    private val stateChannel = Channel<PartialState>()

    private val _state =
        NonNullMutableLiveData(createInitialState())
    override val state: LiveData<State>
        get() = _state

    private val chuckNorrisUseCase = ChuckNorrisFactsUseCase(
        ChuckNorrisRepository(IChuckNorrisService.newInstance())
    )
    private val reducer: IReducer<State, PartialState> =
        Reducer()

    init {
        launch(Dispatchers.Main) {
            for (partialState in stateChannel) {
                _state.value = reducer.reduce(_state.value, partialState)
            }
        }
        launch {
            handleAction(Action.FetchCategories)
        }
    }

    private fun createInitialState() = State(
        isLoadingCategories = false,
        isLoadingFact = false,
        isSpinnerEnabled = false,
        facts = emptyList(),
        categories = emptyList(),
        isKickButtonEnabled = false,
        isClearButtonEnabled = false
    )

    override fun dispatchIntent(intent: UserIntent) {
        handleAction(intentToAction(intent))
    }

    private fun intentToAction(intent: UserIntent): Action {
        return when (intent) {
            is UserIntent.ShowNewFact -> Action.FetchRandomFact(
                intent.category
            )
            is UserIntent.ClearFact -> Action.ClearFact
            is UserIntent.NotifyEventExecuted -> Action.ConsumeEvent
        }
    }

    private fun handleAction(action: Action) {
        launch {
            return@launch when (action) {

                is Action.FetchRandomFact -> {

                    stateChannel.send(
                        PartialState.Loading(
                            isLoadingCategories = false,
                            isLoadingFact = true
                        )
                    )

                    delay(1000)

                    chuckNorrisUseCase.getRandom(action.category).either(
                        onSuccess = { joke ->
                            stateChannel.send(
                                PartialState.JokeRetrievedSuccessfully(
                                    joke
                                )
                            )
                        },
                        onFailure = { exception ->
                            stateChannel.send(
                                PartialState.FetchJokeFailed(
                                    exception
                                )
                            )
                        })
                }

                is Action.FetchCategories -> {

                    stateChannel.send(
                        PartialState.Loading(
                            isLoadingCategories = true,
                            isLoadingFact = false
                        )
                    )

                    delay(1200)

                    chuckNorrisUseCase.getCategories().either({ categories ->
                        stateChannel.send(
                            PartialState.CategoriesRetrievedSuccessfully(
                                categories
                            )
                        )
                    }, { exception ->
                        stateChannel.send(
                            PartialState.FetchJokeFailed(
                                exception
                            )
                        )
                    })
                }

                is Action.ClearFact -> {
                    stateChannel.send(PartialState.JokesCleared)
                }

                is Action.ConsumeEvent -> {
                    stateChannel.send(PartialState.EventConsumed)
                }
            }
        }
    }
}

sealed class PartialState {

    object JokesCleared : PartialState() {
        val jokes = emptyList<Fact>()
        const val isJokeButtonEnabled = true
        const val isClearButtonEnabled = false
        const val isSpinnerEnabled = true
    }

    data class Loading(val isLoadingFact: Boolean, val isLoadingCategories: Boolean) :
        PartialState() {
        val isJokeButtonEnabled = false
        val isSpinnerEnabled = false
    }

    data class JokeRetrievedSuccessfully(val fact: Fact) : PartialState() {
        val isJokeButtonEnabled = true
        val isClearButtonEnabled = true
        val isLoadingFact = false
        val isSpinnerEnabled = true
    }

    data class CategoriesRetrievedSuccessfully(val categories: List<FactsCategory>) :
        PartialState() {
        val isJokeButtonEnabled = true
        val isClearButtonEnabled = false
        val isLoadingCategories = false
        val isSpinnerEnabled = true
    }

    data class FetchJokeFailed(val exception: GenericNetworkException) : PartialState() {
        val isJokeButtonEnabled = true
        val isLoadingFact = false
        val isLoadingCategories = false
        val isSpinnerEnabled = true
        val event = { context: Context ->
            Toast.makeText(context, exception.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }
    }

    data class FetchCategoriesFailed(val exception: GenericNetworkException) : PartialState() {
        val isLoadingCategories = false
        val isSpinnerEnabled = true
    }

    object EventConsumed : PartialState()
}

private sealed class Action {
    data class FetchRandomFact(val category: String?) : Action()
    object ClearFact : Action()
    object FetchCategories : Action()
    object ConsumeEvent : Action()
}