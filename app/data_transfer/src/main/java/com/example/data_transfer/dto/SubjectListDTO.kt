package com.example.data_transfer.dto

import com.example.data_transfer.model.SubjectList

data class SubjectListDto(
    var id: String = "",
    var subjectName: String = "",
    var teacherName: String = "",
    var period: String = "",
    var shift: String = ""
)

fun SubjectListDto.asModel(): SubjectList =
    SubjectList(id, subjectName, teacherName, period, shift)

fun List<SubjectListDto>.asDomainModel(): List<SubjectList> {
    return this.map {
        SubjectList(
            it.id, it.subjectName, it.teacherName, it.period, it.shift
        )
    }
}