package com.developer.rohal.recreationcenteer.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.developer.rohal.recreationcenteer.R
import kotlinx.android.synthetic.main.fragment_join_team.*

class FragmentJoinTeam : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_team, container, false)
        //var  joinList = b?.getStringArrayList("TeamIdList")
       // Log.d("LIST_PLAYER",joinList.toString())
        listJoinTeams.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        //val adp1=CustomAdapter_JoinTeam(context,joinList!!)
        //listJoinTeams.adapter=adp1
        Back_HomeJoinTeam.setOnClickListener()
        {

            val fm = fragmentManager
            val count = fm.backStackEntryCount

            fm.popBackStack()

        }
    }


}
