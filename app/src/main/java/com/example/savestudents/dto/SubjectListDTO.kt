package com.example.savestudents.dto

import com.example.savestudents.model.SubjectList

data class SubjectListDto(
    var id: Long? = 0,
    var title: String? = "",
    var period: String? = "",
    var shift: String? = ""
)

fun List<SubjectListDto>.asDomainModel(): List<SubjectList> {
    return this.map {
        SubjectList(
            it.id, it.title, it.period
        )
    }
}