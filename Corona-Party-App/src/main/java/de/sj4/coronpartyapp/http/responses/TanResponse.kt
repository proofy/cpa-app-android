package de.sj4.coronapartyapp.http.responses

import com.google.gson.annotations.SerializedName

data class TanResponse(
    @SerializedName("tan")
    val tan: String
)
