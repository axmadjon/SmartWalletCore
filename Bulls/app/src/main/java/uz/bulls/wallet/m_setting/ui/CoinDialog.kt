package uz.bulls.wallet.m_setting.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import uz.bulls.wallet.R
import uz.bulls.wallet.m_main.ui.bean.CriptoCoin
import uz.bulls.wallet.m_setting.getShowingCoins
import uz.bulls.wallet.m_setting.saveShowingCoins
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.RecyclerAdapter
import uz.greenwhite.lib.variable.ValueBoolean
import uz.greenwhite.lib.view_setup.ViewSetup


fun showCoinDialog(activity: FragmentActivity) {
    CoinDialog().show(activity.supportFragmentManager, "coin-dialog")
}

class CoinDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val vs = ViewSetup(activity, R.layout.setting_coin)


        val recycler = vs.id<RecyclerView>(R.id.rv_coin_list)
        recycler.layoutManager = GridLayoutManager(activity, 1)

        val showingCoinsIds = getShowingCoins()

        val adapter = SettingCoinAdapter(activity)
        adapter.items = MyArray.from(CriptoCoin.ALL_SUPPORT_COINS.map({ Pair(ValueBoolean(showingCoinsIds.contains(it.id)), it) }))

        val d = AlertDialog.Builder(activity)
                .setTitle("Setting coins")
                .setView(vs.view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.close, { a, b -> saveCoinShowing(adapter.items) })
                .create()

        return d
    }

    private fun saveCoinShowing(items: MyArray<Pair<ValueBoolean, CriptoCoin>>) {
        saveShowingCoins(items.filter { it.first.value }.map { it.second.id })
    }

}

class SettingCoinAdapter(context: Context) : RecyclerAdapter<Pair<ValueBoolean, CriptoCoin>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(ViewSetup(inflater, parent, R.layout.setting_coin_row))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.vsItem.bind(R.id.cb_show_coin, item.first)
        holder.vsItem.textView(R.id.tv_coin_name).text = item.second.name
    }

}
