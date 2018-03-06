package uz.bulls.wallet;

import android.app.Application;

public class BullsApp extends Application {

    private static BullsApp instance;

    public static BullsApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
