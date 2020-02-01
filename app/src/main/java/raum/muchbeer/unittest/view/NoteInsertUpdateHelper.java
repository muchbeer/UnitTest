package raum.muchbeer.unittest.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import raum.muchbeer.unittest.ui.DataStateStatus;

public abstract class NoteInsertUpdateHelper<T> {

    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String GENERIC_ERROR = "Something went wrong";

    private MediatorLiveData<DataStateStatus<T>> result = new MediatorLiveData<>();

    public NoteInsertUpdateHelper() {
        init();
    }

    private void init() {
        result.setValue((DataStateStatus<T>) DataStateStatus.loading(null));
        try {
            final LiveData<DataStateStatus<T>> source = getAction();
            result.addSource(source, tResource-> {
                result.removeSource(source);
                result.setValue(tResource);
                setNewNoteIdIfIsNewNote(tResource);
                onTransactionComplete();
            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setValue(DataStateStatus.<T>error(null, GENERIC_ERROR));
        }
    }

    private void setNewNoteIdIfIsNewNote(DataStateStatus<T> dataStatus){
        if(dataStatus.data != null) {
            if (dataStatus.data.getClass() == Integer.class) {
                int i = (Integer) dataStatus.data;

                if (defineAction().equals(ACTION_INSERT)) {
                    if (i >= 0) {   setNoteId(i);   }
                }
            }
        }
    }

    public abstract void setNoteId(int noteId);

    public abstract LiveData<DataStateStatus<T>> getAction() throws Exception;

    public abstract String defineAction();

    public abstract void onTransactionComplete();

    public final LiveData<DataStateStatus<T>> getAsLiveData(){
        return result;
    }

}
