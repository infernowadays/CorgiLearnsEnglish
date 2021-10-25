package com.lazysecs.data.api

import com.lazysecs.domain.models.Text
import com.lazysecs.domain.models.microsoft.detekt_language.DetectResponse
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupResponse
import com.lazysecs.domain.models.microsoft.translate.TranslateResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchService {

    @POST("/translate")
    fun translate(
        @Query("from") from: String,
        @Query("to") to: String?,
        @Body text: ArrayList<Text>,
    ): Single<Response<List<TranslateResponse>>>

    @POST("/dictionary/lookup")
    fun dictionaryLookup(
        @Query("from") from: String,
        @Query("to") to: String,
        @Body text: ArrayList<Text>,
    ): Single<Response<List<DictionaryLookupResponse>>>

    @POST("/detect")
    fun detect(
        @Body text: ArrayList<Text>,
    ): Single<Response<List<DetectResponse>>>
}