package fr.xebia.mviandroid.android.chucksfacts

interface IViewRenderer<STATE> {
    fun render(state: STATE)
}