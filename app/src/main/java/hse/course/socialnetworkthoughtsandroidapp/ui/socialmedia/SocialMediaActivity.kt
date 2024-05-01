package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.R

import hse.course.socialnetworkthoughtsandroidapp.databinding.SocialMediaActivityLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.FeedFragment
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.NewPostFragment
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.NotificationFragment
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.CurrentProfileFragment
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.SearchFragment

@AndroidEntryPoint
class SocialMediaActivity : AppCompatActivity() {

    private lateinit var binding: SocialMediaActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SocialMediaActivityLayoutBinding.inflate(layoutInflater)
        init()
    }

    private fun init() {
        supportActionBar?.hide()
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, FeedFragment())
            .commit()

        binding.socialMediaNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, FeedFragment()).commit()
                    true
                }

                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, SearchFragment()).commit()
                    true
                }

                R.id.new_post -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, NewPostFragment()).commit()
                    true
                }

                R.id.notifications -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, NotificationFragment()).commit()
                    true
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, CurrentProfileFragment()).commit()
                    true
                }

                else -> false
            }
        }
    }
}
