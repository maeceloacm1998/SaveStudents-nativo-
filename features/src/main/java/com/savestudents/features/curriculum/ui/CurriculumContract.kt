package com.savestudents.features.curriculum.ui

import com.savestudents.components.calendar.EventCalendar
import com.savestudents.components.snackbar.SnackBarCustomType
import com.savestudents.features.addMatter.models.Event
import com.savestudents.core.mvp.BasePresenter
import com.savestudents.core.mvp.BaseView
import java.time.LocalDate

interface CurriculumContract {
    interface View : BaseView<Presenter> {
        fun init()
        fun error()
        fun loadingScreen(visibility: Boolean)
        fun onSetCurrentDate(localDate: LocalDate)
        fun showNotEvents(visibility: Boolean)
        fun updateEventList(eventList: List<Event.EventItem>, day: Int, month: Int)
        fun showSnackBar(message: Int, type: SnackBarCustomType)
        fun updateCalendar(eventCalendarList: List<EventCalendar>)
        fun clearCalendarEvents()
    }

    interface Presenter : BasePresenter {
        suspend fun fetchMatters()
        suspend fun fetchEventsWithDate(timestamp: Long)
        suspend fun deleteEvent(eventItem: Event.EventItem)
        fun onSetCurrentDate()
        fun onGetSelectedDate(): Long
    }
}