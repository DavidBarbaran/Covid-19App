package covid19.coronavirus.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun changeBitmap(context: Context, drawable: Int, width: Int, height: Int): Bitmap? {
    var icon = BitmapFactory.decodeResource(context.resources, drawable)
    icon = Bitmap.createScaledBitmap(icon, width, height, false)
    return icon
}