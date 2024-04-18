package lineage.vetal.server.game.game.effects

import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

object StatBonus {
    private const val MAX_STAT_VALUE = 100
    private val WIT_COMPUTE = doubleArrayOf(1.050, 20.000)
    private val MEN_COMPUTE = doubleArrayOf(1.010, -0.060)
    private val INT_COMPUTE = doubleArrayOf(1.020, 31.375)
    private val STR_COMPUTE = doubleArrayOf(1.036, 34.845)
    private val DEX_COMPUTE = doubleArrayOf(1.009, 19.360)
    private val CON_COMPUTE = doubleArrayOf(1.030, 27.632)

    val BASE_EVASION_ACCURACY = DoubleArray(MAX_STAT_VALUE) {
        sqrt(it.toDouble()) * 6;
    }

    val WIT_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(WIT_COMPUTE[0].pow(it - WIT_COMPUTE[1]) * 100 + .5) / 100;
    }
    val MEN_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(MEN_COMPUTE[0].pow(it - MEN_COMPUTE[1]) * 100 + .5) / 100;
    }
    val INT_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(INT_COMPUTE[0].pow(it - INT_COMPUTE[1]) * 100 + .5) / 100;
    }
    val STR_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(STR_COMPUTE[0].pow(it - STR_COMPUTE[1]) * 100 + .5) / 100;
    }
    val DEX_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(DEX_COMPUTE[0].pow(it - DEX_COMPUTE[1]) * 100 + .5) / 100;
    }
    val CON_BONUS = DoubleArray(MAX_STAT_VALUE) {
        floor(CON_COMPUTE[0].pow(it - CON_COMPUTE[1]) * 100 + .5) / 100;
    }
    val SQRT_MEN_BONUS = DoubleArray(MAX_STAT_VALUE){
        sqrt(MEN_BONUS[it])
    }
    val SQRT_CON_BONUS = DoubleArray(MAX_STAT_VALUE) {
        sqrt(CON_BONUS[it])
    }
}