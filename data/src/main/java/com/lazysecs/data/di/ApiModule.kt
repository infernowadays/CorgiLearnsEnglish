package com.lazysecs.data.di

import com.lazysecs.data.api.SearchService
import com.lazysecs.data.api.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class ApiModule {

    @Provides
    fun provideSearchService(@Named("microsoft") retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    fun provideBaseService(@Named("base") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}