package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter (val ticket:ArrayList<Ticket>,var clickedItem: TicketAdapter.ClickedItem) : RecyclerView.Adapter<TicketAdapter.TicketHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return TicketAdapter.TicketHolder(view)
    }

    override fun getItemCount(): Int {
        return ticket.size
    }

    override fun onBindViewHolder(holder: TicketHolder, position: Int) {
        val tickk = ticket.get(position)
        // holder.slotn.text=slott._id
        holder.plate_no.text="Plate Number: "+tickk.plate_number
        holder.parked_at.text="Parked at "+tickk.park_at
        holder.status.text=tickk.ticket_status

        holder.itemView.setOnClickListener {
            clickedItem.clickedSpot(tickk)

        }
    }

    interface ClickedItem {
        fun clickedSpot(ticket:Ticket)
    }


    class TicketHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var plate_no:TextView=itemView.findViewById(R.id.CompanytxtView)
        var parked_at:TextView=itemView.findViewById(R.id.detailstxtView)
        var status:TextView=itemView.findViewById(R.id.detalslabel)
    }
}