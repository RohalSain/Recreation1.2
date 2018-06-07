package com.developer.rohal.recreationcenteer.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Api.TeamPlayer
import com.developer.rohal.recreationcenteer.Helper.ListArrayString
import com.developer.rohal.recreationcenteer.R
import com.developer.rohal.recreationcenteer.fragment.FragmentCreateTeam
import kotlinx.android.synthetic.main.adpater_players_add.view.*
import org.json.JSONArray


class AddPlayers(context: Context, list_player: JSONArray, idarray1:String, ctx: Context, fpa:FragmentCreateTeam): RecyclerView.Adapter<AddPlayers.ViewHolder>()
{
    val intent: Intent = Intent()
    var context: Context?=null
    var list_player: JSONArray? = null
    private var session: Session? = null
    private var teamPlayer: TeamPlayer? = null
    var ctx: Context?=null
    var dataid:String?=null
    var fragment:FragmentCreateTeam
    var ImagePath:String  = "http://139.59.18.239:6009/basketball/"
    var idArray:String=""
    var tId:String?=null
    var i:Int=0
    init {
        this.context=context
        this.ctx=ctx
        this.list_player=list_player
        this.session = Session(context)
        this.teamPlayer= TeamPlayer(ctx)
        this.fragment=fpa
        this.tId=null
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.adpater_players_add,parent,false)
        return AddPlayers.ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list_player!!.length()
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var dataobj = list_player!!.getJSONObject(position)
        ListArrayString.instance.context = context
        var dataobjname = dataobj.getString("name")
        dataid = dataobj.getString("_id")
        Log.d("ID",dataid)
        var  las:Array<String>?=null
        holder?.t1?.text=dataobjname
        var datPic =ImagePath+dataobj.getString("profilePic")
        holder?.img?.setImageURI(Uri.parse(datPic))
        holder?.b1?.setOnClickListener()
        {
            holder?.b1?.setText(R.string.requested)
            holder?.b1?.setBackgroundColor(Color.TRANSPARENT)
            holder?.b1?.setTextColor(Color.BLACK)
            holder?.b1?.isEnabled=false
            Log.d("CONTEXTgklj",context.toString())
            Log.d("datasetgklj",dataid.toString())
            ListArrayString.instance.context=context
            ListArrayString.instance.LAS.add(dataid!!)
            Log.d("CONTEXT",ListArrayString.instance.context.toString())
            Log.d("LISTA", ListArrayString.instance.LAS.toString())
            Log.d("IDARRAY","$idArray")
            teamPlayer?.setPlayersList(idArray)
            intent.putExtra("players",idArray)
            intent.setAction("action1");
            context?.sendBroadcast(intent)

            val bundle = Bundle()
            bundle.putString("ids",idArray)
            Log.i("BUNDLE", bundle.toString())
            fragment.setArguments(bundle)


        }

    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val t1 = itemView.addPlayerNAme as TextView
        val b1 = itemView.addPlayerButton
        val img = itemView.addPlaerPic
        val v1 = itemView.viewAddPlayer as View

    }
}