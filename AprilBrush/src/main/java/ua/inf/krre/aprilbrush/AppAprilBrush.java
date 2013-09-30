package ua.inf.krre.aprilbrush;

import android.app.Application;
import android.content.Context;

public class AppAprilBrush extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
