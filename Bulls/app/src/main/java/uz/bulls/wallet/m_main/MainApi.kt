package uz.bulls.wallet.m_main

import android.text.TextUtils
import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.datasource.Pref
import uz.bulls.wallet.m_main.bean.CriptoCoin
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.util.Util
import uz.greenwhite.lib.uzum.Uzum

private val K_CASHE = "coin_market_cap:cache"
private val K_CACHE_TIME = "coin_market_cap:cache:time"
private val K_MY_COIN = "coin_market_cap:my_coin"
private val K_MAIN_ADDRESS = "main_coin:main_address"

fun getPref() = Pref(BullsApp.getInstance(), "bulls:main")

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

fun getMyCoins(): MyArray<CriptoCoin> {
    val myCoinsJson = Util.nvl(getPref().load(K_MY_COIN))
    return if (TextUtils.isEmpty(myCoinsJson)) {
        MyArray.emptyArray()
    } else {
        Uzum.toValue(myCoinsJson, CriptoCoin.UZUM_ADAPTER.toArray())
    }
}

fun saveMyCoins(items: MyArray<CriptoCoin>) {
    getPref().save(K_MY_COIN, Uzum.toJson(items, CriptoCoin.UZUM_ADAPTER.toArray()))
}

//##################################################################################################

fun getMainCoinAddress(coinId: String) = Util.nvl(getPref().load("$K_MAIN_ADDRESS:$coinId"))

fun saveMainCoinAddress(coinId: String, publicAddress: String) {
    getPref().save("$K_MAIN_ADDRESS:$coinId", publicAddress)
}

//##################################################################################################