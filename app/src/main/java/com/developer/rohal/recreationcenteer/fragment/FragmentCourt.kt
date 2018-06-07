package com.developer.rohal.recreationcenteer.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Adapter.CourtGetSignUp
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Helper.Support
import com.developer.rohal.recreationcenteer.Interface.RedditAPI

import com.developer.rohal.recreationcenteer.R
import com.developer.rohal.recreationcenteer.PojoCourt
import kotlinx.android.synthetic.main.fragmeent_court.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class FragmentCourt : Fragment() {
    var n = 0
    var mview: View? = null
    lateinit var allCourts:ArrayList<PojoCourt>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mview = inflater!!.inflate(R.layout.fragmeent_court, container, false)
        return mview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("Data","lat "+Support.instance.SignUpLocationlat +" lang "+Support.instance.SignUpLocationlang)
        apiCall(mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetCourts(Support.instance.SignUpLocationlang,Support.instance.SignUpLocationlat), "getCourt")

        Location_Search_Button.setOnClickListener()
        {
            //var location = locationCourt.text.toString().trim()
            //apiCall(mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetCourts(location), "getCourt")
        }
    }

    private fun apiCall(it: View, call: Call<ResponseBody>?, s: String) {
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                Log.d("Api Dashboard", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ", "${json}")
                    if (json == null) {
                        Log.d("Api Like Hit ", " Error Ocuured")
                    } else {
                        if (s.equals("getCourt")) {
                            Log.d("Api(login)", "Server Response: " + response.toString());
                            if (JSONObject(json).get("success").toString().toInt() == 1) {
                                var jsonobj = JSONObject(json)
                                Log.d("JSONOBJ", jsonobj.toString())
                                var data = JSONObject(json).getJSONArray("data")
                                Log.d("DATA", data.toString())
                                var s = data.length()
                                var locList: ArrayList<String> = ArrayList()
                                for (i in 0..s - 1) {
                                    var loc = data.getJSONObject(i).getString("homecourt")
                                    locList.add(loc)
                                    Log.d("LOC", loc)
                                }
                                listLocation.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                                val adp1 = CourtGetSignUp(context, s, locList, data.toString())
                                listLocation.adapter = adp1
                            }
                        }
                    }


                } catch (e: JSONException) {
                    //Helper.instance.showSnackBar(activity!!.findViewById(android.R.id.content), e.toString())
                    Log.e("Api Dashboard", "Server Response(Json Exception Occur): " + e)
                } catch (e: IOException) {
                    Log.e("Api Dashboard", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Dashboard", "Server Response(On Failure): " + t)
                //Helper.instance.showSnackBar(view, "Internet Connectivity Problem")
            }
        })
    }
}
