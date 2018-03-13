package uz.bulls.wallet.m_main.ui

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_coin.arg.ArgCoin
import uz.bulls.wallet.m_coin.arg.ArgCoinInfo
import uz.bulls.wallet.m_coin.getCoinBalance
import uz.bulls.wallet.m_coin.getCoinCore
import uz.bulls.wallet.m_coin.job.CoinBalanceJob
import uz.bulls.wallet.m_coin.ui.openCoinInfoDialog
import uz.greenwhite.lib.job.JobMate
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldDialogFragment
import uz.greenwhite.lib.util.CharSequenceUtil
import uz.greenwhite.lib.util.NumberUtil
import uz.greenwhite.lib.view_setup.ViewSetup
import java.math.BigDecimal

fun showCoinAddressBottomSheetFragment(activity: FragmentActivity, arg: ArgCoin) {
    val dialog = Mold.parcelableArgumentNewInstance(CoinAddressBottomSheetFragment::class.java, arg, ArgCoin.UZUM_ADAPTER)
    dialog.show(activity.supportFragmentManager, "coin-address-bottom-sheet-dialog")
}

class CoinAddressBottomSheetFragment : MoldDialogFragment() {

    private fun getArgCoin() = Mold.parcelableArgument<ArgCoin>(this, ArgCoin.UZUM_ADAPTER)!!

    private val jobMate: JobMate = JobMate()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vsRoot = ViewSetup(activity, R.layout.coin_address_dialog)

        vsRoot.id<View>(R.id.btn_create_new_address).setOnClickListener {
            openCoinInfoDialog(activity, ArgCoinInfo(getArgCoin(), ""))
        }

        val dialog = BottomSheetDialog(activity, theme)
        dialog.setContentView(vsRoot.view)

        reloadContent(vsRoot)

        return dialog
    }

    override fun onStop() {
        super.onStop()
        jobMate.stopListening()
    }

    private fun reloadContent(vsRoot: ViewSetup) {
        val vg = vsRoot.viewGroup<LinearLayout>(R.id.ll_content)
        vg.removeAllViews()

        getCoinCore(getArgCoin().coinId)
                .sort({ l, r -> CharSequenceUtil.compareToIgnoreCase(l.timeMillis, r.timeMillis) })
                .forEach({
                    val vs = ViewSetup(activity, R.layout.coin_address_dialog_row)
                    adapterPopulate(vs, it)

                    vg.addView(vs.view)
                })

    }

    private fun adapterPopulate(vs: ViewSetup, item: CoinCore) {
        vs.textView(R.id.tv_coin_name).text = item.name
        vs.textView(R.id.tv_coin_public_address).text = item.publicAddress

        Picasso.get()
                .load(CoinCore.getQrCodeUrl(item.publicAddress))
                .resize(50, 50)
                .centerCrop()
                .into(vs.imageView(R.id.iv_qr_code))

        if (item.id == CoinCore.ETHEREUM_CLASSIC || item.id == CoinCore.ETHEREUM) {

            val cacheBalance = NumberUtil.formatMoney(getCoinBalance(item.id, item.publicAddress))
            vs.textView(R.id.tv_coin_address_balance).text = "Balance: $cacheBalance"

            jobMate.execute(CoinBalanceJob(item.id, item.publicAddress))
                    .done({
                        val balance = NumberUtil.formatMoney(BigDecimal(it))
                        vs.textView(R.id.tv_coin_address_balance).text = "Balance: $balance"
                    })
        } else {
            vs.textView(R.id.tv_coin_address_balance).text = "Balance not support"
        }
    }
}