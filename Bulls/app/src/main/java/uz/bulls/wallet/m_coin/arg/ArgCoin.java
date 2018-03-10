package uz.bulls.wallet.m_coin.arg;


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
