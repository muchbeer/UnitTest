package raum.muchbeer.unittest.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import raum.muchbeer.unittest.MainActivity;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeNotesListActivity();

    /*@ContributesAndroidInjector
    abstract NoteActivity contributeNotesActivity();*/
}
