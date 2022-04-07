package com.salamtak.app.ui.common

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.openSalamtakDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {

    // Child classes must set the layoutId
    abstract var getLayoutId: Int
    private var shown = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetStyle)
        isCancelable = true
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (shown) return
        super.show(manager, tag)
        shown = true
    }

    override fun dismiss() {
        shown = false
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        shown = false
        super.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(getLayoutId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    fun showToastMessage(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    fun showErrorMessage(message: String) =
        openCustomDialog(getString(R.string.error), message, R.drawable.ic_warning)

    fun showServerErrorMessage(errorResponse: ErrorResponse) {
        if (errorResponse?.errors != null && errorResponse?.errors?.isNotEmpty()!!)
            openSalamtakDialog(
                parentFragmentManager,
                getString(R.string.error),
                errorResponse?.errors?.get(0)?.error!!,
                R.drawable.ic_server_error
            )
    }

    private fun openCustomDialog(title: String, text: String, icon: Int) {
        val dialog = SalamtakDialog(null, true)
        val bundle = Bundle().apply {
            putInt(Constants.KEY_ICON, icon)
            putString(Constants.KEY_TEXT, text)
            putString(Constants.KEY_TITLE, title)
        }
        dialog.arguments = bundle
        dialog.show(parentFragmentManager, getString(R.string.app_name))
    }

    fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + number)
        startActivity(intent)
    }
}