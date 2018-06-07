package com.developer.rohal.recreationcenteer.fragment


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Adapter.AllCourts
import com.developer.rohal.recreationcenteer.Adapter.CourtGetSignUp
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Helper.Support
import com.developer.rohal.recreationcenteer.Interface.RedditAPI
import com.developer.rohal.recreationcenteer.R
import com.example.emilence.recreationcenter.PojoCourtsDetail
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocomplete.MODE_OVERLAY
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragmnet_select_gynasium.*
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.android.synthetic.main.fragmeent_court.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException


class FragmentSelectGynasium : Fragment(), OnMapReadyCallback {
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mView: View
    private lateinit var mMapView: MapView
    private lateinit var myMarker: Marker
    val REQUEST_CODE = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragmnet_select_gynasium, container, false)
        return mView
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0!!
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        setLocation.setOnClickListener {
            startActivityForResult(PlaceAutocomplete.IntentBuilder(MODE_OVERLAY)
                    .build(context as Activity?), REQUEST_CODE);
        }
        backButton.setOnClickListener {
            Log.d("Data ","yes")
        }

        nextButton.setOnClickListener {
            if(Support.instance.SignUpLocationlang.isEmpty()) {
                Toast.makeText(context, "Please Select location", Toast.LENGTH_SHORT).show()
            }
            else if(Support.instance.HomeCourt.isEmpty()) {
                Toast.makeText(context, "Please Select HomeCourt", Toast.LENGTH_SHORT).show()
            }
            else {
                   Support.instance.headerMap["homeCourt"] = RequestBody.create(MediaType.parse("text/plain"), Support.instance.HomeCourt)
                   Support.instance.headerMap["lat"] = RequestBody.create(MediaType.parse("text/plain"), Support.instance.SignUpLocationlat)
                   Support.instance.headerMap["lng"] = RequestBody.create(MediaType.parse("text/plain"), Support.instance.SignUpLocationlang)
                   apiCall(mView, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.registerUser(Support.instance.headerMap), "signUp")

            }
        }

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById(R.id.map)
        if (mMapView != null) {
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val place = PlaceAutocomplete.getPlace(context, data)
            setLocation.text = place.name
            Support.instance.SignUpLocation = place.name.toString()
            Support.instance.SignUpLocationlat = place.latLng.latitude.toString()
            Support.instance.SignUpLocationlang = place.latLng.longitude.toString()
            mGoogleMap.addMarker(MarkerOptions().position(place.latLng).title("${place.name}").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
            var Liberty:CameraPosition = CameraPosition.builder().target(place.latLng).zoom(15.toFloat()).bearing(0.toFloat()).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty))
            apiCall(mView, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetCourts(Support.instance.SignUpLocationlang,Support.instance.SignUpLocationlat), "getCourt")

        }
        backButton.setOnClickListener {
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
                                var locList: ArrayList<PojoCourtsDetail> = ArrayList()
                                for (i in 0..s - 1) {
                                    var loc = data.getJSONObject(i).getString("homecourt")
                                    var dist = data.getJSONObject(i).getJSONObject("dist")
                                    var cal = dist.getString("calculated")
                                    locList.add(PojoCourtsDetail(loc,cal))
                                    Log.d("LOC", loc)
                                }
                                courts.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                                var adapter = AllCourts(locList,object :AllCourts.OnItemClickListener{
                                    override fun onItemClick(position: Int) {
                                        Support.instance.HomeCourt = locList.get(position).gymName
                                        setGym.text = "Selected Gym: "+locList.get(position).gymName
                                    }
                                })
                                courts.adapter = adapter
                                if(locList.size!=0) {
                                    setGym.text = "Selected Gym: "+locList.get(0).gymName
                                }
                            }
                        }
                        else if (s.equals("signUp")) {
                            Log.d("Api(login)", "Server Response: " + response.toString());
                            if (JSONObject(json).get("success").toString().toInt() == 1) {
                                if (JSONObject(json).get("message").toString() == "Registration successful") {
                                    Session(context).setLoggedin(true,
                                            JSONObject(json).getJSONObject("account").getString("name"),
                                            "http://139.59.18.239:6009/basketball/" + JSONObject(json).getJSONObject("account").get("profilePic"),
                                            JSONObject(json).getString("token"),
                                            JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                            "0",
                                            JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                            JSONObject(json).getJSONObject("account").getString("_id"),
                                            JSONObject(json).getJSONObject("account").getString("username"),
                                            JSONObject(json).getJSONObject("account").getString("mobileNumber")
                                    )
                                    fragmentManager?.beginTransaction()
                                            ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                            ?.replace(R.id.container, FragmentLogin())
                                            ?.addToBackStack("dashboard")
                                            ?.commit()
                                }
                            }
                        }
                    }


                } catch (e: JSONException) {
                    Toast.makeText(context, "Sign Up JSON Error Occured ", Toast.LENGTH_SHORT).show()
                    Log.e("Api Dashboard", "Server Response(Json Exception Occur): " + e)
                } catch (e: IOException) {
                    Toast.makeText(context, "Sign Up IO Error Occured ", Toast.LENGTH_SHORT).show()
                    Log.e("Api Dashboard", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Sign Up Failure Error Occured ", Toast.LENGTH_SHORT).show()
                Log.e("Api Dashboard", "Server Response(On Failure): " + t)
                //Helper.instance.showSnackBar(view, "Internet Connectivity Problem")
            }
        })
    }
}
