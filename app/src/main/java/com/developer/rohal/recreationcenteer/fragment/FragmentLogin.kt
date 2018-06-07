package com.developer.rohal.recreationcenteer.fragment


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Interface.RedditAPI
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Helper.GeneralMethod

import com.developer.rohal.recreationcenteer.R
import com.example.emilence.recreationcenter.PojoData
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.lang.IllegalStateException
import java.util.regex.Pattern

class FragmentLogin : Fragment() {
    var mview: View? = null
    var team="0"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mview =  inflater!!.inflate(R.layout.fragment_login, container, false)
        return mview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTypeface()
        SignUp_Button.setOnClickListener {
            it.hideKeyboard()
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    ?.replace(R.id.container, FragmentSignUp())
                    ?.addToBackStack("SignUp Page")
                    ?.commit()
        }
        Login_Button.setOnClickListener {
           it.hideKeyboard()
           val headerMap = HashMap<String, String>()
            HashMap<String, String>()["Content-Type"] = "application/json"
            if(UserName_Text.text.toString().isEmpty()) {
                Toast.makeText(context, "Enter email address", Toast.LENGTH_SHORT).show()
            }
            else if(!validEmail(UserName_Text.text.toString())) {
                Toast.makeText(context, "Enter valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(Password_Text.text.toString().isEmpty()) {
                Toast.makeText(context,"Enter Password", Toast.LENGTH_SHORT).show()
            }
            else {
                loadingLogin.visibility = View.VISIBLE
                apiCall(
                        mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.login(headerMap,
                        PojoData(UserName_Text.text.toString(),Password_Text.text.toString())
                ), "login")
            }

            }
        RecreationLogo.setOnClickListener {
            it.hideKeyboard()
        }
        forgot.setOnClickListener {
            it.hideKeyboard()
        }
        }

    private fun initTypeface() {
        logoText.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/New Athletic M54.ttf")
        UserName_Text.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/arial.ttf")
        Password_Text.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/arial.ttf")
        Login_Button.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/arialbd.ttf")
        SignUp_Button.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/arialbd.ttf")
        forgotPassword.typeface = Typeface.createFromAsset(activity?.assets ,"fonts/ariali.ttf")
    }

    private fun apiCall(it: View, call: Call<ResponseBody>?, s: String) {
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                Log.d("Api Dashboard", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ", "${json}")
                    if (json == null) {
                        Toast.makeText(context,"Error While Connnecting", Toast.LENGTH_SHORT).show()
                    } else {
                        if (s.equals("login")) {
                            Log.d("Api(login)", "Server Response: " + response.toString());
                            if (JSONObject(json).get("success").toString().toInt() == 1) {
                                if (JSONObject(json).get("message").toString().equals("Request successful")) {
                                    Log.d("Data  ",JSONObject(json).toString())
                                    try {
                                        team = JSONObject(json).getJSONObject("account").getString("team")
                                    }
                                    catch (e:JSONException)
                                    {
                                        Log.d("Jex",e.toString())
                                    }
                                    catch (e:IOException) {
                                        Log.d("Ioex", e.toString())
                                    }
                                    if (team == "0")
                                    {
                                        Session(context).setLoggedin(
                                                true,
                                                JSONObject(json).getJSONObject("account").getString("name"),
                                                "http://139.59.18.239:6009/basketball/" + JSONObject(json).getJSONObject("account").getString("profilePic"),
                                                JSONObject(json).getString("token"),
                                                JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                                "0",
                                                "",
                                                JSONObject(json).getJSONObject("account").getString("_id"),
                                                JSONObject(json).getJSONObject("account").getString("username"),
                                                JSONObject(json).getJSONObject("account").getString("mobileNumber")
                                        )
                                        fragmentManager?.beginTransaction()
                                                ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                                ?.replace(R.id.container, FragmentHome())
                                                ?.addToBackStack("dashboard")
                                                ?.commit()
                                    }
                                    else
                                    {
                                        Session(context).setLoggedin(true,
                                                JSONObject(json).getJSONObject("account").getString("name"),
                                                "http://139.59.18.239:6009/basketball/" + JSONObject(json).getJSONObject("account").getString("profilePic"),
                                                JSONObject(json).getString("token"),
                                                JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                                JSONObject(json).getJSONObject("account").getString("team"),
                                                "",
                                                JSONObject(json).getJSONObject("account").getString("_id"),
                                                JSONObject(json).getJSONObject("account").getString("username"),
                                                JSONObject(json).getJSONObject("account").getString("mobileNumber"));
                                        fragmentManager?.beginTransaction()
                                                ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                                ?.replace(R.id.container, FragmentHome())
                                                ?.addToBackStack("dashboard")
                                                ?.commit()
                                    }
                                    Log.d("Detail ",Session(context).detail.toString())
                                }
                                }
                                }
                    }

                    loadingLogin.visibility = View.INVISIBLE
                } catch (e: JSONException) {
                    Toast.makeText(context,"Error Occurred While Connecting", Toast.LENGTH_SHORT).show()
                    loadingLogin.visibility = View.INVISIBLE
                } catch (e: IOException) {
                    Toast.makeText(context,"Error Occurred While Connecting", Toast.LENGTH_SHORT).show()
                    loadingLogin.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context,"Error Occurred While Connecting", Toast.LENGTH_SHORT).show()
                loadingLogin.visibility = View.INVISIBLE
            }
        })
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun validEmail(email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
