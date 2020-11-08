package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PoStackAdapter(val stack: ArrayList<Stack>, var clickedItem: ClickedItem) : RecyclerView.Adapter<PoStackAdapter.StackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.po_row_stack,parent,false)
        return StackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stack.size
    }

    override fun onBindViewHolder(holder: StackViewHolder, position: Int) {
        val stackk = stack.get(position)
        // var compnay:String=near.company
        // holder.bindData(nearest,position)
        //holder.stackId.text=stackk._id
        holder.floorId.text="Floor "+stackk.floor.toInt().toString()
        holder.itemView.setOnClickListener {
            clickedItem.clickedSpot(stackk)

        }

    }
    interface ClickedItem {
        fun clickedSpot(stackk: Stack)
    }


    class StackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //var stackId:TextView=itemView.findViewById(R.id.stackId)
        var floorId:TextView=itemView.findViewById(R.id.floorId)

    }

}