package com.hast.norvialle.presenataion.utils

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.presenataion.base.dresses.AddDressActivity
import com.hast.norvialle.presenataion.utils.dialogs.showDeleteDialog
import com.hast.norvialle.utils.loadPicture
import com.hast.norvialle.utils.rotatePictureFile
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_full_picture.*
import java.lang.ref.WeakReference

/**
 * Created by Konstantyn Zakharchenko on 04.12.2019.
 */
class FullScreenPictureActivity : AppCompatActivity() {
    companion object {
        val PICTURE_FILE_NAME = "PICTURE_FILE_NAME"
        val DRESS = "DRESS"
        val COMMENT = "COMMENT"
        val EDIT_AND_DELETE = "EDIT_AND_DELETE"
    }

    var bitmap = WeakReference<Bitmap>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_picture)
        back.setOnClickListener { finish() }

        var fileName = intent?.let { it.extras?.getString(PICTURE_FILE_NAME)}
        val comment = intent?.extras?.getString(COMMENT)

        val hasControls = intent?.let { it.extras?.getBoolean(EDIT_AND_DELETE, false)}

        if (hasControls == null || !hasControls){
            editDeleteLayout.visibility = View.GONE
            commentText.setText(comment)
        } else{
            editDeleteLayout.visibility = View.VISIBLE
            val dress : Dress? = intent?.let { return@let it.extras?.getSerializable(
                DRESS
            ) as Dress }
            if (dress != null){
                fileName =dress.fileName
                edit.setOnClickListener{
                    openDressEditor(dress)
                }
                delete.setOnClickListener{
                    showDeleteDialog(
                        this,
                        R.string.delete_dialog_dress
                    ) {
                        MainPresenter.deleteDress(dress)
                        finish()
                    }
                }
            }

        }
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

    private fun openDressEditor(dress: Dress) {
        var intent = Intent(this, AddDressActivity::class.java)
        intent.putExtra(AddDressActivity.DRESS, dress)
        startActivityForResult(intent, AddDressActivity.EDIT)
        finish()
    }

}