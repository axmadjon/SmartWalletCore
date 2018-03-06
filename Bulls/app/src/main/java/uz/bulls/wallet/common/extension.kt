package uz.bulls.wallet.common

import org.json.JSONArray
import org.json.JSONObject
import uz.greenwhite.lib.collection.MyArray


fun <E> JSONArray.map(apply: (JSONObject) -> E): MyArray<E> {
    val result = ArrayList<E>()
    for (index in 0..(this.length() - 1)) {
        result.add(apply(getJSONObject(index)))
    }
    return MyArray.from(result)
}

