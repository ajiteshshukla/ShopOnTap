package com.lphaindia.dodapp.dodapp.injectors;

import com.lphaindia.dodapp.dodapp.DodIntentService;
import com.lphaindia.dodapp.dodapp.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
@Singleton
@Component(modules = DodInjectorModule.class)
public interface ServiceInjector {

    //Activities and UI
    void injectMainActivity(MainActivity mainActivity);


    //Services
    void injectDodIntentService(DodIntentService dodIntentService);
}
