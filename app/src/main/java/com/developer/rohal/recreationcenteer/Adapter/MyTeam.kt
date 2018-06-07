package com.developer.rohal.recreationcenteer.Adapter
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.R
import kotlinx.android.synthetic.main.my_team_list.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.HashMap

class MyTeam(var ctx:Context, var teamName:String?, var tId:String?, capId:String?, val listener: OnItemClickListener): RecyclerView.Adapter<MyTeam.ViewHolder>()
{
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    var tName:String?=null
     var teamId:String?=null
    var c:Context?=null
    var cId:String?=null
    var session:Session?=null
    var id:String?=null
    init {
        this.tName=teamName
        this.teamId=tId
        this.c=ctx
        this.cId=capId
        this.session= Session(c)
        this.id=session?.getuserId()

    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.my_team_list,parent,false)

        return MyTeam.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1
         }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var num = 1
        num = num + position
        holder?.t1?.text=tName
      holder?.t1?.setOnClickListener()
      {


                      listener.onItemClick(position)



      }
    }

    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView)
    {
        val t1 = itemView.myTeam_Text as TextView

    }

}