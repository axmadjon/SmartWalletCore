package uz.bulls.wallet.m_main.ui

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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
    private var dialog: BottomSheetDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vsRoot = ViewSetup(activity, R.layout.coin_address_dialog)
        val toolbar = vsRoot.id<Toolbar>(R.id.tb_toolbar)

        val arg = getArgCoin()

        toolbar.setLogo(CoinCore.getCoinIconSmallResId(arg.coinId))
        toolbar.title = CoinCore.getCoinName(arg.coinId)

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { dismissAllowingStateLoss() }

        dialog = BottomSheetDialog(activity, theme)
        dialog!!.setContentView(vsRoot.view)

        dialog!!.setOnShowListener { prepareToolbarMenu(vsRoot) }

        val vg = dialog!!.findViewById(android.support.design.R.id.design_bottom_sheet)
        val bottomSheet = BottomSheetBehavior.from(vg)
        bottomSheet.setBottomSheetCallback(BottomSheetListener({ prepareToolbarMenu(vsRoot) }))

        reloadContent(vsRoot)

        return dialog!!
    }

    override fun onStop() {
        super.onStop()
        jobMate.stopListening()
    }

    private fun prepareToolbarMenu(vsRoot: ViewSetup) {
        val toolbar = vsRoot.id<Toolbar>(R.id.tb_toolbar)

        toolbar.menu.clear()
        val bottomSheet = BottomSheetBehavior.from(dialog!!.findViewById(android.support.design.R.id.design_bottom_sheet))
        val state = bottomSheet.state

        val button = vsRoot.id<FloatingActionButton>(R.id.fab_new_address)

        button.setOnClickListener { generateNewAddress() }

        val vg = vsRoot.viewGroup<LinearLayout>(R.id.ll_content)

        if (state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAllowingStateLoss()

        } else if (state != BottomSheetBehavior.STATE_EXPANDED || vg.childCount <= 3) {

            val item = toolbar.menu.add(Menu.NONE, 1, Menu.NONE, "new address")
            item.setIcon(R.drawable.ic_add_white_24dp)

            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    1 -> generateNewAddress()
                }
                true
            }
            button.hide()
        } else {
            button.show()
        }
    }

    private fun generateNewAddress() {
        openCoinInfoDialog(activity, ArgCoinInfo(getArgCoin(), ""))
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

class BottomSheetListener(val changeState: () -> Unit) : BottomSheetBehavior.BottomSheetCallback() {
    override fun onSlide(bottomSheet: View, slideOffset: Float) {

    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        changeState()
    }

}