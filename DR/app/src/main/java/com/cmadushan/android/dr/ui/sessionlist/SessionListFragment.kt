package com.cmadushan.android.dr.ui.sessionlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cmadushan.android.dr.R

class SessionListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_list, container, false)
        view?.findViewById<TextView>(R.id.idshow)?.setText("Hello")
    }

   }