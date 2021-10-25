package com.lazysecs.data.interactors

import com.lazysecs.data.api.SearchService
import com.lazysecs.domain.models.Result
import com.lazysecs.domain.models.Text
import com.lazysecs.domain.models.microsoft.detekt_language.DetectResponse
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupResponse
import com.lazysecs.domain.models.microsoft.translate.Translation
import io.reactivex.rxjava3.core.Single

class SearchInteractor(private val searchService: SearchService) {

    fun translate(text: String, from: String, to: String): Single<Result<List<Translation>>> {
        return searchService.translate(from = from, to = to, text = arrayListOf(Text(text))).map { response ->
            // it.translations[0] cause list contains different languages if set, but in case of app not used
            val translations = response.body()?.map { it.translations[0] }
            if (response.isSuccessful && !translations.isNullOrEmpty()) {
                Result.Success(translations)
            } else {
                Result.Error(Exception(response.message()))
            }
        }
    }

    fun dictionaryLookup(text: String, from: String, to: String): Single<Result<List<DictionaryLookupResponse>>> {
        return searchService.dictionaryLookup(from = from, to = to, text = arrayListOf(Text(text))).map { response ->
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception(response.message()))
            }
        }
    }

    fun detect(text: String): Single<Result<DetectResponse>> {
        return searchService.detect(text = arrayListOf(Text(text))).map { response ->
            if (response.isSuccessful) {
                Result.Success(response.body()!![0])
            } else {
                Result.Error(Exception(response.message()))
            }
        }
    }
}
