package hse.course.socialnetworkthoughtsandroidapp.viewmodel

import android.content.ClipData
import android.content.Intent
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import hse.course.socialnetworkthoughtsandroidapp.adapter.CurrentProfilePostsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.FeedAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.PostsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.SearchProfilesAdapter
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    private val _searchProfilesAdapter =
        MutableStateFlow(SearchProfilesAdapter(ArrayList(), ::subscribe, ::unsubscribe))
    val searchProfilesAdapter: StateFlow<SearchProfilesAdapter> =
        _searchProfilesAdapter.asStateFlow()

    private val _searchPostsAdapter =
        MutableStateFlow(FeedAdapter(ArrayList(), ::likePost, ::unlikePost))
    val searchPostsAdapter: StateFlow<FeedAdapter> = _searchPostsAdapter.asStateFlow()

    private val _feedAdapter = MutableStateFlow(FeedAdapter(ArrayList(), ::likePost, ::unlikePost))
    val feedAdapter: StateFlow<FeedAdapter> = _feedAdapter.asStateFlow()

    private var clipData: ClipData? = null

    private val _currentProfilePostsAdapter =
        MutableStateFlow(
            CurrentProfilePostsAdapter(
                ArrayList(),
                Profile(),
                ::likePost,
                ::unlikePost,
                ::deletePost
            )
        )
    val currentProfilePostsAdapter: StateFlow<CurrentProfilePostsAdapter> =
        _currentProfilePostsAdapter.asStateFlow()

    private val _currentProfile = MutableStateFlow(Profile())
    val currentProfile: StateFlow<Profile> = _currentProfile.asStateFlow()

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    private val _postsAdapter =
        MutableStateFlow(
            PostsAdapter(
                ArrayList(),
                Profile(),
                ::likePost,
                ::unlikePost,
            )
        )
    val postsAdapter: StateFlow<PostsAdapter> =
        _postsAdapter.asStateFlow()


    private val _code = MutableStateFlow(0)
    val code: StateFlow<Int> = _code.asStateFlow()

    fun getCurrentProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getCurrentProfile()
            if (profile?.posts != null) {
                _currentProfile.value = profile
                _currentProfilePostsAdapter.value =
                    CurrentProfilePostsAdapter(
                        profile.posts,
                        profile,
                        ::likePost,
                        ::unlikePost,
                        ::deletePost
                    )
            }
        }
    }

    fun getProfile(profileId: UUID) {
        viewModelScope.launch {
            val profile = profileRepository.getProfile(profileId)
            if (profile?.posts != null) {
                _profile.value = profile
                _postsAdapter.value =
                    PostsAdapter(
                        profile.posts,
                        profile,
                        ::likePost,
                        ::unlikePost
                    )
            }
        }
    }

    fun setClipData(data: Intent?) {
        this.clipData = data?.clipData
    }

    private fun getMultipartBodyFromData(): MultipartBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        for (i: Int in 0..<clipData?.itemCount!!) {
            val uri = clipData?.getItemAt(i)?.uri
            val file = uri?.toFile()

            file?.let {
                RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    it
                )
            }?.let {
                builder.addFormDataPart(
                    "files",
                    file.name,
                    it
                )
            }
        }
        return builder.build()
    }

    fun createPost(theme: String, content: String) {
        if (clipData != null && clipData?.itemCount!! > 0) {
            val multipartBody = getMultipartBodyFromData()
            viewModelScope.launch {
                postRepository.createWithFiles(theme, content, multipartBody)
            }
        } else {
            viewModelScope.launch {
                _code.value = postRepository.createPost(CreatePost(theme, content))
            }
        }
    }

    fun searchProfiles(nickname: String) {
        if (nickname.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val profiles = searchRepository.searchProfiles(nickname)
            if (profiles != null) {
                _searchProfilesAdapter.value =
                    SearchProfilesAdapter(profiles, ::subscribe, ::unsubscribe)
            }
        }
    }

    fun searchPosts(theme: String) {
        if (theme.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val feed = searchRepository.searchPosts(theme)
            if (feed != null) {
                _searchPostsAdapter.value = FeedAdapter(feed, ::likePost, ::unlikePost)
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

    private fun deletePost(postId: UUID) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }
}