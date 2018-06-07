package com.developer.rohal.recreationcenteer.Interface

import com.example.emilence.recreationcenter.PojoData
import com.example.emilence.recreationcenter.TeamType
import com.example.emilence.recreationcenter.poho
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.http.POST
import retrofit2.http.Multipart

interface RedditAPI
{
    @Headers("Content-Type: application/json")
    @POST("/v1/account/login")
    fun login(
            @HeaderMap headers: Map<String, String>,
            @Body parameters:PojoData
    ):Call<ResponseBody>

    /* @Multipart
     @POST("/v1/account/register")
     fun registerUser(@Header("Authorization") BasicAuth :String,
                      @Part("email") email: RequestBody,
                      @Part("password") password: RequestBody,
                      @Part("confirmPassword") confirmPassword: RequestBody,
                      @Part("name") name: RequestBody,
                      @Part("profilePic")  profilePic:RequestBody,
                      @Part("mobileNumber") mobileNumber: RequestBody
     ): Call<ResponseBody>
 */

    @Multipart
    @POST("/v1/account/register")
    fun registerUser(
            @PartMap partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>

    @Multipart
    @PUT("/v1/account/singleUser")
    fun EditUser(
            @Header("x-auth") token: String?,
            @PartMap partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("/v1/account/forgotPwd")
    fun forgotPassword(
            @Body parameters:poho
    ):Call<ResponseBody>

    @Multipart
    @PUT("/v1/account/{K}")
    fun editProfile(
            @Path("K")  K:String,
            @Header("x-auth") token: String?,
            @PartMap partMap: HashMap<String, RequestBody>
    ):Call<ResponseBody>

    @GET("/v1/team/allPlayers")
    fun GetAllPlayers(
            @Header("x-auth") token: String): Call<ResponseBody>

    @Multipart
    @POST("/v1/team/createTeam")
    fun createTeam(
            @Header("x-auth") token: String?,
            @PartMap maps: LinkedHashMap<String,RequestBody>
//            @Part("players") players:ArrayList<String>
    ): Call<ResponseBody>


    @GET("/v1/team/singleTeam")
    fun getSingleTeam(
            @Header("x-auth") token: String?,
            @Query("teamId") teamId:String
    ): Call<ResponseBody>

    @GET("/v1/team/joinTeam")
    fun getJoinTeam(
            @Header("x-auth") token: String?,
            @Query("teamId") teamId:String
    ): Call<ResponseBody>

    @GET("/v1/team/addPlayer")
    fun getAddPlayer(
            @Header("x-auth") token: String?,
            @Query("teamId") teamId:String
    ): Call<ResponseBody>

    @GET("/v1/account/addHomecourt")
    fun AddHomeCourt(
            @Header("x-auth") token: String?,
            @Query("homeCourt") homeCourt:String
    ): Call<ResponseBody>

    @GET("/v1/account/singleNotification")
    fun Confirm(
            @Header("x-auth") token: String?,
            @Query("notificationId") notificationId:String,
            @Query("response") response:String
    ): Call<ResponseBody>

    @GET("/v1/team/changeCaptain")
    fun ChangeCaptain(
            @Header("x-auth") token: String?,
            @Query("teamId") teamId:String,
            @Query("newCaptain") newCaptain:String
    ): Call<ResponseBody>


    @GET("/v1/team/allTeams")
    fun GetAllTeams(
            @Header("x-auth") token: String): Call<ResponseBody>
    @GET("/v1/team/myTeam")
    fun GetMyTeam(
            @Header("x-auth") token: String): Call<ResponseBody>


    @GET("/v1/team/currentMatch")
    fun GetCurrentMatch(
            @Header("x-auth") token: String): Call<ResponseBody>

    @GET("/v1/account/searchCourts")
    fun GetCourts(
            @Query("lng") lng:String,
            @Query("lat") lat:String
    ): Call<ResponseBody>



    @GET("/v1/account/getNotification")
    fun GetNotifications(
            @Header("x-auth") token: String): Call<ResponseBody>

    @GET("/v1/team/teamTracks")
    fun GetTeamTracks(
            @Header("x-auth") token: String): Call<ResponseBody>
}