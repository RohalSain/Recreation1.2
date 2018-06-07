package com.developer.rohal.recreationcenteer.Api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by emilence on 29/3/18.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void setLoggedin(boolean logggedin,String name,String url, String token,String homeCourt,String teamId,String location,String userId,String emailId,String phoneNumber){
        editor.putBoolean("loggedInmode",logggedin);
        editor.putString("name",name);
        editor.putString("url",url);
        editor.putString("token",token);
        editor.putString("homeCourt,",homeCourt);
        editor.putString("teamId",teamId);
        editor.putString("location",location);
        editor.putString("userId",userId);
        editor.putString("emailId",emailId);
        editor.putString("phoneNumber",phoneNumber);
        editor.commit();
    }
    public String getDetail()
    {
        //return prefs.getString("url","empty");
        Boolean status = prefs.getBoolean("loggedInmode",false);
        String name = prefs.getString("name","no name");
        String url = prefs.getString("url","no url");
        String token = prefs.getString("token","no token");
        String homeCourt = prefs.getString("homeCourt", " no homeCourt");
        String teamId = prefs.getString("teamId", " no team Id");
        String location = prefs.getString("location", " no location");
        String userId = prefs.getString("userId"," no userId");
        String emailId = prefs.getString("emailId"," no emial Id");
        String phoneNumber = prefs.getString("phoneNumber","no Phone Number");
        return " status "+ status+" url "+url+" name "+name+" token "+token+" Homecourt"+homeCourt+" teamId "+teamId+" location "+location+" userId "+userId +" emailId "+emailId+" Phone Number"+phoneNumber;

    }
    public String ProfilePicUrl()
    {
        return prefs.getString("url","empty");
    }
    public String nameUser()
{
    return prefs.getString("name","user");
}
    public String gettoken()
    {
        return prefs.getString("token","");
    }
    public String gethomeCourt()
    {
        return prefs.getString("homeCourt","");
    }
    public String getteamId() {
        return prefs.getString("teamId", "0");
    }
    public String getlocation(){ return prefs.getString("location","No Location");}
    public String getuserId(){ return prefs.getString("userId", "");}
    public String getemailId(){ return  prefs.getString("emailId","empty");}
    public  String getphoneNumber(){ return prefs.getString("phoneNumber","empty");}
    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

}
