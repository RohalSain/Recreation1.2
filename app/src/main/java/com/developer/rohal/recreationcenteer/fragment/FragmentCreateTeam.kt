package com.developer.rohal.recreationcenteer.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.rohal.recreationcenteer.Api.ApiCall
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.Api.TeamPlayer
import com.developer.rohal.recreationcenteer.Helper.ListArrayString
import com.developer.rohal.recreationcenteer.Interface.RedditAPI

import com.developer.rohal.recreationcenteer.R
import com.example.emilence.recreationcenter.TeamType
import kotlinx.android.synthetic.main.fragment_create_team.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentCreateTeam : Fragment() {
    lateinit var mCapturedImageURI: Uri
    val PICK_FROM_FILE = 1
    val REQUEST_IMAGE_CAPTURE = 2
    lateinit var bmp: Bitmap
    var bmpuri: Bitmap?=null
    private var currentImageUri: Uri? = null
    var currentImagePath: String? = null
    var imagePath: File? = null
    var basic=""
    private var session: Session? = null
    lateinit var teamType: TeamType
    private var teamplayer: TeamPlayer? = null
    lateinit var dataId:Any
    var listp:String=""
    var al:Array<String>?=null
    var i=0

    class MyReceiver: BroadcastReceiver()
    {   var listp1:String=""
        override fun onReceive(context: Context?, intent: Intent?)
        {
            listp1=intent?.getStringExtra("players")!!
            Log.d("ON RECEIVE:",listp1)

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ctext.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Regular.otf"))
        createTeamName.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        playerText.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        player_1.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        capText.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        addCreate.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Semibold.otf"))
        //var load = Loader()
        session =  Session(context);
        teamplayer= TeamPlayer(context)
        ListArrayString.instance.context=context
        Log.d("LISTA1", ListArrayString.instance.LAS.toString())
        player_1.text=session?.nameUser()
        var obj = MyReceiver()
        if (al!=null) {
            for (item:String in al!!)
            {
                Log.d("ITEM",item)
            }
        }
        Back_HomeCreateTeam.setOnClickListener()
        {
            val fm = fragmentManager
            val count = fm.backStackEntryCount
            fm.popBackStack()
        }
        createTeamPic.setOnClickListener()
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Select Picture")
            val country_Name: Array<String> = arrayOf("CAMERA", "GALLERY")
            builder.setItems(country_Name, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0)
                {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (savedInstanceState != null)
                    {
                        currentImagePath = savedInstanceState.getString("currentImagePath")
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
                        // set the image file name
                        if (takePictureIntent.resolveActivity(context?.packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                        dialog.cancel()
                    }
                } else {
                    val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(intent, PICK_FROM_FILE)
                    dialog.cancel()
                }
            })
                    .create()
                    .show()
        }
        createTeamButton_lay.setOnClickListener()
        {
            var client=ApiCall()
            var retrofit=client.retrofitClient()
            val retrofitAp = retrofit!!.create(RedditAPI::class.java)
            var token = Session(context)?.gettoken()
            var name =Session(context)?.nameUser()
            var profileUrl= Session(context)?.ProfilePicUrl()
            var court=Session(context)?.gethomeCourt()
            var teamId = Session(context)?.getteamId()
            val teamName = createTeamName.text.toString().trim()
            var teamImage: String = "teamImage\";filename=\"${FilePicture()?.absolutePath}"
            Log.d("token","${token}")
            var array:ArrayList<String>  = ListArrayString.instance.LAS
            var requestBody: RequestBody
            var hashMap: LinkedHashMap<String, RequestBody> = LinkedHashMap<String, RequestBody>();
            var string="["
            for(i in 0..array.size-1) {
                string+= "\""+array.get(i)+"\""
                if (i==array.size-1){
                    string+="]"
                }else{
                    string+=","
                }
            }
            if(teamName.length<1)
            {
                Toast.makeText(context,"enter Team Name", Toast.LENGTH_SHORT ).show()
            }else {
                //load.ShowLoader(context)
                hashMap.put("teamName", RequestBody.create(MediaType.parse("text/plain"), teamName))
                hashMap.put(teamImage, RequestBody.create(MediaType.parse("/Images"), FilePicture()))
                hashMap.put("players", RequestBody.create(MediaType.parse("text/plain"), string))
                hashMap.put("court", RequestBody.create(MediaType.parse("text/plain"),court))
                var call = retrofitAp.createTeam(token!!, hashMap)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());
                        try {
                            var json: String? = null
                            json = response?.body()!!.string()
                            Log.d("JSON", json)
                            var jsonobj = JSONObject(json)
                            var m = jsonobj.getString("message")
                            var s = jsonobj.getString("success")
                            var homeCourt=Session(context).gethomeCourt()
                            if (s == "0" && m == "You already have a team.") {
                                Toast.makeText(context, m, Toast.LENGTH_SHORT).show()
                                //load.HideLoader()
                            } else if (s == "1") {
                                var acc = jsonobj.getJSONObject("data")
                                teamId = acc.getString("_id")
                                val location:String=Session(context)!!.getlocation()
                                Session(context)!!.setLoggedin(true, name, profileUrl, token,homeCourt, teamId,location,session?.getuserId(),session?.getemailId(),session?.getphoneNumber())
                                Toast.makeText(context,"Team Created", Toast.LENGTH_SHORT).show()
                                //load.HideLoader()
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                                //load.HideLoader()
                            }
                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: " + e);
                            //load.HideLoader()
                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            //load.HideLoader()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        //load.HideLoader()
                        Log.e("OnFailure", "onFailure: Something went wrong:")
                        Log.d("Failure", "onResponse: Failure Response: " + t.toString());
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
            }
        }
        addCreate.setOnClickListener()
        {
            var client= ApiCall()
            var retrofit=client.retrofitClient()
            val retrofitAp = retrofit!!.create(RedditAPI::class.java)
            var token = Session(context)?.gettoken()
            var call = retrofitAp.GetAllPlayers(token!!)
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());
                    try {
                        var json:String?=null
                        json= response?.body()!!.string()
                        Log.d("JSON",json)
                        var obj= JSONObject(json)
                        var data=obj.getJSONArray("data")
                        Log.d("DATA",data.toString())
                        var sizeOfPlayers=data.length()
                        Log.d("NUMBER OF PLAYERS",sizeOfPlayers.toString())
                        for(i in 0..sizeOfPlayers-1)
                        {
                            var dataobj = data.getJSONObject(i)
                            Log.d("DATAOBJ",dataobj.toString())
                            var dataobjname = dataobj.getString("name")
                            Log.d("NAME",dataobjname)
                            dataId=dataobj.get("_id")
                            Log.d("IDs",dataId.toString())
                        }
                        val fpa = FragmentAddPlayers()
                        val b = Bundle()
                        b.putString("playerlist", data.toString())
                        fpa.setArguments(b)
                        val fm = fragmentManager
                        val transaction = fm?.beginTransaction();
                        //transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                        transaction?.replace(R.id.container,fpa);
                        transaction?.addToBackStack(tag)
                        transaction?.commit();
                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: " + e);
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ")
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            })
        }
        /*

        */
    }
    fun getImageFileUri(): Uri?
    {
        imagePath = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Tuxuri");
        if (!imagePath!!.exists())
        {
            if (!imagePath!!.mkdirs())
            {

                return null;
            } else
            {
                // Log.d("create new Tux folder");
            }
        }
        //Creating an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        val image: File = File(imagePath, "TUX_" + timeStamp + ".jpg");
        if (!image.exists())
        {
            try
            {
                image.createNewFile();
            } catch (e: IOException)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Uri.fromFile(image);
    }
    //Function for Converting Uri into File
    fun FilePicture(): File
    {
        var f: File = File(context?.cacheDir, "Saii")
        f.createNewFile()
        //Convert bitmap to byte array
        var bitmap: Bitmap? = bmpuri
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();
        //write the bytes in file
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState?.putString("currentImagePath", currentImageUri?.getPath());
    }

    //Overriding Function to Handling Dialog Box Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bundle: Bundle = data!!.extras
            bmp = bundle.get("data") as Bitmap
            bmpuri = bmp
            createTeamPic.setImageURI(Uri.fromFile(FilePicture()))
        } else if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK) {
            try {
                mCapturedImageURI = data!!.data
                createTeamPic.setImageURI(mCapturedImageURI)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, mCapturedImageURI)
                bmpuri = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Image_notfound", Toast.LENGTH_LONG).show()
            }
        }
    }
}
