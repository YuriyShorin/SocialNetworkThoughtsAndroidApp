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
import hse.course.socialnetworkthoughtsandroidapp.databinding.SearchFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.profilesRecyclerView.layoutManager = linearLayoutManager

        binding.searchButton.setOnClickListener {
            val nickname = binding.searchInputLayout.editText?.text.toString()
            socialMediaViewModel.searchProfiles(nickname)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.profilesAdapter.collect { profilesAdapter ->
                    binding.profilesRecyclerView.adapter = profilesAdapter
                }
            }
        }
    }
}