package de.sj4.coronapartyapp.http.responses

import com.google.gson.annotations.SerializedName

data class RegistrationTokenResponse(
    @SerializedName("registrationToken")
    val registrationToken: String
)
