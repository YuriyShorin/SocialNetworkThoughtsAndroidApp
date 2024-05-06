package hse.course.socialnetworkthoughtsandroidapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.SearchPostsFragment
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.SearchProfilesFragment

class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return SearchProfilesFragment()
        }
        return SearchPostsFragment()
    }
}