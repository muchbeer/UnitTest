package raum.muchbeer.unittest.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import raum.muchbeer.unittest.viewmodel.BookListViewModel;
import raum.muchbeer.unittest.viewmodel.NoteViewModel;
import raum.muchbeer.unittest.viewmodel.ViewModelProviderFactory;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel.class)
    public abstract ViewModel bindNoteViewModel(NoteViewModel noteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookListViewModel.class)
    public abstract ViewModel bindBookListViewModel(BookListViewModel bookListViewModel);
}
