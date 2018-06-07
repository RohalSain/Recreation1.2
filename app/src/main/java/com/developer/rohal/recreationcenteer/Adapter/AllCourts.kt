package com.developer.rohal.recreationcenteer.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.rohal.recreationcenteer.R
import com.example.emilence.recreationcenter.PojoCourtsDetail
import kotlinx.android.synthetic.main.list_gym.view.*
import kotlinx.android.synthetic.main.list_location_layout_sign_up.view.*

import org.json.JSONArray

class AllCourts(list:ArrayList<PojoCourtsDetail>,val listener: OnItemClickListener): RecyclerView.Adapter<AllCourts.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    var list:ArrayList<PojoCourtsDetail> ?=null
    init {
           this.list = list
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent?.context).inflate(R.layout.list_gym,parent,false)
        return ViewHolder(v)   }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.locationName?.text = list?.get(position)?.gymName
            holder?.click?.setOnClickListener {
              listener.onItemClick(position)
            }
            var dist: Double? = list?.get(position)?.distance?.toDouble()
            //holder?.distance?.text = list?.get(position)?.distance+" meter  away"
            var km = dist!! / 1000.0
            if(dist.div(1000.0).toInt()==0) {
                holder?.distance?.text = dist.toInt().toString()+" meter  away"
            }

            holder?.distance?.text = km.toString()+" km  away"
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var locationName =  itemView.gymName
        var click = itemView.click
        var distance = itemView.distance
    }
}