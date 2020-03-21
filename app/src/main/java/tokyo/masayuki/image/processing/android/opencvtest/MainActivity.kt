package tokyo.masayuki.image.processing.android.opencvtest

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import tokyo.masayuki.image.processing.android.opencvtest.JavaCVUtilities.Companion.convertBitmap
import tokyo.masayuki.image.processing.android.opencvtest.JavaCVUtilities.Companion.warpHomography

class MainActivity : AppCompatActivity() {
    val WRITE_PERMISSION = 0
    companion object {
        init {
            System.loadLibrary("opencv_java")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, WRITE_PERMISSION)
    }


    override fun onResume() {
        super.onResume()
        val bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.pepper)
//        TestJavaCV.copyFileOrDir("ObjectnessTrainedModel", baseContext, this.packageName)
//        val imageMat = TestJavaCV.loadImageMatWithImageInDataFolder(this)
        val imageMat = TestJavaCV.loadImageMatWithImageInAssets(this)

//        TestJavaCV.calculateBoundingBoxSimple(imageMat2)
//        image_view.setImageBitmap(convertBitmap(imageMat))
        image_view.setImageBitmap(convertBitmap(warpHomography(imageMat), false))
    }
}
