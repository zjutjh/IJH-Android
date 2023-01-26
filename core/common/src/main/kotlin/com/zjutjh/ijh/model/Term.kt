package com.zjutjh.ijh.model

enum class Term(val value: String) {
    FIRST("上"),
    SECOND("下"),
    SHORT("短");
}

fun Int.toTerm(): Term =
    when (this) {
        Term.FIRST.ordinal -> Term.FIRST
        Term.SECOND.ordinal -> Term.SECOND
        Term.SHORT.ordinal -> Term.SHORT
        else -> throw IllegalArgumentException("$this is not in ordinals of Term enum")
    }

fun String.toTerm(): Term =
    when (this) {
        Term.FIRST.value -> Term.FIRST
        Term.SECOND.value -> Term.SECOND
        Term.SHORT.value -> Term.SHORT
        else -> throw IllegalArgumentException("$this is not in values of Term enum")
    }