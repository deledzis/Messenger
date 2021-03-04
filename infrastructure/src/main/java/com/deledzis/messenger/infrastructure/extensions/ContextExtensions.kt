package com.deledzis.messenger.infrastructure.extensions

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.deledzis.messenger.infrastructure.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun Context.drawableCompatFrom(@DrawableRes drawableId: Int): Drawable? {
    return ResourcesCompat.getDrawable(this.resources, drawableId, this.theme)
}

fun Context.colorFrom(@ColorRes colorId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.resources.getColor(colorId, this.theme)
    } else {
        @Suppress("DEPRECATION")
        this.resources.getColor(colorId)
    }
}

fun Context.colorStateListFrom(@ColorRes colorId: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, colorId)
}

fun Context.fontFrom(@FontRes fontId: Int): Typeface? {
    return ResourcesCompat.getFont(this, fontId)
}

fun Context.toast(message: CharSequence): Unit =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.hideSoftKeyboard(windowToken: IBinder?) {
    val imm: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun Fragment.hideSoftKeyboard(view: View? = null) {
    view?.clearFocus()
    val windowToken = view?.windowToken ?: requireActivity().currentFocus?.windowToken
    requireContext().hideSoftKeyboard(windowToken)
}

@Throws(ActivityNotFoundException::class)
fun Context.shareText(content: String, title: String? = null) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, title)
    startActivity(shareIntent)
}

@Throws(ActivityNotFoundException::class)
fun Context.openUrl(url: String) {
    val query: String = Uri.encode(url, "UTF-8")
    val browserIntent = Intent(CATEGORY_BROWSABLE, Uri.parse(Uri.decode(query)))
    browserIntent.action = ACTION_VIEW
    startActivity(browserIntent)
}

@Throws(ActivityNotFoundException::class)
fun Context.openPlayMarket(appUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(appUrl)
        setPackage("com.android.vending")
    }
    startActivity(intent)
}

fun Activity.paintStatusBar(@ColorRes colorId: Int) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = this.colorFrom(colorId)
}

fun Activity.toggleFullscreen(enable: Boolean) {
    val attrs = window.attributes
    if (enable) {
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
    } else {
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    }
    window.attributes = attrs
}

fun Context.showDialog(
    @StringRes messageId: Int,
    @StringRes positiveBtnId: Int? = null,
    @StringRes negativeBtnId: Int? = null,
    onPositive: (() -> Unit)? = null
) {
    val dialog = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme_Light)
        .setMessage(messageId)
        .setBackground(drawableCompatFrom(R.drawable.bgr_shape_dialog))
        .apply {
            positiveBtnId?.let {
                setPositiveButton(positiveBtnId) { dialog, _ ->
                    onPositive?.invoke()
                    dialog.dismiss()
                }
            }
            negativeBtnId?.let {
                setNegativeButton(negativeBtnId) { dialog, _ ->
                    dialog.dismiss()
                }
            }
        }
        .create()
    dialog.show()
}

fun Context.showPopup(v: View, @MenuRes menuId: Int, listener: PopupMenu.OnMenuItemClickListener) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        PopupMenu(this, v, Gravity.CENTER, 0, R.style.PopupMenuStyle).apply {
            setOnMenuItemClickListener(listener)
            inflate(menuId)
            show()
        }
    }
}

inline fun Fragment.loadUrlIntoBitmap(url: String, crossinline callback: (file: Bitmap) -> Unit) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}

fun Context.createNotificationChannel(
    @StringRes channelId: Int,
    @StringRes channelName: Int,
    @StringRes channelDescription: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val id = getString(channelId)
        val name = getString(channelName)
        val descriptionText = getString(channelDescription)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(id, name, importance)

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationChannel.description = descriptionText

        val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        notificationChannel.setSound(ringtoneManager, audioAttributes)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)
    }
}

fun Context.shareBitmap(@NonNull bitmap: Bitmap, fileName: String) {
    //---Save bitmap to external cache directory---//
    //get cache directory
    val cachePath = File(externalCacheDir, "my_images/")
    cachePath.mkdirs()

    //create png file
    val file = File(cachePath, fileName)
    val fileOutputStream: FileOutputStream
    try {
        fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    //---Share File---//
    //get file uri
    val myImageFileUri: Uri = FileProvider.getUriForFile(
        this,
        applicationContext.packageName + ".provider",
        file
    )

    //create a intent
    val intent = Intent(Intent.ACTION_SEND)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
    intent.type = "image/png"

    val chooser = Intent.createChooser(intent, "Share File")
    val resInfoList = packageManager.queryIntentActivities(
        chooser,
        PackageManager.MATCH_DEFAULT_ONLY
    )

    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        grantUriPermission(
            packageName,
            myImageFileUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    startActivity(chooser)
}