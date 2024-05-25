package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import hse.course.socialnetworkthoughtsandroidapp.R


class ImagesPagerAdapter(
    private val files: List<Bitmap>
) :

    RecyclerView.Adapter<ImagesPagerAdapter.ImagesViewHolder>() {

    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postImage: ImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ImagesViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.image_card, viewGroup, false)

        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(imagesViewHolder: ImagesViewHolder, position: Int) {
        imagesViewHolder.postImage.setImageBitmap(files[position])
    }

    override fun getItemCount(): Int {
        return files.size
    }
}