package uz.bulls.wallet.bean;

import org.json.JSONObject;

import uz.greenwhite.lib.collection.MyMapper;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class ETHCoin extends CoinCore {

    public final String publicKey, note;

    public ETHCoin(String id,
                   String name,
                   String privateKey,
                   String publicKey,
                   String publicAddress,
                   String timeMillis,
                   String note) {
        super(id, name, privateKey, publicAddress, timeMillis);
        this.publicKey = publicKey;
        this.note = note;
    }

    public static final MyMapper<ETHCoin, String> KEY_ADAPTER = new MyMapper<ETHCoin, String>() {
        @Override
        public String apply(ETHCoin ETHCoin) {
            return ETHCoin.id;
        }
    };

    public static final UzumAdapter<ETHCoin> UZUM_ADAPTER = new UzumAdapter<ETHCoin>() {
        @Override
        public ETHCoin read(UzumReader in) {
            return new ETHCoin(in.readString(),
                    in.readString(), in.readString(),
                    in.readString(), in.readString(),
                    in.readString(), in.readString());
        }

        @Override
        public void write(UzumWriter out, ETHCoin val) {
            out.write(val.id);
            out.write(val.name);
            out.write(val.privateKey);
            out.write(val.publicKey);
            out.write(val.publicAddress);
            out.write(val.timeMillis);
            out.write(val.note);
        }
    };

    private static final String K_ID = "id";
    private static final String K_NAME = "name";
    private static final String K_PRIVATE_KEY = "private_key";
    private static final String K_PUBLIC_KEY = "public_key";
    private static final String K_PUBLIC_ADDRESS = "public_address";
    private static final String K_TIME_MILLIS = "time_millis";
    private static final String K_NOTE = "note";

    public static ETHCoin toValue(String json) throws Exception {
        JSONObject obj = new JSONObject(json);
        return new ETHCoin(obj.getString(K_ID),
                obj.getString(K_NAME),
                obj.getString(K_PRIVATE_KEY),
                obj.getString(K_PUBLIC_KEY),
                obj.getString(K_PUBLIC_ADDRESS),
                obj.getString(K_TIME_MILLIS),
                obj.getString(K_NOTE));
    }

    public static String toJson(ETHCoin coin) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put(K_ID, coin.id);
        obj.put(K_NAME, coin.name);
        obj.put(K_PRIVATE_KEY, coin.privateKey);
        obj.put(K_PUBLIC_KEY, coin.publicKey);
        obj.put(K_PUBLIC_ADDRESS, coin.publicAddress);
        obj.put(K_TIME_MILLIS, coin.timeMillis);
        obj.put(K_NOTE, coin.note);
        return obj.toString();
    }
}
