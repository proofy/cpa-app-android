package de.sj4.coronapartyapp.http

import de.sj4.coronapartyapp.http.service.DistributionService
import de.sj4.coronapartyapp.http.service.SubmissionService
import de.sj4.coronapartyapp.http.service.VerificationService
import de.sj4.coronapartyapp.service.diagnosiskey.DiagnosisKeyConstants
import de.sj4.coronapartyapp.util.TimeAndDateExtensions.toServerFormat
import de.sj4.coronapartyapp.util.security.VerificationKeys
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

class WebRequestBuilderTest {
    @MockK
    private lateinit var verificationService: VerificationService

    @MockK
    private lateinit var distributionService: DistributionService

    @MockK
    private lateinit var submissionService: SubmissionService

    @MockK
    private lateinit var verificationKeys: VerificationKeys

    private lateinit var webRequestBuilder: WebRequestBuilder

    @Before
    fun setUp() = run {
        MockKAnnotations.init(this)
        webRequestBuilder = WebRequestBuilder(
            distributionService,
            verificationService,
            submissionService,
            verificationKeys
        )
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun retrievingDateIndexIsSuccessful() {
        val urlString = DiagnosisKeyConstants.AVAILABLE_DATES_URL
        coEvery { distributionService.getDateIndex(urlString) }
            .returns(listOf("1900-01-01", "2000-01-01"))

        runBlocking {
            val expectedResult = listOf("1900-01-01", "2000-01-01")
            Assert.assertEquals(webRequestBuilder.asyncGetDateIndex(), expectedResult)
            coVerify {
                distributionService.getDateIndex(urlString)
            }
        }
    }

    @Test
    fun asyncGetHourIndex() {
        val day = Date()
        val urlString = DiagnosisKeyConstants.AVAILABLE_DATES_URL +
                "/${day.toServerFormat()}/${DiagnosisKeyConstants.HOUR}"

        coEvery { distributionService.getHourIndex(urlString) }
            .returns(listOf("1", "2"))

        runBlocking {
            val expectedResult = listOf("1", "2")
            Assert.assertEquals(webRequestBuilder.asyncGetHourIndex(day), expectedResult)
            coVerify {
                distributionService.getHourIndex(urlString)
            }
        }
    }
}
