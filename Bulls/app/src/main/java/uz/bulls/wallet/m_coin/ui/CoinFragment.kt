package uz.bulls.wallet.m_coin.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_coin.generateNewCriptoCoinAddress
import uz.bulls.wallet.m_coin.getCoinInfos
import uz.bulls.wallet.m_coin.saveCoinInfo
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentSwipeRecyclerFragment
import uz.greenwhite.lib.uzum.UzumAdapter
import uz.greenwhite.lib.view_setup.ViewSetup

fun openCoinFragment(activity: Activity, coinId: String) {
    val bundle = Mold.parcelableArgument(coinId, UzumAdapter.STRING)
    Mold.openContent(activity, CoinFragment::class.java, bundle)
}

class CoinFragment : MoldContentSwipeRecyclerFragment<CoinCore>() {

    fun getCoinId() = Mold.parcelableArgument<String>(this, UzumAdapter.STRING)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Mold.makeFloatAction(activity, R.drawable.ic_add_black_24dp).setOnClickListener { generateNewAddress() }

        onRefresh()
    }

    private fun generateNewAddress() {
        saveCoinInfo(generateNewCriptoCoinAddress(getCoinId()))
        onRefresh()
    }

    override fun onRefresh() {
        startRefresh()
        listItems = getCoinInfos(getCoinId())

        Handler().postDelayed({ stopRefresh() }, 100)
    }

    override fun adapterGetLayoutResource(): Int = R.layout.coin_row

    override fun adapterPopulate(vs: ViewSetup, item: CoinCore) {
        vs.textView(R.id.tv_coin_name).text = item.name
        vs.textView(R.id.tv_coin_public_address).text = item.publicAddress
    }
}