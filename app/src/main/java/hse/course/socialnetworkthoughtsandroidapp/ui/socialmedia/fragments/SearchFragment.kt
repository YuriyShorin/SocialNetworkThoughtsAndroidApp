package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.adapter.SearchPagerAdapter
import hse.course.socialnetworkthoughtsandroidapp.databinding.SearchFragmentLayoutBinding

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchPagerAdapter = SearchPagerAdapter(this)
        val searchPager = binding.searchPager
        searchPager.adapter = searchPagerAdapter

        val searchTabLayout = binding.searchTabLayout
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Профиль"), 0)
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Пост"), 1)

        TabLayoutMediator(searchTabLayout, searchPager) { tab, position ->
            if (position == 0) {
                tab.text = "Профиль"
            } else {
                tab.text = "Пост"
            }
        }.attach()

        searchPager.currentItem = 0
    }
}