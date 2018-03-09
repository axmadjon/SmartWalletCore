package uz.bulls.wallet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uz.bulls.wallet.m_main.ui.openCoinMarketCapFragment

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)

        openCoinMarketCapFragment(this)
        finish()
    }
}
