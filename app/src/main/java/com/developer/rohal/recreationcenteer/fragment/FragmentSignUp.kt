package com.developer.rohal.recreationcenteer.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Helper.LocationClass
import com.developer.rohal.recreationcenteer.Helper.Support
import com.developer.rohal.recreationcenteer.Interface.RedditAPI

import com.developer.rohal.recreationcenteer.R
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.dialog_pic.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap
import java.util.regex.Pattern

class FragmentSignUp : Fragment() {
    private lateinit var mView: View
    private lateinit var mCapturedImageURI: Uri
    private val lowQualityImg = 3
    private val GetCode = 4
    private val pickFromGallery = 1
    private lateinit var bmp: Bitmap
    private lateinit var bmpuri: Bitmap

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView =  inflater!!.inflate(R.layout.fragment_sign_up, container, false)
        return mView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("Court is ",Support.instance.SignUpCourt)
        initTypeface()
        userpic.setOnClickListener {
            val view: View = layoutInflater.inflate(R.layout.dialog_pic, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(view)
            val Dilog: Dialog = builder.create()
            Dilog.show()
            view.btn_Gallery.setOnClickListener {
                gallery()
                builder.setInverseBackgroundForced(true)
                Dilog.dismiss()
            }
            view.btn_Camera.setOnClickListener {
                dispatchTakePictureIntentWithLowQuality()
                Dilog.dismiss()
            }

        }
        signUp_Submit.setOnClickListener {
            if (Name_Text.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show()
            } else if (Email_Text.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
            }else if (!validEmail(Email_Text.text.toString().trim())) {
                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
            }
            else if (SignUp_Password_Text.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show()
            }
            else if (SignUp_Password_Text.text.toString().trim().length < 6) {
                Toast.makeText(context, "Password should be of minimum 6 characters", Toast.LENGTH_SHORT).show()
            }else if (SignUp_Confirm_Password_Text.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter confirm Password", Toast.LENGTH_SHORT).show()
            }
            else if (SignUp_Password_Text.text.toString().trim() != SignUp_Confirm_Password_Text.text.toString().trim()) {
                Toast.makeText(context, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show()
            } else if (PhoneNumber.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            }
            else if (PhoneNumber.text.toString().trim().length < 10 || PhoneNumber.text.toString().trim().length > 10) {
                Toast.makeText(context, "Phone number should be 10 digits only", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var headmap=initApi()
                Support.instance.headerMap = headmap
                fragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        ?.replace(R.id.container, FragmentSelectGynasium())
                        ?.addToBackStack("dashboard")
                        ?.commit()
            }
        }
        BackLoggin_Button.setOnClickListener {
            activity.onBackPressed()
        }
    }

    private fun initApi(): HashMap<String, RequestBody> {
        val headerMap = HashMap<String, RequestBody>()
        headerMap["email"] = RequestBody.create(MediaType.parse("text/plain"), Email_Text.text.toString().trim())
        headerMap["password"] = RequestBody.create(MediaType.parse("text/plain"), SignUp_Password_Text.text.toString().trim())
        headerMap["confirmPassword"] = RequestBody.create(MediaType.parse("text/plain"), SignUp_Password_Text.text.toString().trim())
        headerMap["name"] = RequestBody.create(MediaType.parse("text/plain"), Name_Text.text.toString().trim())
        headerMap["profilePic\";filename=\"${FilePicture().absolutePath}"] = RequestBody.create(MediaType.parse("/Images"), FilePicture())
        headerMap["mobileNumber"] = RequestBody.create(MediaType.parse("text/plain"), PhoneNumber.text.toString().trim())
        //headerMap.put("homeCourt", RequestBody.create(MediaType.parse("text/plain"),LocationClass.instance.loc))
        //apiCall(mView!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.registerUser(headerMap), "signUp")
        return headerMap
    }

    private fun initTypeface() {
        Name_Text.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf")
        Email_Text.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf")
        SignUp_Password_Text.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf")
        SignUp_Confirm_Password_Text.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf")
        PhoneNumber.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf")
        signUp_Submit.typeface = Typeface.createFromAsset(activity?.assets, "fonts/arialbd.ttf")
    }

    private fun gallery() {
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                pickFromGallery
        )
    }


    private fun dispatchTakePictureIntentWithLowQuality() {
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), lowQualityImg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == lowQualityImg) {
            bmp = data!!.extras.get("data") as Bitmap
            bmpuri = bmp
            userpic.invalidate()
            userpic.setImageURI(Uri.fromFile(FilePicture()))
            LocationClass.instance.uri = Uri.fromFile(FilePicture())


        }
        else if (requestCode == pickFromGallery && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                mCapturedImageURI = data!!.data
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, mCapturedImageURI)
                bmpuri = bitmap
                userpic.invalidate()
                userpic.setImageURI(Uri.fromFile(FilePicture()))
                LocationClass.instance.uri = Uri.fromFile(FilePicture())
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Image_notfound", Toast.LENGTH_LONG).show()
            }

        }
        else if(requestCode == GetCode && resultCode == AppCompatActivity.RESULT_OK) {
            // do anything
            Log.d("Data","Hello How are you")
        }
    }

    fun FilePicture(): File {
        val f: File = File(context?.cacheDir, bmpuri.toString())
        f.createNewFile()
        val bitmap: Bitmap? = bmpuri
        val bos= ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray();
        val fos= FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }
    private fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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
                        Log.d("Api Like Hit ", " Error Ocuured")
                    } else {
                        if (s.equals("signUp")) {
                            Log.d("Api(login)", "Server Response: " + response.toString());
                            if (JSONObject(json).get("success").toString().toInt() == 1) {
                                if (JSONObject(json).get("message").toString() == "Registration successful") {
                                        Session(context).setLoggedin(true,
                                                JSONObject(json).getJSONObject("account").getString("name"),
                                                "http://139.59.18.239:6009/basketball/" + JSONObject(json).getJSONObject("account").get("profilePic"),
                                                JSONObject(json).getString("token"),
                                                JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                                "0",
                                                JSONObject(json).getJSONObject("account").getString("homeCourt"),
                                                JSONObject(json).getJSONObject("account").getString("_id"),
                                                JSONObject(json).getJSONObject("account").getString("username"),
                                                JSONObject(json).getJSONObject("account").getString("mobileNumber")
                                        )
                                    fragmentManager?.beginTransaction()
                                            ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                            ?.replace(R.id.container, FragmentLogin())
                                            ?.addToBackStack("dashboard")
                                            ?.commit()
                                }
                            }
                        }
                    }


                } catch (e: JSONException) {
                    Log.e("Api Dashboard", "Server Response(Json Exception Occur): " + e)
                } catch (e: IOException) {
                    Log.e("Api Dashboard", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Dashboard", "Server Response(On Failure): " + t)
            }
        })
    }
}
