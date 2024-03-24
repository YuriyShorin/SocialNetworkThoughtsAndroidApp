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
import hse.course.socialnetworkthoughtsandroidapp.databinding.FeedFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding: FeedFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FeedFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.feedRecyclerView.layoutManager = linearLayoutManager

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.feedAdapter.collect { feedAdapter ->
                    binding.feedRecyclerView.adapter = feedAdapter
                }
            }
        }

        socialMediaViewModel.getFeed()
    }
}