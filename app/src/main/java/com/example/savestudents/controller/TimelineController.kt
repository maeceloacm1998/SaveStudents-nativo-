package com.example.savestudents.controller

import com.airbnb.epoxy.EpoxyController
import com.example.savestudents.R
import com.example.savestudents.holder.informationHolder
import com.example.savestudents.model.contract.TimelineContract
import com.example.savestudents.ui_component.alert.alertHolder
import com.example.savestudents.ui_component.separator.separatorHolder
import com.example.savestudents.ui_component.shimmer.shimmerHolder
import com.example.savestudents.ui_component.title.titleHolder

class TimelineController(private val mContract: TimelineContract) : EpoxyController() {
    private var isLoading = false

    override fun buildModels() {
        handleTopItems()
        handleListItems()
    }

    private fun handleTopItems() {
        handleInformationHolder()
        handleSeparator()
        handleAlert()
    }

    private fun handleInformationHolder() {
        if (isLoading) {
            shimmerHolder {
                id("information_shimmer")
                marginTop(24)
                marginRight(24)
                marginLeft(24)
            }
        } else {
            informationHolder {
                id("information")
                title("Algoritmos em Grafos ")
                period("4º Período")
                shift("Turno: Noite")
                teacher("Docente: Rothyele")
                isNotificationActivated(true)
                marginTop(24)
                marginRight(24)
                marginLeft(24)
            }
        }
    }

    private fun handleSeparator() {
        separatorHolder {
            id("separator")
            marginTop(24)
            marginLeft(16)
            marginRight(16)
        }
    }

    private fun handleAlert() {
        if (isLoading) {
            shimmerHolder {
                id("shimmer_alert")
                layout(R.layout.alert_shimmer)
                marginTop(16)
                marginBottom(24)
                marginLeft(16)
                marginRight(16)
            }

        } else {
            alertHolder {
                id("alert_holder")
                title("Notificação Ativa! Você será notificado de todas as atividades presentes nesta matéria")
                marginTop(16)
                marginBottom(24)
                marginLeft(16)
                marginRight(16)
            }
        }
    }

    private fun handleListItems() {
        titleHolder {
            id("list_title")
            title("Cronograma")
            marginTop(24)
            marginBottom(24)
            marginLeft(16)
            marginRight(16)
        }
    }

    fun setLoading(status: Boolean) {
        isLoading = status
        requestModelBuild()
    }

}