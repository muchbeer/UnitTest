package raum.muchbeer.unittest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.ui.DataStateStatus;

public class BookListViewModel extends ViewModel {

    private static final String LOG_TAG = BookListViewModel.class.getSimpleName();

    private MediatorLiveData<List<Note>> book = new MediatorLiveData<>();
    //Inject
  private final  LocalRepository localRepository;

  @Inject
    public BookListViewModel(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public LiveData<DataStateStatus<Integer>> deleteNote(final Note note) throws Exception{
        return localRepository.deleteBook(note);
    }

    public LiveData<List<Note>> observeNotes(){
        return book;
    }

    public void getNotes(){
        final LiveData<List<Note>> source = localRepository.getNotes();
        book.addSource(source, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesList) {
                if(notesList != null){
                    book.setValue(notesList);
                }
                book.removeSource(source);
            }
        });
    }

}
