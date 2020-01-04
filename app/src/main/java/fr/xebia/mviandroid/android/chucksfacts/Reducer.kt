package fr.xebia.mviandroid.android.chucksfacts

interface IReducer<STATE, PARTIAL_STATE> {
    fun reduce(state: STATE, partialState: PARTIAL_STATE): STATE
}

class Reducer : IReducer<State, PartialState> {

    override fun reduce(state: State, partialState: PartialState): State {
        return when (partialState) {

            is PartialState.JokesCleared -> state.copy(
                facts = PartialState.JokesCleared.jokes,
                isSpinnerEnabled = partialState.isSpinnerEnabled,
                isClearButtonEnabled = PartialState.JokesCleared.isClearButtonEnabled,
                isKickButtonEnabled = PartialState.JokesCleared.isJokeButtonEnabled
            )

            is PartialState.JokeRetrievedSuccessfully -> state.copy(
                isSpinnerEnabled = partialState.isSpinnerEnabled,
                isClearButtonEnabled = partialState.isClearButtonEnabled,
                isKickButtonEnabled = partialState.isJokeButtonEnabled,
                isLoadingFact = partialState.isLoadingFact,
                facts = state.facts.toMutableList().apply { add(0, partialState.fact) }
            )

            is PartialState.CategoriesRetrievedSuccessfully -> state.copy(
                categories = partialState.categories.map { it.title },
                isSpinnerEnabled = partialState.isSpinnerEnabled,
                isLoadingCategories = partialState.isLoadingCategories,
                isClearButtonEnabled = partialState.isClearButtonEnabled,
                isKickButtonEnabled = partialState.isJokeButtonEnabled
            )

            is PartialState.Loading -> state.copy(
                isLoadingFact = partialState.isLoadingFact,
                isLoadingCategories = partialState.isLoadingCategories,
                isSpinnerEnabled = partialState.isSpinnerEnabled,
                isKickButtonEnabled = partialState.isJokeButtonEnabled
            )

            is PartialState.FetchJokeFailed -> {
                state.copy(
                    isKickButtonEnabled = partialState.isJokeButtonEnabled,
                    isSpinnerEnabled = partialState.isSpinnerEnabled,
                    isLoadingFact = partialState.isLoadingFact,
                    isLoadingCategories = partialState.isLoadingCategories
                )
            }

            is PartialState.FetchCategoriesFailed -> {
                state.copy(
                    isLoadingCategories = partialState.isLoadingCategories,
                    isSpinnerEnabled = partialState.isSpinnerEnabled
                )
            }
        }
    }
}