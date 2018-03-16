package uz.bulls.wallet.bean;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import uz.bulls.wallet.R;
import uz.greenwhite.lib.collection.MyMapper;
import uz.greenwhite.lib.error.AppError;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class CoinCore {

    public static final String ETHEREUM = "ethereum";
    public static final String ETHEREUM_CLASSIC = "ethereum-classic";

    public final String id;
    public final String name;
    public final String privateKey;
    public final String publicAddress;
    public final String timeMillis;
    public final String note;

    public CoinCore(String id,
                    String name,
                    String privateKey,
                    String publicAddress,
                    String timeMillis,
                    String note) {
        this.id = id;
        this.name = name;
        this.privateKey = privateKey;
        this.publicAddress = publicAddress;
        this.timeMillis = timeMillis;
        this.note = note;
    }

    public CoinCore change(String name, String note) {
        return new CoinCore(this.id, name, this.privateKey, this.publicAddress, this.timeMillis, note);
    }

    public static final MyMapper<CoinCore, String> KEY_ADAPTER = new MyMapper<CoinCore, String>() {
        @Override
        public String apply(CoinCore coinCore) {
            return coinCore.publicAddress;
        }
    };

    public static final UzumAdapter<CoinCore> UZUM_ADAPTER = new UzumAdapter<CoinCore>() {
        @Override
        public CoinCore read(UzumReader in) {
            return new CoinCore(
                    in.readString(), in.readString(),
                    in.readString(), in.readString(),
                    in.readString(), in.readString());
        }

        @Override
        public void write(UzumWriter out, CoinCore val) {
            out.write(val.id);
            out.write(val.name);
            out.write(val.privateKey);
            out.write(val.publicAddress);
            out.write(val.timeMillis);
            out.write(val.note);
        }
    };

    public static <E> UzumAdapter<E> getCoinAdapter(String coinId) {
        switch (coinId) {
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return (UzumAdapter<E>) CoinCore.UZUM_ADAPTER;
            default:
                throw new AppError("coinId not found: " + coinId);
        }
    }

    //##############################################################################################

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

    @DrawableRes
    public static int getCoinIconSmallResId(String coinId) {
        switch (coinId) {
            case CoinCore.ETHEREUM_CLASSIC:
                return R.drawable.ic_ethereum_classic_small;
            case CoinCore.ETHEREUM:
                return R.drawable.ic_ethereum_small;
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

    //##############################################################################################

    private static final String K_ID = "id";
    private static final String K_NAME = "name";
    private static final String K_PRIVATE_KEY = "private_key";
    private static final String K_PUBLIC_ADDRESS = "public_address";
    private static final String K_TIME_MILLIS = "time_millis";
    private static final String K_NOTE = "note";

    public static CoinCore toValue(String json) throws Exception {
        JSONObject obj = new JSONObject(json);
        return new CoinCore(obj.getString(K_ID),
                obj.getString(K_NAME),
                obj.getString(K_PRIVATE_KEY),
                obj.getString(K_PUBLIC_ADDRESS),
                obj.getString(K_TIME_MILLIS),
                obj.getString(K_NOTE));
    }

    public static String toJson(CoinCore coin) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put(K_ID, coin.id);
        obj.put(K_NAME, coin.name);
        obj.put(K_PRIVATE_KEY, coin.privateKey);
        obj.put(K_PUBLIC_ADDRESS, coin.publicAddress);
        obj.put(K_TIME_MILLIS, coin.timeMillis);
        obj.put(K_NOTE, coin.note);
        return obj.toString();
    }
}
