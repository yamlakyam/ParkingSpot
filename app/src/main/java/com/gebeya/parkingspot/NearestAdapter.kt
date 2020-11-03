package com.gebeya.parkingspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

 class NearestAdapter(val nearest: ArrayList<Nearest>) : RecyclerView.Adapter<NearestAdapter.SpotViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return SpotViewHolder(view)
    }


    override fun getItemCount(): Int {
        return nearest.size
    }

    override fun onBindViewHolder(holder: SpotViewHolder, position: Int) {
        val near = nearest.get(position)
       // var compnay:String=near.company
       // holder.bindData(nearest,position)
        holder.company.text=near.company
        holder.info.text=near.floor.toString()
        /*holder.itemView.setOnClickListener {

        }

         */
    }


     class SpotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var company: TextView =itemView.findViewById(R.id.CompanytxtView)
        // private var company:TextView=itemView.CompanytxtView
        var info: TextView =itemView.findViewById(R.id.detailstxtView)
       // var det: TextView =itemView.findViewById(R.id.detalslabel)
        //var book: TextView =itemView.findViewById(R.id.bookBtn)


        /*fun bindData(spotlist:ArrayList<Nearest>,position: Int){
            company.text=spotlist!!.get(position).company
            info.text=spotlist.get(position).floor.toString()
            itemView.setOnClickListener(View.OnClickListener {

                itemClick.getItem(adapterPosition)
            })

        }

         */
    }

}