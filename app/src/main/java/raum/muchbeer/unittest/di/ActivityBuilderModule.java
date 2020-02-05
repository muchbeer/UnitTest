package raum.muchbeer.unittest.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import raum.muchbeer.unittest.BookActivity;
import raum.muchbeer.unittest.BookListActivity;
import raum.muchbeer.unittest.MainActivity;
import raum.muchbeer.unittest.viewmodel.BookListViewModel;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeNotesListActivity();

    @ContributesAndroidInjector
    abstract BookActivity contributeBooksActivity();

    @ContributesAndroidInjector
    abstract BookListActivity contributeBooksListActivity();
}
