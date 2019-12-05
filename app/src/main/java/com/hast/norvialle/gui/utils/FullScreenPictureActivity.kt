package com.hast.norvialle.gui.utils

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.utils.loadPicture
import com.hast.norvialle.utils.rotatePictureFile
import com.hast.norvialle.utils.saveFile
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_full_picture.*
import java.lang.ref.WeakReference

/**
 * Created by Konstantyn Zakharchenko on 04.12.2019.
 */
class FullScreenPictureActivity : AppCompatActivity() {
    companion object {
        val PICTURE_FILE_NAME = "PICTURE_FILE_NAME"
        val COMMENT = "COMMENT"
    }

    var bitmap = WeakReference<Bitmap>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_picture)
        back.setOnClickListener { finish() }

        val fileName = intent?.extras?.getString(PICTURE_FILE_NAME)
        if (fileName != null) {
            progress.visibility = View.VISIBLE
            loadPicture(this, fileName).subscribeBy(
                onNext = {
                    photo.setImageBitmap(it)
                    bitmap = WeakReference(it)
                    progress.visibility = View.GONE
                },
                onError = {
                    progress.visibility = View.GONE
                    finish()
                })
        } else {
            finish()
        }
        val comment = intent?.extras?.getString(COMMENT)
        commentText.setText(comment)


        rotate.setOnClickListener {
            progress.visibility = View.VISIBLE
            rotatePictureFile(bitmap.get()).subscribeBy(onNext = {
                photo.setImageBitmap(it)
                bitmap = WeakReference(it)

            },
                onError = {
                    progress.visibility = View.GONE
                    it.printStackTrace()
                }
                , onComplete = { progress.visibility = View.GONE }
            )
        }
    }

}