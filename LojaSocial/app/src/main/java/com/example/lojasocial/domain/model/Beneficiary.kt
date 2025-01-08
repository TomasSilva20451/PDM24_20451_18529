package com.example.lojasocial.domain.model

data class Beneficiary(
    val id: String = "",
    val nome: String = "",
    val agregadoFamiliar: Int = 0,
    val dataNascimento: String = "",
    val nacionalidade: String = ""
)