package uz.bulls.wallet.m_main

import android.text.TextUtils
import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.datasource.Pref
import uz.bulls.wallet.m_main.bean.CriptoCoin
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.util.Util
import uz.greenwhite.lib.uzum.Uzum
import java.math.BigDecimal

private val K_CASHE = "coin_market_cap:cache"
private val K_CACHE_TIME = "coin_market_cap:cache:time"
private val K_MY_COIN = "coin_market_cap:my_coin"
private val K_MAIN_ADDRESS = "main_coin:main_address"
private val C_COIN_INFOS = "main_coin:coin_infos"
private val C_COIN_BALANCE = "main_coin:coin_balance"

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

fun getCoinCore(coinId: String): MyArray<CoinCore> {
    val coinInfoJson = Util.nvl(getPref().load("${C_COIN_INFOS}:${coinId}"))
    if (coinInfoJson.isEmpty()) {
        return MyArray.emptyArray()
    }
    val uzumAdapter = CoinCore.getCoinAdapter<CoinCore>(coinId).toArray()
    return Uzum.toValue(coinInfoJson, uzumAdapter)
}

fun saveCoinCore(coinCore: CoinCore, mainCoin: Boolean = false) {
    val coinInfos = getCoinCore(coinCore.id).filter { it.publicAddress != coinCore.publicAddress }
    val finalCoinInfos = MyArray.from(coinInfos).append(coinCore)

    val uzumAdapter = CoinCore.getCoinAdapter<CoinCore>(coinCore.id).toArray()
    getPref().save("${C_COIN_INFOS}:${coinCore.id}", Uzum.toJson(finalCoinInfos, uzumAdapter))

    if (mainCoin) {
        saveMainCoinAddress(coinCore.id, coinCore.publicAddress)
    }
}

fun removeCoinCoreAddress(coinCore: CoinCore) {
    val filterCoinCore = getCoinCore(coinCore.id)
            .filter({ it.publicAddress != coinCore.publicAddress })

    val uzumAdapter = CoinCore.getCoinAdapter<CoinCore>(coinCore.id).toArray()
    getPref().save("${C_COIN_INFOS}:${coinCore.id}", Uzum.toJson(MyArray.from(filterCoinCore), uzumAdapter))
}

fun clearCoinCore(coinId: String) {
    getPref().save("${C_COIN_INFOS}:${coinId}", "")
}

//##################################################################################################

fun getCoinBalance(coinId: String, publicAddress: String): BigDecimal {
    val balance = getPref().load("$C_COIN_BALANCE:$coinId:$publicAddress")

    if (TextUtils.isEmpty(balance)) {
        return BigDecimal.ZERO
    }
    return BigDecimal(balance)
}

fun saveCoinBalance(coinId: String, publicAddress: String, balance: BigDecimal) {
    getPref().save("$C_COIN_BALANCE:$coinId:$publicAddress", balance.toPlainString())
}

//##################################################################################################
