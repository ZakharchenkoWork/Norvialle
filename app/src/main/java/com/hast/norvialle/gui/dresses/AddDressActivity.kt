package com.hast.norvialle.gui.dresses

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.utils.FullScreenPictureActivity
import com.hast.norvialle.utils.*
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_add_dress.*
import kotlinx.android.synthetic.main.activity_add_dress.price
import kotlinx.android.synthetic.main.activity_add_dress.toolbar
import kotlinx.android.synthetic.main.activity_add_makeup_artist.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddDressActivity : BaseActivity() {
    companion object {
        val EDIT: Int = 1
        val DRESS: String = "DRESS"
        val REQUEST_PICK_GALLERY: Int = 198
        val REQUEST_TAKE_PHOTO: Int = 208
        val REQUEST_CAMERA_PERMISSION: Int = 168
        val REQUEST_STORAGE_PERMISSION: Int = 121
        val PNG = ".png"
    }

    val presenter: MainPresenter = MainPresenter


    var bitmap = WeakReference<Bitmap>(null)
    var dress: Dress = Dress("", "", 0)
    val tempFileName = UUID.randomUUID().toString() + PNG
    var isTempUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dress)
        setSupportActionBar(toolbar)

        dress = intent?.extras?.getSerializable(DRESS) as Dress

        if (dress == null) {
            dress = Dress("", "", 0)
        } else {
            loadPicture(dress.fileName)
            comment.setText(dress.comment)
            price.setText("" + dress.price)
            plusIcon.visibility = View.GONE

        }
        dressPhoto.setOnClickListener {
            selectImage()
        }

        rotate.setOnClickListener { rotate() }
        price.setOnClickListener {
            priceInputDialog(this, R.string.dressPrice, getFloatValue(price)) {
                price.setText(stringPriceWithPlaceholder(it, ""))
            }
        }
    }

    fun selectImage() {


        val items = if (bitmap.get() != null) arrayOf<CharSequence>(
            getString(R.string.view),
            getString(R.string.take_photo),
            getString(R.string.from_gallery),
            getString(R.string.cancel)
        ) else arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.from_gallery),
            getString(R.string.cancel)
        )
        val builder = AlertDialog.Builder(this)
        builder.setItems(items) { dialog, which ->
            when (items[which]) {

                getString(R.string.view) -> {
                    if(bitmap.get() != null) {
                        openPictureFullScreen(if (!isTempUploaded)dress.fileName else tempFileName, comment.text.toString())
                    }
                }
                getString(R.string.take_photo) -> {
                    takePhoto()
                    dialog.dismiss()
                }
                getString(R.string.from_gallery) -> {
                    pickFromGallery()
                    dialog.dismiss()
                }

                getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    fun openPictureFullScreen(pictureFileName: String?, comment: String) {
        if (pictureFileName != null && !pictureFileName.equals("")) {
            val intent = Intent(this, FullScreenPictureActivity::class.java)
            intent.putExtra(FullScreenPictureActivity.PICTURE_FILE_NAME, pictureFileName)
            intent.putExtra(FullScreenPictureActivity.COMMENT, comment)
            startActivity(intent)
        }
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save -> {
                if (bitmap.get() != null) {
                    if(isTempUploaded) {
                        if (!dress.fileName.equals("")) {
                            deleteFile(this, dress.fileName).subscribeBy(onComplete = {
                                Log.d("AddDressActivity", "deleted: " +dress.fileName)
                            })
                        }
                        dress.fileName = tempFileName
                    }
                    dress.comment = comment.text.toString()
                    dress.price = getIntValue(price.text.toString())
                    presenter.addDress(dress)
                    finish()
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.adding_dress
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!dress.fileName.equals(tempFileName) && isTempUploaded){
            deleteFile(this, tempFileName).subscribeBy(onComplete = {})
        }
    }


    fun loadPicture(fileName: String) {
        loadPicture(this, fileName).subscribeBy(onNext = {
            bitmap = WeakReference(it)
            rotate.visibility = View.VISIBLE
            dressPhoto.setImageBitmap(bitmap.get()) }, onError = {})
    }

    fun rotate() {
        val bitmapOrg = bitmap.get()
        if (bitmapOrg != null) {
            val matrix = Matrix()
            matrix.postRotate(90f)
            val rotatedBitmap = Bitmap.createBitmap(
                bitmapOrg,
                0,
                0,
                bitmapOrg.getWidth(),
                bitmapOrg.getHeight(),
                matrix,
                true
            )
            bitmap = WeakReference(rotatedBitmap)
            dressPhoto.setImageBitmap(bitmap.get())
            isTempUploaded = true
            savePictureToInnerStorage(bitmap.get(), tempFileName)
        }
    }

    fun savePictureToInnerStorage(bitmap: Bitmap?, fileName: String) {
        if (bitmap != null) {
            progress.visibility = View.VISIBLE
            saveFile(this, fileName, bitmap).subscribeBy(
                onError = { progress.visibility = View.GONE
                    it.printStackTrace() },
                onComplete = { progress.visibility = View.GONE}

            )
        }

    }


    fun createImageFile(): File {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        return File(directory, tempFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_GALLERY && data != null && data.data != null) {
                val imageStream = getContentResolver().openInputStream(data.data)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                bitmap = WeakReference(selectedImage)

                dressPhoto.setImageBitmap(selectedImage)
              savePictureToInnerStorage(bitmap.get(), tempFileName)
                isTempUploaded = true
                    rotate.visibility = View.VISIBLE
                    plusIcon.visibility = View.GONE

            } else if (requestCode == REQUEST_TAKE_PHOTO && data != null && data.extras != null) {

                bitmap = WeakReference(data.extras?.get("data") as Bitmap)
                isTempUploaded = true
                savePictureToInnerStorage(bitmap.get(), tempFileName)
                dressPhoto.setImageBitmap(bitmap.get())
                rotate.visibility = View.VISIBLE
                plusIcon.visibility = View.GONE


            }
        }
    }


    fun pickFromGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_DENIED
        ) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, REQUEST_PICK_GALLERY)
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_DENIED
        ) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    val photoFile = createImageFile()
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile.toURI())
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                } catch (ex: IOException) {
                    ex.printStackTrace()

                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                takePhoto()

        } else if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickFromGallery()
        }
    }
}