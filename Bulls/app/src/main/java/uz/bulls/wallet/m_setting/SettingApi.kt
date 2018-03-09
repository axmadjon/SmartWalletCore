package uz.bulls.wallet.m_setting

import uz.bulls.wallet.BullsApp
import uz.bulls.wallet.datasource.Pref
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.util.Util.nvl


val C_SHOWING_COINS = "setting:showing_coins"

fun getPref() = Pref(BullsApp.getInstance(), "bulls:setting")

fun getShowingCoins() = nvl(getPref().load(C_SHOWING_COINS), "")!!.split(",")

fun saveShowingCoins(coinIds: Collection<String>) {
    getPref().save(C_SHOWING_COINS, MyArray.from(coinIds).mkString(","))
}