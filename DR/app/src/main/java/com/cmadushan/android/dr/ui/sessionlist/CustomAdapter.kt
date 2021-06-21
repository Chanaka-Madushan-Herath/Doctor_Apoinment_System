package com.cmadushan.android.dr.ui.sessionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cmadushan.android.dr.R


class CustomAdapter(private val dateList: List<String>,private val timeList: List<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateview: TextView = view.findViewById(R.id.date)
        val timeView:TextView =view.findViewById(R.id.time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val timeView =inflater.inflate(R.layout.single_time_slot, parent, false)

        return ViewHolder(timeView)
    }

    override fun onBindViewHolder(viewHolder: CustomAdapter.ViewHolder, position: Int) {

        viewHolder.dateview.text = dateList[position]
        viewHolder.timeView.text=timeList[position]
    }

    override fun getItemCount() = dateList.size

}