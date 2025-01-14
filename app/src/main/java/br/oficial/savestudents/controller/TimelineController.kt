package br.oficial.savestudents.controller

import br.oficial.savestudents.R
import br.oficial.savestudents.helper.TimelineItemType.*
import br.oficial.savestudents.helper.TimelineItemTypeColorHelper
import br.oficial.savestudents.holder.timelineItemHolder
import br.oficial.savestudents.ui_component.shimmer.shimmerHolder
import com.br.core.utils.DateUtils
import com.airbnb.epoxy.EpoxyController
import com.example.data_transfer.model.TimelineItem

class TimelineController : EpoxyController() {
    private var timelineList: TimelineItem = TimelineItem()

    override fun buildModels() {
        handleListItems()
    }

    private fun handleListItems() {
        if (timelineList.timelineList.isNullOrEmpty()) {
            shimmerHolder {
                id("shimmer_item")
                layout(R.layout.timeline_item_shimmer)
                marginTop(16)
                marginBottom(16)
                marginLeft(16)
                marginRight(16)
            }
        } else {
            timelineList.timelineList?.forEach { item ->
                timelineItemHolder {
                    id(item.id)
                    timelineName(item.subjectName)
                    date(DateUtils.formatDate(item.date, DateUtils.DAY_AND_MONTH_DATE))
                    backgroundType(getBackgroundType(item.type))
                    currentDay(isCurrentDay(item.date))
                    marginTop(6)
                    marginBottom(6)
                    marginLeft(16)
                    marginRight(16)
                }
            }
        }
    }

    private fun isCurrentDay(date: Long): Boolean {
        val currentDay = DateUtils.getCurrentDay()
        return DateUtils.formatDate(
            currentDay, DateUtils.DAY_AND_MONTH_DATE
        ) == DateUtils.formatDate(date, DateUtils.DAY_AND_MONTH_DATE)
    }

    private fun getBackgroundType(type: String): TimelineItemTypeColorHelper {
        when (type) {
            CLASS.type -> {
                return TimelineItemTypeColorHelper.CLASS
            }

            EXAM.type -> {
                return TimelineItemTypeColorHelper.EXAM
            }

            HOLIDAY.type -> {
                return TimelineItemTypeColorHelper.HOLIDAY
            }
        }

        return TimelineItemTypeColorHelper.CLASS
    }

    fun setTimelineList(timelineList: TimelineItem) {
        this.timelineList = timelineList
        requestModelBuild()
    }
}