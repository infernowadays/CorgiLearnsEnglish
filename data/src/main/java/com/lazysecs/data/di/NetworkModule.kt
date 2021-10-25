package com.lazysecs.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        private const val MICROSOFT_TRANSLATOR_URL = "https://api.cognitive.microsofttranslator.com"
        private const val BASE_URL = "https://"
    }

    @Provides
    @Named("query")
    @Singleton
    fun provideQueryInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api-version", "3.0")
                .build()
            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Named("logging")
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Named("headers")
    @Singleton
    fun provideHeadersInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val request: Request = original.newBuilder()
                .addHeader("Ocp-Apim-Subscription-Key", "dd0ee09df8204c8ca297dc8dfefde93a")
                .addHeader("Ocp-Apim-Subscription-Region", "westeurope")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    fun provideOkHttpClient(
        @Named("headers") headersInterceptor: Interceptor,
        @Named("logging") httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("query") queryInterceptor: Interceptor,
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(queryInterceptor)
        okHttpBuilder.addInterceptor(headersInterceptor)
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)
        return okHttpBuilder.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Named("microsoft")
    @Provides
    fun provideMicrosoftRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MICROSOFT_TRANSLATOR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Named("base")
    @Provides
    fun provideBaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}