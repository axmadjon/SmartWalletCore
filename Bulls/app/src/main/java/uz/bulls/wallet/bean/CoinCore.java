package uz.bulls.wallet.bean;


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

    public static <E> UzumAdapter<E> getCoinAdapter(String coinId) {
        switch (coinId) {
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return (UzumAdapter<E>) ETHCoin.UZUM_ADAPTER;
            default:
                throw new AppError("coinId not found: " + coinId);
        }
    }
}
