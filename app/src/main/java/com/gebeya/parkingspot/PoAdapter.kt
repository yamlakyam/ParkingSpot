package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PoAdapter(val poResponse: ArrayList<PoResponse>, var clickedItem: PoAdapter.ClickedItem) : RecyclerView.Adapter<PoAdapter.SlotViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_row_layout,parent,false)
        return PoAdapter.SlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return poResponse.size
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = poResponse.get(position)
        holder.slott.text=slot._id
        holder.occ.text=slot.occupied_by
        //holder.status.text=slot.status.statusName[0]
        holder.itemView.setOnClickListener {
            clickedItem.clickedSpot(slot)
        }

    }

    interface ClickedItem {
        fun clickedSpot(poResponse: PoResponse)
    }

    class SlotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var slott: TextView =itemView.findViewById(R.id.slottLabel)
        var occ: TextView =itemView.findViewById(R.id.occupiedByLabel)
        var status:TextView=itemView.findViewById(R.id.bookedOrFree)
    }

}
