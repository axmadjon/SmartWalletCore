package uz.bulls.wallet.m_main.bean;

import uz.bulls.wallet.bean.CoinCore;
import uz.greenwhite.lib.collection.MyArray;
import uz.greenwhite.lib.collection.MyMapper;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class CriptoCoin {

    public final String id;

    private CoinMarket coinMarket;

    public CriptoCoin(String id) {
        this.id = id;
    }

    public static final MyMapper<CriptoCoin, String> KEY_ADAPTER = new MyMapper<CriptoCoin, String>() {
        @Override
        public String apply(CriptoCoin criptoCoin) {
            return criptoCoin.id;
        }
    };

    public static final MyArray<CriptoCoin> ALL_SUPPORT_COINS = MyArray.from(
            new CriptoCoin(CoinCore.ETHEREUM_CLASSIC),
            new CriptoCoin(CoinCore.ETHEREUM)
    );

    public static final CriptoCoin EMPTY = new CriptoCoin("");

    public void setCoinMarket(CoinMarket coinMarket) {
        this.coinMarket = coinMarket;
    }

    public CoinMarket getCoinMarket() {
        return coinMarket;
    }


    public static final UzumAdapter<CriptoCoin> UZUM_ADAPTER = new UzumAdapter<CriptoCoin>() {
        @Override
        public CriptoCoin read(UzumReader in) {
            return new CriptoCoin(in.readString());
        }

        @Override
        public void write(UzumWriter out, CriptoCoin val) {
            out.write(val.id);
        }
    };
}
