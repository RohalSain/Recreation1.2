package com.developer.rohal.recreationcenteer.Helper

import android.content.Context
import android.widget.CheckBox
import com.example.emilence.recreationcenter.PojoClasses.JoinTeamDetail
import com.example.emilence.recreationcenter.PojoClasses.NewPlayers
import com.example.emilence.recreationcenter.PojoClasses.Players

class Helper private constructor() {
    init {

    }

    private object Holder {
        val INSTANCE = Helper()
    }

    companion object {
        val instance:Helper by lazy { Holder.INSTANCE }
    }
    var totalTeam :ArrayList<JoinTeamDetail> = ArrayList()
    var bechPlayers :ArrayList<Players> = ArrayList()
    var PlayingPlayers :ArrayList<Players> = ArrayList()
    var username :String = " "
    var CaptName :String = " "
    var CaptId : String = " "
    var teamId :String = " "
    var Usertoken :String = " "
    var frag :String = " "
    var newPlayers :ArrayList<NewPlayers> = ArrayList()
    var teamIdSecondOne :String = " "
    var BoxPosition :Int?= null
    var ChangeCaptain :Boolean = false
    var ChangeCaptainStatus :Boolean = false
    var IsCaptain :Boolean = false
}