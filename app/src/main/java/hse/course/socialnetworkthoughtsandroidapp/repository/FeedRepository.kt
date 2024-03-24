package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences
import hse.course.socialnetworkthoughtsandroidapp.api.FeedService
import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val feedService: FeedService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun getFeed(): List<Feed>? {
        val jwtToken = getJwtToken()
        var feed: List<Feed>? = null

        val response = feedService.getFeed("Bearer $jwtToken")
        if (response.code() == 200) {
            feed = response.body()
        }

        return feed
    }
}