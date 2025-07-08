package com.example.asknitt
import androidx.compose.runtime.MutableState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
data class FcmTokenRequest(val fcm_token: String)
data class Settings(
    val dark_mode: Boolean,
    val notify_answers: Boolean,
    val notify_friend_requests: Boolean,
    val notify_updates: Boolean,
    val notify_ai_answers: Boolean
)
data class NotificationPrefs(
    var allNotifications: Boolean,
    var newAnswers: Boolean,
    var friendRequests: Boolean,
    var friendUpdates: Boolean,
    var aiReplies: Boolean
)

data class UserSignup(val username: String, val email: String, val password: String)
data class ApiResponse(val message: String, val user_id: Int?)
data class Answer(
    val id: Int,
    val content: String,
    val created_at: String,
    val is_anonymous: Int,
    val username: String,
    val user_id: Int,
    val upvotes: Int,
    val downvotes: Int,
    val user_vote: Int
)
data class friend(val id: Int, val username: String)
data class friendreq(val id: Int, val username: String, val user_id: Int, val created_at: String)
data class PostAnswerResponse(val message: String)
data class Doubt(
    val id: Int,
    val title: String,
    val description: String,
    val tags: String,
    val created_at: String,
    val is_anonymous: Int,
    val username: String,
    val identity:Int
)
data class Doubtdeets(
    val id: Int,
    val title: String,
    val description: String,
    val tags: String,
    val created_at: String,
    val is_anonymous: Int,
    val username: String,
    val identity:Int,
    val File: MutableList<Attachment>
)
data class DoubtsResponse(
    val doubts: List<Doubt>
)

data class LoginRequest(
    val username: String,
    val password: String
)
data class userlist(
    val id: Int,
    val username: String,
    val questions: Int,
    val answers: Int,
)

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val user_id: Int,
    val username: String
)
data class PostDoubtRequest(
    val title: String,
    val description: String,
    val tags: String,
    val is_anonymous:Boolean,
    val files: List<MultipartBody.Part>
)
data class PostDoubtRequest1(
    val title: MutableState<String>,
    val description: MutableState<String>,
    val tags: MutableState<String>,
    val is_anonymous: MutableState<Boolean>

)
data class PostAnswer(
    val doubt_id: Int,
    val content: String,
    val is_anonymous: Int
)
data class PostDoubtResponse(
    val message: String,
    val user_id: Int
)
data class Attachment(
    val id: Int,
    val file_name: String,
    val file_type: String
)
data class Notification(
    val id: Int,
    val user_id: Int,
    val message: String,
    val is_read: Int,
    val created_at: String,
    val title: String
)




interface ApiService {
    @POST("signup")
    fun signupUser(@Body user: UserSignup): Call<ApiResponse>
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenResponse>
    @GET("doubts")
    fun getFilteredDoubts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("tag") tag: String? = null,
        @Query("notuserid") notuserid: Int? = null,
        @Query("username") username: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Header("Authorization") authHeader: String
    ): Call<List<Doubt>>
    @Multipart
    @POST("doubts/")
    fun postDoubt(
        @Part("title") title: String,
        @Part("description") description: String,
        @Part("tags") tags: String,
        @Part("is_anonymous") isAnonymous: Boolean,
        @Part files: List<MultipartBody.Part>,
        @Header("Authorization") authHeader: String
    ): Call<PostDoubtResponse>
    @GET("users/{user_id}/pfp")
    @Streaming
    fun getProfilePic(@Path("user_id") userId: Int): Call<ResponseBody>
    @GET("doubts/{doubt_id}/attachments")
    fun listAttachments(@Path("doubt_id") doubtId: Int): Call<List<Attachment>>
    @GET("users")
    fun getUserList(@Query("skip") skip: Int,
                        @Query("limit") limit: Int,
                    @Query("tag") tag: String?=null): Call<List<userlist>>
    @GET("attachments/{attachment_id}")
    @Streaming
    fun downloadAttachment(@Path("attachment_id") attachmentId: Int): Call<ResponseBody>

    @Multipart
    @POST("answers/")
    fun postAnswer(
        @Part("doubt_id") doubtId: Int,
        @Part("answer_text") answerText: String,
        @Part("is_anonymous") is_anonymous: Int,
        @Part files: List<MultipartBody.Part>,
        @Header("Authorization") token: String
    ): Call<PostAnswerResponse>
    @FormUrlEncoded
    @POST("answers/{answer_id}/vote")
    fun voteAnswer(
        @Path("answer_id") answerId: Int,
        @Field("vote") vote: Int,
        @Header("Authorization") token: String
    ): Call<PostAnswerResponse>
    @GET("doubts/{doubt_id}/answers")
    fun getAnswers(
        @Path("doubt_id") doubtId: Int,
        @Header("Authorization") token: String
    ): Call<List<Answer>>
    @GET("users/{username}/answers")
    fun getProfileAnswers(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<Answer>>
    @GET("answers/{answer_id}/attachments")
    fun listansAttachments(@Path("answer_id") answer_id: Int): Call<List<Attachment>>
    @GET("{username}/doubts")
    fun getProfileDoubts(
        @Path("username") username: String
    ): Call<List<Doubt>>
    @GET("friends/list")
    fun getFriendList(@Header("Authorization") token: String): Call<List<friend>>
    @POST("friends/request")
    fun sendFriendRequest(@Query("receiver_id") receiver_id: Int,@Header("Authorization") token: String): Call<PostAnswerResponse>
    @POST("friends/respond")
    fun respondFriendRequest(@Query("request_id") request_id: Int,@Query("action") action: String,@Header("Authorization") token: String): Call<PostAnswerResponse>
    @GET("friends/pending")
    fun getPendingRequest(@Header("Authorization") token: String): Call<List<friendreq>>
    @GET("friends/sentpending")
    fun getSentPendingRequest(@Header("Authorization") token: String): Call<List<friendreq>>
    @POST("friends/delete")
    fun deleteFriendRequest(@Query("receiver_id") receiver_id: Int,@Header("Authorization") token: String): Call<PostAnswerResponse>
    @Multipart
    @PUT("doubts/{id}")
    fun updateDoubt(
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part("is_anonymous") isAnonymous: RequestBody,
        @Part("attachments_to_delete") attachmentsToDelete: RequestBody,
        @Part new_files: List<MultipartBody.Part> = emptyList(),
        @Header("Authorization") token: String
    ): Call<PostAnswerResponse>
    @Multipart
    @PUT("answers/{id}")
    fun updateAnswer(
        @Path("id") id: Int,
        @Part("answer_text") answer_text: RequestBody,
        @Part("is_anonymous") isAnonymous: RequestBody,
        @Part("attachments_to_delete") attachmentsToDelete: RequestBody,
        @Part new_files: List<MultipartBody.Part> = emptyList(),
        @Header("Authorization") token: String
    ): Call<PostAnswerResponse>
    @DELETE("doubts/{id}")
    fun deleteDoubt(
        @Path("id") id: Int,
        @Header("Authorization") token: String)
    : Call<PostAnswerResponse>
    @DELETE("answers/{id}")
    fun deleteAnswer(
        @Path("id") id: Int,
        @Header("Authorization") token: String)
            : Call<PostAnswerResponse>
    @Multipart
    @POST("doubts/{id}/askai")
    fun askai(
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("tags") tags: RequestBody,
        @Header("Authorization") authHeader: String
    ): Call<PostDoubtResponse>
    @POST("loginwithtoken")
    fun loginwithtoken(
        @Header("Authorization") authHeader: String
    ): Call<friend>
    @GET("/checknotif")
    fun getNotifications(@Header("Authorization") authHeader: String): Call<Notification>
    @POST("/notifications/{id}/read")
    fun markNotifRead(@Path("id") id: Int,@Header("Authorization") authHeader: String): Call<PostAnswerResponse>
    @FormUrlEncoded
    @POST("/change-password")
    fun changePassword(@Field("old_password") oldPassword: String, @Field("new_password") newPassword: String, @Header("Authorization") token: String
    ): Call<Void>
    @Multipart
    @POST("/edit-profile")
    fun editProfile(@Part("username") username: RequestBody?, @Part("email") email: RequestBody?, @Part profilePicture: MultipartBody.Part?, @Header("Authorization") token: String
    ): Call<Void>
    @GET("/settings")
    fun getSettings(@Header("Authorization") authHeader: String): Call<Settings>
    @POST("/settings/update")
    fun updateSettings(@Body settings: Settings,@Header("Authorization") authHeader: String): Call<PostAnswerResponse>
}

