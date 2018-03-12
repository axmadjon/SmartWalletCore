package uz.bulls.wallet.m_coin.arg;


import android.support.annotation.Nullable;

import java.util.Comparator;

import uz.bulls.wallet.bean.CoinCore;
import uz.bulls.wallet.m_coin.CoinApiKt;
import uz.bulls.wallet.m_main.MainApiKt;
import uz.greenwhite.lib.collection.MyArray;
import uz.greenwhite.lib.util.CharSequenceUtil;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class ArgCoin {

    public final String coinId;

    public ArgCoin(String coinId) {
        this.coinId = coinId;
    }

    protected ArgCoin(UzumReader in) {
        this(in.readString());
    }

    @Nullable
    public CoinCore getMainCoinCore() {
        MyArray<CoinCore> coinAddresses = CoinApiKt.getCoinCore(coinId).sort(new Comparator<CoinCore>() {
            @Override
            public int compare(CoinCore l, CoinCore r) {
                return CharSequenceUtil.compareToIgnoreCase(l.timeMillis, r.timeMillis);
            }
        });
        if (coinAddresses.isEmpty()) {
            return null;
        }
        CoinCore findCoinCore = coinAddresses.find(MainApiKt.getMainCoinAddress(coinId), CoinCore.KEY_ADAPTER);
        if (findCoinCore == null) {
            findCoinCore = coinAddresses.get(0);
            MainApiKt.saveMainCoinAddress(coinId, findCoinCore.publicAddress);
        }
        return findCoinCore;
    }

    protected void write(UzumWriter out) {
        out.write(this.coinId);
    }

    public static final UzumAdapter<ArgCoin> UZUM_ADAPTER = new UzumAdapter<ArgCoin>() {
        @Override
        public ArgCoin read(UzumReader in) {
            return new ArgCoin(in);
        }

        @Override
        public void write(UzumWriter out, ArgCoin val) {
            val.write(out);
        }
    };
}
