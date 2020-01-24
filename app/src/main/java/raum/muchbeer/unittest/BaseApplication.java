package raum.muchbeer.unittest;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import raum.muchbeer.unittest.di.DaggerAppComponent;

public class BaseApplication extends DaggerApplication {


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
