package com.project.babybuy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.FirebaseApp
import com.project.babybuy.login.MediaLoader
import com.project.babybuy.product.ProductActivity
import com.project.babybuy.register.RegisterActivity
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    var prefManager: PrefManager? = null
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        adjustFontScale(this, resources.configuration)
        Album.initialize(
            AlbumConfig.newBuilder(applicationContext)
                .setAlbumLoader(MediaLoader())
                .build()
        )
        FirebaseApp.initializeApp(this)
        prefManager= PrefManager(this)
        printHashKey(this)
        memoryAllocation();
    }


    fun adjustFontScale(
        ctx: Activity,
        configuration: Configuration
    ) {
        configuration.fontScale = 1.0.toFloat()
        val metrics = ctx.resources.displayMetrics
        val wm =
            ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        ctx.resources.updateConfiguration(configuration, metrics)
    }

    private fun memoryAllocation() {

        val handler = Handler()
        handler.postDelayed(Runnable {
            if (!flag) {
                if (prefManager!!.checkLogin()) {

                    val intent = Intent(baseContext, ProductActivity::class.java)
                    startActivity(intent)
                    finishAffinity()


                } else {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
//            // Repeat again after 2 seconds
//            handler.postDelayed(MainActivity.this, 2000)
        }, 2000)





    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.e("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }

}