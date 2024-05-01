package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences
import hse.course.socialnetworkthoughtsandroidapp.api.SearchService
import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val searchService: SearchService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun searchProfiles(nickname: String): List<SearchProfile>? {
        val jwtToken = getJwtToken()
        var profiles: List<SearchProfile>? = null

        val response = searchService.searchProfiles("Bearer $jwtToken", nickname)
        if (response.code() == 200) {
            profiles = response.body()
        }

        return profiles
    }

    suspend fun searchPosts(theme: String): List<Feed>? {
        val jwtToken = getJwtToken()
        var feed: List<Feed>? = null

        val response = searchService.searchPosts("Bearer $jwtToken", theme)
        if (response.code() == 200) {
            feed = response.body()
        }

        return feed
    }
}