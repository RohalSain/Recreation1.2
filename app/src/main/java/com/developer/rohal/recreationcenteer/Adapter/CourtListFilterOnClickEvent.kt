package com.developer.rohal.recreationcenteer.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Helper.LocationClass
import com.developer.rohal.recreationcenteer.Helper.Support
import com.developer.rohal.recreationcenteer.Interface.RedditAPI
import com.developer.rohal.recreationcenteer.R
import kotlinx.android.synthetic.main.list_court_layout.view.*
import kotlinx.android.synthetic.main.list_location_layout_sign_up.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
class CourtListFilterOnClickEvent(context: Context, s:Int, d:String, obj:String, i:Int): RecyclerView.Adapter<CourtListFilterOnClickEvent.ViewHolder>() {
var size:Int?=null
    var pos:Int?=null
    var ctx:Context?=null
    var str:String?=null
    var session:Session?=null
    var data:JSONArray = JSONArray()
    var dataobj:JSONArray= JSONArray()
    var datastr:String?=null
    //var load:Loader?=null
    init {
        this.size=s
        this.str=d
        this.pos=i
        this.data= JSONArray(str)
        this.datastr=obj
        this.dataobj= JSONArray(datastr)
        this.ctx=context
        this.session=Session(ctx)
        //this.load= Loader()
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent?.context).inflate(R.layout.list_court_layout,parent,false)
        return ViewHolder(v)   }
    override fun getItemCount(): Int {
        return size!!
    }
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.b1?.text=data.getJSONObject(position).getString("courtId")
        holder?.b1?.setOnClickListener()
        {
            Support.instance.SignUpCourt = dataobj.getJSONObject(pos!!).getString("location")
            LocationClass.instance.context=ctx
            LocationClass.instance.loc=dataobj.getJSONObject(pos!!).getString("location")
            Log.d("LOCATION SELECTED",LocationClass.instance.loc)
            if(session?.loggedin()==true)
            {
                var token=session?.gettoken()
                var team=session?.getteamId()
                var homeCourt=data.getJSONObject(position).getString("courtId")
                //load?.ShowLoader(ctx!!)
                var call = ApiCall().retrofitClient()!!.create(RedditAPI::class.java).AddHomeCourt(token!!,homeCourt!!)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());
                        try {
                            var json: String? = null
                            json = response?.body()!!.string()
                            Log.d("JSON", json)
                            var jsonobj=JSONObject(json)
                            Log.d("JSONOBJ",jsonobj.toString())
                            if(jsonobj.getString("success")=="1")
                            {
                                var acc=jsonobj.getJSONObject("account")
                                Log.d("ACCOUNT",acc.toString())
                                val username: String = acc.getString("username")
                                val name: String = acc.getString("name")
                                var profile = acc.getString("profilePic")
                                var homeCourt=acc.getString("homeCourt")
                                var id=session?.getuserId()
                                Log.d("pic", "${profile}")
                                var profileUrl = "http://139.59.18.239:6009/basketball/" + profile
                                session!!.setLoggedin(true, name, profileUrl, token, homeCourt, team,dataobj.getJSONObject(pos!!).getString("location"),id,session?.getemailId(),session?.getphoneNumber());
                                //load?.HideLoader()
                                Toast.makeText(ctx,"COURT is selected",Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: JSONException) {
                            Log.d("JSONEXCEPTION", e.toString())
                            //load?.HideLoader()
                        } catch (e: Exception) {
                            Log.d("EXECEPTION", e.toString())
                            //load?.HideLoader()
                        }
                    }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                           // load?.HideLoader()
                            Log.e("OnFailure", "onFailure: Something went wrong: ")
                            Toast.makeText(ctx, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    })

                        }
        }

    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val b1=itemView.courtButton
        val v1=itemView.viewCourt
    }
}