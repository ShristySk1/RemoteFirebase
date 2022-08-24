package com.ayata.esewaremotefirebase

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ayata.esewaremotefirebase.data.model.Promotion
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var updateDailog: AlertDialog
    private lateinit var promotionDailog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    protected fun checkForUpdate(latestAppVersion: Int) {
        if (latestAppVersion > getCurrentVersionCode()) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Update")
            builder.setMessage(
                """
                New Version Available.
                Please Update App
                """.trimIndent()
            )
            builder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which ->
                    goToPlayStore()
                    updateDailog.dismiss()
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> updateDailog.dismiss() })
            // create and show the alert dialog
            updateDailog = builder.create()
            updateDailog.show()
        }
    }

    private fun getCurrentVersionCode(): Int {
        var versionCode = 1
        try {
            val pInfo = applicationContext.packageManager.getPackageInfo(
                applicationContext.packageName, 0
            )
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                pInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            //log exception
        }
        return versionCode
    }

    private fun goToPlayStore() {
        try {
            val appStoreIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            appStoreIntent.setPackage("com.android.vending")
            startActivity(appStoreIntent)
        } catch (exception: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    protected fun checkForPromotion(promotion: Promotion) {
        if (promotion.isPromotionAvailable) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(promotion.title)
            builder.setMessage(
                """
                ${promotion.description}
                """.trimIndent()
            )
            builder.setPositiveButton("Dismiss",
                DialogInterface.OnClickListener { dialog, which ->
                    promotionDailog.dismiss()
                })
            // create and show the alert dialog
            promotionDailog = builder.create()
            promotionDailog.show()
        }
    }
}