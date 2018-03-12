package uz.bulls.wallet.m_main.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_coin.arg.ArgCoin
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.ViewSetup

fun openMainCoinAddressFragment(activity: Activity, arg: ArgCoin) {
    val bundle = Mold.parcelableArgument<ArgCoin>(arg, ArgCoin.UZUM_ADAPTER)
    Mold.openContent(activity, MainCoinAddressFragment::class.java, bundle)
}

class MainCoinAddressFragment : MoldContentFragment() {

    private fun getArgCoin(): ArgCoin = Mold.parcelableArgument<ArgCoin>(this, ArgCoin.UZUM_ADAPTER)

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.vsRoot = ViewSetup(inflater, container, R.layout.main_coin_address)
        return this.vsRoot?.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Mold.setTitle(activity, "Coin Address Info")

        val arg = getArgCoin()
        val mainCoinCore = arg.mainCoinCore

        if (mainCoinCore == null) {
            vsRoot!!.id<View>(R.id.ll_main_coin_address_info)?.visibility = View.GONE
            vsRoot!!.id<View>(R.id.ll_oops)?.visibility = View.VISIBLE

            vsRoot!!.id<View>(R.id.btn_create_new_address).setOnClickListener { }

        } else {
            vsRoot!!.id<View>(R.id.ll_main_coin_address_info)?.visibility = View.VISIBLE
            vsRoot!!.id<View>(R.id.ll_oops)?.visibility = View.GONE

            vsRoot!!.imageView(R.id.iv_coin_logo_icon).setImageResource(CoinCore.getCoinIconResId(mainCoinCore.id))
            vsRoot!!.textView(R.id.tv_coin_address_name).text = mainCoinCore.name
            vsRoot!!.textView(R.id.tv_coin_address).text = mainCoinCore.publicAddress

            Picasso.get()
                    .load(CoinCore.getQrCodeUrl(mainCoinCore.publicAddress, "300"))
                    .into(vsRoot!!.imageView(R.id.iv_qr_code))

            vsRoot!!.id<View>(R.id.btn_create_new).setOnClickListener { }

        }
    }
}