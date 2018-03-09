package uz.bulls.wallet.m_main.ui

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_main.getMyCoins
import uz.bulls.wallet.m_main.ui.bean.CriptoCoin
import uz.bulls.wallet.m_setting.ui.openSettingFragment
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

        addMenu(R.drawable.ic_settings_black_24dp, "Setting", { openSettingFragment(activity) })

        val button = Mold.makeFloatAction(activity, R.drawable.ic_add_black_24dp)
        button.setOnClickListener { addTransactionOrCreateCoin() }

        val viewPage = vsRoot?.id<ViewPager>(R.id.vp_coin_list)
        viewPage?.setOffscreenPageLimit(3)
        viewPage?.setClipChildren(false)

        val myCoins = getMyCoins().append(CriptoCoin.EMPTY)
        viewPage?.adapter = MainAdapter(activity, myCoins, jobMate)
    }

    private fun addTransactionOrCreateCoin() {
        val viewPage = vsRoot?.id<ViewPager>(R.id.vp_coin_list)
        val currentItem = viewPage?.currentItem


    }
}
