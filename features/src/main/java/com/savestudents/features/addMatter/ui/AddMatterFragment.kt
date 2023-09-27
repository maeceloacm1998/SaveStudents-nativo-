package com.savestudents.features.addMatter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.savestudents.components.button.disabled
import com.savestudents.components.textInput.TextInputCustomView
import com.savestudents.core.utils.BaseFragment
import com.savestudents.core.utils.DateUtils
import com.savestudents.features.NavigationActivity
import com.savestudents.features.R
import com.savestudents.features.addMatter.models.Matter
import com.savestudents.features.databinding.FragmentAddMatterBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AddMatterFragment :
    BaseFragment<FragmentAddMatterBinding, NavigationActivity>(FragmentAddMatterBinding::inflate),
    AddMatterContract.View {

    override val presenter: AddMatterContract.Presenter by inject { parametersOf(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity?.handleTitleToolbar("Adicionar Matéria")
        lifecycleScope.launch {
            presenter.fetchMatters()
        }
        setupViews()
    }

    private fun setupViews() {
        binding.run {
            initialHourTextLayout.onClickInputNotKeyboard {
                val picker = handleTimePicker(getString(R.string.add_matter_select_initial_hour))
                picker.show(parentFragmentManager, "")

                picker.run {
                    addOnPositiveButtonClickListener {
                        val time: String =
                            DateUtils.formatTime(this.hour.toString(), this.minute.toString())
                        presenter.saveInitialHourSelected(time)
                        setTimeInEditText(binding.initialHourTextLayout, time)
                    }
                }
            }

            finalHourTextLayout.onClickInputNotKeyboard {
                val picker = handleTimePicker(getString(R.string.add_matter_select_final_hour))
                picker.show(parentFragmentManager, "")

                picker.run {
                    addOnPositiveButtonClickListener {
                        val time: String =
                            DateUtils.formatTime(this.hour.toString(), this.minute.toString())
                        presenter.saveFinalHourSelected(time)
                        setTimeInEditText(binding.finalHourTextLayout, time)
                    }
                }
            }

            matterTextInput.onItemSelected { item ->
                presenter.matterSelect(item)
            }

            submitButton.setOnClickListener {
                val daysSelected: List<String> = chipGroup.children
                    .toList()
                    .filter { (it as Chip).isChecked }
                    .map { (it as Chip).text.toString() }

                submitButton.disabled(true)
                lifecycleScope.launch {
                    presenter.registerMatter(daysSelected)
                }
            }
        }
    }

    private fun setTimeInEditText(editable: TextInputCustomView, time: String) {
        editable.editTextDefault.setText(time)
    }

    private fun handleTimePicker(title: String): MaterialTimePicker {
        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(8)
            .setMinute(0)
            .setInputMode(INPUT_MODE_CLOCK)
            .setTitleText(title)
            .build()
    }

    override fun loading(loading: Boolean) {
        binding.run {
            if (loading) {
                this.loading.root.isVisible = true
                this.error.root.isVisible = false
                this.layout.isVisible = false

            } else {
                this.loading.root.isVisible = false
                this.error.root.isVisible = false
                this.layout.isVisible = true
            }
        }
    }

    override fun showError() {
        binding.error.root.isVisible = true
    }

    override fun setMatterOptions(matterList: List<String>) {
        binding.matterTextInput.setAutocompleteItems = matterList
    }

    override fun handleMatterSelect(matter: Matter) {
        binding.run {
            matterInformation.isVisible = true
            matterName.text = matter.matterName
            period.text = getString(R.string.add_matter_period, matter.period)
            teacher.text = getString(R.string.add_matter_teacher, matter.teacherName)
        }
    }
}