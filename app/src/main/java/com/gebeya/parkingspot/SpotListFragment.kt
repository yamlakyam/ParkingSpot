package com.gebeya.parkingspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_spot_list.*
import kotlinx.android.synthetic.main.row_layout.*


class SpotListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spot_list, container, false)
    }

    /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rcSpots.hasFixedSize()
        rcSpots.layoutManager=LinearLayoutManager(context)
        rcSpots.itemAnimator=DefaultItemAnimator()
        rcSpots.adapter=NearestAdapter(nearest ,R.layout.row_layout)
    }

    inner class NearestAdapter(val nearest: ArrayList<Nearest>, val itemLayout: Int) :RecyclerView.Adapter<SpotListFragment.SpotViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout,parent,false)
            return SpotViewHolder(view)
        }

        override fun getItemCount(): Int {
            return nearest.size
        }

        override fun onBindViewHolder(holder: SpotViewHolder, position: Int) {
            val near = nearest.get(position)
            holder.updateNearest(near)
        }

    }


    inner class SpotViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private var company:TextView=itemView.findViewById(R.id.CompanytxtView)
        private var info:TextView=itemView.findViewById(R.id.detailslabel)
        private var det:TextView=itemView.findViewById(R.id.detailtxtxView)
        private var book:TextView=itemView.findViewById(R.id.bookBtn)

        fun updateNearest(near:Nearest){

        }
    }

     */

}