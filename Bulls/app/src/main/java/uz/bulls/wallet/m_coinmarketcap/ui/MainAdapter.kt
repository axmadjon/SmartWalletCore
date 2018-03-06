package uz.bulls.wallet.m_coinmarketcap.ui

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_coinmarketcap.ui.bean.CriptoCoin
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.view_setup.ViewSetup

class MainAdapter(val ctx: Context, val items: MyArray<CriptoCoin>) : PagerAdapter() {

    override fun isViewFromObject(view: View?, obj: Any?): Boolean = view == obj

    override fun destroyItem(container: ViewGroup?, position: Int, obj: Any?) {
        container?.removeView(obj as View)
    }

    override fun getCount(): Int = items.size()

    fun getItem(position: Int) = items.get(position)

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val vs = ViewSetup(ctx, R.layout.main_coin_row)
        val item = getItem(position)

        vs.imageView(R.id.coin_logo).setImageResource(item.getCoinIconResId())
        container?.addView(vs.view)
        return vs.view
    }
}