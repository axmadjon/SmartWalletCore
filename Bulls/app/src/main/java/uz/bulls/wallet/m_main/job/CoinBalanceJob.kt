package uz.bulls.wallet.m_main.job

import android.text.TextUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.common.compareTo
import uz.bulls.wallet.m_main.saveCoinBalance
import uz.greenwhite.lib.error.AppError
import uz.greenwhite.lib.job.ShortJob
import java.math.BigDecimal

private val ETHEREUM_API = "PSDK6YHBHHIRJCIFSR4FSVVCDTVFC1W1GR"
private val ETHEREUM_DIVIDE_AMOUNT = BigDecimal("1000000000000000000")

class CoinBalanceJob(private val coinId: String,
                     private val publicAddress: String) : ShortJob<String> {

    override fun execute(): String {
        val client = OkHttpClient()
        val builder = Request.Builder()

        if (coinId == CoinCore.ETHEREUM_CLASSIC) {
            builder.url("https://api.gastracker.io/v1/addr/${publicAddress}")
        } else if (coinId == CoinCore.ETHEREUM) {
            builder.url("https://api.etherscan.io/api?module=account&action=balancemulti&address=$publicAddress&tag=latest&apikey=${ETHEREUM_API}")
//          eth  1000000000000000000
        }

        val response = client.newCall(builder.build()).execute()

        val json = response.body()?.string()!!

        var balance: String? = null

        if (coinId == CoinCore.ETHEREUM_CLASSIC) {
            balance = JSONObject(json).getJSONObject("balance").getString("ether")

        } else if (coinId == CoinCore.ETHEREUM) {
            balance = JSONObject(json)
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getString("balance")

            if (BigDecimal.ZERO.compareTo(balance) != 0) {
                balance = BigDecimal(balance).divide(ETHEREUM_DIVIDE_AMOUNT).toPlainString()
            }
        }

        if (TextUtils.isEmpty(balance)) {
            throw AppError("balance is empty coinId:$balance")
        }
        saveCoinBalance(coinId, publicAddress, BigDecimal(balance))
        return balance!!
    }
}