package hse.course.socialnetworkthoughtsandroidapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class ImagesUtils {

    companion object {

        fun base64ListToBitmapList(imagesBase64: List<String>): List<Bitmap> {
            return buildList {
                imagesBase64.forEach { imageBase64 ->
                    val imageAsBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
                    if (bitmap != null) {
                        add(bitmap)
                    }
                }
            }
        }

        fun base64ToBitmap(imageBase64: String) : Bitmap? {
            val imageAsBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
            if (bitmap != null) {
                return bitmap
            }
            return null
        }
    }
}