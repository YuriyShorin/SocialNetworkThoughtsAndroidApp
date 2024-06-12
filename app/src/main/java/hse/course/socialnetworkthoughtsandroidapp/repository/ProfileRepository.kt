package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences

import hse.course.socialnetworkthoughtsandroidapp.api.ProfileService
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.UUID

import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val profileService: ProfileService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun getCurrentProfile(): Profile? {
        val jwtToken = getJwtToken()
        var profile: Profile? = null

        val response = profileService.getCurrentProfile("Bearer $jwtToken")
        if (response.code() == 200) {
            profile = response.body()
        }

        return profile
    }

    suspend fun getProfile(profileId: UUID): Profile? {
        val jwtToken = getJwtToken()
        var profile: Profile? = null

        val response = profileService.getProfile("Bearer $jwtToken", profileId)
        if (response.code() == 200) {
            profile = response.body()
        }

        return profile
    }

    suspend fun subscribe(profileId: UUID) {
        val jwtToken = getJwtToken()

        profileService.subscribe("Bearer $jwtToken", profileId)
    }

    suspend fun unsubscribe(profileId: UUID) {
        val jwtToken = getJwtToken()

        profileService.unsubscribe("Bearer $jwtToken", profileId)
    }

    suspend fun getCurrentProfileSubscriptions(): List<SearchProfile>? {
        val jwtToken = getJwtToken()
        var profiles: List<SearchProfile>? = null

        val response = profileService.getCurrentProfileSubscriptions("Bearer $jwtToken")
        if (response.code() == 200) {
            profiles = response.body()
        }

        return profiles
    }

    suspend fun getProfileSubscriptions(profileId: UUID): List<SearchProfile>? {
        val jwtToken = getJwtToken()
        var profiles: List<SearchProfile>? = null

        val response = profileService.getProfileSubscriptions("Bearer $jwtToken", profileId)
        if (response.code() == 200) {
            profiles = response.body()
        }

        return profiles
    }

    suspend fun getCurrentProfileSubscribers(): List<SearchProfile>? {
        val jwtToken = getJwtToken()
        var profiles: List<SearchProfile>? = null

        val response = profileService.getCurrentProfileSubscribers("Bearer $jwtToken")
        if (response.code() == 200) {
            profiles = response.body()
        }

        return profiles
    }

    suspend fun getProfileSubscribers(profileId: UUID): List<SearchProfile>? {
        val jwtToken = getJwtToken()
        var profiles: List<SearchProfile>? = null

        val response = profileService.getProfileSubscribers("Bearer $jwtToken", profileId)
        if (response.code() == 200) {
            profiles = response.body()
        }

        return profiles
    }

    suspend fun updateProfile(
        nickname: RequestBody,
        status: RequestBody,
        image: MultipartBody.Part?
    ) {
        val jwtToken = getJwtToken()
        profileService.updateProfile("Bearer $jwtToken", nickname, status, image)
    }
}