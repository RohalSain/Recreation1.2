package com.developer.rohal.recreationcenteer.Api;

import android.content.Context;
import android.content.SharedPreferences;

public class TeamPlayer {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    public TeamPlayer(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("MyPref", 0);
        editor = prefs.edit();
    }
    public void setPlayersList(String listP) {
        editor.putString("PlayerList", listP);
    }
    public String getPlayerList()
    {
        return prefs.getString("PlayerList"," ");
    }

}