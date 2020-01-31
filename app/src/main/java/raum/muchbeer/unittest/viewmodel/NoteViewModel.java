package raum.muchbeer.unittest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.ui.DataStateStatus;

import static raum.muchbeer.unittest.repo.LocalRepository.NOTE_TITLE_NULL;

public class NoteViewModel extends ViewModel {

    private static final String LOG_TAG = NoteViewModel.class.getSimpleName();

    //inject
    private final LocalRepository localRepository;

    //var
    private MutableLiveData<Note> note = new MutableLiveData<>();
    private boolean isNewNote;

    private Subscription updateSubscription, insertSubscription;

    @Inject
    public NoteViewModel(LocalRepository localRepository) {
        this.localRepository = localRepository;   }


    public LiveData<Note> observeNote() {  return note;  }


    public LiveData<DataStateStatus<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                localRepository.insertNote(note.getValue())
        );
    }


    public void setNote(Note note) throws Exception {
        if(note.getTitle() ==null || note.getTitle().equals("")) {
            throw  new Exception(NOTE_TITLE_NULL);
        }
        this.note. setValue(note);
    }


    public LiveData<DataStateStatus<Integer>> updateNote() throws Exception{
        return LiveDataReactiveStreams.fromPublisher(
                localRepository.updateNote(note.getValue())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                updateSubscription = subscription;
                            }
                        }) );
    }


}
