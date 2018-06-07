package com.developer.rohal.recreationcenteer.fragment


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Helper.Counter
import com.developer.rohal.recreationcenteer.Interface.RedditAPI

import com.developer.rohal.recreationcenteer.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException

class FragmentHome : Fragment() {
    var status = "false"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        side_button1_lay.setOnClickListener {
            parent.openDrawer(Gravity.START)
        }
        side_button1.setOnClickListener {
            parent.openDrawer(Gravity.START)
        }
        locationTextHome.text = Session(context).gethomeCourt()
        userPic_Side.setImageURI(Uri.parse(Session(context)?.ProfilePicUrl()))
        name_Side.text = Session(context)?.nameUser()
        locationTextHome.text = Session(context)?.getlocation()
        createTeamButton.setOnClickListener {
            Log.d("team ID is ", Session(context).getteamId())
                if (Session(context).getteamId() == "0") {
                    /*
                val count = fragmentManager.backStackEntryCount
                for (i in 0 until count) {
                    fragmentManager.popBackStack()
                } */
                    val transaction = fragmentManager.beginTransaction()
                    //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                    transaction?.replace(R.id.container, FragmentCreateTeam());
                    transaction?.addToBackStack(tag)
                    transaction?.commit();

                } else {
                    val alertDilog = AlertDialog.Builder(context).create()
                    alertDilog.setTitle("Alert")
                    alertDilog.setMessage("You already have a Team")
                    alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialogInterface, i ->
                        alertDilog.cancel()
                    })
                    alertDilog.show()
                }
            }
            joinTeamButton.setOnClickListener()
            {
                //var team=Session(context)?.getteamId()
                var team = "0"
                if (team == "0") {
                    var client = ApiCall()
                    var retrofit = client.retrofitClient()
                    val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                    var token = Session(context).gettoken()
                    var call = retrofitAp.GetAllTeams(token!!)
                    call.enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                            Log.d("server", " rohal onResponse: Server Response: " + response.toString());
                            try {
                                var json: String? = null
                                json = response?.body()!!.string()
                                Log.d("JSON", json)
                                var obj = JSONObject(json)
                                var data = obj.getJSONArray("data")
                                Log.d("DATA", data.toString())
                                var sizeOfPlayers = data.length()
                                Log.d("NUMBER OF TEAMS", sizeOfPlayers.toString())
                                var id: ArrayList<String> = ArrayList()
                                var TeamName: ArrayList<String> = ArrayList()
                                for (i in 0..sizeOfPlayers - 1) {
                                    id.add(data.getJSONObject(i).getString("_id"))
                                    /*if (data.getJSONObject(i).getString("teamName") == null) {
                                    TeamName.add("TeamName")
                                } else {
                                    TeamName.add(data.getJSONObject(i).getString("teamName"))
                                }*/
                                }
                                if (id.size < 1) {
                                    Toast.makeText(context, "Currently No Teams are Available", Toast.LENGTH_SHORT).show()
                                }
                                Log.d("IDS", id.toString())
                                // Log.d("NAMES",TeamName.toString())
                                //var teamlist=data.toString()
                                // Log.d("Array",teamlist.toString())

                                var b = Bundle()
                                b.putStringArrayList("TeamIdList", id)
                                b.putStringArrayList("TeamNameList", TeamName)
                                var fpa = FragmentJoinTeam()
                                fpa.setArguments(b)
                                val count = fragmentManager.backStackEntryCount
                                for (i in 0 until count) {
                                    fragmentManager.popBackStack()
                                }
                                val transaction = fragmentManager.beginTransaction()
                                //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                transaction?.replace(R.id.container, fpa);
                                transaction?.addToBackStack(tag)
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
                } else {
                    val alertDilog = AlertDialog.Builder(context).create()
                    alertDilog.setTitle("Alert")
                    alertDilog.setMessage("You already have a Team")
                    alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialogInterface, i ->
                        alertDilog.cancel();
                    })
                    alertDilog.show()
                }
            }
            logOut.setOnClickListener {
                initDialogBox("Are you sure You want to Logout?", "signOut")
                /*
            val alertDilog = AlertDialog.Builder(context).create()
            alertDilog.setTitle("Alert")
            alertDilog.setMessage("Are you sure You want to Logout?")
            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", {
                dialogInterface, i ->
                Session(context).setLoggedin(false,"","","wrong","","0","No Location","","","");
                removeFragment()
                val transaction = fragmentManager.beginTransaction()
                //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                transaction?.replace(R.id.container, FragmentLogin());
                transaction?.addToBackStack(tag)
                transaction?.commit();
                alertDilog.cancel();
            })
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                dialogInterface, i ->
                alertDilog.cancel();
            })
            alertDilog.show()
            */
            }
        Home_Side.setOnClickListener {
            val transaction = fragmentManager.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
            transaction?.replace(R.id.container, FragmentHome());
            transaction?.addToBackStack(tag)
            transaction?.commit();

        }
        myTeam_Side.setOnClickListener {
            HomeText.text="My Teams"
            notificationHome1.visibility=View.GONE
            Log.d("team id is ",Session(context).getteamId())
                if (Session(context).getteamId() == "0") {
                    val fragment = FragmentCreateTeam()
                    //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                    fragmentManager.beginTransaction()
                            ?.replace(R.id.container, fragment)
                            ?.addToBackStack(tag)
                            ?.commit()
                    //Container.closeDrawer(Gravity.START);
                } else {
                    var client = ApiCall()
                    var retrofit = client.retrofitClient()
                    val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                    var token = Session(context).gettoken()
                    var teamId = Session(context)?.getteamId()
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
                                Log.d("JSON", json)
                                var obj = JSONObject(json)
                                var data = obj.getJSONObject("data")
                                Log.d("DATA", data.toString())
                                var captainName = data.getString("captainName")
                                var teamName = data.getString("teamName")
                                var teamId = data.getString("_id")
                                Log.d("CAPTAIN", captainName)
                                Log.d("TeamName", teamName)
                                Log.d("TeamId", teamId)



                                val fpa = FragmentMyTeam()
                                val b = Bundle()
                                b.putString("captain", captainName)
                                b.putString("teamName", teamName)
                                b.putString("teamId", teamId)
                                fpa.setArguments(b)

                                val fm = fragmentManager
                                val transaction = fm?.beginTransaction();
                                //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                transaction?.replace(R.id.top_Home2, fpa);
                                joinTeamButton.visibility = View.GONE
                                createTeamButton.visibility = View.GONE
                                currentMatch.visibility = View.GONE
                                transaction?.addToBackStack("abc");
                                transaction?.commit();
                                //Container.closeDrawer(Gravity.START);
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
            }
        }

         fun initDialogBox(message: String, type: String) {
            val alertDilog = AlertDialog.Builder(context).create()
            alertDilog.setTitle("Alert")
            alertDilog.setMessage(message)
            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", { dialogInterface, i ->
                if (type.equals("signOut")) {
                    Session(context).setLoggedin(false, "", "", "wrong", "", "0", "No Location", "", "", "");
                    val transaction = fragmentManager.beginTransaction()
                    //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                    transaction?.replace(R.id.container, FragmentLogin());
                    transaction?.addToBackStack(tag)
                    transaction?.commit();
                    alertDilog.cancel();
                }
                Log.d("init Box Status", status.toString())
                alertDilog.cancel()
            })
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", { dialogInterface, i ->
                alertDilog.cancel();
            })
            alertDilog.show()
        }


}
