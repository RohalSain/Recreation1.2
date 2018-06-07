package com.developer.rohal.recreationcenteer.fragment


import android.content.Loader
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.rohal.recreationcenteer.Adapter.MyTeam
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Interface.RedditAPI
import com.developer.rohal.recreationcenteer.R
import com.example.emilence.recreationcenter.PojoClasses.Helper
import com.example.emilence.recreationcenter.PojoClasses.Players
import kotlinx.android.synthetic.main.fragment_add_players.*
import kotlinx.android.synthetic.main.fragment_my_team.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.HashMap


class FragmentMyTeam : Fragment() {
    var session:Session?=null
    //var loader: Loader = Load
    var pn=1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            session = Session(context)
            val b = arguments
            var cName = b?.getString("captain")
            var tName = b?.getString("teamName")
            var tId = b?.getString("teamId")
            var cId=b?.getString("captainId")

            Log.d("TEAM", tName)
            var f = FragmentMyTeam()
            listMyTeams.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            val adp1 = MyTeam(context, tName, tId,cId,
                    object : MyTeam.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            //loader.ShowLoader(context)
                            Log.d("item", "item clicked")
                            var client = ApiCall()
                            var retrofit = client.retrofitClient()
                            val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                            var token = session?.gettoken()
                            var teamId = session?.getteamId()
                            val headerMap = HashMap<String, String>()
                            headerMap.put("teamId", teamId!!)
                            Log.d("TEAMID", teamId)
                            var call = retrofitAp.getSingleTeam(token!!, teamId)
                            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                                    Log.d("server", "onResponse: Server Response: " + response.toString());
                                    try {
                                        var json: String? = null
                                        json = response?.body()!!.string()
                                        Log.d("team", json)
                                        var obj = JSONObject(json)
                                        var data = obj.getJSONObject("data")
                                        Log.d("DATA", data.toString())
                                        var playingPlayers=data.getJSONArray("teamPlayers")
                                        var ppn=playingPlayers.length()
                                        pn=ppn
                                        Log.d("PLAYING-PLAYER",ppn.toString())
                                        var captainName = data.getString("captainName")
                                        var teamName = data.getString("teamName")
                                        var teamId = data.getString("_id")
                                        var capId = data.getString("teamCaptain")
                                        var benchPalyers = data.getString("benchPlayers")
                                        Log.d("CAPTAIN", captainName)
                                        Log.d("TeamName", teamName)
                                        Log.d("TeamId", teamId)
                                        JSONObject(json).getJSONObject("data").getJSONArray("teamPlayers")
                                        //
                                        val cast: JSONArray = JSONObject(json).getJSONObject("data").getJSONArray("teamPlayers")
                                        if(cast.length() == 0) {
                                            Log.d("Players Size ","0")
                                        }
                                        else
                                        {
                                            Helper.instance.PlayingPlayers.clear()
                                            for (i in 0 until cast.length()) {
                                                if(!cast.getJSONObject(i).getString("name").equals("$captainName")) {
                                                    Helper.instance.PlayingPlayers.add(
                                                            Players(cast.getJSONObject(i).getString("_id"),
                                                                    cast.getJSONObject(i).getString("name"),
                                                                    teamId
                                                            ))
                                                }
                                            }
                                        }
                                        val cast1: JSONArray = JSONObject(json).getJSONObject("data").getJSONArray("benchPlayers")
                                        if(cast1.length() == 0) {
                                            Helper.instance.bechPlayers.clear()
                                            Log.d("BenchPlayer Size ","0")
                                        }
                                        else
                                        {
                                            Helper.instance.bechPlayers.clear()
                                            for (i in 0 until cast1.length()) {
                                                Helper.instance.bechPlayers.add(Players(
                                                        cast1.getJSONObject(i).getString("_id"),
                                                        cast1.getJSONObject(i).getString("name"),
                                                        teamId
                                                ))
                                            }
                                        }

                                        val fpa = FragmentMyTeam()
                                        val b = Bundle()
                                        b.putString("captain", captainName)
                                        b.putString("teamName", teamName)
                                        b.putString("teamId", teamId)
                                        b.putString("capId",capId)
                                        b.putInt("playersSize",ppn)
                                        b.putString("playingPlayers",playingPlayers.toString())
                                        b.putString("benchPlayers",benchPalyers)
                                        fpa.setArguments(b)
                                        //loader.HideLoader()
                                        val fm = fragmentManager
                                        val transaction = fm?.beginTransaction();
                                        //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                        transaction?.replace(R.id.container, fpa);
                                        transaction?.addToBackStack(tag);
                                        transaction?.commit();
                                    } catch (e: JSONException) {
                                        Log.e("JSONException", "onResponse: JSONException: " + e);
                                    } catch (e: IOException) {
                                        Log.e("IOexception", "onResponse: JSONException: ");
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("OnFailure", "onFailure: Something went wrong: ")
                                    //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            })
                        }
                    })
            listMyTeams.adapter = adp1
        }
    }


