package fr.xebia.mviandroid.android.chucksfacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.xebia.mviandroid.R
import fr.xebia.mviandroid.domain.entities.Fact
import kotlinx.android.synthetic.main.fact_item_layout.view.*

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val facts = mutableListOf<Fact>()

    override fun getItemCount(): Int = facts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fact_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val fact = facts[position]
            textView.text = fact.value
            Picasso.get()
                .load(fact.icon_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }

    fun update(facts: List<Fact>) {
        this.facts.clear()
        this.facts.addAll(facts)
        notifyDataSetChanged()
    }
}