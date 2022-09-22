package br.com.savestudents.controller

import com.airbnb.epoxy.EpoxyController
import br.com.savestudents.constants.HomeConstants
import br.com.savestudents.holder.createTimelineItemHolder
import br.com.savestudents.holder.responseErrorHolder
import br.com.savestudents.model.CreateTimelineItem
import br.com.savestudents.model.contract.CreateTimelineContract
import br.com.savestudents.ui_component.separator.separatorHolder
import java.text.SimpleDateFormat
import java.util.*

class CreateTimelineController(private val mContract: CreateTimelineContract) : EpoxyController() {
    private val DATE_IN_PARSE = "dd/MM"
    private var timelineItemsList: MutableList<CreateTimelineItem> = mutableListOf()

    override fun buildModels() {
        if(timelineItemsList.isEmpty()) {
            handleResponseError()
        } else {
            handleTimelineItemsList()
        }
    }

    private fun handleResponseError() {
        responseErrorHolder {
            id(HomeConstants.Filter.TYPE_FILTER_ERROR)
            message("No momento está vazio.")
            description("Crie atividades para esta matéria")
        }
    }

    private fun handleTimelineItemsList() {
        timelineItemsList.forEach { item ->
            createTimelineItemHolder {
                id(item.id)
                mId(item.id)
                subjectName(item.subjectName)
                date(formatDate(item.date))
                clickEditButtonListener(mContract::clickEditButtonListener)
                clickDeleteButtonListener(mContract::clickDeleteButtonListener)
                marginLeft(10)
                marginRight(10)
                marginTop(10)
                marginBottom(5)
            }

            separatorHolder {
                id("$item.id separator")
                marginRight(10)
                marginLeft(10)
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val pattern = SimpleDateFormat(DATE_IN_PARSE, Locale.getDefault())
        return pattern.format(Date(timestamp))
    }

    fun setTimelineItemsList(timelineItemsList: MutableList<CreateTimelineItem>) {
        this.timelineItemsList = timelineItemsList
        requestModelBuild()
    }
}