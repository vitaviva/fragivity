package com.github.fragivity.deeplink

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.github.fragivity.AbsBaseFragment
import com.github.fragivity.annotation.DeepLink
import com.my.example.R


const val URI = "myapp://fragitiy.github.com/"

@DeepLink(uri = URI)
class DeepLinkFragment : AbsBaseFragment() {
    override val titleName: String?
        get() = "Deep Links"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragivity_deep_link, container, false)
    }
}

fun Context.sendNotification() {

    val manager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //NotificationChannel和createNotificationChannel都是Android8.0以后新增的API
        val channel = NotificationChannel(
            "important",
            "Important",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URI))
    val pi = PendingIntent.getActivity(this, 0, intent, 0)//点击通知栏后跳转到相应的activity
    val notification = NotificationCompat.Builder(this, "important")//使用NotificationCompat兼容8.0以前的系统
        .setContentTitle("Fragivity Example")
        .setContentText(
            "This is a notification for testing fragivity deep links"
        )//默认显示文本，过长会省略
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("This is a notification for testing fragivity deep links.")
        )//通知中显示完整长文字
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        )
        .setSmallIcon(R.mipmap.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        .setContentIntent(pi)
        .setAutoCancel(true)
        .build()
    manager.notify(1, notification)
}