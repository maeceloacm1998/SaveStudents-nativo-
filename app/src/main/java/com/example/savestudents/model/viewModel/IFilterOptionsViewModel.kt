package com.example.savestudents.model.viewModel

interface IFilterOptionsViewModel {
    fun getShiftOptions(collectionPath: String, orderByName: String)
    fun getPeriodOptions(collectionPath: String, orderByName: String)
}