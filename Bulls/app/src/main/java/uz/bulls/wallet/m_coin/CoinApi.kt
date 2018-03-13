package uz.bulls.wallet.m_coin

import android.text.TextUtils
import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.datasource.Pref
import uz.bulls.wallet.m_main.saveMainCoinAddress
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.util.Util
import uz.greenwhite.lib.uzum.Uzum
import java.math.BigDecimal


private val C_COIN_INFOS = "coin_infos"
private val C_COIN_BALANCE = "coin_balance"

fun getPref() = Pref(BullsApp.getInstance(), "bulls:coin")

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
