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
import raum.muchbeer.unittest.util.DateUtil;
import raum.muchbeer.unittest.view.NoteInsertUpdateHelper;

import static raum.muchbeer.unittest.repo.LocalRepository.NOTE_TITLE_NULL;

public class NoteViewModel extends ViewModel {

    private static final String LOG_TAG = NoteViewModel.class.getSimpleName();

    public static final String NO_CONTENT_ERROR = "Can't save note with no content";

    //inject
    private final LocalRepository localRepository;

    //var
    private MutableLiveData<Note> note = new MutableLiveData<>();
    public enum ViewState {VIEW, EDIT}

    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    private boolean isNewNote;

    private Subscription updateSubscription, insertSubscription;

    @Inject
    public NoteViewModel(LocalRepository localRepository) {
        this.localRepository = localRepository;   }


    public LiveData<Note> observeNote() {  return note;  }


    public LiveData<DataStateStatus<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                localRepository.insertNote(note.getValue())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        insertSubscription = subscription;
                    }
                })
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

    public LiveData<ViewState> observeViewState(){ return viewState;   }

    public void setViewState(ViewState viewState){ this.viewState.setValue(viewState);   }

    public void setIsNewNote(boolean isNewNote){  this.isNewNote = isNewNote;   }

    public LiveData<DataStateStatus<Integer>> saveNote() throws Exception{
        if(!shouldAllowSave()){
            throw new Exception(NO_CONTENT_ERROR);
        }

        cancelPendingTransactions();

        return new NoteInsertUpdateHelper<Integer>() {
            @Override
            public void setNoteId(int noteId) {
                isNewNote = false;
                Note currentNote = note.getValue();
                currentNote.setId(noteId);
                note.setValue(currentNote);
            }

            @Override
            public LiveData<DataStateStatus<Integer>> getAction() throws Exception {
                if(isNewNote){
                    return insertNote();
                }
                else{
                    return updateNote();
                }
            }

            @Override
            public String defineAction() {
                if(isNewNote){
                    return ACTION_INSERT;
                }
                else{
                    return ACTION_UPDATE;
                }
            }

            @Override
            public void onTransactionComplete() {
                updateSubscription = null;
                insertSubscription = null;
            }
        }.getAsLiveData();
            }

    private void cancelPendingTransactions(){
        if(insertSubscription != null){  cancelInsertTransaction();  }

        if(updateSubscription != null){  cancelUpdateTransaction();  }
    }

    private void cancelUpdateTransaction(){
        updateSubscription.cancel();
        updateSubscription = null;
    }

    private void cancelInsertTransaction(){
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private boolean shouldAllowSave() throws Exception{
        try{
            return removeWhiteSpace(note.getValue().getContent()).length() > 0;
        }catch (NullPointerException e){
            throw new Exception(NO_CONTENT_ERROR);     }
    }

    public void updateNote(String title, String content) throws Exception{
        if(title == null || title.equals("")){
            throw new NullPointerException("Title can't be null");
        }
        String temp = removeWhiteSpace(content);
        if(temp.length() > 0){
            Note updatedNote = new Note(note.getValue());
            updatedNote.setTitle(title);
            updatedNote.setContent(content);
            updatedNote.setTimestamp(DateUtil.getCurrentTimeStamp());

            note.setValue(updatedNote);
        }
    }

    private String removeWhiteSpace(String string){
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        return string;
    }

    public boolean shouldNavigateBack(){

        if(viewState.getValue() == ViewState.VIEW){  return true;   }

        else{  return false;  }
    }


}



