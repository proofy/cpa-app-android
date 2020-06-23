package de.sj4.coronapartyapp.http.requests

import com.google.gson.annotations.SerializedName

data class ReqistrationRequest(
    @SerializedName("registrationToken")
    val registrationToken: String
)
