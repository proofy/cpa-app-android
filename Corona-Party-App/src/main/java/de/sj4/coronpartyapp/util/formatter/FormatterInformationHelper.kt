@file:JvmName("FormatterInformationHelper")

package de.sj4.coronapartyapp.util.formatter

import de.sj4.coronapartyapp.BuildConfig
import de.sj4.coronapartyapp.CoronaWarnApplication
import de.sj4.coronapartyapp.R

fun formatVersion(): String {
    val appContext = CoronaWarnApplication.getAppContext()
    val versionName: String = BuildConfig.VERSION_NAME
    return appContext.getString(R.string.information_version).format(versionName)
}
