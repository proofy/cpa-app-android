@file:JvmName("FormatterAccessibilityHelper")

package de.sj4.coronapartyapp.util.formatter

import de.sj4.coronapartyapp.CoronaWarnApplication
import de.sj4.coronapartyapp.R

fun formatSuffix(string: String?, suffix: Int): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return if (string != null) {
        "$string ${appContext.getString(suffix)}"
    } else ""
}

fun formatButton(string: String?): String = formatSuffix(string, R.string.suffix_button)

fun formatImage(string: String?): String = formatSuffix(string, R.string.suffix_image)
