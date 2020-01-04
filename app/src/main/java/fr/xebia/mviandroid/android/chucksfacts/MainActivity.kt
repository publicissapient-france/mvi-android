package fr.xebia.mviandroid.android.chucksfacts

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.xebia.mviandroid.R
import fr.xebia.mviandroid.android.setVisibility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    IViewRenderer<State> {

    private val spinnerAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
    }

    private val recyclerViewAdapter: Adapter by lazy {
        Adapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.adapter = spinnerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        val viewModel: IModel<State, UserIntent> = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
            .also {
                it.state.observe(this, Observer { render(it) })
            }

        kickButton.setOnClickListener {
            viewModel.dispatchIntent(
                UserIntent.ShowNewFact(
                    spinner.selectedItem?.let { it as String })
            )
        }

        clearButton.setOnClickListener {
            viewModel.dispatchIntent(UserIntent.ClearFact)
        }
    }

    override fun render(state: State) {
        with(state) {
            progressBar.setVisibility(isLoadingFact)
            categoriesProgressBar.setVisibility(isLoadingCategories)
            kickButton.isEnabled = isKickButtonEnabled
            clearButton.isEnabled = isClearButtonEnabled
            spinner.isEnabled = isSpinnerEnabled
            spinnerAdapter.apply {
                clear()
                addAll(categories)
            }
            recyclerViewAdapter.update(state.facts)
        }
    }
}
