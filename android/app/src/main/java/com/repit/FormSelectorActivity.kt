package com.repit.v2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.repit.v2.databinding.ActivityFormSelectorBinding
import com.repit.v2.utils.FormParser

data class SurveyIndex(val surveys: List<SurveyItem>)
data class SurveyItem(val id: String, val name: String, val file: String)

class FormSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val surveys = loadSurveysIndex()

        binding.recyclerViewSurveys.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSurveys.adapter = SurveyAdapter(surveys) { item ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("FORM_FILE", item.file)
                putExtra("FORM_NAME", item.name)
            }
            startActivity(intent)
        }

        binding.buttonExportHistoric.setOnClickListener {
            // Reusing MainActivity logic for export via a separate action if needed, 
            // but for now, we'll keep it simple: export all from any screen.
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("ACTION_EXPORT", true)
            }
            startActivity(intent)
        }
    }

    private fun loadSurveysIndex(): List<SurveyItem> {
        val jsonString = assets.open("surveys_index.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, SurveyIndex::class.java).surveys
    }

    class SurveyAdapter(private val items: List<SurveyItem>, private val onClick: (SurveyItem) -> Unit) :
        RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.textView.text = item.name
            holder.itemView.setOnClickListener { onClick(item) }
        }

        override fun getItemCount() = items.size
    }
}
