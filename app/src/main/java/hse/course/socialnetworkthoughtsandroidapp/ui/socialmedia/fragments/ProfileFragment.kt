package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
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
import hse.course.socialnetworkthoughtsandroidapp.databinding.ProfileFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel

import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.postsRecyclerView.layoutManager = linearLayoutManager

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.profile.collect { profile ->
                    binding.profileAppBar.title = profile.nickname
                    binding.statusTextView.text = profile.status
                    binding.subscribesButton.text =
                        getString(R.string.subscribes, profile.subscribes)
                    binding.subscribersButton.text =
                        getString(R.string.subscribers, profile.subscribers)
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

        socialMediaViewModel.getProfile()
    }
}
