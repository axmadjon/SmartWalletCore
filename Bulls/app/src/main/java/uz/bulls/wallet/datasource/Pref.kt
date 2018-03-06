package uz.bulls.wallet.datasource

import android.content.Context
import android.content.SharedPreferences

import uz.greenwhite.lib.uzum.Uzum
import uz.greenwhite.lib.uzum.UzumAdapter

class Pref(context: Context, prefName: String) {

    private val sp: SharedPreferences

    init {
        this.sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    fun load(key: String): String? {
        return sp.getString(key, null)
    }

    fun <E> load(key: String, adapter: UzumAdapter<E>): E? {
        val value = load(key) ?: return null
        return Uzum.toValue(value, adapter)
    }

    fun save(key: String, value: String?) {
        val edit = sp.edit()
        if (value != null) {
            edit.putString(key, value)
        } else {
            edit.remove(key)
        }
        edit.apply()
    }

    fun <E> save(key: String, value: E, adapter: UzumAdapter<E>) {
        val txt: String = Uzum.toJson(value, adapter); save(key, txt)
    }
}
