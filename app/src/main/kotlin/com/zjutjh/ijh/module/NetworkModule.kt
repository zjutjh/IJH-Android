package com.zjutjh.ijh.module

import android.util.Log
import com.zjutjh.ijh.network.interceptor.APIExceptionInterceptor
import com.zjutjh.ijh.network.interceptor.WeJHAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.time.Duration

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun okHttpClientCommonBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(APIExceptionInterceptor())
            .readTimeout(Duration.ofSeconds(5))
    }

    @DefaultOkHttpClient
    @Provides
    fun provideDefaultOkHttpClient(): OkHttpClient {
        Log.i("Module", "provided DefaultOkHttpClient")

        return okHttpClientCommonBuilder().build()
    }

    @WeJHAuthOkHttpClient
    @Provides
    fun provideWeJHAuthInterceptorOkHttpClient(interceptor: WeJHAuthInterceptor): OkHttpClient {
        Log.i("Module", "provided WeJHAuthOKHttpClient")

        return okHttpClientCommonBuilder()
            .addInterceptor(interceptor)
            .build()
    }
}