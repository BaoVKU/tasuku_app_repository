package com.example.tasuku.data

import android.content.SharedPreferences
import android.util.Log
import com.example.tasuku.data.repositories.AuthenticationRepository
import com.example.tasuku.data.repositories.AuthenticationRepositoryImpl
import com.example.tasuku.data.repositories.GroupRepository
import com.example.tasuku.data.repositories.GroupRepositoryImpl
import com.example.tasuku.data.repositories.ScheduleRepository
import com.example.tasuku.data.repositories.ScheduleRepositoryImpl
import com.example.tasuku.data.repositories.TaskRepository
import com.example.tasuku.data.repositories.TaskRepositoryImpl
import com.example.tasuku.data.repositories.UserRepository
import com.example.tasuku.data.repositories.UserRepositoryImpl
import com.example.tasuku.data.repositories.WorkspaceRepository
import com.example.tasuku.data.repositories.WorkspaceRepositoryImpl
import com.example.tasuku.network.AuthenticationApiService
import com.example.tasuku.network.GroupApiService
import com.example.tasuku.network.ScheduleApiService
import com.example.tasuku.network.TaskApiService
import com.example.tasuku.network.UserApiService
import com.example.tasuku.network.WorkspaceApiService
import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseUrl {
    const val URL = "http://192.168.1.9:7749/"
    const val API_URL = "http://192.168.1.9:7749/api/"
}

interface AppContainer {
    val authenticationRepository: AuthenticationRepository
    val userRepository: UserRepository
    val sharedPreferences: SharedPreferences
    val workspaceRepository: WorkspaceRepository
    val taskRepository: TaskRepository
    val groupRepository: GroupRepository
    val scheduleRepository: ScheduleRepository
}

class DefaultAppContainer(private val appSharedPreferences: SharedPreferences): AppContainer {

    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor {
            chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + sharedPreferences.getString("api_token", ""))
                .build()
            chain.proceed(request)
        }
//        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val authenticationApiService: AuthenticationApiService by lazy {
        retrofit.create(AuthenticationApiService::class.java)
    }

    private val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    private val workspaceApiService: WorkspaceApiService by lazy {
        retrofit.create(WorkspaceApiService::class.java)
    }

    private val taskApiService: TaskApiService by lazy {
        retrofit.create(TaskApiService::class.java)
    }

    private val groupApiService: GroupApiService by lazy {
        retrofit.create(GroupApiService::class.java)
    }

    private val scheduleApiService: ScheduleApiService by lazy {
        retrofit.create(ScheduleApiService::class.java)
    }

    override val sharedPreferences: SharedPreferences by lazy {
        appSharedPreferences
    }

    override val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryImpl(authenticationApiService)
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userApiService)
    }

    override val workspaceRepository: WorkspaceRepository by lazy {
        WorkspaceRepositoryImpl(workspaceApiService)
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(taskApiService)
    }

    override val groupRepository: GroupRepository by lazy {
        GroupRepositoryImpl(groupApiService)
    }

    override val scheduleRepository: ScheduleRepository by lazy {
        ScheduleRepositoryImpl(scheduleApiService)
    }
}