package com.savestudents.features.accountRegister.ui

import com.savestudents.core.accountManager.model.UserAccount
import com.savestudents.features.mvp.BasePresenter
import com.savestudents.features.mvp.BaseView

interface AccountRegisterContract {

    interface View : BaseView<Presenter> {
        fun setInstitutionList(institutions: List<String>)
        fun showEmailFormatError()
        fun showBirthdateFormatError()
        fun showEmptyNameField()
        fun showEmptyEmailField()
        fun showEmptyInstitutionField()
        fun showEmptyBirthDateField()
        fun showEmptyPasswordField()
        fun clearFieldsError()
        fun goToHomeFragment()
        fun errorRegisterUser(error: Boolean)
        fun loading(loading: Boolean)
    }

    interface Presenter : BasePresenter {
        suspend fun fetchInstitution()
        suspend fun validateFields(user: UserAccount, password: String)
        suspend fun saveData(user: UserAccount, password: String)
    }
}