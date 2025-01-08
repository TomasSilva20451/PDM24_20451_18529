package com.example.lojasocial.domain.model

data class Appointment(
    val id: String = "",
    val beneficiaryId: String = "",
    val data: String = "",       // Ex.: "2025-01-10" ou outro formato
    val horario: String = "",    // Ex.: "14:00"
    val status: String = "pendente", // "pendente", "confirmado", etc.
    val observacoes: String = ""
)