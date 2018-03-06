package uz.bulls.wallet.m_coinmarketcap.ui

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_coinmarketcap.ui.bean.DEFAULT_COINS
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.ViewSetup

fun openCoinMarketCapFragment(activity: Activity) {
    Mold.openContent(activity, CoinMarketCapFragment::class.java)
}

class CoinMarketCapFragment : MoldContentFragment() {

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vsRoot = ViewSetup(inflater, container, R.layout.main_content);
        return vsRoot!!.view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewPage = vsRoot?.id<ViewPager>(R.id.vp_coin_list)
        viewPage?.adapter = MainAdapter(activity, DEFAULT_COINS)
    }

}
