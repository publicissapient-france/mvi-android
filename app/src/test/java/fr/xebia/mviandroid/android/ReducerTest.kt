package fr.xebia.mviandroid.android

import fr.xebia.mviandroid.android.chucksfacts.PartialState
import fr.xebia.mviandroid.android.chucksfacts.Reducer
import fr.xebia.mviandroid.android.chucksfacts.State
import fr.xebia.mviandroid.domain.entities.Fact
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class ReducerTest {

    @Test
    fun `reduce JokeRetrievedSuccessfully should add a joke to the top of the list`() {
        //Given
        val someFact = Fact("some fact title", "https://fake.com/some-fact-url.png")
        val newFact = Fact("new fact title", "https://fake.com/new-fact-url.png")

        val currentState = State(
            isLoadingCategories = false,
            isLoadingFact = true,
            isSpinnerEnabled = false,
            facts = listOf(someFact),
            categories = emptyList(),
            isKickButtonEnabled = false,
            isClearButtonEnabled = false
        )

        val partialState = PartialState.JokeRetrievedSuccessfully(
            newFact
        )

        val expectedNewState = currentState.copy(
            facts = listOf(newFact) + currentState.facts,
            isSpinnerEnabled = true,
            isLoadingFact = false,
            isKickButtonEnabled = true,
            isClearButtonEnabled = true
        )

        val reducer = Reducer()

        //When
        val newState = reducer.reduce(currentState, partialState)

        //Then
        assertThat(newState, `is`(expectedNewState))
    }
}