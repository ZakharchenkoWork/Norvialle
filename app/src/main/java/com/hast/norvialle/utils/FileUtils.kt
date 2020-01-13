package com.hast.norvialle.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference


/**
 * Created by Konstantyn Zakharchenko on 04.12.2019.
 */

object Pictures {
    val files = HashMap<String, WeakReference<Bitmap>>()
}

fun saveFile(context: Context, fileName: String, bitmap: Bitmap): Observable<Boolean> {
    return Observable.create<Boolean> {

        val cw = ContextWrapper(context.applicationContext)
        val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }

        val path = File(directory, fileName)

        try {
            val fos = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            it.onComplete()
        } catch (e: Exception) {
            it.onError(e)
            Log.e("SAVE_IMAGE", e.message, e)
        }

    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun deleteFile(context: Context, fileName: String): Observable<Boolean> {
    return Observable.create<Boolean> {
        val cw = ContextWrapper(context.applicationContext)
        val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
        if (directory.exists()) {
            File(directory, fileName).delete()
        }
        it.onComplete()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun loadPicture(context: Context, fileName: String): Observable<Bitmap> {
    return Observable.create<Bitmap> {
        if (fileName.isNotEmpty()) {
            val cw = ContextWrapper(context.applicationContext)
            val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
            if (directory.exists()) {
                it.onNext(BitmapFactory.decodeStream(FileInputStream(File(directory, fileName))))
            } else {
                it.onError(IOException("Pictures folder does not exists"))
            }
        } else {
            it.onError(IOException("Picture not set"))
        }
        it.onComplete()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun loadPictureThumbnail(context: Context, fileName: String): Observable<Bitmap> {
    return Observable.create<Bitmap> {
        val callbacks = it
        if (fileName.isNotEmpty()) {
            var isFound = false
            val bitmap = Pictures.files[fileName]
            bitmap?.let {
                bitmap.get()?.let { picture ->
                    callbacks.onNext(picture)
                    isFound = true
                }
            }
            if (!isFound) {

                val cw = ContextWrapper(context.applicationContext)
                val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
                if (directory.exists()) {
                    val thumbnail = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeStream(
                            FileInputStream(
                                File(
                                    directory,
                                    fileName
                                )
                            )
                        ), 100, 100
                    )
                    Pictures.files[fileName] = WeakReference(thumbnail)
                    it.onNext(
                        thumbnail
                    )
                } else {
                    it.onError(IOException("Pictures folder does not exists"))
                }
            }
        } else {
            it.onError(IOException("Picture not set"))
        }
        it.onComplete()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun rotatePictureFile(bitmap: Bitmap?): Observable<Bitmap> {
    return Observable.create<Bitmap> {


        if (bitmap != null) {
            val matrix = Matrix()
            matrix.postRotate(90f)
            it.onNext(
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            )

            it.onComplete()
        } else {
            it.onError(java.lang.Exception("bitmap is null"))
        }
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}