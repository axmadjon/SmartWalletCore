package uz.bulls.wallet.m_main.ui.bean;

import android.support.annotation.DrawableRes;

import uz.bulls.wallet.R;
import uz.greenwhite.lib.collection.MyArray;
import uz.greenwhite.lib.error.AppError;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class CriptoCoin {

    public static final String C_ETH = "ethereum";
    public static final String C_ETC = "ethereum-classic";

    public final String id;

    private CoinMarket coinMarket;

    public CriptoCoin(String id) {
        this.id = id;
    }

    public static final MyArray<CriptoCoin> ALL_SUPPORT_COINS = MyArray.from(
            new CriptoCoin(C_ETC),
            new CriptoCoin(C_ETH)
    );

    public static final CriptoCoin EMPTY = new CriptoCoin("");

    public void setCoinMarket(CoinMarket coinMarket) {
        this.coinMarket = coinMarket;
    }

    public CoinMarket getCoinMarket() {
        return coinMarket;
    }

    @DrawableRes
    public int getIconResId() {
        switch (id) {
            case C_ETC:
                return R.drawable.ic_ethereum_classic;
            case C_ETH:
                return R.drawable.ic_ethereum;
            default:
                throw AppError.Required();
        }
    }

    public CharSequence getName() {
        switch (id) {
            case C_ETC:
                return "Ethereum Classic";
            case C_ETH:
                return "Ethereum";
            default:
                throw AppError.Required();
        }
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
