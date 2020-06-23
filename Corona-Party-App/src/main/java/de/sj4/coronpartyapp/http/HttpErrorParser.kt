package de.sj4.coronapartyapp.http

import de.sj4.coronapartyapp.exception.http.BadGatewayException
import de.sj4.coronapartyapp.exception.http.BadRequestException
import de.sj4.coronapartyapp.exception.http.ConflictException
import de.sj4.coronapartyapp.exception.http.CwaClientError
import de.sj4.coronapartyapp.exception.http.CwaInformationalNotSupportedError
import de.sj4.coronapartyapp.exception.http.CwaRedirectNotSupportedError
import de.sj4.coronapartyapp.exception.http.CwaServerError
import de.sj4.coronapartyapp.exception.http.CwaSuccessResponseWithCodeMismatchNotSupportedError
import de.sj4.coronapartyapp.exception.http.CwaUnknownHostException
import de.sj4.coronapartyapp.exception.http.CwaWebException
import de.sj4.coronapartyapp.exception.http.ForbiddenException
import de.sj4.coronapartyapp.exception.http.GatewayTimeoutException
import de.sj4.coronapartyapp.exception.http.GoneException
import de.sj4.coronapartyapp.exception.http.HTTPVersionNotSupported
import de.sj4.coronapartyapp.exception.http.InternalServerErrorException
import de.sj4.coronapartyapp.exception.http.NetworkAuthenticationRequiredException
import de.sj4.coronapartyapp.exception.http.NetworkConnectTimeoutException
import de.sj4.coronapartyapp.exception.http.NetworkReadTimeoutException
import de.sj4.coronapartyapp.exception.http.NotFoundException
import de.sj4.coronapartyapp.exception.http.NotImplementedException
import de.sj4.coronapartyapp.exception.http.ServiceUnavailableException
import de.sj4.coronapartyapp.exception.http.TooManyRequestsException
import de.sj4.coronapartyapp.exception.http.UnauthorizedException
import de.sj4.coronapartyapp.exception.http.UnsupportedMediaTypeException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class HttpErrorParser : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())
            return when (val code = response.code) {
                200 -> response
                201 -> response
                202 -> response
                204 -> response
                400 -> throw BadRequestException()
                401 -> throw UnauthorizedException()
                403 -> throw ForbiddenException()
                404 -> throw NotFoundException()
                409 -> throw ConflictException()
                410 -> throw GoneException()
                415 -> throw UnsupportedMediaTypeException()
                429 -> throw TooManyRequestsException()
                500 -> throw InternalServerErrorException()
                501 -> throw NotImplementedException()
                502 -> throw BadGatewayException()
                503 -> throw ServiceUnavailableException()
                504 -> throw GatewayTimeoutException()
                505 -> throw HTTPVersionNotSupported()
                511 -> throw NetworkAuthenticationRequiredException()
                598 -> throw NetworkReadTimeoutException()
                599 -> throw NetworkConnectTimeoutException()
                else -> {
                    if (code in 100..199) throw CwaInformationalNotSupportedError(code)
                    if (code in 200..299) throw CwaSuccessResponseWithCodeMismatchNotSupportedError(
                        code
                    )
                    if (code in 300..399) throw CwaRedirectNotSupportedError(code)
                    if (code in 400..499) throw CwaClientError(code)
                    if (code in 500..599) throw CwaServerError(code)
                    throw CwaWebException(code)
                }
            }
        } catch (err: UnknownHostException) {
            throw CwaUnknownHostException()
        }
    }
}
