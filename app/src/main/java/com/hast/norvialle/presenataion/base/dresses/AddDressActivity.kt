package com.hast.norvialle.presenataion.base.dresses

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.databinding.ActivityAddDressBinding
import com.hast.norvialle.domain.AddDressViewModel
import com.hast.norvialle.presenataion.base.BaseActivity
import com.hast.norvialle.presenataion.utils.FullScreenPictureActivity
import com.hast.norvialle.presenataion.utils.dialogs.priceInputDialog
import com.hast.norvialle.utils.deleteFile
import com.hast.norvialle.utils.saveFile
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_add_dress.*
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddDressActivity : BaseActivity() {
    companion object {
        const val EDIT: Int = 1
        const val DATA_TYPE: String = "DRESS"
        const val REQUEST_PICK_GALLERY: Int = 198
        const val REQUEST_TAKE_PHOTO: Int = 208
        const val REQUEST_CAMERA_PERMISSION: Int = 168
        const val REQUEST_STORAGE_PERMISSION: Int = 121
        const val PNG = ".png"
    }


    private val tempFileName = UUID.randomUUID().toString() + PNG
    private var isTempUploaded = false
    private var isSaving = false
    private var oldFileName :String? = ""

    private lateinit var viewModel: AddDressViewModel
    private lateinit var binding: ActivityAddDressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_dress)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(AddDressViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.setDress(getIntentData(DATA_TYPE))

        oldFileName = viewModel.dressLiveData.value?.fileName
        plusIcon.visibility = View.GONE

        dressPhoto.setOnClickListener {
            selectImage()
        }
        price.setOnClickListener {
            priceInputDialog(
                this,
                R.string.dressPrice,
                price.getIntValue().toFloat()
            ) {
                price.setText(stringPriceWithPlaceholder(it, ""))
                price.postDelayed({
                    comment.validationCheck()
                }, 100)
            }
        }

    }

    private fun selectImage() {


        val items = if (viewModel.hasPicture()) arrayOf<CharSequence>(
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
                    openPictureFullScreen(viewModel.dressLiveData.value)
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

    private fun openPictureFullScreen(dress: Dress?) {
        if (dress != null) {
            val intent = Intent(this, FullScreenPictureActivity::class.java)
            intent.putExtra(FullScreenPictureActivity.DATA_TYPE, dress)
            startActivity(intent)
        }
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (!oldFileName.isNullOrEmpty() && oldFileName.equals(viewModel.getPictureFileName())){
                    deleteFile(this, tempFileName).subscribeBy(onError = {}, onNext = {}, onComplete = {})
                }
                isSaving = true
                viewModel.save()
                finishWithOkResult()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.adding_dress
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isSaving && isTempUploaded) {
            deleteFile(this, tempFileName).subscribeBy(onError = {}, onNext = {}, onComplete = {})
        }
    }

    private fun savePictureToInnerStorage(bitmap: Bitmap?, fileName: String, action: () -> Unit) {
        if (bitmap != null) {
            progress.visibility = View.VISIBLE
            saveFile(this, fileName, bitmap).subscribeBy(
                onError = {
                    viewModel.loadingVisibility.value = false
                    it.printStackTrace()
                },
                onComplete = {
                    action.invoke()
                    viewModel.loadingVisibility.value = false
                }

            )
        }

    }


    private fun createImageFile(): File {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        return File(directory, tempFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (isResultDataOk(resultCode, requestCode, REQUEST_PICK_GALLERY)) {
            intent?.let {
                val data = it.data
                if (data != null) {
                    val imageStream = contentResolver.openInputStream(data)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)

                    dressPhoto.setImageBitmap(selectedImage)
                    savePictureToInnerStorage(selectedImage, tempFileName) {
                        viewModel.setPicture(tempFileName)
                        isTempUploaded = true
                        plusIcon.visibility = View.GONE
                    }
                }
            }
        } else if (isResultDataOk(resultCode, requestCode, REQUEST_TAKE_PHOTO)) {
            intent?.let {
                val data = it.extras
                if (data != null) {
                    val bitmap = data.get("data") as Bitmap

                    savePictureToInnerStorage(bitmap, tempFileName) {
                        viewModel.setPicture(tempFileName)
                        isTempUploaded = true
                        plusIcon.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun pickFromGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_DENIED
        ) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, REQUEST_PICK_GALLERY)
        } else {
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
            if (takePictureIntent.resolveActivity(packageManager) != null) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        } else if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickFromGallery()
        }
    }
}