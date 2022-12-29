package com.zjutjh.ijh.module

import android.util.Log
import com.zjutjh.ijh.network.APIResultCallAdapterFactory
import com.zjutjh.ijh.network.service.ServiceURL
import com.zjutjh.ijh.network.service.WeJHAuthorizedService
import com.zjutjh.ijh.network.service.WeJHService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private fun retrofitCommonBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(APIResultCallAdapterFactory())
    }

    @Singleton
    @Provides
    fun provideWeJhService(@DefaultOkHttpClient client: OkHttpClient): WeJHService {
        Log.i("Module", "provided WeJHService")

        return retrofitCommonBuilder()
            .baseUrl(ServiceURL.WE_JH_API_BASE_URL)
            .client(client)
            .build()
            .create(WeJHService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeJHAuthorizedService(
        @WeJHAuthOkHttpClient client: OkHttpClient
    ): WeJHAuthorizedService {
        Log.i("Module", "provided WeJHAuthorizedService")

        return retrofitCommonBuilder()
            .baseUrl(ServiceURL.WE_JH_API_BASE_URL)
            .client(client)
            .build()
            .create(WeJHAuthorizedService::class.java)
    }
}