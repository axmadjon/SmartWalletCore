package uz.bulls.wallet.m_coin.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_coin.arg.ArgCoin
import uz.bulls.wallet.m_coin.arg.ArgCoinInfo
import uz.bulls.wallet.m_coin.generateNewCriptoCoinAddress
import uz.bulls.wallet.m_coin.getCoinInfos
import uz.bulls.wallet.m_coin.saveCoinInfo
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentSwipeRecyclerFragment
import uz.greenwhite.lib.mold.RecyclerAdapter
import uz.greenwhite.lib.util.CharSequenceUtil
import uz.greenwhite.lib.view_setup.ViewSetup


fun openCoinFragment(activity: Activity, arg: ArgCoin) {
    val bundle = Mold.parcelableArgument(arg, ArgCoin.UZUM_ADAPTER)
    Mold.openContent(activity, CoinFragment::class.java, bundle)
}

class CoinFragment : MoldContentSwipeRecyclerFragment<CoinCore>() {

    fun getArgCoin() = Mold.parcelableArgument<ArgCoin>(this, ArgCoin.UZUM_ADAPTER)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Mold.setTitle(activity, CoinCore.getCoinName(getArgCoin().coinId))

        Mold.makeFloatAction(activity, R.drawable.ic_add_black_24dp).setOnClickListener { generateNewAddress() }

        onRefresh()
    }

    override fun hasItemDivider(): Boolean = false

    private fun generateNewAddress() {
        saveCoinInfo(generateNewCriptoCoinAddress(getArgCoin().coinId))
        onRefresh()
    }

    override fun reloadContent() {
        onRefresh()
    }

    override fun onRefresh() {
        startRefresh()
        listItems = getCoinInfos(getArgCoin().coinId).sort({ l, r -> CharSequenceUtil.compareToIgnoreCase(l.timeMillis, r.timeMillis) })
        Handler().postDelayed({ stopRefresh() }, 100)
    }

    override fun onItemClick(holder: RecyclerAdapter.ViewHolder, item: CoinCore) {
        openCoinInfoDialog(activity, ArgCoinInfo(getArgCoin(), item.publicAddress))
    }

    override fun adapterGetLayoutResource(): Int = R.layout.coin_row

    override fun adapterPopulate(vs: ViewSetup, item: CoinCore) {
        vs.textView(R.id.tv_coin_name).text = item.name
        vs.textView(R.id.tv_coin_public_address).text = item.publicAddress

        Picasso.get()
                .load(CoinCore.getQrCodeUrl(item.publicAddress))
                .resize(50, 50)
                .centerCrop()
                .into(vs.imageView(R.id.iv_qr_code))
    }
}