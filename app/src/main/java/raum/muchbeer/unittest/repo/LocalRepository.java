package raum.muchbeer.unittest.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.roomdb.NoteDao;
import raum.muchbeer.unittest.ui.DataStateStatus;

@Singleton
public class LocalRepository {

    public static final String NOTE_TITLE_NULL = "Note title cannot be null";
    public static final String INVALID_NOTE_ID = "Invalid id. Can't delete note";
    public static final String DELETE_SUCCESS = "Delete success";
    public static final String DELETE_FAILURE = "Delete failure";
    public static final String UPDATE_SUCCESS = "Update success";
    public static final String UPDATE_FAILURE = "Update failure";
    public static final String INSERT_SUCCESS = "Insert success";
    public static final String INSERT_FAILURE = "Insert failure";

    private int timeDelay = 0;
    private TimeUnit timeUnit = TimeUnit.SECONDS;


    //inject
    @NonNull
    private static NoteDao noteDao;


    @Inject
    public LocalRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    //Returning Flowable because Flowable can be convertable to LiveData
    public Flowable<DataStateStatus<Integer>> insertNote(final Note note) throws Exception{

        checkTitle(note);
      return   noteDao.insertNote(note).delaySubscription(timeDelay, timeUnit)
                                .map(new Function<Long, Integer>() {
                                    @Override
                                    public Integer apply(Long aLong) throws Exception {
                                        long inserted = aLong;
                                        return (int) inserted;
                                    }
                                })
                                .onErrorReturn(new Function<Throwable, Integer>() {
                                    @Override
                                    public Integer apply(Throwable throwable) throws Exception {
                                        return -1;
                                    }
                                })
                                .map(new Function<Integer, DataStateStatus<Integer>>() {
                                    @Override
                                    public DataStateStatus<Integer> apply(Integer integer) throws Exception {
                                       if(integer > 0) {
                                           return DataStateStatus.success(integer, INSERT_SUCCESS);
                                       } else
                                        return DataStateStatus.error(null, INSERT_FAILURE);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .toFlowable();
    }

    public Flowable<DataStateStatus<Integer>> updateNote(final Note note) throws Exception{

        checkTitle(note);

        return noteDao.updateNote(note)
                .delaySubscription(timeDelay, timeUnit)
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        return -1;
                    }
                })
                .map(new Function<Integer, DataStateStatus<Integer>>() {
                    @Override
                    public DataStateStatus<Integer> apply(Integer integer) throws Exception {

                        if(integer > 0){
                            return DataStateStatus.success(integer, UPDATE_SUCCESS);
                        }
                        return DataStateStatus.error(null, UPDATE_FAILURE);
                    }
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();
    }

    public LiveData<DataStateStatus<Integer>> deleteNote(final Note note) throws Exception{

        checkId(note);

        return LiveDataReactiveStreams.fromPublisher(
                noteDao.deleteNote(note)
                        .onErrorReturn(new Function<Throwable, Integer>() {
                            @Override
                            public Integer apply(Throwable throwable) throws Exception {
                                return -1;
                            }
                        })
                        .map(new Function<Integer, DataStateStatus<Integer>>() {
                            @Override
                            public DataStateStatus<Integer> apply(Integer integer) throws Exception {
                                if(integer > 0){
                                    return DataStateStatus.success(integer, DELETE_SUCCESS);
                                }
                                return DataStateStatus.error(null, DELETE_FAILURE);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<List<Note>> getNotes(){
        return noteDao.getNotes();
    }

    private void checkId(Note note) throws Exception{
        if(note.getId() < 0){
            throw new Exception(INVALID_NOTE_ID);
        }
    }
    private void checkTitle(Note note) throws Exception{
        if(note.getTitle() == null){
            throw new Exception(NOTE_TITLE_NULL);
        }
    }

}
