package de.sj4.coronapartyapp.ui.submission

import de.sj4.coronapartyapp.storage.SubmissionRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SubmissionTanViewModelTest {
    private var viewModel: SubmissionTanViewModel = SubmissionTanViewModel()

    @Test
    fun tanFormatValid() {
        viewModel.tan.postValue("ZWFPC7NG47")
        viewModel.isValidTanFormat.value?.let { assertTrue(it) }

        viewModel.tan.postValue("ABC")
        viewModel.isValidTanFormat.value?.let { assertFalse(it) }

        viewModel.tan.postValue("ZWFPC7NG48")
        viewModel.isValidTanFormat.value?.let { assertFalse(it) }

        viewModel.tan.postValue("ZWFPC7NG4A")
        viewModel.isValidTanFormat.value?.let { assertFalse(it) }
    }

    @Test
    fun testTanStorage() {
        val sr = mockk<SubmissionRepository> {
            every { setTeletan(any()) } just Runs
        }
        val tan = "ZWFPC7NG47"
        sr.setTeletan(tan)

        verify(exactly = 1) {
            sr.setTeletan(
                withArg {
                    assertEquals(it, tan)
                })
        }
    }
}
