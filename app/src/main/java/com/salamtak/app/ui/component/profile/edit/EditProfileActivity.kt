package com.salamtak.app.ui.component.profile.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialTypeAttachments
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.UpdateProfileResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.login.LoginViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_transparent.*
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : BaseActivity(), AttachCallBacks {
    override val layoutId: Int
        get() = R.layout.activity_edit_profile

    val profileActivityViewModel: UpdateProfileViewModel by viewModels()

    val loginViewModel: LoginViewModel by viewModels()

    lateinit var imageSelection: ImageSelection

//    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    override fun initializeViewModel() {
//        profileActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(UpdateProfileViewModel::class.java)
//        loginViewModel = viewModelFactory.create(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        hideNotificationBar()
        super.onCreate(savedInstanceState)
        imageSelection = ImageSelection(this, supportFragmentManager, false, 1)
        initViews()
    }

    private fun initViews() {
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        iv_camera.setOnClickListener {
            browseClicked()
//            onChooseImage()
        }
        iv_user_image.setOnClickListener {
            browseClicked()
//            onChooseImage()
        }
        btn_update.setOnClickListener {
            profileActivityViewModel.updateProfile(
                et_first_name.text.toString(),
                et_last_name.text.toString(),
                et_phone.text.toString(),
                et_email.text.toString().trim()
            )
        }

        var user = profileActivityViewModel.getUser()
        bindData(user)

    }

    override fun observeViewModel() {
        observeToast(profileActivityViewModel.showToast)
        observeError(profileActivityViewModel.showError)
//        observe(profileActivityViewModel.getProfileInfoResponse, ::handleProfileResponse)
        observe(profileActivityViewModel.updateProfileResponse, ::handleUpdateProfileResponse)
//        observe(
//            profileActivityViewModel.requestVerificationNumberResponseMutableLiveData,
//            ::handleVerificationResponse
//        )

        observe(profileActivityViewModel.formState, ::handleFormState)
    }


    private fun handleFormState(formState: ProfileFormState) {
        btn_update.isEnabled = true
        if (formState?.firstNameError != null) {
            et_first_name.error = getString(formState.firstNameError)
            et_first_name.shakeView()
        }

        if (formState?.lastNameError != null) {
            et_last_name.error = getString(formState.lastNameError)
            et_last_name.shakeView()
        }

        if (formState?.phoneError != null) {
            et_phone.error = getString(formState.phoneError)
            et_phone.shakeView()
        }

        if (formState.emailError != null) {
            et_email.error = formState.emailError
            et_email.shakeView()
        }

    }

    private fun handleUpdateProfileResponse(resource: Resource<UpdateProfileResponse>?) {
        when (resource) {
            is Resource.Loading -> {
                btn_update.isEnabled = false
                showLoadingView(progress_bar)
            }
            is Resource.Success -> {
                progress_bar.toGone()
                btn_update.isEnabled = true
                if (profileActivityViewModel.phoneUpdated) {
                    loginViewModel.requestVerifyNumber(Constants.TYPE_PHONE)
                    var user = profileActivityViewModel.getUser()
                    navigateToVerifyNumberScreen(user.phone)
                } else {
//                    showMessage(getString(R.string.profile_updated_successfully))
                    onBackPressed()
                }
            }

            is Resource.NetworkError -> {
                progress_bar.toGone()
                btn_update.isEnabled = true
                resource.errorCode?.let {
                    var error = profileActivityViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                btn_update.isEnabled = true
                resource.errorResponse?.let { showServerErrorMessage(it) }
            }
        }
    }

//    private fun handleProfileResponse(resource: Resource<ProfileResponse>?) {
//        when (resource) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.Success -> {
//                progress_bar.toGone()
//                bindData(resource.data!!.data!!)
//            }
//
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = profileActivityViewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { showServerErrorMessage(it) }
//            }
//        }
//    }

    private fun bindData(data: User) {
        et_email.setText(data.email ?: "")
        et_first_name.setText(data.firstName)
        et_last_name.setText(data.lastName)
        et_phone.setText(data.phone)
        iv_user_image.loadCircleImage(data.imageUrl)
    }

//    private fun onChooseImage() {
//        choosePhotoFromGallary()
//    }
//
//    fun choosePhotoFromGallary() {
//        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
//
//            val galleryIntent = Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            )
//
//            startActivityForResult(galleryIntent, Constants.GALLERY)
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // do your stuff
//            } else {
//                Toast.makeText(
//                    this, "REQUEST_READ_EXTERNAL_STORAGE Denied",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else -> super.onRequestPermissionsResult(
//                requestCode, permissions!!,
//                grantResults
//            )
//        }
//    }


//    private fun takePhotoFromCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, Constants.CAMERA)
//    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode === Activity.RESULT_OK) {
            imageSelection.onActivityResult(requestCode, resultCode, data, this)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Constants.GALLERY) {
//            if (data != null) {
//                val contentURI = data.data
//                try {
//                    var bitmap =
//                        MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
//                    scannedImageView.setImageBitmap(bitmap)
//
//                    val tempUri = com.salamtak.app.utils.getImageUri(this, bitmap)
//
//                    val finalFile = File(getRealPathFromURI(tempUri, contentResolver))
//                    finalFile.absolutePath
//                    Log.i("file", finalFile.absolutePath)
//
//
////                    var bitmap =
////                        MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
////                    bitmap = scaleDown(bitmap, 1024f, false)
////                    var out = ByteArrayOutputStream()
////                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
////
////                    val tempUri = getImageUri(this, bitmap)
////
////                    val finalFile = File(getRealPathFromURI(tempUri, contentResolver))
//                    profileActivityViewModel.image.value = finalFile.absolutePath
//                    Log.i("file", finalFile.absolutePath)
//
//                    //  iv_user_image!!.setImageBitmap(bitmap)
//                    Glide.with(this)
//                        .load(bitmap)
//                        .circleCrop()
//                        .into(iv_user_image);
//                } catch (e: IOException) {
//                    e.printStackTrace()
////                    showDialog(this, "Failed!")
//                    //openSalamtakDialog(getString(R.string.alert), "Failed!", R.drawable.ic_info)
//                    openSalamtakDialog(supportFragmentManager, "", "Failed!", R.drawable.ic_info)
//                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } else if (requestCode == Constants.CAMERA) {
//            var bitmap = data!!.extras!!.get("data") as Bitmap
//            bitmap = scaleDown(bitmap, 1024f, false)
//            var out = ByteArrayOutputStream()
//
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
//
//            val tempUri = getImageUri(this, bitmap)
//            if (tempUri != null) {
//                val finalFile = File(getRealPathFromURI(tempUri, contentResolver))
//                profileActivityViewModel.image.value = finalFile.absolutePath
//                Log.i("file", finalFile.absolutePath)
//            }
//
//            //iv_user_image!!.setImageBitmap(bitmap)
//
//            Glide.with(this)
//                .load(bitmap)
//                .circleCrop()
//                .into(iv_user_image);
//
//
////            saveImage(thumbnail)
////            showDialog(activity!!, "Image Saved!")
//            //Toast.makeText(AddQuestionActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//    }


    override fun browseClicked(
        type: FinancialTypeAttachments,
        position: Int,
        allowedNumToUpload: Int, categoryId: Int
    ) {

    }

    override fun browseClicked() {
        imageSelection.position = 0
        imageSelection.askPermission()
    }

    override fun handleAttachedImage(bitmap: Bitmap, file: File) {
        profileActivityViewModel.image.value = file.absolutePath
        Log.i("file", file.absolutePath)

        iv_user_image!!.setImageBitmap(bitmap)
    }

    override fun handleAttachedImage(bitmap: Bitmap, file: File, typeId: String) {

    }

    override fun handleAttachedPDF(file: File) {
    }

    override fun imagesMoreThan3() {

    }

    override fun bigFile() {
        showMessage(getString(R.string.big_file_message))
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
        return if (path != null)
            Uri.parse(path)
        else
            null
    }


//    fun checkPermissionREAD_EXTERNAL_STORAGE(
//        context: Context?
//    ): Boolean {
//        val currentAPIVersion = Build.VERSION.SDK_INT
////        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
//        return if (ContextCompat.checkSelfPermission(
//                context!!,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    (context as Activity?)!!,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            ) {
//                showMessage(
//                    "External storage",
//                    ::yesClicked
//                )
//            } else {
//                ActivityCompat
//                    .requestPermissions(
//                        (context as Activity?)!!,
//                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
//                    )
//            }
//            false
//        } else {
//            true
//        }
////        } else {
////            true
////        }
//    }

//    fun yesClicked() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ),
//            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
//        )
//    }

//    override fun deleteImage(position: Int, typePosition: Int) {
//
//    }
//
//    override fun selectImage(position: Int, attachment: Attachment) {
//        //       startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to attachment.thumbnailUrl))
//    }

    override fun cancelUpload() {

    }

}
