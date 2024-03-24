package hse.course.socialnetworkthoughtsandroidapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import hse.course.socialnetworkthoughtsandroidapp.adapter.CurrentProfilePostsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.FeedAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.ProfilesAdapter
import hse.course.socialnetworkthoughtsandroidapp.model.CreatePost
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.repository.FeedRepository
import hse.course.socialnetworkthoughtsandroidapp.repository.PostRepository
import hse.course.socialnetworkthoughtsandroidapp.repository.ProfileRepository
import hse.course.socialnetworkthoughtsandroidapp.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

import javax.inject.Inject

@HiltViewModel
class SocialMediaViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    private val searchRepository: SearchRepository,
    private val feedRepository: FeedRepository
) :
    ViewModel() {

    private val _profilesAdapter =
        MutableStateFlow(ProfilesAdapter(ArrayList(), ::subscribe, ::unsubscribe))
    val profilesAdapter: StateFlow<ProfilesAdapter> = _profilesAdapter.asStateFlow()

    private val _feedAdapter = MutableStateFlow(FeedAdapter(ArrayList(), ::likePost, ::unlikePost))
    val feedAdapter: StateFlow<FeedAdapter> = _feedAdapter.asStateFlow()

    private val _currentProfilePostsAdapter =
        MutableStateFlow(
            CurrentProfilePostsAdapter(
                ArrayList(),
                Profile(),
                ::likePost,
                ::unlikePost
            )
        )
    val currentProfilePostsAdapter: StateFlow<CurrentProfilePostsAdapter> =
        _currentProfilePostsAdapter.asStateFlow()

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    private val _code = MutableStateFlow(0)
    val code: StateFlow<Int> = _code.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            if (profile?.posts != null) {
                _profile.value = profile
                _currentProfilePostsAdapter.value =
                    CurrentProfilePostsAdapter(profile.posts, profile, ::likePost, ::unlikePost)
            }
        }
    }

    fun createPost(theme: String, content: String) {
        viewModelScope.launch {
            _code.value = postRepository.createPost(CreatePost(theme, content))
        }
    }

    fun searchProfiles(nickname: String) {
        if (nickname.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val profiles = searchRepository.searchProfiles(nickname)
            if (profiles != null) {
                _profilesAdapter.value = ProfilesAdapter(profiles, ::subscribe, ::unsubscribe)
            }
        }
    }

    private fun subscribe(id: UUID) {
        viewModelScope.launch {
            profileRepository.subscribe(id)
        }
    }

    private fun unsubscribe(id: UUID) {
        viewModelScope.launch {
            profileRepository.unsubscribe(id)
        }
    }

    fun getFeed() {
        viewModelScope.launch {
            val feed = feedRepository.getFeed()
            if (feed != null) {
                _feedAdapter.value = FeedAdapter(feed, ::likePost, ::unlikePost)
            }
        }
    }

    private fun likePost(postId: UUID) {
        viewModelScope.launch {
            postRepository.likePost(postId)
        }
    }

    private fun unlikePost(postId: UUID) {
        viewModelScope.launch {
            postRepository.unlikePost(postId)
        }
    }
}