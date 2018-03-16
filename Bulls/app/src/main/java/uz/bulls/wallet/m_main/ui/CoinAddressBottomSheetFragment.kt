package uz.bulls.wallet.m_main.ui

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_main.*
import uz.bulls.wallet.m_main.arg.ArgCoin
import uz.bulls.wallet.m_main.arg.ArgCoinAddress
import uz.bulls.wallet.m_main.job.CoinBalanceJob
import uz.greenwhite.lib.job.JobMate
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldDialogFragment
import uz.greenwhite.lib.util.CharSequenceUtil
import uz.greenwhite.lib.util.NumberUtil
import uz.greenwhite.lib.util.Util
import uz.greenwhite.lib.view_setup.UI
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
    private var vsRoot: ViewSetup? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.vsRoot = ViewSetup(activity, R.layout.coin_address_dialog)
        val toolbar = vsRoot!!.id<Toolbar>(R.id.tb_toolbar)

        val arg = getArgCoin()

        toolbar.setLogo(CoinCore.getCoinIconSmallResId(arg.coinId))
        toolbar.title = CoinCore.getCoinName(arg.coinId)

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { dismissAllowingStateLoss() }

        dialog = BottomSheetDialog(activity, theme)
        dialog!!.setContentView(vsRoot!!.view)

        dialog!!.setOnShowListener { prepareToolbarMenu() }

        val vg = dialog!!.findViewById(android.support.design.R.id.design_bottom_sheet)
        val bottomSheet = BottomSheetBehavior.from(vg)
        bottomSheet.setBottomSheetCallback(BottomSheetListener({ prepareToolbarMenu() }))

        return dialog!!
    }

    override fun onStart() {
        super.onStart()
        reloadContent()
    }

    override fun onStop() {
        super.onStop()
        jobMate.stopListening()
    }

    private fun prepareToolbarMenu() {
        val toolbar = vsRoot!!.id<Toolbar>(R.id.tb_toolbar)

        toolbar.menu.clear()
        val bottomSheet = BottomSheetBehavior.from(dialog!!.findViewById(android.support.design.R.id.design_bottom_sheet))
        val state = bottomSheet.state

        val button = vsRoot!!.id<FloatingActionButton>(R.id.fab_new_address)

        button.setOnClickListener { generateNewAddress() }

        val vg = vsRoot!!.viewGroup<LinearLayout>(R.id.ll_content)

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

    private fun generateNewAddress(publicAddress: String = "") {
        var coinAddress = Util.nvl(publicAddress)
        if (coinAddress.isEmpty()) {
            val arg = getArgCoin()
            val newCoin = generateNewCriptoCoinAddress(arg.coinId)
            saveCoinCore(newCoin)
            coinAddress = newCoin.publicAddress

        }
        openCoinAddressFragment(activity, ArgCoinAddress(getArgCoin(), coinAddress))
    }

    private fun removeCoinAddress(coinCore: CoinCore) {
        var message = "Вы точно хотите удалить «${coinCore.name}» адрес?\n" +
                "После удалений вы не сможите востановить его!" +
                "<i>(Если у вас есть копия то вы можите не волнуяс удалить даную адрес)</i>"

        val mainAddress = getMainCoinAddress(coinCore.id) == coinCore.publicAddress
        if (mainAddress) {
            message += "<br/><br/><b>Этот адрес выбра как главный!</b>"
        }
        UI.confirm(activity, "Warning!!!", Html.fromHtml(message), {
            removeCoinCoreAddress(coinCore)
            if (mainAddress) {
                saveMainCoinAddress(coinCore.id, "")
            }
            reloadContent()
        })
    }

    private fun reloadContent() {
        val vg = vsRoot!!.viewGroup<LinearLayout>(R.id.ll_content)
        vg.removeAllViews()

        val allCoins = getCoinCore(getArgCoin().coinId)

        vsRoot!!.id<View>(R.id.ll_address_content).visibility = if (allCoins.isEmpty) View.GONE else View.VISIBLE
        vsRoot!!.id<View>(R.id.ll_oops).visibility = if (allCoins.isEmpty) View.VISIBLE else View.GONE

        if (allCoins.isEmpty) {
            vsRoot!!.id<View>(R.id.ll_oops).setOnClickListener { generateNewAddress() }

        } else {
            allCoins.sort({ l, r -> CharSequenceUtil.compareToIgnoreCase(l.timeMillis, r.timeMillis) })
                    .forEach({ item ->
                        val vs = ViewSetup(activity, R.layout.coin_address_dialog_row)
                        adapterPopulate(vs, item)
                        vs.view.setOnClickListener { generateNewAddress(item.publicAddress) }
                        vs.view.setOnLongClickListener {
                            UI.popup()
                                    .option(R.string.open, {
                                        generateNewAddress(item.publicAddress)
                                    })
                                    .option(R.string.edit, {
                                        openCoinEditAddressFragment(activity, ArgCoinAddress(getArgCoin(), item.publicAddress))
                                    })
                                    .option(R.string.remove, {
                                        removeCoinAddress(item)
                                    })
                                    .show(vs.view)
                            true
                        }
                        vg.addView(vs.view)
                    })
        }
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