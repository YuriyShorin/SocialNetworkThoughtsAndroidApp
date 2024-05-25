package hse.course.socialnetworkthoughtsandroidapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hse.course.socialnetworkthoughtsandroidapp.adapter.CommentsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.CurrentProfilePostsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.FeedAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.PostsAdapter
import hse.course.socialnetworkthoughtsandroidapp.adapter.ProfilesAdapter
import hse.course.socialnetworkthoughtsandroidapp.model.CreateComment
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
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
import java.io.File
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
        MutableStateFlow(ProfilesAdapter(ArrayList(), ::subscribe, ::unsubscribe))
    val searchProfilesAdapter: StateFlow<ProfilesAdapter> =
        this._searchProfilesAdapter.asStateFlow()

    private val _searchPostsAdapter =
        MutableStateFlow(FeedAdapter(ArrayList(), ::likePost, ::unlikePost))
    val searchPostsAdapter: StateFlow<FeedAdapter> = _searchPostsAdapter.asStateFlow()

    private val _subscriptionsAdapter =
        MutableStateFlow(ProfilesAdapter(ArrayList(), ::subscribe, ::unsubscribe))
    val subscriptionsAdapter: StateFlow<ProfilesAdapter> = _subscriptionsAdapter.asStateFlow()

    private val _subscribersAdapter =
        MutableStateFlow(ProfilesAdapter(ArrayList(), ::subscribe, ::unsubscribe))
    val subscribersAdapter: StateFlow<ProfilesAdapter> = _subscribersAdapter.asStateFlow()

    private val _feedAdapter = MutableStateFlow(FeedAdapter(ArrayList(), ::likePost, ::unlikePost))
    val feedAdapter: StateFlow<FeedAdapter> = _feedAdapter.asStateFlow()

    private val _commentsAdapter = MutableStateFlow(CommentsAdapter(ArrayList()))
    val commentsAdapter: StateFlow<CommentsAdapter> = _commentsAdapter.asStateFlow()

    private var images: List<File>? = null

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

    fun createPost(theme: String, content: String) {
        val themeBody = RequestBody.create(MediaType.parse("text/plain"), theme)
        val contentBody = RequestBody.create(MediaType.parse("text/plain"), content)
        val images: List<MultipartBody.Part>? = getImagesFromData()
        viewModelScope.launch {
            _code.value = postRepository.createPost(themeBody, contentBody, images)
        }
    }

    fun searchProfiles(nickname: String) {
        if (nickname.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val profiles = searchRepository.searchProfiles(nickname)
            if (profiles != null) {
                this@SocialMediaViewModel._searchProfilesAdapter.value =
                    ProfilesAdapter(profiles, ::subscribe, ::unsubscribe)
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

    fun getSubscriptions(profileId: UUID?) {
        viewModelScope.launch {
            val profiles: List<SearchProfile>? = if (profileId == null) {
                profileRepository.getCurrentProfileSubscriptions()
            } else {
                profileRepository.getProfileSubscriptions(profileId)
            }

            if (profiles != null) {
                this@SocialMediaViewModel._subscriptionsAdapter.value =
                    ProfilesAdapter(profiles, ::subscribe, ::unsubscribe)
            }
        }
    }

    fun getSubscribers(profileId: UUID?) {
        viewModelScope.launch {
            val profiles: List<SearchProfile>? = if (profileId == null) {
                profileRepository.getCurrentProfileSubscribers()
            } else {
                profileRepository.getProfileSubscribers(profileId)
            }

            if (profiles != null) {
                this@SocialMediaViewModel._subscribersAdapter.value =
                    ProfilesAdapter(profiles, ::subscribe, ::unsubscribe)
            }
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

    fun getComments(postId: UUID) {
        viewModelScope.launch {
            val comments = postRepository.getComments(postId)
            if (comments != null) {
                _commentsAdapter.value = CommentsAdapter(comments)
            }
        }
    }

    fun commentPost(createComment: CreateComment) {
        viewModelScope.launch {
            _code.value = postRepository.commentPost(createComment)
        }.invokeOnCompletion {
            getComments(createComment.postId)
        }
    }

    fun setImages(images: List<File>) {
        this.images = images
    }

    private fun getImagesFromData(): List<MultipartBody.Part>? {
        return images?.let { images ->
            buildList {
                images.forEach { image ->
                    val imageBody = RequestBody.create(MediaType.parse("image/*"), image)
                    add(MultipartBody.Part.createFormData("images", image.name, imageBody))
                }
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