package com.developer.rohal.recreationcenteer.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.rohal.recreationcenteer.R
import kotlinx.android.synthetic.main.list_location_layout_sign_up.view.*

import org.json.JSONArray

class CourtGetSignUp(context: Context,n:Int,lName:ArrayList<String>,data:String): RecyclerView.Adapter<CourtGetSignUp.ViewHolder>() {
   var s:Int?=null
    var ctx:Context?=null
    var str:String?=null
    var data1:JSONArray = JSONArray()
    var courtId:ArrayList<String> = ArrayList()
    var locationName:ArrayList<String>?=null
    init {
        this.s=n
        this.locationName=lName
        this.str=data
        this.ctx=context
        this.data1= JSONArray(str)
        this.courtId = ArrayList()

    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent?.context).inflate(R.layout.list_location_layout_sign_up,parent,false)
        return ViewHolder(v)   }

    override fun getItemCount(): Int {
        return s!!
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.t1?.text=locationName?.get(position)
        var arr = data1.getJSONObject(position).getJSONArray("courts")
        var size=arr.length()

        holder?.cl?.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1=CourtListFilterOnClickEvent(ctx!!,size,arr.toString(),str!!,position)
        holder?.cl?.adapter=adp1
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val t1=itemView.locationName
        val cl=itemView.listCourt

    }
}