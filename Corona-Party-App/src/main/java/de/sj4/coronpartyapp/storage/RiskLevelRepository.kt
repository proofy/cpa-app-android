package de.sj4.coronapartyapp.storage

import androidx.lifecycle.MutableLiveData
import de.sj4.coronapartyapp.risk.RiskLevel
import de.sj4.coronapartyapp.risk.RiskLevelConstants

object RiskLevelRepository {

    /**
     * LiveData variables that can be consumed in a ViewModel to observe RiskLevel changes
     */
    val riskLevelScore = MutableLiveData(RiskLevelConstants.UNKNOWN_RISK_INITIAL)
    val riskLevelScoreLastSuccessfulCalculated =
        MutableLiveData(LocalData.lastSuccessfullyCalculatedRiskLevel().raw)

    /**
     * Set the new calculated [RiskLevel]
     * Calculation happens in the [de.sj4.coronapartyapp.transaction.RiskLevelTransaction]
     *
     * @see de.sj4.coronapartyapp.transaction.RiskLevelTransaction
     * @see de.sj4.coronapartyapp.risk.RiskLevelCalculation
     *
     * @param riskLevel
     */
    fun setRiskLevelScore(riskLevel: RiskLevel) {
        val rawRiskLevel = riskLevel.raw
        riskLevelScore.postValue(rawRiskLevel)

        setLastCalculatedScore(rawRiskLevel)
        setLastSuccessfullyCalculatedScore(riskLevel)
    }

    /**
     * Set the current risk level from the last calculated risk level.
     * This is necessary if the app has no connectivity and the risk level transaction
     * fails.
     *
     * @see de.sj4.coronapartyapp.transaction.RiskLevelTransaction
     *
     */
    fun setLastCalculatedRiskLevelAsCurrent() {
        var lastRiskLevelScore = getLastCalculatedScore()
        if (lastRiskLevelScore == RiskLevel.UNDETERMINED) {
            lastRiskLevelScore = RiskLevel.UNKNOWN_RISK_INITIAL
        }
        riskLevelScore.postValue(lastRiskLevelScore.raw)
    }

    /**
     * Get the last calculated RiskLevel
     *
     * @return
     */
    fun getLastCalculatedScore(): RiskLevel = LocalData.lastCalculatedRiskLevel()

    /**
     * Set the last calculated RiskLevel
     *
     * @param rawRiskLevel
     */
    private fun setLastCalculatedScore(rawRiskLevel: Int) =
        LocalData.lastCalculatedRiskLevel(rawRiskLevel)

    /**
     * Get the last successfully calculated [RiskLevel]
     *
     * @see RiskLevel
     *
     * @return
     */
    private fun getLastSuccessfullyCalculatedScore(): RiskLevel =
        LocalData.lastSuccessfullyCalculatedRiskLevel()

    /**
     * Refreshes repository variable with local data
     *
     */
    fun refreshLastSuccessfullyCalculatedScore() {
        riskLevelScoreLastSuccessfulCalculated.postValue(
            getLastSuccessfullyCalculatedScore().raw
        )
    }

    /**
     * Set the last successfully calculated [RiskLevel]
     *
     * @param riskLevel
     */
    private fun setLastSuccessfullyCalculatedScore(riskLevel: RiskLevel) {
        if (!RiskLevel.UNSUCCESSFUL_RISK_LEVELS.contains(riskLevel)) {
            LocalData.lastSuccessfullyCalculatedRiskLevel(riskLevel.raw)
            riskLevelScoreLastSuccessfulCalculated.postValue(riskLevel.raw)
        }
    }
}
