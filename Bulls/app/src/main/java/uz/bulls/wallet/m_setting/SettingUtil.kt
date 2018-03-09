package uz.bulls.wallet.m_setting

import uz.bulls.wallet.m_main.ui.bean.CriptoCoin
import uz.greenwhite.lib.collection.MyArray


fun getShowingCoinsNames(): String {
    val showingCoinsIds = getShowingCoins()
    return MyArray.from(CriptoCoin.ALL_SUPPORT_COINS
            .filter({ showingCoinsIds.isEmpty() || showingCoinsIds.contains(it.id) })
            .map { it.name })
            .mkString(", ")
}