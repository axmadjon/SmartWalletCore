package uz.bulls.wallet.bean;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import uz.bulls.wallet.R;
import uz.greenwhite.lib.collection.MyMapper;
import uz.greenwhite.lib.error.AppError;
import uz.greenwhite.lib.uzum.UzumAdapter;

public class CoinCore {

    public static final String ETHEREUM = "ethereum";
    public static final String ETHEREUM_CLASSIC = "ethereum-classic";

    public final String id;
    public final String name;
    public final String privateKey;
    public final String publicAddress;
    public final String timeMillis;

    public CoinCore(String id, String name, String privateKey, String publicAddress, String timeMillis) {
        this.id = id;
        this.name = name;
        this.privateKey = privateKey;
        this.publicAddress = publicAddress;
        this.timeMillis = timeMillis;
    }

    public static final MyMapper<CoinCore, String> KEY_ADAPTER = new MyMapper<CoinCore, String>() {
        @Override
        public String apply(CoinCore coinCore) {
            return coinCore.publicAddress;
        }
    };

    public static <E> UzumAdapter<E> getCoinAdapter(String coinId) {
        switch (coinId) {
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return (UzumAdapter<E>) ETHCoin.UZUM_ADAPTER;
            default:
                throw new AppError("coinId not found: " + coinId);
        }
    }

    @NonNull
    public static String getQrCodeUrl(String publicAddress) {
        return getQrCodeUrl(publicAddress, "100");
    }

    @NonNull
    public static String getQrCodeUrl(String publicAddress, String size) {
        return "https://chart.googleapis.com/chart?chs=" + size + "x" + size + "&cht=qr&chl=" + publicAddress + "&choe=UTF-8";
    }

    @DrawableRes
    @NonNull
    public static int getCoinIconResId(String coinId) {
        switch (coinId) {
            case CoinCore.ETHEREUM_CLASSIC:
                return R.drawable.ic_ethereum_classic;
            case CoinCore.ETHEREUM:
                return R.drawable.ic_ethereum;
            default:
                throw AppError.Required();
        }
    }

    @NonNull
    public static CharSequence getCoinName(String coinId) {
        switch (coinId) {
            case CoinCore.ETHEREUM_CLASSIC:
                return "Ethereum Classic";
            case CoinCore.ETHEREUM:
                return "Ethereum";
            default:
                throw AppError.Required();
        }
    }
}
