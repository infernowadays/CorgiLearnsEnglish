package com.lazysecs.data.api

import retrofit2.Response
import retrofit2.http.POST

interface UserService {

    @POST("/auth/create_account")
    suspend fun createAccount(

    ): Response<Unit>

    @POST("/auth/login")
    suspend fun login(

    ): Response<Unit>
}