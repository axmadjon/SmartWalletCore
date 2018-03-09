package uz.bulls.wallet.m_setting.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_setting.getShowingCoinsNames
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.ViewSetup

fun openSettingFragment(activity: Activity) {
    Mold.openContent(activity, SettingFragment::class.java)
}

class SettingFragment : MoldContentFragment() {

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vsRoot = ViewSetup(inflater, container, R.layout.setting)
        return vsRoot!!.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vsRoot?.id<View>(R.id.ll_coin_showing)?.setOnClickListener({ showCoinDialog(activity) })

        vsRoot?.textView(R.id.tv_showing_Coins)?.text = getShowingCoinsNames()
    }
}