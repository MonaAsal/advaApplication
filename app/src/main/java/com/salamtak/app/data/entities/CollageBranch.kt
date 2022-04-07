package com.salamtak.app.data.entities

data class CollageBranch(
    val busFees: Int,
    val id: String,
    val name: String,
    val semesters: List<Semester>
)