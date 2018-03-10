package uz.bulls.wallet.m_coin.arg;

import uz.greenwhite.lib.uzum.UzumAdapter;
import uz.greenwhite.lib.uzum.UzumReader;
import uz.greenwhite.lib.uzum.UzumWriter;

public class ArgCoinInfo extends ArgCoin {

    public final String publicAddress;

    public ArgCoinInfo(ArgCoin arg, String publicAddress) {
        super(arg.coinId);
        this.publicAddress = publicAddress;
    }

    protected ArgCoinInfo(UzumReader in) {
        super(in);
        this.publicAddress = in.readString();
    }

    @Override
    protected void write(UzumWriter out) {
        super.write(out);
        out.write(publicAddress);
    }

    public static final UzumAdapter<ArgCoinInfo> UZUM_ADAPTER = new UzumAdapter<ArgCoinInfo>() {
        @Override
        public ArgCoinInfo read(UzumReader in) {
            return new ArgCoinInfo(in);
        }

        @Override
        public void write(UzumWriter out, ArgCoinInfo val) {
            val.write(out);
        }
    };
}
