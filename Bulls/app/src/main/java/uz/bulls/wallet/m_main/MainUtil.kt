package uz.bulls.wallet.m_main

import org.ethereum.crypto.ECKey
import org.spongycastle.util.encoders.Hex
import uz.bulls.wallet.bean.CoinCore

fun generateNewCriptoCoinAddress(coinId: String): CoinCore {
    if (CoinCore.ETHEREUM == coinId || CoinCore.ETHEREUM_CLASSIC == coinId) {
        val ecKey = ECKey()
        val privateKey = Hex.toHexString(ecKey.privKeyBytes)
        val publicAddress = "0x${Hex.toHexString(ecKey.address)}"

        var coinName = "Ethereum"
        if (CoinCore.ETHEREUM_CLASSIC == coinId) {
            coinName = "Ethereum Classic"
        }
        val timeMillis = System.currentTimeMillis().toString()
        return CoinCore(coinId, coinName, privateKey, publicAddress, timeMillis, "")
    }
    throw RuntimeException("coinId not support: $coinId")
}