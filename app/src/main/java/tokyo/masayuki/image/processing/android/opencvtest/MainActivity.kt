package tokyo.masayuki.image.processing.android.opencvtest

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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
        TestJavaCV.calculateBoundingBox(this, this.packageName)
//        var outFile: java.io.File? = java.io.File("/data/data/"+getPackageName(), "ObjNessB2W8HSV.idx.yml")
//        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        var outputSalMat = Mat()
//
//        val bitmapMat = Mat()
//        Utils.bitmapToMat(bitmap, bitmapMat)
//        val grayMat = Mat()
//        Imgproc.cvtColor(bitmapMat, grayMat, Imgproc.COLOR_RGBA2GRAY)
//        val affineMat = Imgproc.getRotationMatrix2D(Point((bitmap.width / 2).toDouble(), (bitmap.height / 2).toDouble()), 45.0, 1.0)
//        val warpMat = Mat()
//        Imgproc.warpAffine(grayMat, warpMat, affineMat, Size(bitmap.width.toDouble(), bitmap.height.toDouble()))
//        Imgproc.threshold(warpMat,warpMat,128.0,255.0,Imgproc.THRESH_BINARY)
//        val imageMat = TestJavaCV.convertMat(warpMat)
//        println("elemSize: ${imageMat.elemSize()}")

        // BING
//        val objectBING = opencv_saliency.ObjectnessBING.create()
//        val path: Uri = Uri.parse("file:///data/user/0/tokyo.masayuki.image.processing.android.opencvtest/app_OpenCV_data/pepper.jpg")
//        println(path.toString())
//        println(assetPath?.get(0))
//        val path: Uri = Uri.parse("assets://pepper.jpg")
//        val input = assets.open("pepper.jpg")
//        objectBING.setTrainingPath("file:///android_asset/ObjectnessTrainedModel")
//
//        val bingMat = opencv_core.Mat()
//        val bool = objectBING.computeSaliency(imageMat, bingMat)
//        outputSalMat = Mat(bingMat.address())
//        Utils.matToBitmap(outputSalMat, outputBitmap)

//        // Static
//        val staticSaliency = opencv_saliency.StaticSaliencyFineGrained.create()
//        val staticMat = opencv_core.Mat()
//        staticSaliency.computeSaliency(imageMat, staticMat)
//        println("static mat: ${staticMat.empty()}")
//        val tempMat = Mat(staticMat.address())
//        Imgproc.cvtColor(tempMat, outputSalMat, Imgproc.COLOR_GRAY2RGBA)
//        println("elemSize: ${outputSalMat.elemSize()}")
//        Utils.matToBitmap(outputSalMat, outputBitmap)

//        // Test
//        val testMat = Mat(imageMat.address())
//        Utils.matToBitmap(testMat, outputBitmap)
//
//        image_view.setImageBitmap(outputBitmap)
    }
}
