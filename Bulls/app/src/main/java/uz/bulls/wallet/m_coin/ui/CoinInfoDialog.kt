package uz.bulls.wallet.m_coin.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.squareup.picasso.Picasso
import uz.bulls.wallet.R
import uz.bulls.wallet.bean.CoinCore
import uz.bulls.wallet.bean.ETHCoin
import uz.bulls.wallet.m_coin.arg.ArgCoinInfo
import uz.bulls.wallet.m_coin.getCoinInfos
import uz.bulls.wallet.m_coin.saveCoinInfo
import uz.greenwhite.lib.error.AppError
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.mold.MoldDialogFragment
import uz.greenwhite.lib.view_setup.ViewSetup

fun openCoinEditDialog(activity: FragmentActivity, arg: ArgCoinInfo) {
    val dialog = Mold.parcelableArgumentNewInstance(CoinEditDialog::class.java, arg, ArgCoinInfo.UZUM_ADAPTER)
    dialog.show(activity.supportFragmentManager, "coin-edit-dialog")
}

fun openCoinInfoDialog(activity: FragmentActivity, arg: ArgCoinInfo) {
    val dialog = Mold.parcelableArgumentNewInstance(CoinInfoDialog::class.java, arg, ArgCoinInfo.UZUM_ADAPTER)
    dialog.show(activity.supportFragmentManager, "coin-info-dialog")
}

class CoinInfoDialog : MoldDialogFragment() {

    private fun getArgCoinInfo() = Mold.parcelableArgument<ArgCoinInfo>(this, ArgCoinInfo.UZUM_ADAPTER)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vsRoot = ViewSetup(activity, R.layout.coin_info_dialog)

        val arg = getArgCoinInfo()
        val coinCores = getCoinInfos(arg.coinId)
        val coinCoreInfo = coinCores.find(arg.publicAddress, CoinCore.KEY_ADAPTER)

        if (coinCoreInfo == null) {
            dismissAllowingStateLoss()
        }

        vsRoot.textView(R.id.tv_coin_name).text = coinCoreInfo.name
        vsRoot.textView(R.id.tv_coin_public_address).text = coinCoreInfo.publicAddress

        Picasso.get()
                .load(CoinCore.getQrCodeUrl(coinCoreInfo.publicAddress, "200"))
                .resize(200, 200)
                .centerCrop()
                .into(vsRoot.imageView(R.id.iv_qr_code))

        return AlertDialog.Builder(activity)
                .setTitle("Coin Info")
                .setView(vsRoot.view)
                .setNegativeButton(R.string.close, null)
                .setPositiveButton(R.string.edit, { _, _ -> openCoinEditDialog(activity, getArgCoinInfo()); dismissAllowingStateLoss() })
                .create()
    }

}

class CoinEditDialog : MoldDialogFragment() {

    private fun getArgCoinInfo() = Mold.parcelableArgument<ArgCoinInfo>(this, ArgCoinInfo.UZUM_ADAPTER)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val vsRoot = ViewSetup(activity, R.layout.coin_edit_dialog)

        val arg = getArgCoinInfo()
        val coinCores = getCoinInfos(arg.coinId)
        val coinCoreInfo = coinCores.find(arg.publicAddress, CoinCore.KEY_ADAPTER)

        if (coinCoreInfo == null) {
            dismissAllowingStateLoss()
        }

        vsRoot.editText(R.id.et_name).setText(coinCoreInfo.name)
        if (coinCoreInfo is ETHCoin) {
            vsRoot.editText(R.id.et_note).visibility = View.VISIBLE
            vsRoot.editText(R.id.et_note).setText(coinCoreInfo.note)
        }

        return AlertDialog.Builder(activity)
                .setTitle("Coin Info")
                .setView(vsRoot.view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, { _, _ -> saveCoinInfoToPref(vsRoot, coinCoreInfo) })
                .create()
    }

    private fun saveCoinInfoToPref(vsRoot: ViewSetup, coinCoreInfo: CoinCore) {
        val coinName = vsRoot.editText(R.id.et_name).text.toString()
        val coinNote = vsRoot.editText(R.id.et_note).text.toString()

        val finalCoinInfo: CoinCore?
        if (coinCoreInfo is ETHCoin) {
            finalCoinInfo = ETHCoin(coinCoreInfo.id, coinName,
                    coinCoreInfo.privateKey, coinCoreInfo.publicKey,
                    coinCoreInfo.publicAddress, coinCoreInfo.timeMillis,
                    coinNote)
        } else {
            throw AppError.Unsupported()
        }

        saveCoinInfo(finalCoinInfo)

        Mold.getContentFragment<MoldContentFragment>(activity).reloadContent()

    }

}