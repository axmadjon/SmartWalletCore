package uz.bulls.wallet.m_main.job

import android.text.TextUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import uz.bulls.wallet.common.map
import uz.bulls.wallet.m_main.getCacheTime
import uz.bulls.wallet.m_main.getCoinMarketCache
import uz.bulls.wallet.m_main.saveCacheTime
import uz.bulls.wallet.m_main.saveCoinMarketCache
import uz.bulls.wallet.m_main.bean.CoinMarket
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.job.ShortJob


private val C_TICKER = "ticker"

class CoinMarketJob(val coinId: String) : ShortJob<CoinMarket> {

    @Throws(Exception::class)
    override fun execute(): CoinMarket? {
        val client = OkHttpClient()
        val key = "${C_TICKER}=${coinId}"

        var json = getCoinMarketCache(key, "")
        val time = getCacheTime(key)

        val timeLimit = 5 * 60 * 1000

        if (TextUtils.isEmpty(json) || 0 == time ||
                (System.currentTimeMillis().toInt() - time >= timeLimit)) {
            val response = client.newCall(Request.Builder()
                    .url("https://api.coinmarketcap.com/v1/ticker/${coinId}")
                    .build()).execute()

            json = response.body()?.string()!!

            saveCoinMarketCache(key, json)
            saveCacheTime(key, System.currentTimeMillis().toInt())
            return toCMCValue(JSONArray(json))[0]
        }
        return toCMCValue(JSONArray(json))[0]
    }

    /*

        {
            "id": "bitcoin",
            "name": "Bitcoin",
            "symbol": "BTC",
            "rank": "1",
            "price_usd": "11316.5",
            "price_btc": "1.0",
            "24h_volume_usd": "6411300000.0",
            "market_cap_usd": "191281667850",
            "available_supply": "16902900.0",
            "total_supply": "16902900.0",
            "max_supply": "21000000.0",
            "percent_change_1h": "-0.33",
            "percent_change_24h": "-1.88",
            "percent_change_7d": "7.46",
            "last_updated": "1520321966"
        },

     */

    fun toCMCValue(json: JSONArray): MyArray<CoinMarket> = json.map {
        CoinMarket(it.optString("id"),
                it.optString("name"),
                it.optString("symbol"),
                it.optString("rank"),
                it.optString("price_usd"),
                it.optString("price_btc"),
                it.optString("24h_volume_usd"),
                it.optString("market_cap_usd"),
                it.optString("available_supply"),
                it.optString("total_supply"),
                it.optString("percent_change_1h"),
                it.optString("percent_change_24h"),
                it.optString("percent_change_7d"),
                it.optString("last_updated")
        )
    }
}
