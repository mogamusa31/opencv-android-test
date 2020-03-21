package tokyo.masayuki.image.processing.android.opencvtest

import android.graphics.Bitmap
import org.bytedeco.javacpp.opencv_core
import org.bytedeco.javacpp.opencv_saliency
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * JavaCVUtilities.kt
 * opencvTest
 * Created by 田村 柾優紀 on 2020/03/21.
 * Copyright ©︎ 2020 田村 柾優紀. All rights reserved.
 */
class JavaCVUtilities {
    companion object {
        fun convertBitmap(imageMat: opencv_core.Mat, isBgr: Boolean): Bitmap {
            val bitmapMat = Mat(imageMat.address())
            if (isBgr) Imgproc.cvtColor(bitmapMat, bitmapMat, Imgproc.COLOR_BGR2RGBA)
            var outputBitmap =
                Bitmap.createBitmap(bitmapMat.width(), bitmapMat.height(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(bitmapMat, outputBitmap)
            outputBitmap = Bitmap.createScaledBitmap(outputBitmap!!, 500, 500, true)
            return outputBitmap
        }

        fun warpAffine(imageMat: opencv_core.Mat): opencv_core.Mat {
            val bitmapMat = Mat(imageMat.address())
            val grayMat = Mat()
            Imgproc.cvtColor(bitmapMat, grayMat, Imgproc.COLOR_RGBA2GRAY)
            val affineMat = Imgproc.getRotationMatrix2D(
                Point(
                    (bitmapMat.width() / 2).toDouble(),
                    (bitmapMat.height() / 2).toDouble()
                ), 45.0, 1.0
            )
            val warpMat = Mat()
            Imgproc.warpAffine(
                grayMat,
                warpMat,
                affineMat,
                Size(bitmapMat.width().toDouble(), bitmapMat.height().toDouble())
            )
            Imgproc.threshold(warpMat, warpMat, 128.0, 255.0, Imgproc.THRESH_BINARY)
            return TestJavaCV.convertMat(warpMat)
        }

        fun warpHomography(imageMat: opencv_core.Mat): opencv_core.Mat {
            val bitmapMat = Mat(imageMat.address())
//        Imgproc.cvtColor(bitmapMat, bitmapMat, Imgproc.COLOR_RGBA2GRAY)
//        val affineMat = Imgproc.getRotationMatrix2D(Point((bitmapMat.width() / 2).toDouble(), (bitmapMat.height() / 2).toDouble()), 45.0, 1.0)
//        val affineMat = Mat(3,3,CvType.CV_32F)

            val homographyMat = calculateHomographyMat(imageMat)

            val outputMat = Mat()
            Imgproc.warpPerspective(
                bitmapMat,
                outputMat,
                homographyMat,
                Size(bitmapMat.width().toDouble(), bitmapMat.height().toDouble()),
                Imgproc.INTER_LINEAR
            )
//        Imgproc.threshold(warpMat,warpMat,128.0,255.0,Imgproc.THRESH_BINARY)
            return TestJavaCV.convertMat(outputMat)
        }

        fun calculateStaticSaliency(imageMat: opencv_core.Mat): opencv_core.Mat {
            // Static
            val staticSaliency = opencv_saliency.StaticSaliencyFineGrained.create()
            val staticMat = opencv_core.Mat()
            staticSaliency.computeSaliency(imageMat, staticMat)
            println("staticMat is empty: ${staticMat.empty()}")
            println("staticMat is empty: ${staticMat.elemSize()}")
            println("imageMat is empty: ${imageMat.elemSize()}")
//        val staticMat2 = Imgcodecs.imdecode(Mat(staticMat.address()), 1)
            val outputMat = Mat(staticMat.address())
            val imageTestMat = Mat(imageMat.address())
            println("outputMat: rows ${outputMat.rows()}, cols ${outputMat.cols()}")
            println("imageTestMat: rows ${imageTestMat.rows()}, cols ${imageTestMat.cols()}")
//        return TestJavaCV.convertMat(staticMat2)
            return TestJavaCV.convertMat(outputMat)
        }

        fun calculateObjectnessBING(imageMat: opencv_core.Mat): opencv_core.Mat {
            // BING
            val objectBING = opencv_saliency.ObjectnessBING.create()
            objectBING.setTrainingPath("file:///android_asset/ObjectnessTrainedModel")
            val bingMat = opencv_core.Mat()
            objectBING.computeSaliency(imageMat, bingMat)
            return bingMat
        }

        fun calculateHomographyMat(imageMat: opencv_core.Mat): Mat {
            val bitmapMat = Mat(imageMat.address())
            val width = bitmapMat.width().toFloat()
            val height = bitmapMat.height().toFloat()
            //変換元座標設定
//        val srcPoint = floatArrayOf(52f, 38f, 18f, 100f, 100f, 100f, 100f, 58f)
//        val srcPoint = floatArrayOf(0f, 0f, 0f, 150f, 150f, 150f, 150f, 0f)
            val srcPoint = floatArrayOf(0f, 0f, 0f, height, width, height, width, 0f)
            val srcPointMat = Mat(4, 2, CvType.CV_32F)
            srcPointMat.put(0, 0, srcPoint)

            //変換先座標設定
//        val dstPoint = floatArrayOf(0f, 38f, 0f, 100f, 100f, 100f, 100f, 38f)
//        val dstPoint = floatArrayOf(0f, 0f, 0f, 150f, 150f, 150f, 150f, 0f)
            val dstPoint = floatArrayOf(0f, 0f, 0f, height, width, height, width - 50.toFloat(), 0f)
            val dstPointMat = Mat(4, 2, CvType.CV_32F)
            dstPointMat.put(0, 0, dstPoint)
            return Imgproc.getPerspectiveTransform(srcPointMat, dstPointMat)
        }

        fun calculateHomographyMatTest(imageMat: opencv_core.Mat): Mat {
            val homographyPoint = floatArrayOf(
                0.6666666666666666f,
                0f,
                0f,
                0f,
                0.6666666666666666f,
                0f,
                0f,
                -0.0022222222222222222f,
                1f
            )
            val homographyMat = Mat(3, 3, CvType.CV_32F)
            homographyMat.put(0, 0, homographyPoint)
            return homographyMat
        }
    }
}