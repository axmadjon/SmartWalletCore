package uz.bulls.wallet.m_coinmarketcap

import android.text.TextUtils
import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.datasource.Pref

private val K_CASHE = "coin_market_cap:cache"
private val K_CACHE_TIME = "coin_market_cap:cache:time"

fun getPref() = Pref(BullsApp.getInstance(), "bulls_pref")

fun getCoinMarketCache(key: String, default: String): String {
    var result = getPref().load("${key}:${K_CASHE}")
    if (TextUtils.isEmpty(result)) {
        result = default
    }
    return result!!
}

fun getCacheTime(key: String): Int {
    var result = getPref().load("${key}:${K_CACHE_TIME}")
    if (TextUtils.isEmpty(result)) {
        result = "0"
    }
    return result!!.toInt()
}

fun saveCoinMarketCache(key: String, value: String) {
    getPref().save("${key}:${K_CASHE}", value)
}

fun saveCacheTime(key: String, value: Int) {
    getPref().save("${key}:${K_CACHE_TIME}", value.toString())
}