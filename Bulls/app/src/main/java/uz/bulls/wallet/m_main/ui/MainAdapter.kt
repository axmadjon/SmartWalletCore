package uz.bulls.wallet.m_main.ui

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_main.ui.bean.CriptoCoin
import uz.bulls.wallet.m_main.ui.job.CoinMarketJob
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.job.JobMate
import uz.greenwhite.lib.util.NumberUtil
import uz.greenwhite.lib.view_setup.ViewSetup
import java.math.BigDecimal

class MainAdapter(val ctx: Context, val items: MyArray<CriptoCoin>, val jobMate: JobMate) : PagerAdapter() {

    override fun isViewFromObject(view: View?, obj: Any?): Boolean = view == obj

    override fun destroyItem(container: ViewGroup?, position: Int, obj: Any?) {
        container?.removeView(obj as View)
    }

    override fun getCount(): Int = items.size()

    fun getItem(position: Int) = items.get(position)

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val vs = ViewSetup(ctx, R.layout.main_coin_row)
        val item = getItem(position)

        if (!item.id.isEmpty()) {
            vs.id<View>(R.id.fl_cripto_coin).visibility = View.VISIBLE
            vs.id<View>(R.id.fl_add_cripto_coin).visibility = View.GONE

            jobMate.execute(CoinMarketJob(item.id))
                    .done({
                        if (item.id == it.id) {
                            item.coinMarket = it; reloadView(vs, item)
                        }
                    })

            reloadView(vs, item)

            vs.imageView(R.id.coin_logo).setImageResource(item.iconResId)

        } else {
            vs.id<View>(R.id.fl_cripto_coin).visibility = View.GONE
            vs.id<View>(R.id.fl_add_cripto_coin).visibility = View.VISIBLE


        }

        container?.addView(vs.view)
        return vs.view
    }

    private fun reloadView(vs: ViewSetup, item: CriptoCoin) {
        if (item.coinMarket != null) {
            vs.textView(R.id.tv_coin_name).text = item.coinMarket?.name
            vs.textView(R.id.tv_symbol).text = item.coinMarket?.symbol
            vs.textView(R.id.tv_coin_price).text = "$${NumberUtil.formatMoney(BigDecimal(item.coinMarket?.priceUsd))}"
            vs.textView(R.id.tv_coin_percent).text = "(${item.coinMarket?.percentChange24h}%)"

        }
    }
}