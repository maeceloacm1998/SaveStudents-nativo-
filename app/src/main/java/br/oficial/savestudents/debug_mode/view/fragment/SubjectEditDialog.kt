package br.oficial.savestudents.debug_mode.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.oficial.savestudents.R
import br.oficial.savestudents.databinding.SubjectEditDialogBinding
import br.oficial.savestudents.debug_mode.view.activity.EditSubjectActivity
import br.oficial.savestudents.debug_mode.view.activity.EditTimelineActivity

class SubjectEditDialog: DialogFragment() {
    lateinit var binding: SubjectEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubjectEditDialogBinding.inflate(inflater, container, false)
        clickCloseDialog()
        clickEditSubject()
        clickEditTimeline()

        return binding.root
    }

    private fun clickCloseDialog() {
        binding.closeDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun clickEditSubject() {
        binding.editSubject.setOnClickListener {
            val intent = EditSubjectActivity.newInstance(mId, binding.subjectEditContainer.context)
            startActivityForResult(intent, EditSubjectActivity.EDIT_SUBJECT_REQUEST_CODE)
            dismiss()
        }
    }


    private fun clickEditTimeline() {
        binding.editTimeline.setOnClickListener {
            val intent = EditTimelineActivity.newInstance(it.context, mId)
            startActivity(intent)
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Theme_Dialog)
    }

    companion object {
        const val TAG = "SubjectEditDialog"
        private var mId: String? = null

        fun saveBundle(id: String) {
            this.mId = id
        }
    }
}