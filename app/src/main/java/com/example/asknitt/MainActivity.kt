package com.example.asknitt
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import coil.compose.rememberAsyncImagePainter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import androidx.media3.effect.Brightness
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.asknitt.ui.theme.AskNITTTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.get
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import org.intellij.lang.annotations.JdkConstants.CalendarMonth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.indices
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString
import kotlin.collections.lastOrNull
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mapIndexed
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.collections.toMutableList
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

var token: String = ""
var user_id: Int = 0
var user: String = ""
val retrofit = Retrofit.Builder()
    .baseUrl("http://127.0.0.1:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
var settings=Settings(false, false,false,false,false)
fun fetchProfilePicture(
    userId: Int,
    context: Context,
    onResult: (Bitmap?) -> Unit
) {
    val call = apiService.getProfilePic(userId)
    call.enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
        override fun onResponse(
            call: retrofit2.Call<okhttp3.ResponseBody>,
            response: retrofit2.Response<okhttp3.ResponseBody>
        ) {
            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream()

                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    withContext(Dispatchers.Main) {
                        onResult(bitmap ?: getDefaultBitmap(context))
                    }
                }
            } else {
                onResult(getDefaultBitmap(context))
            }
        }


        override fun onFailure(
            call: retrofit2.Call<okhttp3.ResponseBody>,
            t: Throwable
        ) {
            onResult(getDefaultBitmap(context))
        }
    })
}
fun updateAnswer(
    id: Int,
    answer_text: String,
    isAnonymous: Int,
    deleteAttachmentIds: List<Int>,
    files: List<Uri>,
    context: Context,
    token: String, navController: NavController,
    callback: (PostAnswerResponse) -> Unit
) {
    val contentResolver = context.contentResolver
    val authHeader = "Bearer $token"
    val requestTitle = answer_text.toRequestBody("text/plain".toMediaType())
    val requestAnon = isAnonymous.toString().toRequestBody("text/plain".toMediaType())
    val requestDelete = deleteAttachmentIds.joinToString(",").toRequestBody("text/plain".toMediaType())
    val fileParts = files.mapIndexed { index, uri ->
        val fileName = getFileName(uri, context)
        val inputStream = contentResolver.openInputStream(uri)!!
        val bytes = inputStream.readBytes()
        val requestFile = bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("new_files", fileName, requestFile)
    }
    apiService.updateAnswer(id, requestTitle, requestAnon, requestDelete, fileParts, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                navController.popBackStack()
                callback(response.body() ?: PostAnswerResponse("Something went wrong"))
            }
            else{
                callback(response.body() ?: PostAnswerResponse("Something went wrong"))
            }
        }

        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            callback(PostAnswerResponse("Network error: ${t.localizedMessage}"))
        }
    })
}
fun updateDoubt(
    id: Int,
    title: String,
    description: String,
    tags: String,
    isAnonymous: Int,
    deleteAttachmentIds: List<Int>,
    files: List<Uri>,
    context: Context,
    token: String, navController: NavController,
    callback: (PostAnswerResponse) -> Unit
) {
    val contentResolver = context.contentResolver
    val authHeader = "Bearer $token"
    val requestTitle = title.toRequestBody("text/plain".toMediaType())
    val requestDescription = description.toRequestBody("text/plain".toMediaType())
    val requestTags = tags.toRequestBody("text/plain".toMediaType())
    val requestAnon = isAnonymous.toString().toRequestBody("text/plain".toMediaType())
    val requestDelete = deleteAttachmentIds.joinToString(",").toRequestBody("text/plain".toMediaType())

    val fileParts = files.mapIndexed { index, uri ->
        val fileName = getFileName(uri, context)
        val inputStream = contentResolver.openInputStream(uri)!!
        val bytes = inputStream.readBytes()
        val requestFile = bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("new_files", fileName, requestFile)
    }

    apiService.updateDoubt(id, requestTitle, requestDescription, requestTags, requestAnon, requestDelete, fileParts, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                navController.popBackStack()
                callback(response.body() ?: PostAnswerResponse("Something went wrong"))
            }
            else{
                callback(response.body() ?: PostAnswerResponse("Something went wrong"))
            }
        }

        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            callback(PostAnswerResponse("Network error: ${t.localizedMessage}"))
        }
    })
}
fun deleteDoubt(doubtId: Int, context: Context, reload: MutableState<Boolean>?, onResult: () -> Unit) {
    val authHeader = "Bearer $token"
    apiService.deleteDoubt(doubtId, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                if (reload != null) {
                    reload.value=!reload.value
                }
                Toast.makeText(context, "Doubt deleted", Toast.LENGTH_SHORT).show()

                onResult()
            } else {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
        }
    })
}
fun deleteAnswer(answerId: Int, context: Context, reload: MutableState<Boolean>?, onResult: () -> Unit) {
    val authHeader = "Bearer $token"
    apiService.deleteAnswer(answerId, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                if (reload != null) {
                    reload.value=!reload.value
                }
                Toast.makeText(context, "Answer deleted", Toast.LENGTH_SHORT).show()

                onResult()
            } else {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
        }
    })
}

fun getFileName(uri: Uri, context: Context): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = it.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result ?: "file"
}


private fun getDefaultBitmap(context: Context): Bitmap {
    return BitmapFactory.decodeResource(context.resources, R.drawable.defaultpfp)
}




val apiService = retrofit.create(ApiService::class.java)
fun signupUser(username:String, email:String, password:String,errorMsg: MutableState<String?>, navController: NavController) {
    val user = UserSignup(username, email, password)
    apiService.signupUser(user).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                loginUser(email, password, errorMsg,navController)
                navigate("details",navController)
            } else {
                errorMsg.value= response.message()
                println("Error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            errorMsg.value= t.message
            println("Failed: ${t.message}")
        }
    })
}
fun loginUser(email: String, password: String, errorMsg: MutableState<String?>, navController: NavController) {
    apiService.loginUser(email, password).enqueue(object : Callback<TokenResponse> {
        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
            if (response.isSuccessful) {
                 token = response.body()?.access_token.toString()

                SecurePrefs.saveToken(token)
                user_id = response.body()?.user_id!!
                user=response.body()?.username.toString()
                navigate("details",navController)
            } else {
                errorMsg.value=response.message()
                println("Login failed! Code: ${response.code()}, Message: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
            errorMsg.value=t.message
            println("Login error: ${t.message}")
        }
    })
}
fun getNotification(token: String, callback: (Notification) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getNotifications(authHeader = authHeader).enqueue(object : Callback<Notification> {
        override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
            if (response.isSuccessful) {
                callback(response.body() ?: Notification(0,0,"",0,"",""))
            } else {
               callback(Notification(0,0,"",0,"",""))
            }
        }

        override fun onFailure(call: Call<Notification>, t: Throwable) {
            callback(Notification(0,0,"",0,"",""))
        }
    })
}
fun getSettings(token: String, callback: (Settings) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getSettings(authHeader = authHeader).enqueue(object : Callback<Settings> {
        override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
            if (response.isSuccessful) {
                println(response.body())
                callback(response.body() ?: Settings(true, true, true, true, true))
            } else {
                println(response.errorBody())
                callback( Settings(true, true, true, true, true))
            }
        }

        override fun onFailure(call: Call<Settings>, t: Throwable) {
            println(t)
            callback( Settings(true, true, true, true, true))
        }
    })
}
fun updateSettings(settings: Settings,token: String, callback: (PostAnswerResponse) -> Unit) {
    val authHeader = "Bearer $token"

    apiService.updateSettings(settings,authHeader = authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                println(response.body())
                response.body()?.let { callback(it) }
            } else {
                println(response.errorBody())

            }
        }

        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            println(t)
        }
    })
}

fun prepareMultipleFiles(context: Context, uris: List<Uri>): List<MultipartBody.Part> {
    val parts = mutableListOf<MultipartBody.Part>()

    for (uri in uris) {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload_", null, context.cacheDir)

        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
        val fileName = getFileNameFromUri(context, uri)
        parts.add(MultipartBody.Part.createFormData("files", fileName, requestBody))
    }

    return parts
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = "file"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = it.getString(index)
            }
        }
    }
    return name
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SecurePrefs.init(this)
        enableEdgeToEdge()
        setContent {
            AskNITTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
sealed class TabIcon {
    data class Vector(val imageVector: ImageVector) : TabIcon()
    data class Painter(val painter: androidx.compose.ui.graphics.painter.Painter) : TabIcon()
}


enum class TabDestination(val route: String, val label: String, val index: Int) {
    HOME("home", "Home", index=0),
    FEED("feed", "Feed", index=1),
    SEARCH("search", "Search", index=2),
    USERS("users", "Users", index=3),
    QUESTION("question", "Question", index=4),
    QUESTIONDEETS("questiondeets", "Questiondeets", index=5),
    QUESTIONPROFILE("questionprofile", "Questionprofile", index=6),
    QUESTIONFRIENDS("questionfriends", "Questionfriends", index=7);
    val icon: TabIcon
        @Composable
        get() = when (this) {
            HOME -> TabIcon.Vector(Icons.Filled.Home)
            FEED -> TabIcon.Painter(painterResource(id = R.drawable.doubts))
            SEARCH -> TabIcon.Vector(Icons.Filled.Search)

            USERS -> TabIcon.Painter(painterResource(id= R.drawable.people))
            QUESTION -> TabIcon.Painter(painterResource(id= R.drawable.people))
            QUESTIONDEETS -> TabIcon.Painter(painterResource(id= R.drawable.people))
            QUESTIONPROFILE -> TabIcon.Painter(painterResource(id= R.drawable.people))
            QUESTIONFRIENDS -> TabIcon.Painter(painterResource(R.drawable.people))
        }
}
enum class ProfileDestination(val route: String, val label: String, val index: Int) {
    QUESTION("question", "Question", index=0),
    ANSWER("answer", "Answer", index=1), ;
}
enum class FriendDestination(val route: String, val label: String, val index: Int) {
    LIST("list", "List", index=0),
    PENDING("pending", "Pending", index=1),
    SENT("sent", "Sent", index=2);
}
fun postDoubt(
    mutedoubt: PostDoubtRequest1, context: Context,
    attachedFiles: MutableState<MutableList<Uri>>, accessToken: String,
    isLoading: MutableState<Boolean>, callback: (Boolean) -> Unit) {

    val authHeader = "Bearer $accessToken"
    val files = prepareMultipleFiles(context, attachedFiles.value)
    println(files)
    val doubt=PostDoubtRequest(mutedoubt.title.value, mutedoubt.description.value, mutedoubt.tags.value, mutedoubt.is_anonymous.value,files)
    apiService.postDoubt(doubt.title,doubt.description, doubt.tags,doubt.is_anonymous,doubt.files, authHeader).enqueue(object : Callback<PostDoubtResponse> {
        override fun onResponse(call: Call<PostDoubtResponse>, response: Response<PostDoubtResponse>) {
            if (response.isSuccessful) {
                val msg = response.body()?.message
                Toast.makeText(context, "Posted: $msg", Toast.LENGTH_SHORT).show()
                mutedoubt.title.value = ""
                mutedoubt.description.value=""
                mutedoubt.tags.value=""
                mutedoubt.is_anonymous.value=false
                isLoading.value=false
            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                println("Error: ${response.code()} ${response}" )
            }
        }

        override fun onFailure(call: Call<PostDoubtResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            println("Error: ${t.message}")
        }
    })

}
fun askai(
    doubt: Doubtdeets, context: Context,
     accessToken: String, reload: MutableState<Boolean>,
    isLoading: MutableState<Boolean>, callback: (Boolean) -> Unit) {
    val authHeader = "Bearer $accessToken"
    val requestTitle = doubt.title.toRequestBody("text/plain".toMediaType())
    val requestDescription = doubt.description.toRequestBody("text/plain".toMediaType())
    val requestTags = doubt.tags.toRequestBody("text/plain".toMediaType())
    apiService.askai(doubt.id,requestTitle,requestDescription,requestTags, authHeader).enqueue(object : Callback<PostDoubtResponse> {
        override fun onResponse(call: Call<PostDoubtResponse>, response: Response<PostDoubtResponse>) {
            if (response.isSuccessful) {
                val msg = response.body()?.message
                isLoading.value=false
                reload.value=!reload.value
                Toast.makeText(context, "Posted: $msg", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                reload.value=!reload.value
                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostDoubtResponse>, t: Throwable) {
            if (t.message?.contains("Timeout") != true) Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            reload.value=!reload.value


            println("Error: ${t.message}")
        }
    })

}
fun postAnswer(
    answer: PostAnswer, context: Context,
    attachedFiles: MutableState<MutableList<Uri>>, accessToken: String,
    isLoading: MutableState<Boolean>, callback: (Boolean) -> Unit) {
    val authHeader = "Bearer $accessToken"
    val files = prepareMultipleFiles(context, attachedFiles.value)
    apiService.postAnswer(answer.doubt_id,answer.content, answer.is_anonymous, files, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                val msg = response.body()?.message
                Toast.makeText(context, "Posted: $msg", Toast.LENGTH_SHORT).show()
                isLoading.value=!isLoading.value
            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            println("Error: ${t.message}")
        }
    })
}
fun sendfriendreq(
    receiver_id: Int, Token: String, context: Context, reload: MutableState<Boolean>?, callback: (PostAnswerResponse) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.sendFriendRequest(receiver_id = receiver_id, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                if (reload != null) {
                    reload.value=!reload.value
                }
                val msg = response.body()?.message
                Toast.makeText(context, "Posted: $msg", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            println("Error: ${t.message}")
        }
    })
}
fun deletefriendreq(
    receiver_id: Int, Token: String, context: Context, reload: MutableState<Boolean>?, callback: (PostAnswerResponse) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.deleteFriendRequest(receiver_id = receiver_id, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                if (reload != null) {
                    reload.value=!reload.value
                }
                val msg = response.body()?.message
                Toast.makeText(context, "Deleted: $msg", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            println("Error: ${t.message}")
        }
    })
}
fun deleteNotification(id: Int, Token: String, callback: (PostAnswerResponse) -> Unit) {
    val authHeader = "Bearer $Token"
    apiService.markNotifRead(id = id, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {

                val msg = response.body()?.message

            } else {

                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            println("Error: ${t.message}")
        }
    })
}
fun respondfriendreq(
    request_id: Int, action: String, Token: String, context: Context, reload: MutableState<Boolean>?, callback: (PostAnswerResponse) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.respondFriendRequest(request_id,action,authHeader ).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                if (reload != null) {
                    reload.value=!reload.value
                }
                val msg = response.body()?.message
                Toast.makeText(context, "Posted: $msg", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                println("Error: ${response.code()} ${response}" )
            }
        }
        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            println("Error: ${t.message}")
        }
    })
}
fun listAttachments(doubtId: Int, callback: (List<Attachment>) -> Unit) {
    apiService.listAttachments(doubtId).enqueue(object : Callback<List<Attachment>> {
        override fun onResponse(call: Call<List<Attachment>>, response: Response<List<Attachment>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading attachments: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Attachment>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun listansAttachments(answerId: Int, callback: (List<Attachment>) -> Unit) {
    apiService.listansAttachments(answer_id=answerId).enqueue(object : Callback<List<Attachment>> {
        override fun onResponse(call: Call<List<Attachment>>, response: Response<List<Attachment>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading attachments: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Attachment>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun getfriendList(Token: String, callback: (List<friend>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getFriendList(authHeader).enqueue(object : Callback<List<friend>> {
        override fun onResponse(call: Call<List<friend>>, response: Response<List<friend>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<friend>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun fetchPendingRequest(Token: String, callback: (List<friendreq>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getPendingRequest(authHeader).enqueue(object : Callback<List<friendreq>> {
        override fun onResponse(call: Call<List<friendreq>>, response: Response<List<friendreq>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<friendreq>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun fetchSentPendingRequest(Token: String, callback: (List<friendreq>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getSentPendingRequest(authHeader).enqueue(object : Callback<List<friendreq>> {
        override fun onResponse(call: Call<List<friendreq>>, response: Response<List<friendreq>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<friendreq>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}

fun fetchDoubts(skip: Int,
                tag: String? = null, notuserid: Int?=null,
                username: String? = null,
                startDate: String? = null,
                endDate: String? = null,token: String, callback: (List<Doubt>) -> Unit) {
    val authHeader="Bearer $token"
    apiService.getFilteredDoubts(skip, 10, tag, notuserid,username, startDate, endDate, authHeader).enqueue(object : Callback<List<Doubt>> {
        override fun onResponse(call: Call<List<Doubt>>, response: Response<List<Doubt>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Doubt>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun fetchProfileDoubts(
                username: String, callback: (List<Doubt>) -> Unit) {
    apiService.getProfileDoubts(username).enqueue(object : Callback<List<Doubt>> {
        override fun onResponse(call: Call<List<Doubt>>, response: Response<List<Doubt>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Doubt>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun loadAnswers(doubtId: Int, callback: (List<Answer>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getAnswers(doubtId, authHeader).enqueue(object : Callback<List<Answer>> {
        override fun onResponse(call: Call<List<Answer>>, response: Response<List<Answer>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Answer>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun fetchProfileAnswers(username: String, callback: (List<Answer>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getProfileAnswers(username, authHeader).enqueue(object : Callback<List<Answer>> {
        override fun onResponse(call: Call<List<Answer>>, response: Response<List<Answer>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Answer>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun loadProfileAnswers(username: String, callback: (List<Answer>) -> Unit) {
    val authHeader = "Bearer $token"
    apiService.getProfileAnswers(username, authHeader).enqueue(object : Callback<List<Answer>> {
        override fun onResponse(call: Call<List<Answer>>, response: Response<List<Answer>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Answer>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}
fun voteAnswer(
    answerId: Int,
    vote: Int,
    context: Context,
    token: String,
    callback: (PostAnswerResponse) -> Unit
) {
    val authHeader = "Bearer $token"
    val voteBody = vote.toString().toRequestBody("text/plain".toMediaType())
    apiService.voteAnswer(answerId, vote, authHeader).enqueue(object : Callback<PostAnswerResponse> {
        override fun onResponse(call: Call<PostAnswerResponse>, response: Response<PostAnswerResponse>) {
            if (response.isSuccessful) {
                response.body()?.let { callback(it) }
            } else {
                Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                callback(PostAnswerResponse("Failed to vote"))
            }
        }

        override fun onFailure(call: Call<PostAnswerResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            callback(PostAnswerResponse("Something went wrong"))
        }
    })
}

fun fetchUserList(skip: Int, tag: String?, callback: (List<userlist>) -> Unit) {
    apiService.getUserList(skip, 10, tag).enqueue(object : Callback<List<userlist>> {
        override fun onResponse(call: Call<List<userlist>>, response: Response<List<userlist>>) {
            if (response.isSuccessful) {
                println(response)
                callback(response.body() ?: emptyList())
            } else {
                println("Error loading doubts: ${response.code()}")
                callback(emptyList())
            }
        }
        override fun onFailure(call: Call<List<userlist>>, t: Throwable) {
            println("Network error: ${t.message}")
            callback(emptyList())
        }
    })
}


@Composable
fun MyDoubts(Username: String,navController: NavController,question: MutableState<Doubtdeets>,expanded:MutableState<Boolean>,modifier: Modifier = Modifier, onEdit: (question: Doubtdeets) -> Unit?) {
    val doubts = remember { mutableStateListOf<Doubt>() }
    var currentPage by remember { mutableStateOf(0) }
    val reload= remember { mutableStateOf(false) }
    var prevsearch = 0
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit,reload.value) {
        doubts.clear()
        currentPage=0
        loadMyDoubts(currentPage, Username,doubts) {currentPage++; prevsearch=doubts.size }
    }


    LaunchedEffect(listState) {
        println(prevsearch)

            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { index ->
                    if (index != null && index >= doubts.size - 3 && !isLoading && prevsearch>=(currentPage-1)*10 && currentPage!=0) {
                        isLoading = true
                        prevsearch=doubts.size
                        loadMyDoubts(currentPage, Username, doubts) {
                            currentPage++
                            isLoading = false
                        }

                    }
                }

    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Backdrop),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (doubts.size==0){
            item{
                Text("No Questions Found", color=Uncleanwhite)
            }
        }
        items(doubts.size) { doubt ->
            val doubtcontent=doubts[doubt]
            DoubtCard(doubtcontent, navController, question,reload,expanded, onEdit = {queestions-> onEdit(queestions)})
        }

        if (isLoading && prevsearch>=(currentPage-1)*10) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(navController: NavController, question: MutableState<Doubtdeets>,expanded:MutableState<Boolean>, modifier: Modifier = Modifier, gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?) {
    var search by remember { mutableStateOf("") }
    var prevsearch = 0
    val reload= remember { mutableStateOf(false) }
    var searchcontent by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var clicked by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val doubts = remember { mutableStateListOf<Doubt>() }
    var currentPage by remember { mutableStateOf(0) }
    val context= LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val calendarclicked = remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var calendarDisplayMonth by remember { mutableStateOf(YearMonth.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatRangeText(): String {
        return when {
            startDate != null && endDate != null ->
                "${startDate!!.format(dateFormatter)} - ${endDate!!.format(dateFormatter)}"
            startDate != null ->
                "${startDate!!.format(dateFormatter)} - ..."
            else -> "Select date range"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                expanded.value=false;
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row() {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it; clicked = false },
                label = { if (search == "") Text("Search") },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(0.dp).focusRequester(focusRequester).fillMaxWidth(0.9f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                trailingIcon = {
                    Column() {
                        Row() {
                            IconButton(onClick = { if (search!="") {expanded.value=false;searchcontent = search;clicked = true} }) {
                                Icon(
                                    Icons.Default.Search,
                                    ""
                                )
                            }
                            IconButton(onClick = {expanded.value=false; calendarclicked.value = !calendarclicked.value }) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar),
                                    "", tint = Uncleanwhite
                                )
                            }
                        }
                        if (calendarclicked.value) {
                            CalendarMonth()
                        }
                    }
                }
            )
        }
        if (calendarclicked.value) {
            Dialog(onDismissRequest = { calendarclicked.value = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardColor,
                    tonalElevation = 8.dp,
                    modifier = Modifier.wrapContentHeight()
                ) {
                    Column {
                        CalendarViewForRange(
                            currentMonthToDisplay = calendarDisplayMonth,
                            startDate = startDate,
                            endDate = endDate,
                            onDateClicked = { date ->
                                if (startDate == null || date.isBefore(startDate) || (endDate != null && startDate != null )) {
                                    startDate = date
                                    endDate = null
                                } else if (endDate == null && !date.isBefore(startDate)) {

                                    endDate = date
                                } else if (endDate != null && !date.isBefore(startDate)) {
                                    endDate = date
                                }
                            },
                            onMonthChanged = { newMonth ->
                                calendarDisplayMonth = newMonth
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = { calendarclicked.value = false; startDate = null; endDate = null }, colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(136,0,21,255), contentColor = Uncleanwhite)) {
                                Text("Cancel")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {

                                if (startDate != null && endDate != null) {
                                    clicked = true
                                    calendarclicked.value = false
                                } else {
                                    Toast.makeText(context, "Please select a date range", Toast.LENGTH_SHORT).show()
                                }
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(34,177,76,255), contentColor = Uncleanwhite)) {
                                Text("Apply")
                            }
                        }
                    }
                }
            }
        }

        val scrollState = rememberScrollState()
        Spacer(Modifier.height(10.dp))
        if (clicked) {
            if (clicked) {
                LaunchedEffect(searchcontent, reload.value) {
                    doubts.clear()
                    currentPage=0
                    loadMoreDoubts(currentPage, searchcontent,startDate=startDate.toString(), endDate = endDate.toString(), list=doubts) {  currentPage++; prevsearch=doubts.size }
                }

                LaunchedEffect(listState) {


                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .collect { index ->
                                if (index != null && index >= doubts.size - 3 && !isLoading && prevsearch>=(currentPage-1)*10 && currentPage!=0) {
                                    isLoading = true
                                    prevsearch = doubts.size
                                    loadMoreDoubts(currentPage, searchcontent, null,startDate.toString(),endDate.toString(),doubts) {
                                        currentPage++
                                        isLoading = false
                                    }
                                }
                            }

                }


                LazyColumn(
                    state = listState,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Backdrop)
                        .clickable { focusManager.clearFocus() },

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (doubts.size==0){
                        item{
                            Text("No Such Questions Found", color=Uncleanwhite)
                        }
                    }
                    items(doubts.size) { doubt ->
                        val doubtcontent = doubts[doubt]
                        DoubtCard(doubtcontent, navController, question,expanded=expanded,reload=reload, gotoprofile = { userid,  username-> gotoprofile(userid, username)},onEdit = {queestions->onEdit(queestions)})
                    }

                    if (isLoading && prevsearch!=doubts.size) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarViewForRange(
    currentMonthToDisplay: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateClicked: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = currentMonthToDisplay.lengthOfMonth()
    val firstDayOfMonthOffset = currentMonthToDisplay.atDay(1).dayOfWeek.value % 7
    val monthName = currentMonthToDisplay.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
    val year = currentMonthToDisplay.year.toString()
    val currentMonth = LocalDate.now()

    Column(modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { onMonthChanged(currentMonthToDisplay.minusMonths(1)) }, colors=ButtonDefaults.buttonColors(containerColor = QsBackdrop, contentColor = Uncleanwhite)) { Text("<") }
            Text("$monthName $year", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { if (currentMonth.year == currentMonthToDisplay.year && currentMonth.month != currentMonthToDisplay.month) onMonthChanged(currentMonthToDisplay.plusMonths(1)) }, colors=ButtonDefaults.buttonColors(containerColor = QsBackdrop, contentColor = if (currentMonth.year == currentMonthToDisplay.year && currentMonth.month != currentMonthToDisplay.month) Uncleanwhite else Color.DarkGray)) { Text(">") }
        }
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    day.take(3),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center, color= Uncleanwhite,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(firstDayOfMonthOffset) {
                Box(modifier = Modifier.aspectRatio(1f))
            }

            items(daysInMonth) { dayIndex ->
                val dayNumber = dayIndex + 1
                val currentDateInGrid = currentMonthToDisplay.atDay(dayNumber)
                val currentDate = LocalDate.now()
                val isSelectedStartDate = currentDateInGrid == startDate
                val isSelectedEndDate = currentDateInGrid == endDate
                val isInRange = startDate != null && endDate != null &&
                        !currentDateInGrid.isBefore(startDate) && !currentDateInGrid.isAfter(endDate)

                val cellColor = when {
                    isSelectedStartDate || isSelectedEndDate -> EnergeticTeal
                    isInRange -> EnergeticTeal.copy(alpha=0.5f)
                    else -> Color.Transparent
                }
                val textColor = when {
                    isSelectedStartDate || isSelectedEndDate -> Color.DarkGray
                    isInRange ->Color.Gray
                    else -> Uncleanwhite
                }
                val cellshape = when {
                    isSelectedStartDate || isSelectedEndDate -> RoundedCornerShape(10.dp)
                    else -> RoundedCornerShape(0.dp)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(cellshape)
                        .background(cellColor)
                        .clickable { if (currentDateInGrid.isBefore(currentDate)) onDateClicked(currentDateInGrid) }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        dayNumber.toString(),
                        color = if (currentDateInGrid.isBefore(currentDate)) textColor else Color.DarkGray,
                        fontWeight = if (isSelectedStartDate || isSelectedEndDate) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(userId: Int, username: String,selectedDestination1: TabDestination,navController: NavController, question: MutableState<Doubtdeets>,modifier: Modifier = Modifier,expanded:MutableState<Boolean>,  gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?, onEditAnswer: (answer: Answer, attachment: List<Attachment>) -> Unit?) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current


    val friendlist = remember { mutableStateListOf<friend?>(null) }
    val pendingfriendlist = remember { mutableStateListOf<friendreq?>(null) }
    var selectedDestination by rememberSaveable { mutableStateOf(ProfileDestination.QUESTION) }
    var dest by rememberSaveable { mutableStateOf(mutableListOf(0,0)) }
    val navController1 = rememberNavController()
    var reload = remember { mutableStateOf(false)}
    var refresh = remember { mutableStateOf(false)}
    val size = remember { mutableStateOf(0.5f) }
    val density = LocalDensity.current

    val shapeC = RoundedPolygon.star(
        50,
        rounding = CornerRounding(0.1f),
        innerRadius = size.value,
    )
    val shapeD = RoundedPolygon.star(
        9,
        rounding = CornerRounding(0.3f),
        innerRadius = 0.2f,
    )
    val morph2 = remember {
        Morph(shapeC, shapeD)
    }

    val interactionSource1 = remember {
        MutableInteractionSource()
    }
    val isPressed1 by interactionSource1.collectIsPressedAsState()

    val animatedProgress1 = animateFloatAsState(
        targetValue = if (isPressed1) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    val questions= remember { mutableStateListOf<Doubt>() }
    val answers= remember { mutableStateListOf<Answer>() }
    val questionno= remember { mutableStateOf(0) }
    val answerno= remember { mutableStateOf(0) }
    val vibe= remember { mutableStateOf(0) }
    LaunchedEffect(reload.value) {
        println(username)
        getfriendList(token){
            friendlist.clear()
            friendlist.addAll(it)
        }
        fetchPendingRequest(token){
            pendingfriendlist.clear()
            pendingfriendlist.addAll(it)
            fetchSentPendingRequest(token){
                pendingfriendlist.addAll(it)
            }
        }
    }

    LaunchedEffect(userId, refresh.value) {
        bitmap=null
        questions.clear()
        answers.clear()
        fetchProfilePicture( userId,context) { result ->
            bitmap = result
            println(bitmap)
        }
        fetchProfileDoubts(username) { newDoubts ->
            questions.addAll(newDoubts)
            questionno.value=questions.size
        }
        fetchProfileAnswers(username) { newDoubts ->
            answers.addAll(newDoubts)
            answerno.value=answers.size
            for (i in answers){
                vibe.value+=i.upvotes
                vibe.value-=i.downvotes
            }
        }

    }
    LaunchedEffect(refresh.value) {
        navController1.navigate("question")
        selectedDestination=ProfileDestination.QUESTION
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(CardColor), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(
                        CardColor
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)

                            .clip(MorphPolygonShape(morph2, animatedProgress1.value))
                            .background(EnergeticTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        bitmap?.let {


                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = "Home",

                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } ?: run {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))

                        }
                    }


                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        username,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = EnergeticTeal
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    var text by remember { mutableStateOf("Follow")}
                    for (i in friendlist){
                        if (i != null) {
                            if (i.id==userId) text="Followed"
                        }
                    }
                    for (i in pendingfriendlist){
                        if (i != null) {
                            if (i.user_id==userId) text="Pending"
                        }
                    }
                    val context= LocalContext.current
                    if (userId!= user_id) {
                        IconButton(
                            onClick = { if (text == "Follow") sendfriendreq(userId, token, context, reload){} }, enabled = text=="Follow",
                            modifier = Modifier.size(90.dp, 30.dp)
                        ) {
                            Text(text, color = if (text == "Follow") Color.Red else Color.Gray)
                        }
                    }
                }
                Text("Questions: ${questionno.value} • Answers: ${answerno.value} • Vibe: ${vibe.value}", color = Uncleanwhite, style=MaterialTheme.typography.titleSmall)
                PrimaryTabRow(
                    selectedTabIndex = ProfileDestination.entries.indexOf(selectedDestination),
                    containerColor = EnergeticTeal,
                    contentColor = Color.White,


                ) {
                    ProfileDestination.entries.forEach { destination ->

                        if ((destination != ProfileDestination.QUESTION || true) ) {
                            Tab(text = {Text(destination.label)},
                                selected = selectedDestination == destination,
                                onClick = {
                                    dest[0]=dest[1]
                                    dest[1]=destination.index
                                    selectedDestination = destination
                                    navController1.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                },

                                selectedContentColor = Color.Black,
                                unselectedContentColor = Color.Gray
                            )
                        }
                    }
                }
            }


        }
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = ProfileDestination.QUESTION.route,
            modifier = Modifier,
            enterTransition = {if (dest[1]-dest[0]>0) slideInHorizontally { it } + fadeIn() + expandIn( animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)) else  slideInHorizontally { -it } + fadeIn() + expandIn( animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)) },
        ) {
            composable(ProfileDestination.QUESTION.route) { QuestionList(questions, refresh, navController,navController1, question, Modifier.padding(innerPadding),expanded, gotoprofile= {ids, name-> gotoprofile( ids,name)}, onEdit = {queestions->onEdit(queestions)}, onEditAnswer = { answer, attachment -> onEditAnswer(answer, attachment)}) }
            composable(ProfileDestination.ANSWER.route) { AnswerList(answers,refresh, navController,navController1, question, Modifier.padding(innerPadding), expanded, gotoprofile= {ids, name-> gotoprofile( ids,name)}, onEdit = {queestions->onEdit(queestions)}, onEditAnswer = { answer, attachment -> onEditAnswer(answer, attachment)}) }
        }

    }

}
@Composable
fun QuestionList( questions: SnapshotStateList<Doubt>, reload: MutableState<Boolean>,navController: NavController,navController1: NavController,question: MutableState<Doubtdeets>, modifier: Modifier = Modifier, expanded: MutableState<Boolean>,  gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?, onEditAnswer: (answer: Answer, attachment: List<Attachment>) -> Unit?){
    val listState= rememberLazyListState()

    BackHandler {
        expanded.value=false
        navController.popBackStack()
    }
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Backdrop),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(questions.size) { doubt ->
            val doubtcontent = questions[doubt]
            DoubtCard(doubtcontent, navController, question, expanded = expanded, reload = reload,gotoprofile = { userid,  username-> gotoprofile(userid, username) }, onEdit = {queestions->onEdit(queestions)})
        }

    }
}
@Composable
fun AnswerList(answers: SnapshotStateList<Answer>, reload: MutableState<Boolean>, navController: NavController,navController1: NavController, question: MutableState<Doubtdeets>, modifier: Modifier = Modifier, expanded: MutableState<Boolean>, gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?, onEditAnswer: (answer: Answer, attachment: List<Attachment>) -> Unit?){
    val listState= rememberLazyListState()
    val attachmentlist= remember { mutableStateListOf<List<Attachment>>() }
    BackHandler {
        expanded.value=false
        navController.popBackStack()
    }
    println(answers)
    LaunchedEffect(answers) {
        val fetchedAnswers = answers
        attachmentlist.clear()
        repeat(fetchedAnswers.size) {
            attachmentlist.add(emptyList())
        }

        fetchedAnswers.forEachIndexed { index, answer ->
            val userId = answer.user_id
            listansAttachments(answer.id) { attachments ->
                attachmentlist[index] = attachments
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Backdrop),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (attachmentlist.isNotEmpty() && answers.isNotEmpty()) {
            items(attachmentlist.size) { answer ->
                val attachment = attachmentlist[answer]
                val doubtcontent = answers[answer]
                ChatBubble(navController,
                    text = answers[answer].content,
                    file = attachment.toMutableList(),
                    upvotes = answers[answer].upvotes,
                    downvotes = answers[answer].downvotes,
                    answer = answers[answer], reload = reload,
                    isQuestion = false, bubbleMaxWidth = 400.dp,  onEdit = {}, onEditAnswer = { username, userid-> onEditAnswer(username, userid)}, gotoprofile = { userid, username-> gotoprofile(userid, username)}
                )
            }
        }

    }
}
@Composable
fun UsersScreen(modifier: Modifier = Modifier, navController: NavController,username: String,expanded:MutableState<Boolean>, onClick: (Int, String) -> Unit) {
    val users = remember { mutableStateListOf<userlist>() }
    var currentPage by remember { mutableStateOf(0) }
    var prevsearch = 0
    var clicked by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var searchcontent by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(search) {
        users.clear()
        currentPage=0
        loadMoreUsers(currentPage, search,users) {currentPage++; prevsearch=users.size }

    }

    LaunchedEffect(listState) {
        if (prevsearch>=(currentPage-1)*10 && prevsearch!=0) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { index ->
                    if (index != null && index >= users.size - 3 && !isLoading) {
                        isLoading = true
                        prevsearch=users.size
                        loadMoreUsers(currentPage, "", users) {
                            currentPage++
                            isLoading = false
                        }

                    }
                }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Backdrop)
            .clickable {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            OutlinedTextField(
                value = search,
                onValueChange = { search = it; clicked = false },
                label = { if (search == "") Text("Search") },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(0.dp).focusRequester(focusRequester).fillMaxWidth(0.9f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                trailingIcon = {
                    Column() {
                        Row() {
                            IconButton(onClick = { focusManager.clearFocus(); searchcontent = search;clicked = true }) {
                                Icon(
                                    Icons.Default.Search,
                                    ""
                                )
                            }

                        }

                    }
                }
            )
        }
        if (users.size==0){
            item{
                Text("No Users Found", color=Uncleanwhite)
            }
        }
        items(users.size) { doubt ->
            val doubtcontent=users[doubt]
            if (doubtcontent.username!=username)  UserCard(doubtcontent, modifier = Modifier.clickable { onClick(doubtcontent.id, doubtcontent.username) }, reload = null)
        }

        if (isLoading && prevsearch>=(currentPage-1)*10) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun questionAdder(modifier: Modifier = Modifier, userId:Int, token:String,expanded:MutableState<Boolean> ,navController: NavController){
    var title = remember { mutableStateOf("") }
    val remove = remember { mutableStateListOf<Uri>() }
    var description = remember { mutableStateOf("") }
    var tagInput = remember { mutableStateOf("") }
    var isAnonymous = remember { mutableStateOf(false) }
    var isLoading = remember { mutableStateOf(false) }
    val transition = rememberInfiniteTransition()
    var attachedFiles = remember { mutableStateOf(mutableListOf<Uri>()) }
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        for (uri in uris){
            attachedFiles.value.add(uri)
        }
    }
    val hueShift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing)
        )
    )

    val hue = (hueShift + 2 * (360f / 16)) % 360f
    val color = Color.hsv(hue, 0.9f,0.9f)
    LaunchedEffect(remove.size) {
        println(1)
        if (remove.isNotEmpty()) {

            for (i in remove) {
                attachedFiles.value.remove(i)
            }
            remove.clear()
        }
    }

    val tags = tagInput.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val scrollState = rememberScrollState()
    val scrollState1 = rememberScrollState()
    val scrollState2 = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .background(CardColor)
                .padding(16.dp)
                .verticalScroll(scrollState1)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = QsBackdrop),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = title.value,
                        onValueChange = {
                            if (it.length <= 255) title.value = it
                        },
                        label = { Text("Title") },
                        placeholder = { Text("What's your question about?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(Uncleanwhite)
                    )
                    Text(
                        "${title.value.length}/255",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.End).padding(bottom = 8.dp), color = Uncleanwhite
                    )
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Description") },
                        placeholder = { Text("Explain your question in detail...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 10, textStyle = TextStyle(Uncleanwhite)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = tagInput.value,
                        onValueChange = {
                            if (it.length <= 255) tagInput.value = it
                        },
                        label = { Text("Tags") },
                        placeholder = { Text("e.g. kotlin, fastapi, android") },
                        modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(Uncleanwhite)
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp).horizontalScroll(scrollState2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag) }, colors = AssistChipDefaults.assistChipColors(containerColor = ChipBack,labelColor =
                                    Uncleanwhite)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Anonymous", tint= Uncleanwhite)
                        Text(
                            "Ask Anonymously",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp), color = Uncleanwhite
                        )
                        Switch(
                            checked = isAnonymous.value,
                            onCheckedChange = { isAnonymous.value= it }, colors = SwitchDefaults.colors(
                                checkedThumbColor = CleanWhite,
                                checkedTrackColor = EnergeticTeal.copy(alpha = 0.8f),
                                uncheckedThumbColor = Uncleanwhite,
                                uncheckedTrackColor = DeepNittBlue.copy(alpha = 0.6f),
                                disabledCheckedThumbColor = EnergeticTeal.copy(alpha = 0.4f),
                                disabledUncheckedThumbColor = Uncleanwhite.copy(alpha = 0.3f),
                                disabledCheckedTrackColor = EnergeticTeal.copy(alpha = 0.2f),
                                disabledUncheckedTrackColor = DeepNittBlue.copy(alpha = 0.3f)
                            )
                        )
                    }
                    val context= LocalContext.current
                    Spacer(modifier = Modifier.height(16.dp))
                    Row( verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { filePicker.launch("*/*") },

                            ) {
                            Icon(
                                painter = painterResource(R.drawable.clip),
                                "Attach",
                                tint = Uncleanwhite
                            )
                        }
                        Row(Modifier.horizontalScroll(scrollState), verticalAlignment = Alignment.CenterVertically) {

                        attachedFiles.takeIf { it.value.isNotEmpty() }?.let {
                            it.value.forEach { uri ->
                                val type = remember(uri) { getMimeType(context, uri) }
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)
                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)
                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                    else -> painterResource(R.drawable.file)
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text(getFileName(uri, context) ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                        Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {remove.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                                )
                            }
                        }
                            }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            isLoading.value = true
                            postDoubt(PostDoubtRequest1(title, description, tagInput, isAnonymous), context, attachedFiles, token, isLoading){

                            }
                        },
                        enabled = title.value.isNotBlank() && !isLoading.value,
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color)
                    ) {
                        if (isLoading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text("Post Question", color= CleanBlack)
                        }
                    }
                }
            }
        }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun questionEditor(modifier: Modifier = Modifier, question:Doubtdeets,userId:Int, token:String,expanded:MutableState<Boolean> ,navController: NavController){
    var title = remember { mutableStateOf(question.title) }
    val remove = remember { mutableStateListOf<Uri>() }
    val removeattached = remember { mutableStateListOf<Attachment>() }
    val removeattachedid = remember { mutableStateListOf<Int>() }
    var description = remember { mutableStateOf(question.description) }
    var tagInput = remember { mutableStateOf(question.tags) }
    var isAnonymous = remember { mutableStateOf(question.is_anonymous) }
    var isLoading = remember { mutableStateOf(false) }
    val transition = rememberInfiniteTransition()
    val alreadyattachedfiles= remember{ mutableStateListOf<Attachment>() }
    var attachedFiles = remember { mutableStateOf(mutableListOf<Uri>()) }
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        for (uri in uris){
            attachedFiles.value.add(uri)
        }
    }
    val hueShift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing)
        )
    )

    val hue = (hueShift + 2 * (360f / 16)) % 360f
    val color = Color.hsv(hue, 0.9f,0.9f)
    LaunchedEffect(remove.size) {
        println(1)
        if (remove.isNotEmpty()) {

            for (i in remove) {
                attachedFiles.value.remove(i)
            }
            remove.clear()
        }
    }
    LaunchedEffect(removeattached.size) {
        if (removeattached.isNotEmpty()) {

            for (i in removeattached) {
                alreadyattachedfiles.remove(i)
                removeattachedid.add(i.id)
            }
            removeattached.clear()
        }
    }
    LaunchedEffect(Unit) {
        alreadyattachedfiles.clear()
        alreadyattachedfiles.addAll(question.File)
    }

    val tags = tagInput.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val scrollState = rememberScrollState()
    val scrollState1 = rememberScrollState()
    val scrollState2 = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
            .background(CardColor)
            .padding(16.dp)
            .verticalScroll(scrollState1)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = QsBackdrop),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = title.value,
                    onValueChange = {
                        if (it.length <= 255) title.value = it
                    },
                    label = { Text("Title") },
                    placeholder = { Text("What's your question about?") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(Uncleanwhite)
                )
                Text(
                    "${title.value.length}/255",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End).padding(bottom = 8.dp), color = Uncleanwhite
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    placeholder = { Text("Explain your question in detail...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 10, textStyle = TextStyle(Uncleanwhite)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tagInput.value,
                    onValueChange = {
                        if (it.length <= 255) tagInput.value = it
                    },
                    label = { Text("Tags") },
                    placeholder = { Text("e.g. kotlin, fastapi, android") },
                    modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(Uncleanwhite)
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp).horizontalScroll(scrollState2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag) }, colors = AssistChipDefaults.assistChipColors(containerColor = ChipBack,labelColor =
                                Uncleanwhite)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Anonymous", tint= Uncleanwhite)
                    Text(
                        "Ask Anonymously",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp), color = Uncleanwhite
                    )
                    Switch(
                        checked = if (isAnonymous.value==0) false else true,
                        onCheckedChange = { isAnonymous.value= if (it) 1 else 0 }, colors = SwitchDefaults.colors(
                            checkedThumbColor = CleanWhite,
                            checkedTrackColor = EnergeticTeal.copy(alpha = 0.8f),
                            uncheckedThumbColor = Uncleanwhite,
                            uncheckedTrackColor = DeepNittBlue.copy(alpha = 0.6f),
                            disabledCheckedThumbColor = EnergeticTeal.copy(alpha = 0.4f),
                            disabledUncheckedThumbColor = Uncleanwhite.copy(alpha = 0.3f),
                            disabledCheckedTrackColor = EnergeticTeal.copy(alpha = 0.2f),
                            disabledUncheckedTrackColor = DeepNittBlue.copy(alpha = 0.3f)
                        )
                    )
                }
                val context= LocalContext.current
                Spacer(modifier = Modifier.height(16.dp))
                Row( verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { filePicker.launch("*/*") },

                        ) {
                        Icon(
                            painter = painterResource(R.drawable.clip),
                            "Attach",
                            tint = Uncleanwhite
                        )
                    }
                    Row(Modifier.horizontalScroll(scrollState), verticalAlignment = Alignment.CenterVertically) {
                        alreadyattachedfiles.takeIf { it.isNotEmpty() }?.let {
                            it.forEach { uri ->
                                val type = uri.file_type
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)
                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)
                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                    else -> painterResource(R.drawable.file)
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text(uri.file_name.toString() ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                        Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {removeattached.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                                )
                            }
                        }
                        attachedFiles.takeIf { it.value.isNotEmpty() }?.let {
                            it.value.forEach { uri ->
                                val type = remember(uri) { getMimeType(context, uri) }
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)
                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)
                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                    else -> painterResource(R.drawable.file)
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text(uri.lastPathSegment.toString() ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                        Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {remove.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        isLoading.value = true
                        updateDoubt(question.id,title.value,description.value,tagInput.value,isAnonymous.value,removeattachedid,attachedFiles.value.toList(), context, token, navController ){

                        }
                    },
                    enabled = title.value.isNotBlank() && !isLoading.value,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color)
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Post Question", color= CleanBlack)
                    }
                }
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun answerEditor(modifier: Modifier = Modifier, answer:Answer, attachment: List<Attachment>,userId:Int, token:String,expanded:MutableState<Boolean> ,navController: NavController){
    val remove = remember { mutableStateListOf<Uri>() }
    val removeattached = remember { mutableStateListOf<Attachment>() }
    val removeattachedid = remember { mutableStateListOf<Int>() }
    var description = remember { mutableStateOf(answer.content) }
    var isAnonymous = remember { mutableStateOf(answer.is_anonymous) }
    var isLoading = remember { mutableStateOf(false) }
    val transition = rememberInfiniteTransition()
    val alreadyattachedfiles= remember{ mutableStateListOf<Attachment>() }
    var attachedFiles = remember { mutableStateOf(mutableListOf<Uri>()) }
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        for (uri in uris){
            attachedFiles.value.add(uri)
        }
    }
    val hueShift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing)
        )
    )

    val hue = (hueShift + 2 * (360f / 16)) % 360f
    val color = Color.hsv(hue, 0.9f,0.9f)
    LaunchedEffect(remove.size) {
        println(1)
        if (remove.isNotEmpty()) {

            for (i in remove) {
                attachedFiles.value.remove(i)
            }
            remove.clear()
        }
    }
    LaunchedEffect(removeattached.size) {
        if (removeattached.isNotEmpty()) {

            for (i in removeattached) {
                alreadyattachedfiles.remove(i)
                removeattachedid.add(i.id)
            }
            removeattached.clear()
        }
    }
    LaunchedEffect(Unit) {
        alreadyattachedfiles.clear()
        alreadyattachedfiles.addAll(attachment)
    }
    val scrollState = rememberScrollState()
    val scrollState1 = rememberScrollState()
    val scrollState2 = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
            .background(CardColor)
            .padding(16.dp)
            .verticalScroll(scrollState1)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = QsBackdrop),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {


                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    placeholder = { Text("Enter Answer...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 10, textStyle = TextStyle(Uncleanwhite)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Anonymous", tint= Uncleanwhite)
                    Text(
                        "Answer Anonymously",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp), color = Uncleanwhite
                    )
                    Switch(
                        checked = if (isAnonymous.value==0) false else true,
                        onCheckedChange = { isAnonymous.value= if (it) 1 else 0 }, colors = SwitchDefaults.colors(
                            checkedThumbColor = CleanWhite,
                            checkedTrackColor = EnergeticTeal.copy(alpha = 0.8f),
                            uncheckedThumbColor = Uncleanwhite,
                            uncheckedTrackColor = DeepNittBlue.copy(alpha = 0.6f),
                            disabledCheckedThumbColor = EnergeticTeal.copy(alpha = 0.4f),
                            disabledUncheckedThumbColor = Uncleanwhite.copy(alpha = 0.3f),
                            disabledCheckedTrackColor = EnergeticTeal.copy(alpha = 0.2f),
                            disabledUncheckedTrackColor = DeepNittBlue.copy(alpha = 0.3f)
                        )
                    )
                }
                val context= LocalContext.current
                Spacer(modifier = Modifier.height(16.dp))
                Row( verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { filePicker.launch("*/*") },

                        ) {
                        Icon(
                            painter = painterResource(R.drawable.clip),
                            "Attach",
                            tint = Uncleanwhite
                        )
                    }
                    Row(Modifier.horizontalScroll(scrollState), verticalAlignment = Alignment.CenterVertically) {
                        alreadyattachedfiles.takeIf { it.isNotEmpty() }?.let {
                            it.forEach { uri ->
                                val type = uri.file_type
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)
                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)
                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                    else -> painterResource(R.drawable.file)
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text(uri.file_name.toString() ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                        Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {removeattached.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                                )
                            }
                        }
                        attachedFiles.takeIf { it.value.isNotEmpty() }?.let {
                            it.value.forEach { uri ->
                                val type = remember(uri) { getMimeType(context, uri) }
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)
                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)
                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                    else -> painterResource(R.drawable.file)
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text(uri.lastPathSegment.toString() ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                        Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {remove.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        isLoading.value = true
                        updateAnswer(answer.id, description.value, isAnonymous.value, removeattachedid.toList(), attachedFiles.value.toList(), context, token, navController){

                        }
                    },
                    enabled = description.value.isNotBlank() && !isLoading.value,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color)
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Update Answer", color= CleanBlack)
                    }
                }
            }
        }
    }

}
fun getMimeType(context: Context, uri: Uri): String? {
    return context.contentResolver.getType(uri)
}
@Composable
fun Home(modifier: Modifier = Modifier,navController: NavController, question: MutableState<Doubtdeets>, expanded:MutableState<Boolean>, gotoprofile : (Int, String)-> Unit, ) {
    val doubts = remember { mutableStateListOf<Doubt>() }
    var currentPage by remember { mutableStateOf(0) }
    val reload= remember { mutableStateOf(false) }
    var prevsearch = 0
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit,reload.value) {
        doubts.clear()
        loadMoreDoubts(currentPage, "", user_id,list=doubts) {currentPage++; prevsearch=doubts.size }
    }

    LaunchedEffect(listState) {
        println(prevsearch)

            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { index ->
                    if (index != null && index >= doubts.size - 3 && !isLoading && prevsearch>=(currentPage-1)*10 && currentPage!=0) {
                        isLoading = true
                        prevsearch=doubts.size
                        loadMoreDoubts(currentPage, "",user_id,list=doubts) {
                            currentPage++
                            isLoading = false
                        }

                    }
                }

    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Backdrop),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (doubts.size==0){
            item{
                Text("No Questions Found", color=Uncleanwhite)
            }
        }
        items(doubts.size) { doubt ->
            val doubtcontent=doubts[doubt]
            if (doubtcontent.identity!= user_id) DoubtCard(doubtcontent, navController, question, expanded=expanded, reload=reload,gotoprofile = { userid,  username-> gotoprofile(userid, username)})
        }

        if (isLoading && prevsearch>=(currentPage-1)*10) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
fun loadMoreDoubts(page: Int,tag: String?,notuserid: Int?=null,startDate: String?="1700-01-01", endDate: String?="3000-12-31", list: SnapshotStateList<Doubt>, onDone: () -> Unit) {

        fetchDoubts(page * 10, tag=tag ,notuserid=notuserid,startDate =   if (startDate=="null") "1700-01-01" else startDate.toString(), endDate = if (endDate=="null") "3000-12-31" else endDate.toString(), token = token) { newDoubts ->
            list.addAll(newDoubts)
            onDone()
        }

}
fun loadMoreUsers(page: Int,tag: String?, list: SnapshotStateList<userlist>, onDone: () -> Unit) {
    fetchUserList(page * 10, tag) { newDoubts ->
        list.addAll(newDoubts)
        onDone()
    }
}
fun loadMyDoubts(page: Int,username: String, list: SnapshotStateList<Doubt>, onDone: () -> Unit) {
    fetchDoubts(page * 10, username = username, token = token) { newDoubts ->
        list.addAll(newDoubts)
        onDone()
    }
}
@Composable
fun UserCard(user: userlist, reload: MutableState<Boolean>?, modifier: Modifier = Modifier){
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val density = LocalDensity.current


    LaunchedEffect(user) {
        delay(100)
        fetchProfilePicture( user.id,context) { result ->
            bitmap = result
            println(bitmap)
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp).clip(WideHexagonShape(20f)).clickable {  }, colors = CardDefaults.cardColors(containerColor = UserCardColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(12.dp))
            Column {

                Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
                    Box(modifier.padding(0.dp).clip(CircleShape).size(50.dp).background(CleanBlack)) {
                        bitmap?.let {

                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = "Home",

                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: run {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))

                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(user.username, style = MaterialTheme.typography.titleMedium, color = CleanWhite)
                }
                if (user.questions!=-4)  Text(" • ${user.questions} Questions • ${user.answers} Answers •", style = MaterialTheme.typography.bodySmall, color = Uncleanwhite, textAlign = TextAlign.Center, modifier= Modifier.fillMaxWidth()) else{
                    IconButton(onClick = { deletefriendreq(user.id, token, context,reload){
                        if (reload != null) {
                            reload.value=!reload.value
                        }
                    } }, Modifier.fillMaxWidth()){
                        Text("DELETE", color=Color.Cyan, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Right)
                    }
                }

            }
        }
    }
}
@Composable
fun ReqUserCard(user: friendreq,sentornot: Int, reload: MutableState<Boolean>, modifier: Modifier = Modifier){
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val density = LocalDensity.current


    LaunchedEffect(user) {
        delay(100)
        fetchProfilePicture( user.user_id,context) { result ->
            bitmap = result
            println(bitmap)
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp).clip(WideHexagonShape(20f)).clickable {  }, colors = CardDefaults.cardColors(containerColor = UserCardColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                if (sentornot==0) {
                }
                else{
                    Text("   PENDING...", style = MaterialTheme.typography.bodyMedium, color = Uncleanwhite)

                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
                    Box(modifier.padding(0.dp).clip(CircleShape).size(50.dp).background(EnergeticTeal)) {
                        bitmap?.let {

                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = "Home",

                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: run {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))

                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(user.username, style = MaterialTheme.typography.titleMedium, color = CleanWhite)
                }
                if (sentornot==0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                            Button(
                                onClick = {
                                    respondfriendreq(
                                        user.id,
                                        "accepted",
                                        token,
                                        context, reload
                                    ) {  }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        34,
                                        177,
                                        76
                                    ), contentColor = Color(15, 79, 34)
                                )
                            ) {
                                Text("ACCEPT")
                            }


                            Button(
                                onClick = {
                                    deletefriendreq(user.user_id, token, context, reload) {  }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        217,
                                        0,
                                        33
                                    ), contentColor = Color(112, 0, 17)
                                )
                            ) {
                                Text("REJECT")
                            }


                    }
                }
                Text("• Sent At ${user.created_at.replace("T", " ")} •", style = MaterialTheme.typography.bodySmall, color = Uncleanwhite, textAlign = TextAlign.Center, modifier=Modifier.fillMaxWidth())

            }
        }
    }
}



class WideHexagonShape(private val cornerRadius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height
        val effectiveRadius =
            cornerRadius.coerceAtMost(minOf(width / 4f, height / 2f, 20f ))
        val points = listOf(
            Pair(width * 0.1f, 0f),
            Pair(width * 0.9f, 0f),
            Pair(width, height * 0.5f),
            Pair(width * 0.9f, height),
            Pair(width * 0.1f, height),
            Pair(0f, height * 0.5f)
        )
        path.moveTo(
            points[0].first + effectiveRadius * cos(
                Math.toRadians(150.0).toFloat()
            ),
            points[0].second + effectiveRadius * sin(Math.toRadians(150.0).toFloat())
        )

        for (i in points.indices) {
            val currentPoint = points[i]
            val nextPoint = points[(i + 1) % points.size]
            val prevPoint = points[(i - 1 + points.size) % points.size]
            val prevDx = currentPoint.first - prevPoint.first
            val prevDy = currentPoint.second - prevPoint.second
            val prevLength = sqrt(prevDx * prevDx + prevDy * prevDy)
            val p1x = currentPoint.first - (prevDx / prevLength) * effectiveRadius
            val p1y = currentPoint.second - (prevDy / prevLength) * effectiveRadius
            val nextDx = nextPoint.first - currentPoint.first
            val nextDy = nextPoint.second - currentPoint.second
            val nextLength = sqrt(nextDx * nextDx + nextDy * nextDy)
            val p2x = currentPoint.first + (nextDx / nextLength) * effectiveRadius
            val p2y = currentPoint.second + (nextDy / nextLength) * effectiveRadius
            path.lineTo(p1x, p1y)
            path.quadraticBezierTo(currentPoint.first, currentPoint.second, p2x, p2y)
        }
        path.close()
        return Outline.Generic(path)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun questionDetails(navController: NavController,
    question: Doubtdeets,expanded:MutableState<Boolean>,
    onBack: () -> Unit,
    onSendAnswer: (PostAnswer, Context, MutableState<MutableList<Uri>>, MutableState<Boolean>) -> Unit, gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?, onEditAnswer: (answer: Answer, attachment: List<Attachment>) -> Unit?
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var bitmaplist = remember { mutableStateListOf<Bitmap?>() }
    var attachmentlist = remember { mutableStateListOf<List<Attachment>>() }
    var reload = remember { mutableStateOf(false) }
    var answers = remember { mutableStateListOf<Answer>() }
    var isai = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val is_anonoymous= remember { mutableStateOf(0) }
    val isLoading = remember { mutableStateOf(false) }
    var attachedFiles = remember { mutableStateListOf<Uri>() }
    var remove = remember { mutableStateListOf<Uri>() }
    val filePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            for (uri in uris) {
                attachedFiles.add(uri)
                println(attachedFiles)
            }
        }
    LaunchedEffect(reload.value) {
        println(1)
        attachmentlist.clear()
        answers.clear()
        bitmaplist.clear()
        val bitmapCache = mutableMapOf<Int, Bitmap?>()
        loadAnswers(question.id) { fetchedAnswers ->
            answers.clear()
            answers.addAll(fetchedAnswers)
            for (i in answers){
                if (i.user_id==5){
                    isai.value=true
                }
            }
            bitmaplist.clear()
            attachmentlist.clear()
            repeat(fetchedAnswers.size) {
                bitmaplist.add(null)
                attachmentlist.add(emptyList())
            }

            fetchedAnswers.forEachIndexed { index, answer ->
                val userId = answer.user_id
                if (bitmapCache.containsKey(userId)) {
                    bitmapCache[userId]?.let {
                        bitmaplist[index] = it.copyBitmap()
                    }
                } else {
                    fetchProfilePicture(userId, context) { bitmapResult ->
                        bitmapResult?.let {
                            bitmapCache[userId] = it
                            bitmaplist[index] = it.copyBitmap()
                        }
                    }
                }
                listansAttachments(answer.id) { attachments ->
                    attachmentlist[index] = attachments
                }
            }
        }
    }

    LaunchedEffect(remove.size) {
        println(1)
        if (remove.isNotEmpty()) {

            for (i in remove) {
                attachedFiles.remove(i)
            }
            remove.clear()
        }
    }
    LaunchedEffect(question) {
        fetchProfilePicture( question.identity,context) { result ->
            bitmap = result

        }
    }
    val scrollState= rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val bubbleMaxWidth = 0.75f*screenWidth - 100.dp

    var newAnswer by remember { mutableStateOf("") }
    val scrollState2 = rememberScrollState()
    Scaffold(
        topBar = {

            Column(modifier = Modifier.background(CleanBlack).padding(2.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(verticalArrangement = Arrangement.Center){
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint= CleanWhite)
                    }
                    Text(question.title, style = MaterialTheme.typography.titleLarge, color = Uncleanwhite, fontWeight = FontWeight.Bold)

                }
                Box() {
                    Row(
                        modifier = Modifier.padding(top = 3.dp, bottom = 5.dp)
                            .horizontalScroll(scrollState2).fillMaxWidth(0.8f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        question.tags.split(',').forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag) }, colors = AssistChipDefaults.assistChipColors(
                                    containerColor = ChipBack, labelColor =
                                        Uncleanwhite
                                )
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        IconButton(onClick = {isai.value=true;expanded.value=false;isLoading.value=true;askai(question, context,token, reload, isLoading){} }, enabled = !isai.value){
                            if(!isLoading.value) {
                                if (!isai.value) {
                                    Image(
                                        painter = painterResource(R.drawable.deepseek),
                                        "deepseek"
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.deepseek),
                                        "deepseek",
                                        tint = Color.Gray
                                    )
                                }
                            }
                            else{
                                CircularProgressIndicator(color= EnergeticTeal, strokeCap = StrokeCap.Butt)
                            }
                        }
                    }

                }
            }
        },
        bottomBar = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

                    Text(
                        text = "Anonymous",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 10.dp,end = 8.dp)
                    )
                    Switch(
                        checked = is_anonoymous.value.equals(1),
                        thumbContent = { if (is_anonoymous.value.equals(1)) { Icon(Icons.Default.AccountCircle, contentDescription = "Anonymous", tint=Color.Black, modifier = Modifier.size(SwitchDefaults.IconSize).padding(0.dp))} else {null} },colors=SwitchDefaults.colors(checkedThumbColor = Color.Black, uncheckedThumbColor = Color.Gray, checkedTrackColor = EnergeticTeal, uncheckedTrackColor = Color.DarkGray, checkedBorderColor = EnergeticTeal, uncheckedIconColor = Color.LightGray),
                        onCheckedChange = { is_anonoymous.value = if (it) 1 else 0 },
                    )
                }
                Row(Modifier.horizontalScroll(scrollState), verticalAlignment = Alignment.CenterVertically) {

                    attachedFiles.takeIf { it.isNotEmpty() }?.let {
                        it.forEach { uri ->
                            val type = remember(uri) { getMimeType(context, uri) }
                            println(type)
                            val icon= when {
                                type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                type?.startsWith("audio") == true ->
                                    painterResource(R.drawable.audio)
                                type?.startsWith("video") == true ->
                                    painterResource(R.drawable.video)
                                type == "application/pdf" -> painterResource(R.drawable.pdf)
                                type == "application/msword" ||
                                        type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)
                                type == "application/vnd.ms-powerpoint" ||
                                        type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)
                                else -> painterResource(R.drawable.file)
                            }
                            AssistChip(
                                onClick = {},
                                label = { Text(getFileName(uri, context) ) }, colors = AssistChipDefaults.assistChipColors(containerColor = CleanBlack,labelColor =
                                    Uncleanwhite), leadingIcon = {Image(icon, "", modifier = Modifier.size(40.dp))}, trailingIcon = { IconButton(onClick = {remove.add(uri)}){ Icon(Icons.Default.Close, "close") } }
                            )
                        }
                    }
                }


                Row(
                    Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(EnergeticTeal)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { filePicker.launch("*/*") },Modifier.size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.clip),
                            "Attach",
                            tint = Color.DarkGray
                        )
                    }
                    TextField(
                        value = newAnswer,
                        onValueChange = { newAnswer = it },
                        placeholder = { Text("Write an answer...") },
                        modifier = Modifier.weight(1f), colors = TextFieldDefaults.colors(focusedContainerColor = DeepNittBlue, unfocusedContainerColor = CleanBlack.copy(alpha=0.8f), focusedTextColor = Uncleanwhite)
                    )
                    IconButton(onClick = {
                        if (newAnswer.isNotBlank()) {
                            onSendAnswer(
                                PostAnswer(question.id, newAnswer, is_anonymous = is_anonoymous.value),
                                context,
                                mutableStateOf(attachedFiles.toMutableList()),
                                reload
                            )
                            is_anonoymous.value=0
                            newAnswer = ""
                            attachedFiles.clear()
                        }
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint= CardColor)
                    }
                }
            }

        }, modifier = Modifier.background(CleanBlack).fillMaxSize()
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.background(CleanBlack).fillMaxWidth().padding(innerPadding)) {

            item {
                Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row() {
                        if (question.is_anonymous==0) {
                            bitmap?.let {
                                Image(
                                    bitmap!!.asImageBitmap(),
                                    contentDescription = "Home",
                                    modifier = Modifier.size(50.dp).clip(CircleShape)
                                )
                            } ?: run {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))

                            }
                        }
                        else{
                            Image(
                                painterResource(R.drawable.anonymous),
                                contentDescription = "Home",
                                modifier = Modifier.size(50.dp).clip(CircleShape)
                            )
                        }
                        ChatBubble(navController,text = question.description, question.File, isQuestion = true, question = question, onEdit = {onEdit(it)}, onEditAnswer = { username, userid-> onEditAnswer(username, userid)}, gotoprofile = { username, userid-> gotoprofile(username, userid)})


                    }
                    Text(
                        "By: ${question.username}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray, modifier = Modifier.padding(start = 50.dp)
                    )
                    Text(
                        "${question.created_at.replace("T", " ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray, modifier = Modifier.padding(start = 50.dp)
                    )

                }

            }
            if (attachmentlist.size>0 && bitmaplist.size>0) {
                items(attachmentlist.size) { answer ->
                    val bitmap1 = bitmaplist[answer]
                    val attachment = attachmentlist[answer]
                    var poster= if (answers[answer].is_anonymous.equals(1)) "Anonymous" else answers[answer].username


                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(modifier=Modifier.fillMaxWidth(0.75f), horizontalAlignment = Alignment.End) {
                            Row(horizontalArrangement = Arrangement.End) {
                                ChatBubble(navController,text = answers[answer].content,reload=reload, file = attachment.toMutableList(),upvotes=answers[answer].upvotes, downvotes = answers[answer].downvotes,answer=answers[answer], isQuestion = false, bubbleMaxWidth = bubbleMaxWidth, onEdit = {onEdit(it)}, onEditAnswer = { username, userid-> onEditAnswer(username, userid) }, gotoprofile = { username, userid-> gotoprofile(username, userid)})
                                Spacer(modifier = Modifier.width(8.dp))
                                if (answers[answer].is_anonymous==0) {
                                    bitmap1?.let {
                                        Image(
                                            bitmap1.asImageBitmap(),
                                            contentDescription = "User avatar",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                    } ?: run {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(30.dp).padding(8.dp)
                                        )
                                    }
                                }
                                else{
                                    Image(
                                        painterResource(R.drawable.anonymous),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(50.dp).clip(CircleShape)
                                    )
                                }



                            }
                            Text(
                                "By: ${poster}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray, modifier = Modifier.padding(end = 50.dp)
                            )
                            Text(
                                "${answers[answer].created_at.replace("T", " ")}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray, modifier = Modifier.padding(end = 50.dp)
                            )
                        }

                    }

                }
            }
        }
    }
}
fun Bitmap.copyBitmap(): Bitmap {
    return this.copy(this.config ?: Bitmap.Config.ARGB_8888, true)
}



fun downloadFile(attachmentId: Int, context: Context) {
    apiService.downloadAttachment(attachmentId).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful && response.body() != null) {
                val contentDisposition = response.headers()["Content-Disposition"]
                val fileName = contentDisposition?.substringAfter("filename=")?.replace("\"", "") ?: "file"

                Thread {
                    try {
                        val file = File(context.getExternalFilesDir(null), fileName)
                        val sink = file.sink().buffer()
                        sink.writeAll(response.body()!!.source())
                        sink.close()
                        Handler(Looper.getMainLooper()).post {
                            openFile(context, file)
                        }
                    } catch (e: Exception) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Error saving file: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } else {
                Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }

    })
}
fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    val mimeType = when (file.extension.lowercase()) {
        "pdf" -> "application/pdf"
        "jpg", "jpeg", "png" -> "image/*"
        "mp4" -> "video/*"
        "mp3" -> "audio/*"
        "doc", "docx" -> "application/msword"
        "ppt", "pptx" -> "application/vnd.ms-powerpoint"
        "txt" -> "text/plain"
        else -> "*/*"
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ChatBubble(navController: NavController,text: String, file: MutableList<Attachment>? = null, reload: MutableState<Boolean>?=null, upvotes: Int? = null, downvotes: Int? = null, answer: Answer? = null, isQuestion: Boolean, bubbleMaxWidth: Dp=280.dp, question: Doubtdeets?= null, gotoprofile: (Int, String) -> Unit, onEdit: (question: Doubtdeets) -> Unit?, onEditAnswer: (answer: Answer, attachment: List<Attachment>) -> Unit?) {
    val bubbleColor = if (isQuestion) Color(0xFF0D47A1) else Color(0xFFE3F2FD)
    val textColor = if (isQuestion) Color.White else Color.Black
    val alignment = if (isQuestion) Alignment.Start else Alignment.End
    val poster = if (isQuestion) question?.identity else answer?.user_id
    val isexpanded= remember { mutableStateOf(false) }
    val postername = if (isQuestion) question?.username else answer?.username
    var dropdownExpanded by remember { mutableStateOf(false) }
    val context= LocalContext.current
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isQuestion) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, shape = RoundedCornerShape(16.dp))
                .padding(12.dp)
                .widthIn(max = bubbleMaxWidth)
                .combinedClickable (onClick={isexpanded.value=!isexpanded.value},
                    onLongClick = {
                        if (answer!=null && (answer.is_anonymous==0 || answer.user_id== user_id) || (question!=null  && (question.is_anonymous==0 || question.identity== user_id))) dropdownExpanded = true

                    }
                ), contentAlignment = Alignment.CenterEnd
        ) {
            Column() {
                if (isexpanded.value) {
                    Text(text = text, color = textColor)
                }
                else{
                    Text(text = text, maxLines = 2, overflow = TextOverflow.Ellipsis, color=textColor)
                }
                if (file != null) {
                    Row(Modifier.horizontalScroll(rememberScrollState())) {
                    for (i in file) {

                                val type=i.file_type
                                val icon= when {
                                    type?.startsWith("image") == true ->painterResource(R.drawable.image)
                                    type?.startsWith("audio") == true ->
                                        painterResource(R.drawable.audio)

                                    type?.startsWith("video") == true ->
                                        painterResource(R.drawable.video)

                                    type == "application/pdf" -> painterResource(R.drawable.pdf)
                                    type == "application/msword" ||
                                            type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> painterResource(R.drawable.doc)

                                    type == "application/vnd.ms-powerpoint" ||
                                            type == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> painterResource(R.drawable.ppt)

                                    else -> painterResource(R.drawable.file)
                                }

                                    AssistChip(
                                        onClick = { downloadFile(i.id, context = context) },
                                        shape = RoundedCornerShape(60.dp),
                                        label = {
                                            Text(
                                                i.file_name,
                                                style = MaterialTheme.typography.labelSmall,
                                                maxLines = 1
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = CleanBlack, labelColor =
                                                Uncleanwhite
                                        ),
                                        leadingIcon = {
                                            Image(
                                                icon,
                                                "",
                                                modifier = Modifier.size(if (isQuestion) 40.dp else 30.dp)
                                                    .clip(
                                                        RoundedCornerShape(40.dp)
                                                    )
                                            )
                                        }
                                    )
                                }




                    }
                }
                if (upvotes != null && downvotes!=null && answer!=null) {
                    Row() {
                        IconButton(onClick = {if (answer.user_vote!=1) voteAnswer(answer.id, 1,context, token) {
                            if (reload != null) {
                                reload.value = !reload.value
                            }
                        }
                            else if (answer.user_vote==1) voteAnswer(answer.id, 0,context, token){
                                if (reload != null) {
                                    reload.value=!reload.value
                                }
                        } }) {
                            Row() {
                                if (answer.user_vote!=1) {
                                    Icon(
                                        painterResource(R.drawable.lit),
                                        contentDescription = "Upvote",
                                        tint = Color.Gray, modifier = Modifier.height(20.dp)
                                    )
                                }
                                else{
                                    Image(
                                        painterResource(R.drawable.lit),
                                        contentDescription = "Upvote", modifier=Modifier.height(20.dp)
                                    )
                                }
                                Text(upvotes.toString(), color = CleanBlack, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        IconButton(onClick = {if (answer.user_vote!=-1) voteAnswer(answer.id, -1, context,token){
                            if (reload != null) {
                                reload.value=!reload.value
                            }
                        }
                        else if (answer.user_vote==-1) voteAnswer(answer.id, 0,context, token){
                            if (reload != null) {
                                reload.value=!reload.value
                            }
                        } }) {
                            Row() {
                                if (answer.user_vote!=-1)
                                Icon(
                                    painterResource(R.drawable.trash),
                                    contentDescription = "Downvote",
                                    tint = Color.Gray, modifier=Modifier.height(20.dp)
                                )
                                else Image(
                                    painterResource(R.drawable.trash),
                                    contentDescription = "Upvote",
                                     modifier=Modifier.height(20.dp)
                                )
                                Text(downvotes.toString(), color = CleanBlack, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
            DropdownMenu(containerColor = textColor.copy(alpha=0.7f),
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
                if (poster == user_id) {
                    DropdownMenuItem(
                        text = { Text("Edit", color= bubbleColor) },
                        onClick = {
                            dropdownExpanded = false
                            if (question != null  && isQuestion) {
                                onEdit(question)
                            }
                            if (answer != null && !isQuestion) {
                                if (file != null) {
                                    onEditAnswer(answer, file.toList())
                                }
                                else onEditAnswer(answer, emptyList())
                            }
                        }, leadingIcon = {Icon(Icons.Default.Edit, contentDescription = "Edit", tint = bubbleColor)}
                    )
                    Box(modifier = Modifier.height(2.dp).background(bubbleColor).fillMaxWidth())
                    val context= LocalContext.current
                    DropdownMenuItem(
                        text = { Text("Delete", color= bubbleColor) },
                        onClick = {
                            dropdownExpanded = false
                            if (question != null && isQuestion) {
                                deleteDoubt(question.id, context, reload) {
                                }
                                navController.popBackStack()

                            }
                            if (answer!=null && !isQuestion) {
                                deleteAnswer(answer.id, context, reload) {

                                }
                            }
                        }, leadingIcon = {Icon(Icons.Default.Clear, contentDescription = "Edit", tint = bubbleColor)}
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text("View Profile", color= bubbleColor) },
                        onClick = {
                            dropdownExpanded = false
                            if (poster != null) {
                                if (postername != null) {
                                    gotoprofile(poster, postername)
                                }
                            }
                        }, leadingIcon = {Icon(Icons.Default.Face, contentDescription = "Edit", tint = bubbleColor)}
                    )
                }
            }
        }
    }
}

@Composable
fun DoubtCard(
    doubt: Doubt,
    navController: NavController,
    question: MutableState<Doubtdeets>, reload: MutableState<Boolean>?,
    expanded: MutableState<Boolean>, gotoprofile: (Int, String) -> Unit?= { _, _ -> },  onEdit: (question: Doubtdeets) -> Unit?= { _ -> }
) {
    val currentUserId= user_id
    val poster = if (doubt.is_anonymous == 1) "Anonymous" else doubt.username
    val attachment = remember { mutableStateOf(emptyList<Attachment>()) }
    val number_ans = remember { mutableStateOf(0) }
    val showDropdown = remember { mutableStateOf(false) }
    val dropdownOffset = remember { mutableStateOf(Offset.Zero) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        loadAnswers(doubt.id) { fetchedAnswers ->
            number_ans.value = fetchedAnswers.size
        }
        listAttachments(doubt.id) { attachments ->
            attachment.value = attachments
        }
    }



    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.90f)
            .combinedClickable(
                onClick = {
                    expanded.value = false
                    question.value = Doubtdeets(
                        doubt.id, doubt.title, doubt.description, doubt.tags,
                        doubt.created_at, doubt.is_anonymous, poster, doubt.identity,
                        attachment.value.toMutableList()
                    )
                    navController.navigate("questiondeets")
                },
                onLongClick = {
                    if (doubt.is_anonymous==0 || doubt.identity== user_id) dropdownExpanded = true
                }
            )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardColor, contentColor = Uncleanwhite),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = doubt.created_at.replace("T", " "),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Right
                    )
                }
                Row {
                    Text(text = doubt.title, style = MaterialTheme.typography.titleMedium)
                    if (attachment.value.isNotEmpty()) {
                        Icon(
                            painterResource(R.drawable.clip),
                            contentDescription = "Attachment",
                            modifier = Modifier.size(20.dp),
                            tint = Uncleanwhite
                        )
                    }
                }
                Text(text = doubt.description, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Posted by $poster • ${doubt.tags}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${number_ans.value} Answers",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        DropdownMenu(containerColor = Uncleanwhite,
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
        ) {
            if (doubt.identity == currentUserId) {
                DropdownMenuItem(
                    text = { Text("Edit", color= Backdrop) },
                    onClick = {
                        dropdownExpanded = false
                        onEdit(Doubtdeets(
                            doubt.id, doubt.title, doubt.description, doubt.tags,
                            doubt.created_at, doubt.is_anonymous, poster, doubt.identity,
                            attachment.value.toMutableList()
                        ))
                    }, leadingIcon = {Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Backdrop)}
                )
                Box(modifier = Modifier.height(2.dp).background(Color.Black).fillMaxWidth())
                val context= LocalContext.current
                DropdownMenuItem(
                    text = { Text("Delete", color= Backdrop) },
                    onClick = {
                        dropdownExpanded = false
                        deleteDoubt(doubt.id, context, reload) {
                        }
                    }, leadingIcon = {Icon(Icons.Default.Clear, contentDescription = "Edit", tint = Backdrop)}
                )
            } else {
                DropdownMenuItem(
                    text = { Text("View Profile", color= Backdrop) },
                    onClick = {
                        dropdownExpanded = false
                        gotoprofile(doubt.identity, doubt.username)
                    }, leadingIcon = {Icon(Icons.Default.Face, contentDescription = "Edit", tint = Backdrop)}
                )
            }
        }
    }
}


var DeepNittBlue = Color(26, 35, 126)
var EnergeticTeal = Color(0, 188, 212)
var CleanWhite = Color.White
var Uncleanwhite = Color(255, 255, 255, 200)
var CleanBlack = Color.Black
var CardColor = Color(23,23,63,255)
var Backdrop = Color(0,3,33,255)
var UserCardColor = Color(0xFF182848)
var QsBackdrop = Color(0,1,80,255)
var ChipBack = Color(99,0,107,255)
var Bottombarcolor=Color(0xFF1A237E)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var USER_ID=0
    var USER_NAME=""
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("details") { DetailsScreen(navController) }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Friends(navController: NavController,expanded:MutableState<Boolean>, onBack: () -> Unit, onRefresh: () -> Unit, onClick: (Int, String) -> Unit){
    var selectedDestination by rememberSaveable { mutableStateOf(FriendDestination.LIST) }
    var dest by rememberSaveable { mutableStateOf(mutableListOf(0,0)) }
    val navController1 = rememberNavController()
    BackHandler{
        onBack()
    }
    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth().background(CleanBlack)){
                Row(){
                    Spacer(Modifier.width(10.dp))
                    IconButton(onClick = {expanded.value=false;onBack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Uncleanwhite)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text = "Friends", style = MaterialTheme.typography.titleLarge, color = Uncleanwhite)

                }
            PrimaryTabRow(modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = FriendDestination.entries.indexOf(selectedDestination),
            containerColor = EnergeticTeal,
            contentColor = Color.White,
            indicator = {}


        ) {
            FriendDestination.entries.forEach { destination ->

                if ((destination != FriendDestination.LIST || true)) {
                    Tab(
                        text = { Text(destination.label) },
                        selected = selectedDestination == destination,
                        onClick = {
                            dest[0] = dest[1]
                            dest[1] = destination.index
                            selectedDestination = destination
                            navController1.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true

                                restoreState = true
                            }
                        },

                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
            }
        } }
    ) {innerPadding->
        Column(Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController1,
                startDestination = FriendDestination.LIST.route,
                modifier = Modifier,
                enterTransition = {
                    if (dest[1] - dest[0] > 0) slideInHorizontally { it } + fadeIn() + expandIn(
                        animationSpec = spring(
                            dampingRatio = 0.4f,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) else slideInHorizontally { -it } + fadeIn() + expandIn(
                        animationSpec = spring(
                            dampingRatio = 0.4f,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                },
            ) {
                composable(FriendDestination.LIST.route) {
                    ListFriends(navController) {userid, username -> onClick(userid,username)}

                }
                composable(FriendDestination.PENDING.route) {
                    ListPendingFriends(navController,0) {userid, username -> onClick(userid,username)}
                }
                composable(FriendDestination.SENT.route) {
                    ListPendingFriends(navController,1) {userid, username -> onClick(userid,username)}
                }
            }
        }
    }

}
@Composable
fun ListFriends(navController: NavController, onClick: (Int, String) -> Unit){
    val friends = remember { mutableStateListOf<friend>() }
    val focusManager = LocalFocusManager.current
    var refresh by remember { mutableStateOf(false) }
    var reload = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isLoading by remember { mutableStateOf(false) }
    BackHandler{
        navController.popBackStack()
    }
    LaunchedEffect(refresh, reload.value) {
        friends.clear()
        getfriendList(token){
            friends.addAll(it)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Backdrop)
            .clickable {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(friends.size) { doubt ->
            val doubtcontent=userlist(friends[doubt].id,friends[doubt].username,-4,-6)
            UserCard(doubtcontent, modifier = Modifier.clickable { onClick(friends[doubt].id,friends[doubt].username) }, reload = reload)
        }
        if (isLoading ) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
@Composable
fun ListPendingFriends(navController: NavController, sentornot:Int, onClick: (Int, String) -> Unit){
    val friends = remember { mutableStateListOf<friendreq>() }
    val focusManager = LocalFocusManager.current
    var refresh = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isLoading by remember { mutableStateOf(false) }
    BackHandler{
        navController.popBackStack()
    }
    LaunchedEffect(refresh.value) {
        friends.clear()
        if (sentornot==0){
            fetchPendingRequest(token){
                friends.addAll(it)
            }
        }
        else{
            fetchSentPendingRequest(token){
                friends.addAll(it)
            }
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Backdrop)
            .clickable {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(friends.size) { doubt ->
            val doubtcontent=friends[doubt]
            ReqUserCard(doubtcontent,sentornot,refresh, modifier = Modifier.clickable { onClick(friends[doubt].id,friends[doubt].username) })
        }
        if (isLoading ) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
fun sendNotification(context: Context, notificationId: Int,title: String, body: String, isNotifications: NotificationPrefs) {
    val channelId = "default_channel_id"
    val channelName = "Default Channel"

    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    val flags = PendingIntent.FLAG_IMMUTABLE
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.deepseek)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
    if ((isNotifications.aiReplies && title.contains("using ai"))||(isNotifications.newAnswers && title.contains("answered your question"))||(isNotifications.friendRequests && title.contains("friend request"))||(isNotifications.friendUpdates && title.contains("Your friend"))) {
        manager.notify(notificationId, notificationBuilder.build())
    }
}
@Composable
fun HomeScreen(navController: NavController){
    val colHeightPx = remember { mutableStateOf(1) }
    val colWidthPx = remember { mutableStateOf(1) }
    val screen_no= remember { mutableStateOf(0) }
    val context= LocalContext.current
    BackHandler {
    }
    if (screen_no.value==0) {
        Column(
            modifier = Modifier
                .fillMaxSize().background(Color(0xFF121212))
                .onGloballyPositioned { coordinates ->
                    colHeightPx.value = coordinates.size.height; colWidthPx.value =
                    coordinates.size.width;
                }, verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var density = LocalDensity.current
            var height = with(density) { colHeightPx.value.toDp() }
            var width = with(density) { colWidthPx.value.toDp() }
            var tileWidth = width / 16
            var tileHeight = height / (16 * height / width)
            Image(
                painter = painterResource(id = R.drawable.logo1i),
                contentDescription = "logo",
                Modifier.size(width * 0.7f)
            )
            signinscreen(navController, tileWidth, tileHeight, screen_no)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Notifications permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }

        }
    }

}
fun signwithtoken(
     accessToken: String, navController: NavController,
    isLoading: MutableState<Boolean>, callback: (friend?) -> Unit) {

    val authHeader = "Bearer $accessToken"
    apiService.loginwithtoken(authHeader).enqueue(object : Callback<friend> {
        override fun onResponse(call: Call<friend>, response: Response<friend>) {
            if (response.isSuccessful) {

                callback(response.body())
            } else {
                println("Error: ${response.code()} ${response}" )
                isLoading.value=false
                callback(friend(0,""))
            }
        }

        override fun onFailure(call: Call<friend>, t: Throwable) {
            println("Error: ${t.message}")
            isLoading.value=false
            callback(friend(0,""))
        }
    })

}
@Composable
fun signinscreen(navController: NavController, tileWidth: Dp, tileHeight: Dp, screen_no: MutableState<Int>){
    val isLoading=remember { mutableStateOf(true) }
    var errorMsg = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            val trial_token = SecurePrefs.getToken()
            if (trial_token != null) {
                signwithtoken(
                    trial_token,
                    navController,
                    isLoading = isLoading
                ) {uinfo ->
                    isLoading.value = false
                    if (uinfo != null && uinfo.id !== 0) {
                        user_id = uinfo.id
                        user = uinfo.username
                        token = trial_token
                        navController.navigate("details")
                    }

                }
            } else {
                println("token")
                isLoading.value = false
            }
        } catch (e: Exception) {
            isLoading.value = false

        }
    }
    var username by remember { mutableStateOf("") }
    var emailid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("Done") }
    Box(
        modifier = Modifier.background(Color(0xFF121212)))
    {
        var mode by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier

                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier.clip(RoundedCornerShape(tileHeight))) {
                IconButton(
                    onClick = { mode = 0 },
                    modifier = Modifier
                        .size(tileWidth * 5, tileHeight * 1.5f)
                        .background(if (mode == 0) Color.White else Color.Gray)
                ) {
                    Text(
                        "SIGN IN",
                        color = if (mode == 0) Color.Black else Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
                IconButton(
                    onClick = {  mode = 1},
                    modifier = Modifier
                        .size(tileWidth * 5, tileHeight * 1.5f)
                        .background(if (mode == 1) Color.White else Color.Gray)
                ) {

                    Text(
                        "SIGN UP",
                        color = if (mode == 1) Color.Black else Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(if (mode==1) "Username" else "Email") }, colors=TextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.Gray, focusedContainerColor = Color.Black, unfocusedContainerColor = Color.Black)
            )
            if (mode==1){
                OutlinedTextField(
                    value = emailid,
                    onValueChange = { emailid = it },
                    label = { Text("Email Id") }, colors=TextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.Gray, focusedContainerColor = Color.Black, unfocusedContainerColor = Color.Black)
                )
            }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(), colors=TextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.Gray, focusedContainerColor = Color.Black, unfocusedContainerColor = Color.Black)
            )
            Row(
                modifier = Modifier.padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {

            }
            val context= LocalContext.current
            errorMsg.value?.let { if (it=="Unauthorized") Text("Invalid Username or Password", color = Color.Red) else Text(it, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { if (mode==1){
                    signupUser(username,emailid,password,errorMsg,navController)
                } else{
                    loginUser(username,password,errorMsg,navController) }},
                modifier = Modifier
                    .size(tileWidth * 5, tileHeight * 2),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5722),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(tileHeight)
            ) {
                if (isLoading.value){
                    CircularProgressIndicator(color= EnergeticTeal, strokeCap = StrokeCap.Round)
                }
                else {
                    Text(if (mode == 0) "SIGN IN" else "SIGN UP")
                }
            }
        }

    }
}
@Composable
fun SettingsScreen(
    navController: NavController, profilescreen: MutableState<Boolean>,
    isDarkMode: Boolean,
    isNotifications: NotificationPrefs,
    onToggleDarkMode: () -> Unit,
    onToggleNotification: (Int) -> Unit,
) {
    val context= LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(isDarkMode) }
    var reload by remember { mutableStateOf(false) }
    var Uncleanwhite by remember { mutableStateOf(Uncleanwhite) }
    var DeepNittBlue by remember { mutableStateOf(DeepNittBlue) }
    var Backdrop by remember { mutableStateOf(Backdrop) }
    var CleanBlack by remember { mutableStateOf(CleanBlack) }
    var CleanWhite by remember { mutableStateOf(CleanWhite) }
    var EnergeticTeal by remember { mutableStateOf(EnergeticTeal) }
    LaunchedEffect(darkMode) {
        if (darkMode){
            DeepNittBlue = Color(26, 35, 126)
            EnergeticTeal = Color(0, 188, 212)
            CleanWhite = Color.White
            Uncleanwhite = Color(255, 255, 255, 200)
            CleanBlack = Color.Black
            Backdrop = Color(0,3,33,255)

        }
        else{
            DeepNittBlue = Color(230, 220, 129)
            EnergeticTeal = Color(255, 87, 34)
            CleanWhite = Color.Black
            Uncleanwhite = Color(0, 0, 0, 200)
            CleanBlack = Color.White
            Backdrop = Color(250, 250, 250)
        }
    }

    Scaffold(topBar={
        Row(){
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "$reload", tint= Uncleanwhite)
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = Uncleanwhite
            )
        }
    }) {innerpadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Backdrop)
                .padding(innerpadding)
                .verticalScroll(rememberScrollState())
        ) {


            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDialog.value=true

                    },
                colors = CardDefaults.cardColors(containerColor = CleanBlack),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint= Uncleanwhite)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Edit Profile", style = MaterialTheme.typography.titleMedium, color= Uncleanwhite)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                colors = CardDefaults.cardColors(containerColor = CleanBlack),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                ChangePasswordScreen(Uncleanwhite, CleanBlack, EnergeticTeal) { }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CleanBlack),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.darkmode), contentDescription = null, modifier = Modifier.height(30.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Dark Mode", style = MaterialTheme.typography.titleMedium, color= Uncleanwhite)
                    }

                    Switch(
                        checked = isDarkMode, thumbContent = {if (isDarkMode) Icon(painterResource(R.drawable.dark), contentDescription = null, modifier = Modifier.size(SwitchDefaults.IconSize), tint= Color.White) else Icon(painterResource(R.drawable.light), contentDescription = null, modifier = Modifier.size(SwitchDefaults.IconSize), tint = Color.Black)}, colors = SwitchDefaults.colors(uncheckedThumbColor = Color.White, checkedThumbColor = Color.Black, uncheckedBorderColor = Color.Gray, uncheckedTrackColor = Color.LightGray, checkedTrackColor = Color.DarkGray, checkedBorderColor = Color.DarkGray),
                        onCheckedChange = { darkMode=!darkMode;onToggleDarkMode() },
                    )




        }
    }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CleanBlack),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row() {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint= Uncleanwhite)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Notification Settings", style = MaterialTheme.typography.titleMedium, color= Uncleanwhite)
                    }
                    Spacer(Modifier.height(12.dp))
                    NotificationToggle(DeepNittBlue,EnergeticTeal,"Receieve Notifications", isNotifications.allNotifications)  { onToggleNotification(0)}
                    Spacer(Modifier.height(12.dp))
                    NotificationToggle(DeepNittBlue,EnergeticTeal,"Answer Notifications", isNotifications.newAnswers)  { onToggleNotification(1)}
                    Spacer(Modifier.height(12.dp))
                    NotificationToggle(DeepNittBlue,EnergeticTeal,"Friend Requests Notifications", isNotifications.friendRequests)  { onToggleNotification(2)}
                    Spacer(Modifier.height(12.dp))
                    NotificationToggle(DeepNittBlue,EnergeticTeal,"Friend Update Notifications", isNotifications.friendUpdates)  { onToggleNotification(3)}
                    Spacer(Modifier.height(12.dp))
                    isNotifications.aiReplies?.let {
                        NotificationToggle(DeepNittBlue,EnergeticTeal,"AI answer Notifications",
                            it
                        )  { onToggleNotification(4)}
                    }
                }
            }
        }
    }
    if (showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            EditProfileCard(showDialog, profilescreen) { }
        }
    }
}
@Composable
fun EditProfileCard(showDialog:MutableState<Boolean>, profilescreen: MutableState<Boolean>, onSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        selectedImageUri = it
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            ,colors = CardDefaults.cardColors(containerColor = CleanBlack),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Edit Profile", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("New Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("New Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            Button(onClick = { launcher.launch("image/*") }, colors = ButtonDefaults.elevatedButtonColors(contentColor = Uncleanwhite, containerColor = CardColor)) {
                Text("Choose Profile Picture")
            }

            selectedImageUri?.let {
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val token = SecurePrefs.getToken() ?: return@Button

                val usernameBody = if (username.isNotBlank()) username.toRequestBody("text/plain".toMediaType()) else null
                val emailBody = if (email.isNotBlank()) email.toRequestBody("text/plain".toMediaType()) else null

                val imagePart = selectedImageUri?.let {
                    val stream = context.contentResolver.openInputStream(it)!!
                    val bytes = stream.readBytes()
                    stream.close()
                    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("pfp", "profile.jpg", requestBody)
                }

                apiService.editProfile(usernameBody, emailBody, imagePart, "Bearer $token")
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) showDialog.value=false; if(username!="") user=username; profilescreen.value=!profilescreen.value; onSuccess()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            }, colors=ButtonDefaults.buttonColors(containerColor = EnergeticTeal, contentColor = CleanBlack)) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun ChangePasswordScreen(Uncleanwhite:Color, CleanBlack: Color, EnergeticTeal: Color, onSuccess: () -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var isclicked by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val context= LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).clickable { isclicked=true }) {
        Row() {
            Icon(painter = painterResource(R.drawable.changepassword), "", tint= Uncleanwhite, modifier = Modifier.height(30.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Change Password",
                style = MaterialTheme.typography.titleMedium,
                color = Uncleanwhite
            )
        }
        if (isclicked) {
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Current Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            errorMsg?.let {
                Text(it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                if (newPassword != confirmPassword) {
                    errorMsg = "Passwords do not match"
                    return@Button
                }

                val token = SecurePrefs.getToken() ?: return@Button
                val call = apiService.changePassword(oldPassword, newPassword, "Bearer $token")
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            isclicked=false
                            Toast.makeText(context,"Password Changed",Toast.LENGTH_SHORT)
                            onSuccess()
                        } else {
                            errorMsg = "Failed to change password"
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        errorMsg = "Network error: ${t.message}"
                    }
                })
            }, colors = ButtonDefaults.buttonColors(containerColor = EnergeticTeal, contentColor = CleanBlack)) {
                Text("Change Password")
            }
        }
    }
}

@Composable
fun NotificationToggle(DeepNittBlue: Color, EnergeticTeal: Color,label: String, isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Text(label, color= EnergeticTeal, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Start)
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = EnergeticTeal, checkedTrackColor = DeepNittBlue)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController1: NavController) {
    val Token= token
    val User_id= user_id
    val context = LocalContext.current
    var get= remember{ mutableStateOf(false)}
    var Username=user
    val size = remember { mutableStateOf(0.5f) }
    val isdarkmode= remember{ mutableStateOf(false) }
    val userprofile= remember{ mutableStateOf(false) }
    val isnotification= remember{ mutableStateOf(NotificationPrefs(true,true,true,true,true)) }
    val notifications = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableStateOf(TabDestination.HOME) }
    var dest by rememberSaveable { mutableStateOf(mutableListOf(0,0)) }
    LaunchedEffect(isdarkmode.value) {
        if (isdarkmode.value){
            DeepNittBlue = Color(26, 35, 126)
            EnergeticTeal = Color(0, 188, 212)
            CleanWhite = Color.White
            Uncleanwhite = Color(255, 255, 255, 200)
            CleanBlack = Color.Black
            CardColor = Color(23,23,63,255)
            Backdrop = Color(0,3,33,255)
            UserCardColor = Color(0xFF182848)
            QsBackdrop = Color(0,1,80,255)
            ChipBack = Color(99,0,107,255)
            Bottombarcolor=Color(0xFF1A237E)
        }
        else{
            DeepNittBlue = Color(230, 220, 129)
            EnergeticTeal = Color(255, 87, 34)
            CleanWhite = Color.Black
            Uncleanwhite = Color(0, 0, 0, 200)
            CleanBlack = Color.White
            CardColor = Color(232, 232, 192)
            Backdrop = Color(250, 250, 250)
            UserCardColor = Color(0xFFFFF9C4)
            QsBackdrop = Color(255, 253, 231)
            ChipBack = Color(255, 204, 128)
            Bottombarcolor = Color(255, 236, 179)

        }
    }
    val shapeC = RoundedPolygon.star(
         24,
        rounding = CornerRounding(0.1f),
        innerRadius = size.value,
    )
    val shapeD = RoundedPolygon.star(
        9,
        rounding = CornerRounding(0.3f),
        innerRadius = 0.2f,
    )
    val listState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    val shapeA = RoundedPolygon(
        numVertices = 8,
        rounding = CornerRounding( 0.2f)
    )

    val notifcopy = remember { mutableStateListOf<Notification>() }

    LaunchedEffect(notifications.value) {
        delay(500)
        if (Token!="") {

            getNotification(Token){
                notification->

                if (notification.user_id!=0){
                    sendNotification(context = context,notification.id, notification.title, notification.message, isnotification.value)
                    deleteNotification(notification.id, Token){

                    }

                }

                notifications.value=!notifications.value
            }
        }
        else{
            notifications.value=!notifications.value
        }

    }


    LaunchedEffect(notifcopy.size) {
        if (notifcopy.size==0){

        }
    }
    val shapeB = remember {
        RoundedPolygon.star(
            6,
            rounding = CornerRounding(0.1f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    val morph2 = remember {
        Morph(shapeC, shapeD)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val interactionSource1 = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isPressed1 by interactionSource1.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    val animatedProgress1 = animateFloatAsState(
        targetValue = if (isPressed1) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(User_id, userprofile.value) {
        Username= user
        fetchProfilePicture( User_id,context) { result ->
            bitmap = result

        }
    }
    val settingsFetched = remember { mutableStateOf(false) }
    val initialSyncComplete = remember { mutableStateOf(false) }

    LaunchedEffect(Token) {
        if (Token.isNotEmpty()) {
            getSettings(Token) { settings ->

                isdarkmode.value = settings.dark_mode
                isnotification.value.newAnswers = settings.notify_answers
                isnotification.value.friendUpdates = settings.notify_updates
                isnotification.value.friendRequests = settings.notify_friend_requests
                isnotification.value.aiReplies = settings.notify_ai_answers
                isnotification.value.allNotifications =
                    settings.notify_answers || settings.notify_friend_requests ||
                            settings.notify_updates || settings.notify_ai_answers

                settingsFetched.value = true
            }
        }
    }
    LaunchedEffect(settingsFetched.value) {
        if (settingsFetched.value) {
            delay(2000)
            initialSyncComplete.value = true
        }
    }

    LaunchedEffect(
        isdarkmode.value,
        isnotification.value.newAnswers,
        isnotification.value.friendUpdates,
        isnotification.value.friendRequests,
        isnotification.value.aiReplies
    ) {
        if (initialSyncComplete.value) {
            updateSettings(
                Settings(
                    isdarkmode.value,
                    isnotification.value.newAnswers,
                    isnotification.value.friendRequests,
                    isnotification.value.friendUpdates,
                    isnotification.value.aiReplies ?: false
                ),
                Token
            ) {

            }
        }
    }


    val expanded = remember { mutableStateOf(false) }
    val answer= remember { mutableStateOf(Answer(0, "", "", 0,"",0,0,0,0)) }
    val file = remember { mutableStateOf(emptyList<Attachment>()) }

    Scaffold(
        bottomBar = {
            Box(contentAlignment = Alignment.Center) {
                BottomAppBar(
                    containerColor = Bottombarcolor,
                    contentColor = CleanWhite,
                    tonalElevation = 4.dp
                ) {

                    PrimaryTabRow(
                        selectedTabIndex = TabDestination.entries.indexOf(selectedDestination),
                        containerColor = Color.Transparent,
                        contentColor = CleanWhite,
                        indicator = {}

                        ) {
                        TabDestination.entries.forEach { destination ->

                            if ((destination != TabDestination.HOME || true) && !destination.route.contains("question")) {
                                Tab(
                                    selected = selectedDestination == destination,
                                    onClick = {
                                        expanded.value=false
                                        dest[0]=dest[1]
                                        dest[1]=destination.index
                                        selectedDestination = destination
                                        navController.navigate(destination.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true

                                            restoreState = true
                                        }
                                    },

                                    icon = {
                                        when (val icon = destination.icon) {
                                            is TabIcon.Vector -> Icon(imageVector = icon.imageVector, contentDescription = destination.label, modifier = Modifier.size(30.dp))
                                            is TabIcon.Painter -> Icon(painter = icon.painter, contentDescription = destination.label,modifier = Modifier.size(60.dp))
                                        }
                                    },
                                    selectedContentColor = CleanWhite,
                                    unselectedContentColor = Color.Gray
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .offset(0.dp, -54.dp)
                        .clip(MorphPolygonShape(morph, animatedProgress.value))
                        .background(EnergeticTeal)
                        .clickable(

                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(TabDestination.QUESTION.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true

                                restoreState = true
                            }

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Home", tint = CleanWhite)
                }
            }
        },
        topBar = {
            TopAppBar( title={}, colors= TopAppBarDefaults.topAppBarColors(containerColor = DeepNittBlue), modifier = Modifier.height(20.dp)

            )

        }, modifier = Modifier.background(DeepNittBlue)

    ) { innerPadding ->
        val question = remember { mutableStateOf(Doubtdeets(0,"","","","", 0, "", 0,mutableListOf<Attachment>())) }

        var USER_ID by remember {  mutableStateOf(0)}
        var USER_NAME by remember {  mutableStateOf("")}
        Column(modifier = Modifier.fillMaxSize().clickable { expanded.value=false }.background(Backdrop).padding(innerPadding)) {
            Row(modifier = Modifier.fillMaxWidth().size(80.dp).background(DeepNittBlue), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                Row(){
                    Icon(painter =  painterResource(R.drawable.logo2) , contentDescription = "", tint= CleanWhite)
                }
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {

                    Box(
                        modifier = Modifier
                            .size(70.dp)

                            .clip(MorphPolygonShape(morph2, animatedProgress1.value))
                            .background(CleanBlack)
                            .clickable(
                                interactionSource = interactionSource1,
                                indication = null
                            ) {
                                expanded.value = !expanded.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        bitmap?.let {



                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "Home",

                            modifier = Modifier.fillMaxSize()
                        )
                        } ?: run {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))

                        }
                    }
                }
            }


            NavHost(
                navController = navController,
                startDestination = TabDestination.HOME.route,
                modifier = Modifier,
                enterTransition = {if (dest[1]-dest[0]>0) slideInHorizontally { it } + fadeIn() + expandIn( animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)) else  slideInHorizontally { -it } + fadeIn() + expandIn( animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium))  },
               ) {
                composable(TabDestination.HOME.route) {Home(navController=navController, question = question, expanded=expanded){userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")} }
                composable(TabDestination.FEED.route) { MyDoubts(Username,navController, question, expanded=expanded){queestion -> expanded.value=false; question.value=queestion; navController.navigate("questioneditor")} }
                composable(TabDestination.SEARCH.route) { SearchScreen(navController, question,expanded=expanded, gotoprofile = {userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")}, onEdit = {queestion -> expanded.value=false; question.value=queestion; navController.navigate("questioneditor")}) }
                composable(TabDestination.USERS.route) { UsersScreen(navController = navController, username = Username, expanded=expanded){userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")} }
                composable(TabDestination.QUESTION.route) { questionAdder(userId = User_id, token = Token, navController = navController, expanded=expanded)}
                composable(TabDestination.QUESTIONDEETS.route) { questionDetails(navController,question.value, expanded=expanded, onBack = {expanded.value=false;selectedDestination=TabDestination.HOME;navController.popBackStack()}, onSendAnswer = {postAnswer: PostAnswer, context: Context, attachedFiles: MutableState<MutableList<Uri>>, isLoading: MutableState<Boolean> -> expanded.value=false; postAnswer(postAnswer,context, attachedFiles, token, isLoading ){} }, gotoprofile = {userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")}, onEdit = {queestion -> expanded.value=false; question.value=queestion; navController.navigate("questioneditor")}, onEditAnswer = {Answer,attacment ->  answer.value=Answer; file.value=attacment.toList(); navController.navigate("answeredit")}) }
                composable(TabDestination.QUESTIONPROFILE.route) {ProfileScreen(USER_ID,USER_NAME,selectedDestination, navController,question, expanded=expanded, gotoprofile = {userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")}, onEdit = {queestion -> expanded.value=false; question.value=queestion; navController.navigate("questioneditor")}, onEditAnswer = {Answer,attacment ->  answer.value=Answer; file.value=attacment.toList(); navController.navigate("answeredit")}) }
                composable(TabDestination.QUESTIONFRIENDS.route) { Friends(navController,expanded=expanded, onBack = {expanded.value=false;selectedDestination=TabDestination.HOME;navController.navigate("home")}, onRefresh = {navController.navigate("questionfriends")}, onClick={userid, username -> expanded.value=false;USER_ID=userid; USER_NAME=username; navController.navigate("questionprofile")}) }
                composable("questioneditor") { questionEditor(question = question.value,userId = User_id, token = Token, navController = navController, expanded=expanded)  }
                composable("answeredit") { answerEditor(answer = answer.value, attachment= file.value,userId = User_id, token = Token, navController = navController, expanded=expanded)  }
                composable("settings"){ SettingsScreen(navController = navController,userprofile, isDarkMode = isdarkmode.value, isnotification.value, onToggleDarkMode = {isdarkmode.value=!isdarkmode.value}, onToggleNotification = {number-> if (number==0){isnotification.value.allNotifications=!isnotification.value.allNotifications; isnotification.value.newAnswers=isnotification.value.allNotifications; isnotification.value.friendUpdates=isnotification.value.allNotifications;isnotification.value.friendRequests=isnotification.value.allNotifications; isnotification.value.aiReplies=isnotification.value.allNotifications } else if (number==1){isnotification.value.newAnswers=!isnotification.value.newAnswers; } else if (number==2) {isnotification.value.friendRequests=!isnotification.value.friendRequests} else if (number==3){isnotification.value.friendUpdates=!isnotification.value.friendUpdates} else {isnotification.value.aiReplies=!isnotification.value.aiReplies!!
                }; if (isnotification.value.friendUpdates||isnotification.value.friendRequests||isnotification.value.newAnswers|| isnotification.value.aiReplies!!) isnotification.value.allNotifications=true else isnotification.value.allNotifications=false })}
            }

        }
        AnimatedVisibility(visible = expanded.value, enter = fadeIn(), exit = fadeOut()) {

            Column(verticalArrangement = Arrangement.spacedBy(1.dp), modifier = Modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.End) {
                Spacer(Modifier.height(80.dp))
                Card(colors = CardDefaults.cardColors(containerColor = CleanWhite), modifier = Modifier.width(130.dp).clickable {navController.navigate("questionfriends"); expanded.value=false  },   elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp),  horizontalArrangement = Arrangement.SpaceEvenly) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = CleanBlack
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Friends", color = CleanBlack)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CleanWhite), modifier = Modifier.width(130.dp).clickable { expanded.value=false; navController.navigate("settings") },   elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Favorite",
                            tint = CleanBlack
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Settings", color = CleanBlack)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CleanWhite), modifier = Modifier.width(130.dp).clickable { USER_ID=user_id; USER_NAME=Username; navController.navigate("questionprofile"); expanded.value=false  },   elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Favorite",
                            tint = CleanBlack
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Profile", color = CleanBlack)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CleanWhite), modifier = Modifier.width(130.dp).clickable {navController1.navigate("home"); SecurePrefs.clearToken(); token=""; user=""; user_id=0 ; expanded.value=false },   elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = CleanBlack
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Logout", color = CleanBlack)
                    }
                }
            }
        }

    }
}
@Composable
fun CustomTabIndicator(tabPositions: List<TabPosition>, selectedIndex: Int) {
    val transition = updateTransition(targetState = selectedIndex, label = "TabIndicator")

    val indicatorLeft by transition.animateDp(label = "Left") {
        tabPositions[it].left
    }
    val indicatorRight by transition.animateDp(label = "Right") {
        tabPositions[it].right
    }

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(horizontal = 16.dp)
            .height(4.dp)
            .background(EnergeticTeal, RoundedCornerShape(50))
    )
}


fun navigate(nextpage:String,navController: NavController) {
    navController.navigate(nextpage)
}

class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        matrix.rotateZ(30*percentage)
        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AskNITTTheme {
        Greeting("Android")
    }
}