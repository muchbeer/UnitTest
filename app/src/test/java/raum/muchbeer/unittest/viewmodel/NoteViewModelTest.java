package raum.muchbeer.unittest.viewmodel;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;

import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.InstantExecutorExtension;
import raum.muchbeer.unittest.util.LiveDataTestUtil;
import raum.muchbeer.unittest.util.TestUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static raum.muchbeer.unittest.repo.LocalRepository.INSERT_SUCCESS;

@ExtendWith(InstantExecutorExtension.class)
public class NoteViewModelTest {


    // system under test
    private NoteViewModel noteViewModel;

   @Mock
    private LocalRepository localRepository;



    @BeforeEach
    public void init(){
       MockitoAnnotations.initMocks(this);
   //    localRepository = Mockito.mock(LocalRepository.class);
        noteViewModel = new NoteViewModel(localRepository);   }

         /*   can't observe a note that hasn't been set */
    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {
        //Arrange
       LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();
        //Action
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());
        //Assert
        Assertions.assertNull(note);
    }

    /* Observe a note has been set and onChanged will trigger in activity  */

    @Test
    void observeNote_whenSet() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();
        // Act
        noteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(noteViewModel.observeNote());
        // Assert
        Assertions.assertEquals(note, observedNote);  }

 /*
        Insert a new note and observe row returned
     */

    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<DataStateStatus<Integer>> returnedData = SingleToFlowable.just(DataStateStatus.success(insertedRow, INSERT_SUCCESS));
        Mockito.when(localRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
       // noteViewModel.setIsNewNote(true);
        DataStateStatus<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.insertNote());

        // Assert
        Assertions.assertEquals(DataStateStatus.success(insertedRow, INSERT_SUCCESS), returnedValue);



    }

        /*
        insert: dont return a new row without observer
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        // Act
        noteViewModel.setNote(note);
        // Assert
        verify(localRepository, never()).insertNote(any(Note.class));
    }

    /*
        set note, null title, throw exception
     */

    @Test
    void setNote_nullTitle_throwException() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        // Assert
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                // Act
                noteViewModel.setNote(note);
            }
        });
    }

}
