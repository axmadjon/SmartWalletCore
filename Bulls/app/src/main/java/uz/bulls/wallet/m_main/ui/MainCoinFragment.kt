package uz.bulls.wallet.m_main.ui

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_coin.generateNewCriptoCoinAddress
import uz.bulls.wallet.m_coin.saveCoinCore
import uz.bulls.wallet.m_main.bean.CriptoCoin
import uz.bulls.wallet.m_main.getMyCoins
import uz.bulls.wallet.m_main.saveMyCoins
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup

fun openCoinMarketCapFragment(activity: Activity) {
    Mold.openContent(activity, MainCoinFragment::class.java)
}

private fun replaceInstanceCoinMarketCapFragment(activity: Activity) {
    Mold.replaceContent(activity, MainCoinFragment())
}

class MainCoinFragment : MoldContentFragment() {

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vsRoot = ViewSetup(inflater, container, R.layout.main_content);
        return vsRoot!!.view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Mold.setTitle(activity, "Dashboard")

        val button = Mold.makeFloatAction(activity, R.drawable.ic_add_black_24dp)
        button.setOnClickListener { addTransactionOrCreateCoin() }

        val viewPage = vsRoot?.id<ViewPager>(R.id.vp_coin_list)
        viewPage?.setOffscreenPageLimit(3)
        viewPage?.setClipChildren(false)

        var myCoinSelectCoin = getMyCoins()
        if (myCoinSelectCoin.size() < CriptoCoin.ALL_SUPPORT_COINS.size()) {
            myCoinSelectCoin = myCoinSelectCoin.append(CriptoCoin.EMPTY)
        }
        viewPage?.adapter = MainAdapter(activity, myCoinSelectCoin, jobMate, { addTransactionOrCreateCoin() })
    }

    private fun addTransactionOrCreateCoin() {
        val viewPage = vsRoot?.id<ViewPager>(R.id.vp_coin_list)
        val adapter: MainAdapter = viewPage?.adapter as MainAdapter
        val item = adapter.getItem(viewPage.currentItem) ?: return

        when {
            item.id.isEmpty() -> showAllCoinsDialog()
            else -> addNewTransaction(item)
        }
    }

    private fun showAllCoinsDialog() {
        val dialog = UI.bottomSheet().title("Select coins")
        val myCoins = getMyCoins()
        CriptoCoin.ALL_SUPPORT_COINS.filter { !myCoins.contains(it.id, CriptoCoin.KEY_ADAPTER) }.forEach {
            dialog.option(ContextCompat.getDrawable(activity, CoinCore.getCoinIconResId(it.id)), CoinCore.getCoinName(it.id), {
                saveMyCoins(myCoins.append(it))
                saveCoinCore(generateNewCriptoCoinAddress(it.id), mainCoin = true)
                replaceInstanceCoinMarketCapFragment(activity)
            })
        }
        dialog.show(activity)
    }

    private fun addNewTransaction(criptoCoin: CriptoCoin) {

    }
}
