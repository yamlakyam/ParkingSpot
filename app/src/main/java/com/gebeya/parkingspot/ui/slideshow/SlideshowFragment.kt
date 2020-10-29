package com.gebeya.parkingspot.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gebeya.parkingspot.HomeActivity
import com.gebeya.parkingspot.Location
import com.gebeya.parkingspot.R
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.SessionManager
import com.gebeya.parkingspot.ui.home.HomeMapFragment
import kotlinx.android.synthetic.main.fragment_slideshow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideshowFragment : Fragment() {

    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it

            
            sessionManager= SessionManager(requireContext())
            val call = retrofitInterface!!.findspot("${sessionManager.fetchAuthToken()}",38.7,8.94)

            button2.setOnClickListener {



                call.enqueue(object: Callback<List<Location>> {
                    override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<Location>>,
                        response: Response<List<Location>>
                    ) {
                        /*for(i in response.body()!!.listIterator()){
                            Toast.makeText(requireContext(), "${response.body()}",Toast.LENGTH_LONG).show()
                        }

                         */
                        if (response.code()==200 && response.body()!=null) {

                            Toast.makeText(requireContext(),"connected ${response.body()}", Toast.LENGTH_LONG).show()
                            //toasted the token to check if its working.

                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)

                        } else if (response.code() == 400) {

                            Toast.makeText(requireContext(), "client error", Toast.LENGTH_LONG)
                                .show()
                        }

                    }

                })

            }



        })
        return root
    }
}