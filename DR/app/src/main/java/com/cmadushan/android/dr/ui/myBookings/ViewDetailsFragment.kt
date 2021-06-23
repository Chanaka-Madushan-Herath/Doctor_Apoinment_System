package com.cmadushan.android.dr.ui.myBookings

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.cmadushan.android.dr.R

class ViewDetailsFragment : Fragment() {

   private val args by navArgs<ViewDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_view_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.dname).text = "Dr."+args.bookingDetails.DocName
        view.findViewById<TextView>(R.id.specialization).text="( "+args.bookingDetails.Specialization+" )"
        view.findViewById<TextView>(R.id.hospital).text=args.bookingDetails.Hospital
        view.findViewById<TextView>(R.id.Name).text = args.bookingDetails.Name
        view.findViewById<TextView>(R.id.Address).text=args.bookingDetails.Address
        view.findViewById<TextView>(R.id.Email).text=args.bookingDetails.Email
        view.findViewById<TextView>(R.id.Tp).text=args.bookingDetails.Tp
    }
}