package fr.xebia.mviandroid.android.chucksfacts

import fr.xebia.mviandroid.domain.entities.Fact

data class State(
    val isLoadingCategories: Boolean,
    val isLoadingFact: Boolean,
    val isSpinnerEnabled: Boolean,
    val facts: List<Fact>,
    val categories: List<String>,
    val isKickButtonEnabled: Boolean,
    val isClearButtonEnabled: Boolean
)