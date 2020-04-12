package fr.xebia.mviandroid.android.chucksfacts

sealed class UserIntent {
    data class ShowNewFact(val category: String?) : UserIntent()
    object ClearFact : UserIntent()
    object NotifyEventExecuted : UserIntent()
}