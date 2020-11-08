package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PoSlotAdapter(val slot:ArrayList<Slot>,var clickedItem: PoSlotAdapter.ClickedItem) : RecyclerView.Adapter<PoSlotAdapter.SlotHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_row_slot,parent,false)
        return PoSlotAdapter.SlotHolder(view)
    }

    override fun getItemCount(): Int {
        return slot.size
    }

    override fun onBindViewHolder(holder: SlotHolder, position: Int) {
        val slott = slot.get(position)
        //holder.slotn.text=slott._id
        holder.desc.text=slott.description
        holder.stat.text=slott.status.statusName[0]
        holder.itemView.setOnClickListener {
            clickedItem.clickedSpot(slott)

        }
    }

    interface ClickedItem {
        fun clickedSpot(slot: Slot)
    }


    class SlotHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //var slotn: TextView =itemView.findViewById(R.id.poslotName)
        var desc: TextView =itemView.findViewById(R.id.poslotdesc)
        var stat:TextView=itemView.findViewById(R.id.status)

    }



}