package de.sj4.coronapartyapp.http.interceptor

import de.sj4.coronapartyapp.exception.CwaWebSecurityException
import okhttp3.Interceptor
import okhttp3.Response
import javax.net.ssl.SSLException

class WebSecurityVerificationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: SSLException) {
            throw CwaWebSecurityException(e)
        }
    }
}
