package com.achulkov.loftcoin.fcm;

import com.achulkov.loftcoin.BaseComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        FcmModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class FcmComponent {

    abstract void inject(FcmService service);

}
