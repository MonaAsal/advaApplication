package com.salamtak.app.data.entities

data class MaritalStatus(
    var id: Int,
    val name: String
)
{
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}