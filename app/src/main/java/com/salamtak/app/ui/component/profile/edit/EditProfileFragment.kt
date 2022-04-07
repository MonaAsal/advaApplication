package com.salamtak.app.ui.component.profile.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialTypeAttachments
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.UpdateProfileResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.login.LoginViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_transparent.*
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() , AttachCallBacks {
    override val layoutId: Int
        get() = R.layout.fragment_edit_profile

    private val profileActivityViewModel: UpdateProfileViewModel by viewModels()

    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var imageSelection: ImageSelection


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()
        imageSelection = activity?.let { ImageSelection(requireActivity(), it?.supportFragmentManager, false, 1) }!!
        tv_toolbar_title.text=getString(R.string.edit_profile)
        initViews()
    }

    private fun initViews() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
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
        observe(profileActivityViewModel.updateProfileResponse, ::handleUpdateProfileResponse)
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


    private fun bindData(data: User) {
        et_email.setText(data.email ?: "")
        et_first_name.setText(data.firstName)
        et_last_name.setText(data.lastName)
        et_phone.setText(data.phone)
        iv_user_image.loadCircleImage(data.imageUrl)
    }



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


    override fun cancelUpload() {

    }



}