package com.repit.v2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.repit.v2.R

data class Institution(
    val name: String,
    val province: String,
    val municipality: String,
    val type: String
)

class InstitutionAdapter(
    private var institutions: List<Institution>,
    private val onSelected: (Institution) -> Unit
) : RecyclerView.Adapter<InstitutionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvInstitutionName)
        val tvDetail: TextView = view.findViewById(R.id.tvInstitutionDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_institution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = institutions[position]
        holder.tvName.text = item.name
        holder.tvDetail.text = "${item.type} - ${item.municipality}, ${item.province}"
        holder.itemView.setOnClickListener { onSelected(item) }
    }

    override fun getItemCount() = institutions.size

    fun updateList(newList: List<Institution>) {
        institutions = newList
        notifyDataSetChanged()
    }
}
