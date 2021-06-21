package com.cmadushan.android.dr.ui.sessionlist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cmadushan.android.dr.R
import com.cmadushan.android.dr.ui.bookSession.BookSessionFragment
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class CustomAdapter(private val timestampList: List<Timestamp>, private val doctorId: String) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateview: TextView = view.findViewById(R.id.date)
        val timeView:TextView =view.findViewById(R.id.time)
        val chanel:Button=view.findViewById(R.id.ChanelButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val timeView =inflater.inflate(R.layout.single_time_slot, parent, false)

        return ViewHolder(timeView)
    }

    override fun onBindViewHolder(viewHolder: CustomAdapter.ViewHolder, position: Int) {
        val timeStamp=timestampList[position]
        val milliseconds = timeStamp.seconds * 1000 + timeStamp.nanoseconds / 1000000
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val getDate = Date(milliseconds)
        val date = dateFormat.format(getDate).toString()
        val timeFormat = SimpleDateFormat("HH:mm")
        val getTime = Date(milliseconds)
        val time = timeFormat.format(getTime).toString()

        viewHolder.dateview.text = date
        viewHolder.timeView.text=time
        viewHolder.chanel.setOnClickListener {
            val details= SessionDetails(doctorId,timeStamp)
            val action= SessionListFragmentDirections.actionSessionListFragmentToBookSessionFragment(details)
           viewHolder.dateview. findNavController().navigate(action)
        }
    }

    override fun getItemCount() = timestampList.size

}