package com.ayata.esewaremotefirebase.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.avyaas.nameonline.core.dialog.EnumDialogType
import com.avyaas.nameonline.core.dialog.ProgressDialog
import com.ayata.esewaremotefirebase.R
import com.ayata.esewaremotefirebase.constants.AppContracts
import com.ayata.esewaremotefirebase.data.model.Promotion
import com.ayata.esewaremotefirebase.data.model.Quote
import com.ayata.esewaremotefirebase.utils.showSnackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    protected val databaseRef = FirebaseFirestore.getInstance()
    protected val collectionRef = databaseRef.collection(AppContracts.COLLECTION_NAME)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    fun getCollectionReference() = collectionRef


    protected fun checkForUpdate(latestAppVersion: Int) {
        if (latestAppVersion > getCurrentVersionCode()) {
            val message = """
                New Version Available in Playstore. Please Update App
                """.trimIndent()
            progressDialog = ProgressDialog.newInstance(
                EnumDialogType.ALERT,
                title = "Update",
                message = message,
                updateFunction = {
                    goToPlayStore()
                })
            progressDialog.show(supportFragmentManager, "alert")
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
            appStoreIntent.setPackage("com.ayata.esewaremotefirebase")
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
            val message =
                """
                ${promotion.description}
                """.trimIndent()
            progressDialog = ProgressDialog.newInstance(
                EnumDialogType.PROMOTION,
                title = promotion.title,
                message = message
            )
            progressDialog.show(supportFragmentManager, "promotion")
        }
    }

    fun insertQuote(author: String, quote: String, view: View) {
        collectionRef.add(Quote(author, quote))//add method auto generates id
        view.showSnackbar(getString(R.string.quote_added_success_msg))
        supportFragmentManager.popBackStackImmediate()

    }

    fun updateQuote(id: String, author: String, quote: String, view: View) {
        collectionRef.document(id).update("author", author, "quote", quote)
        view.showSnackbar(getString(R.string.quote_updated_success_msg))
    }
}