package uz.bulls.wallet.m_coinmarketcap.ui.bean

import uz.bulls.wallet.R
import uz.greenwhite.lib.collection.MyArray

val C_BTC = "bitcoin"
val C_ETH = "ethereum"
val C_ETC = "ethereum-classic"
val C_XRP = "ripple"

val DEFAULT_COINS = MyArray.from(
        CriptoCoin(C_BTC),
        CriptoCoin(C_ETH),
        CriptoCoin(C_ETC),
        CriptoCoin(C_XRP))

class CriptoCoin(val coinId: String, var coinMarket: CoinMarket? = null) {

    fun getCoinIconResId(): Int {
        when (coinId) {
            C_BTC -> return R.mipmap.ic_bitcoin
            C_ETH -> return R.mipmap.ic_ethereum
            C_ETC -> return R.mipmap.ic_ethereum_classic
            C_XRP -> return R.mipmap.ic_ripple
            else -> {
                return R.drawable.ic_launcher
            }
        }
    }
}