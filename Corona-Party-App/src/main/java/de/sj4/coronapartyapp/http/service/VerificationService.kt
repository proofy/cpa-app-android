package de.sj4.coronapartyapp.http.service

import de.sj4.coronapartyapp.http.requests.RegistrationTokenRequest
import de.sj4.coronapartyapp.http.requests.ReqistrationRequest
import de.sj4.coronapartyapp.http.requests.TanRequestBody
import de.sj4.coronapartyapp.http.responses.RegistrationTokenResponse
import de.sj4.coronapartyapp.http.responses.TanResponse
import de.sj4.coronapartyapp.http.responses.TestResultResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface VerificationService {

    @POST
    suspend fun getRegistrationToken(
        @Url url: String,
        @Header("cwa-fake") fake: String,
        @Body requestBody: RegistrationTokenRequest
    ): RegistrationTokenResponse

    @POST
    suspend fun getTestResult(
        @Url url: String,
        @Header("cwa-fake") fake: String,
        @Body request: ReqistrationRequest
    ): TestResultResponse

    @POST
    suspend fun getTAN(
        @Url url: String,
        @Header("cwa-fake") fake: String,
        @Body requestBody: TanRequestBody
    ): TanResponse
}
