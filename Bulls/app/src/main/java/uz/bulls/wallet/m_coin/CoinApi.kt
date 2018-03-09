package uz.bulls.wallet.m_coin

import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.datasource.Pref
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.util.Util
import uz.greenwhite.lib.uzum.Uzum


private val C_COIN_INFOS = "coin_infos"

fun getPref() = Pref(BullsApp.getInstance(), "bulls:coin")

fun getCoinInfos(coinId: String): MyArray<CoinCore> {
    val coinInfoJson = Util.nvl(getPref().load("${C_COIN_INFOS}:${coinId}"))
    if (coinInfoJson.isEmpty()) {
        return MyArray.emptyArray()
    }
    val uzumAdapter = CoinCore.getCoinAdapter<CoinCore>(coinId).toArray()
    return Uzum.toValue(coinInfoJson, uzumAdapter)
}

fun saveCoinInfo(coinCore: CoinCore) {
    val coinInfos = getCoinInfos(coinCore.id).append(coinCore)
    val uzumAdapter = CoinCore.getCoinAdapter<CoinCore>(coinCore.id).toArray()
    getPref().save("${C_COIN_INFOS}:${coinCore.id}", Uzum.toJson(coinInfos, uzumAdapter))
}

fun clearCoinInfo(coinId: String) {
    getPref().save("${C_COIN_INFOS}:${coinId}", "")
}