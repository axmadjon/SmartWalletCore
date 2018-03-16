package uz.bulls.wallet.m_main.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.m_main.*
import uz.bulls.wallet.m_main.arg.ArgCoinAddress
import uz.greenwhite.lib.error.AppError
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup

fun openCoinAddressFragment(activity: Activity, arg: ArgCoinAddress) {
    val bundle = Mold.parcelableArgument(arg, ArgCoinAddress.UZUM_ADAPTER)
    Mold.openContent(activity, CoinAddressFragment::class.java, bundle)
}

fun replaceCoinAddressFragment(activity: Activity, arg: ArgCoinAddress) {
    val content = Mold.parcelableArgumentNewInstance(CoinAddressFragment::class.java,
            arg, ArgCoinAddress.UZUM_ADAPTER)
    Mold.replaceContent(activity, content)
}

fun openCoinEditAddressFragment(activity: Activity, arg: ArgCoinAddress) {
    val bundle = Mold.parcelableArgument(arg, ArgCoinAddress.UZUM_ADAPTER)
    Mold.openContent(activity, CoinEditAddressFragment::class.java, bundle)
}

class CoinAddressFragment : MoldContentFragment() {

    private fun getArgCoinAddress() = Mold.parcelableArgument(this, ArgCoinAddress.UZUM_ADAPTER)

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vsRoot = ViewSetup(inflater, container, R.layout.coin_address)
        return vsRoot?.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val arg = getArgCoinAddress()

        Mold.setTitle(activity, if (arg.publicAddress.isEmpty()) "Create Address" else "Coin information")

        val coin = arg.findAddress()

        if (coin == null) {
            vsRoot!!.id<View>(R.id.ll_address_info).visibility = View.GONE
            vsRoot!!.id<View>(R.id.ll_oops).visibility = View.VISIBLE

            val coins = getCoinCore(arg.coinId)
            if (!arg.publicAddress.isEmpty() && coins.nonEmpty()) {
                val dialog = UI.bottomSheet()
                        .title("Select address")
                coins.forEach {
                    dialog.option(CoinCore.getCoinIconSmallResId(it.id), "${it.name}\n${it.publicAddress}", {
                        replaceCoinAddressFragment(activity, ArgCoinAddress(arg, it.publicAddress))
                    })
                }
                dialog.show(activity)
                return
            }

            vsRoot!!.id<View>(R.id.btn_create_new_address).setOnClickListener {
                val newCoin = generateNewCriptoCoinAddress(arg.coinId)
                saveCoinCore(newCoin, true)

                replaceCoinAddressFragment(activity, ArgCoinAddress(arg, newCoin.publicAddress))
            }
        } else {

            addSubMenu("Edit address", { openCoinEditAddressFragment(activity, getArgCoinAddress()) })

            addSubMenu("Remove", { removeCoinCoreAddress(coin); activity.recreate() })

            vsRoot!!.id<View>(R.id.ll_address_info).visibility = View.VISIBLE
            vsRoot!!.id<View>(R.id.ll_oops).visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        val arg = getArgCoinAddress()
        val coin = arg.findAddress() ?: return

        vsRoot!!.textView(R.id.tv_name).text = coin.name
        vsRoot!!.textView(R.id.tv_public_address).text = coin.publicAddress

        Picasso.get()
                .load(CoinCore.getQrCodeUrl(coin.publicAddress, "200"))
                .resize(200, 200)
                .centerCrop()
                .into(vsRoot!!.imageView(R.id.iv_qr_code))
    }
}

class CoinEditAddressFragment : MoldContentFragment() {
    private fun getArgCoinAddress() = Mold.parcelableArgument(this, ArgCoinAddress.UZUM_ADAPTER)

    private var vsRoot: ViewSetup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vsRoot = ViewSetup(inflater, container, R.layout.coin_edit_address)
        return vsRoot?.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val arg = getArgCoinAddress()

        Mold.setTitle(activity, "Edit ${CoinCore.getCoinName(arg.coinId)} address")

        val foundCoin = arg.findAddress() ?: throw AppError("Coin is null")

        vsRoot!!.editText(R.id.et_name).setText(foundCoin.name)
        vsRoot!!.editText(R.id.et_note).setText(foundCoin.note)

        val mainAddress = getMainCoinAddress(arg.coinId)
        vsRoot!!.compoundButton<CompoundButton>(R.id.cb_main).isChecked = mainAddress == arg.publicAddress

        vsRoot!!.id<View>(R.id.btn_save).setOnClickListener { saveAddress(foundCoin) }
    }

    private fun saveAddress(foundCoin: CoinCore) {
        val name = vsRoot!!.editText(R.id.et_name).text.toString().trim()
        val note = vsRoot!!.editText(R.id.et_note).text.toString().trim()
        val mainAddress = vsRoot!!.compoundButton<CompoundButton>(R.id.cb_main).isChecked

        if (name.isEmpty()) {
            UI.alertError(activity, "Coin name is required")
            return
        }

        saveCoinCore(foundCoin.change(name, note), mainAddress)
        activity.onBackPressed()
    }
}