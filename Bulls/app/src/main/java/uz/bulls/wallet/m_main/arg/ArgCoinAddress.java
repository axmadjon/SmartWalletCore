package uz.bulls.wallet.m_main.arg;

import uz.bulls.wallet.bean.CoinCore;
import uz.bulls.wallet.m_main.MainApiKt;
import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class ArgCoinAddress extends ArgCoin {

    public final String publicAddress;

    public ArgCoinAddress(ArgCoin arg, String publicAddress) {
        super(arg.coinId);
        this.publicAddress = publicAddress;
    }

    public ArgCoinAddress(String coinId, String publicAddress) {
        super(coinId);
        this.publicAddress = publicAddress;
    }

    protected ArgCoinAddress(UzumReader in) {
        super(in);
        this.publicAddress = in.readString();
    }

    @Override
    protected void write(UzumWriter out) {
        super.write(out);
        out.write(this.publicAddress);
    }

    public CoinCore findAddress() {
        return MainApiKt.getCoinCore(this.coinId)
                .find(this.publicAddress, CoinCore.KEY_ADAPTER);
    }

    public static final UzumAdapter<ArgCoinAddress> UZUM_ADAPTER = new UzumAdapter<ArgCoinAddress>() {
        @Override
        public ArgCoinAddress read(UzumReader in) {
            return new ArgCoinAddress(in);
        }

        @Override
        public void write(UzumWriter out, ArgCoinAddress val) {
            val.write(out);
        }
    };
}
