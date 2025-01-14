package br.oficial.savestudents.debug_mode.controller

import com.airbnb.epoxy.EpoxyController
import br.oficial.savestudents.R
import br.oficial.savestudents.ui_component.checkbox.checkboxRadioHolder
import br.oficial.savestudents.ui_component.shimmer.shimmerHolder
import com.example.data_transfer.model.FilterOption
import com.example.data_transfer.model.contract.SelectOptionsContract

class SelectOptionsController(private val contract: SelectOptionsContract) : EpoxyController() {
    private var loading: Boolean = true
    private var optionsList: List<FilterOption> = mutableListOf()

    override fun buildModels() {
        if (loading) {
            shimmerHolder {
                id("checkbox_shimmer")
                layout(R.layout.checkbox_radio_shimmer)
                marginLeft(16)
                marginRight(5)
            }
        } else {
            optionsList.forEach { item ->
                checkboxRadioHolder {
                    id(item.id)
                    title(item.name)
                    check(contract::clickedCheckboxListener)
                    marginLeft(16)
                    marginRight(5)
                }
            }
        }
    }

    fun setOptionsList(data: List<FilterOption>) {
        optionsList = data
        requestModelBuild()
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
        requestModelBuild()
    }
}