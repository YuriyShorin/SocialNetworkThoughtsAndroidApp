package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.databinding.CurrentProfileFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.utils.ImagesUtils
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel

import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentProfileFragment : Fragment() {

    private lateinit var binding: CurrentProfileFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    private var profileNickname: String? = null

    private var profileStatus: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentProfileFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.postsRecyclerView.layoutManager = linearLayoutManager

        binding.subscriptionsButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_holder, SubscriptionsFragment(null))
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.subscribersButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_holder, SubscribersFragment(null))
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.profileAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_holder, ProfileSettingsFragment(profileNickname, profileStatus))
                        ?.addToBackStack(null)
                        ?.commit()
                    true
                }

                else -> false
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.currentProfile.collect { profile ->
                    binding.profileAppBar.title = profile.nickname
                    binding.statusTextView.text = profile.status
                    binding.subscriptionsButton.text =
                        getString(R.string.subscribes, profile.subscribes)
                    binding.subscribersButton.text =
                        getString(R.string.subscribers, profile.subscribers)
                    profile.profileImage?.let { Log.d("info", it) }
                    if (profile.profileImage != null) {
                        val profileImage = ImagesUtils.base64ToBitmap(profile.profileImage)
                        if (profileImage != null) {
                            binding.profilePicture.setImageBitmap(profileImage)
                        }
                    }
                    profileNickname = profile.nickname
                    profileStatus = profile.status

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.currentProfilePostsAdapter.collect { currentProfilePostsAdapter ->
                    binding.postsRecyclerView.adapter = currentProfilePostsAdapter
                }
            }
        }

        socialMediaViewModel.getCurrentProfile()
    }
}
