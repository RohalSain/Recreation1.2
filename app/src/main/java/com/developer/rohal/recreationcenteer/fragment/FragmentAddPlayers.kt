package com.developer.rohal.recreationcenteer.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.rohal.recreationcenteer.Adapter.AddPlayers

import com.developer.rohal.recreationcenteer.R
import com.developer.rohal.recreationcenteer.R.id.Back_CreatePlayer
import com.developer.rohal.recreationcenteer.R.id.listPlayersAdd
import kotlinx.android.synthetic.main.fragment_add_players.*
import org.json.JSONArray

class FragmentAddPlayers : Fragment() {
    var plsize: Int? = null
    var idarray: String = ""
    var ids: String = ""
    var c: Bundle? = null
    var ctx: Context? = null
    val fpa = FragmentCreateTeam()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_players, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val b = arguments
        var listplayer = b?.getString("playerlist")
        Log.d("LIST_PLAYER", listplayer.toString())
        var pl = JSONArray(listplayer)
        Log.d("LIST_OBJECT", pl.toString())
        plsize = pl.length()
        Log.d("LIST_SIZE", plsize.toString())
        ctx = activity.applicationContext
        listPlayersAdd.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1 = AddPlayers(context, pl, idarray, ctx!!, fpa)
        listPlayersAdd.adapter = adp1
        Back_CreatePlayer.setOnClickListener()
        {

            val fm = fragmentManager
            val count = fm.backStackEntryCount
            var c = count - 1

            fm.popBackStack()


        }
    }
}

