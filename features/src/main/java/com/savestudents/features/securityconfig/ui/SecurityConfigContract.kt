package com.savestudents.features.securityconfig.ui

import com.savestudents.core.mvp.BasePresenter
import com.savestudents.core.mvp.BaseView

interface SecurityConfigContract {
    interface View : BaseView<Presenter> {
        fun handleEmail(email: String)
        fun handleName(name: String)
        fun handleDate(date: String)
    }

    interface Presenter : BasePresenter {
        fun fetchUser()
    }
}