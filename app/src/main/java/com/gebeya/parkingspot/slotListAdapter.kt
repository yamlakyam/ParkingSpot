package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class slotListAdapter(val slots:ArrayList<Slot>,var clickedItem: slotListAdapter.ClickedItem) : RecyclerView.Adapter<slotListAdapter.SlotHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_slot,parent,false)
        return slotListAdapter.SlotHolder(view)
    }

    override fun getItemCount(): Int {
        return slots.size
    }

    override fun onBindViewHolder(holder: SlotHolder, position: Int) {
        val slott = slots.get(position)
        holder.slotn.text=slott._id
        holder.desc.text=slott.description
        holder.itemView.setOnClickListener {
            clickedItem.clickedSpot(slott)

        }
    }

    interface ClickedItem {
        fun clickedSpot(slots: Slot)
    }


    class SlotHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var slotn: TextView =itemView.findViewById(R.id.slotName)
        var desc:TextView=itemView.findViewById(R.id.desc)

    }



}

