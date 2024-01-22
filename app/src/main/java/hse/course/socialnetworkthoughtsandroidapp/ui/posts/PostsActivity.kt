package hse.course.socialnetworkthoughtsandroidapp.ui.posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hse.course.socialnetworkthoughtsandroidapp.R

class PostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        supportActionBar?.hide()
        setContentView(R.layout.posts_activity_layout)
    }
}