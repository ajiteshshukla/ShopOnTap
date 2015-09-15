package com.lphaindia.dodapp.dodapp.injectors;

import android.content.Context;
import com.lphaindia.dodapp.dodapp.injectors.DaggerServiceInjector;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public final class Injectors {

    private Injectors() {
        throw new AssertionError();
    }

    public static volatile ServiceInjector serviceInjector;

    public static void initialize(Context context) {
        if (serviceInjector == null) {
            synchronized (Injectors.class) {
                if (serviceInjector == null) {
                    DodInjectorModule module = new DodInjectorModule(context);
                    serviceInjector = DaggerServiceInjector.builder()
                            .dodInjectorModule(module)
                            .build();
                }
            }
        }
    }
}
