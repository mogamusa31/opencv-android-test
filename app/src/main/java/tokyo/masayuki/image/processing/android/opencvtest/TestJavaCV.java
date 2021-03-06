package tokyo.masayuki.image.processing.android.opencvtest;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_saliency;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * null.java
 * opencvTest
 * Created by 田村 柾優紀 on 2020/03/14.
 * Copyright ©︎ 2020 田村 柾優紀. All rights reserved.
 */
public class TestJavaCV {
    static opencv_core.Mat convertMat(Mat mat) {
        final Mat impmat = mat;
        opencv_core.Mat mat2 = new opencv_core.Mat((Pointer) null) {
            {
                address = impmat.getNativeObjAddr();
            }
        };
        return mat2;
    }

    static FloatPointer calculateBoundingBox(Context context, opencv_core.Mat imageMat) throws IOException {
        String folderName = "model";
        String outputDirPath = context.getFilesDir().getPath() + "/" + folderName;
//        String outputDirPath = "/data/data/" + context.getPackageName() + "/" + folderName;
        copyAssets(context, outputDirPath, folderName);
        opencv_saliency.ObjectnessBING objectnessBING = opencv_saliency.ObjectnessBING.create();
        // assets
        objectnessBING.setTrainingPath(context.getFilesDir().getPath() + "/" + folderName);
        opencv_core.Mat bingMat = new opencv_core.Mat();
        Boolean bool = objectnessBING.computeSaliency(imageMat, bingMat);
        FloatPointer pointer = objectnessBING.getobjectnessValues();
        if (Objects.isNull(pointer)) {
            System.out.println("pointer is null");
        }
        System.out.println("saliency: " + bool);
        System.out.println(bingMat.empty());
        return pointer;
    }

    static FloatPointer calculateBoundingBoxSimple(opencv_core.Mat imageMat) {
        String folderName = "model";
        opencv_saliency.ObjectnessBING objectnessBING = opencv_saliency.ObjectnessBING.create();
        // assets
        objectnessBING.setTrainingPath("file:///android_asset/model/");
        opencv_core.Mat bingMat = new opencv_core.Mat();
        Boolean bool = objectnessBING.computeSaliency(imageMat, bingMat);
        FloatPointer pointer = objectnessBING.getobjectnessValues();
        if (Objects.isNull(pointer)) {
            System.out.println("pointer is null");
        }
        System.out.println("saliency: " + bool);
        System.out.println(bingMat.empty());
        return pointer;
    }

    static opencv_core.Mat loadImageMatWithImageInDataFolder(Context context) throws IOException {
        String folderName = "image";
        String outputDir = "/data/data/" + context.getPackageName() + "/" + folderName;
        copyAssets(context, outputDir, folderName);
        String[] files = context.getAssets().list(folderName);
        String path = outputDir + "/" + files[0];
        Mat bitmapMat = Imgcodecs.imread(path);
        return convertMat(bitmapMat);
    }

    static opencv_core.Mat loadImageMatWithImageInAssets(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pepper);
        Mat bitmapMat = new Mat();
        Utils.bitmapToMat(bitmap, bitmapMat);
        return convertMat(bitmapMat);
    }

    static void copyAssets(Context context, String outputDirPath, String folderName) {
        File fileDir = new File(outputDirPath);
        fileDir.mkdir();
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(folderName);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(folderName + "/" + filename);
                File outFile = new File(outputDirPath, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
        File file = new File(outputDirPath);
        for (String fileName : file.list()) {
            System.out.println(fileName);
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    // Test
    static opencv_core.Mat testLoadImageMatInDataFolder(Context context) throws IOException {
        String folderName = "image";
        String outputDir = "/data/data/" + context.getPackageName() + "/" + folderName;
        copyAssets(context, outputDir, folderName);
        String[] files = context.getAssets().list(folderName);
        String path = outputDir + "/" + files[0];
        // bitmapMat is bgr format
        Mat bitmapMat = Imgcodecs.imread(path);
        return convertMat(bitmapMat);
    }

    static Bitmap testLoadBitmapInDataFolder(Context context) throws IOException {
        String folderName = "image";
        String outputDir = "/data/data/" + context.getPackageName() + "/" + folderName;
        copyAssets(context, outputDir, folderName);
        String[] files = context.getAssets().list(folderName);
        String path = outputDir + "/" + files[0];
        // bitmapMat is bgr format
        Mat bitmapMat = Imgcodecs.imread(path);
        Mat testMat = new Mat(bitmapMat.getNativeObjAddr());
        Bitmap outputBitmap = Bitmap.createBitmap(testMat.width(), testMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(testMat, outputBitmap);
        outputBitmap = Bitmap.createScaledBitmap(outputBitmap, 500,500, true);
        return outputBitmap;
    }

    static Bitmap testLoadBitmapInAssets(Context context) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pepper);
        Mat bitmapMat = new Mat();
        Utils.bitmapToMat(bitmap, bitmapMat);
        opencv_core.Mat imageMat = TestJavaCV.convertMat(bitmapMat);
        Mat testMat = new Mat(imageMat.address());
        Bitmap outputBitmap = Bitmap.createBitmap(testMat.width(), testMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(testMat, outputBitmap);
        return outputBitmap;
    }
}
