package com.gebeya.parkingspot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_spot_list.*
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class SpotListFragment : Fragment(), NearestAdapter.ClickedItem {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    var spotData: ArrayList<Nearest> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //var bundle=this.arguments


        return inflater.inflate(R.layout.fragment_spot_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //var bundle = arguments ?: return
        //var lat = bundle.getDouble("lat")
        //var long = bundle.getDouble("long")


        //Toast.makeText(requireContext(),"$lat",Toast.LENGTH_LONG)
        //Log.d("Bundle", "$lat")

        val params = HashMap<String, Double>()
        params.put("longitude", 38.7)
        params.put("latitude", 8.9)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(requireContext())
        var recyclerView: RecyclerView = rcSpots
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL)
        )
        var call = retrofitInterface!!.findspot("Bearer ${sessionManager.fetchAuthToken()}", params)

        call.enqueue(object : Callback<ArrayList<Nearest>> {
            override fun onFailure(call: Call<ArrayList<Nearest>>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ArrayList<Nearest>>,
                response: Response<ArrayList<Nearest>>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    var resp = response.body()!!
                    recyclerView.adapter = NearestAdapter(resp,this@SpotListFragment)

                    // Toast.makeText(requireContext(), resp[0].floor, Toast.LENGTH_SHORT).show()
/*
                    for(i in 0..resp.size-1){
                        arrayListLoc.add(resp[i].location.coordinates)
                    }*/

                } else if (response.code() == 400) {

                    Toast.makeText(requireContext(), "client error", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })


    }

    override fun clickedSpot(nearest: Nearest) {
        Log.e("tag","clicked")
        findNavController().navigate(R.id.action_spotListFragment_to_bookFragment)


    }

}