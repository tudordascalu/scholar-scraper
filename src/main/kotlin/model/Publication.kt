package model

@kotlinx.serialization.Serializable
data class Publication(val title: String, val author: String, val year: Int?, val journal: String?)
