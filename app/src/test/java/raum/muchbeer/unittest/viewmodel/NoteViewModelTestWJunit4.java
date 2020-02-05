package raum.muchbeer.unittest.viewmodel;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleToFlowable;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.roomdb.NoteDao;
import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.LiveDataTestUtil;
import raum.muchbeer.unittest.util.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static raum.muchbeer.unittest.repo.LocalRepository.INSERT_SUCCESS;
import static raum.muchbeer.unittest.repo.LocalRepository.NOTE_TITLE_NULL;
import static raum.muchbeer.unittest.repo.LocalRepository.UPDATE_SUCCESS;
import static raum.muchbeer.unittest.viewmodel.NoteViewModel.NO_CONTENT_ERROR;

@RunWith(JUnit4.class)
public class NoteViewModelTestWJunit4 {

    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    //Junit4 throw exception
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // system under test
    private NoteViewModel noteViewModel;

    private NoteDao noteDao;

    @Mock
    private LocalRepository localRepository;

    @Before
    public void init() throws Exception{
        MockitoAnnotations.initMocks(this);
        //    localRepository = Mockito.mock(LocalRepository.class);
        noteViewModel = new NoteViewModel(localRepository);
    }


    /* Observe a note has been set and onChanged will trigger in activity  */
    @Test
   public void observeEmptyNoteWhenNoteSet() throws Exception {
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();
    Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());
    assertNull(note);     }

     /*   Insert a new note and observe row returned  */

    @Test
    public void observeNote_whenSet() throws Exception {
// Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        noteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertEquals(note, observedNote);
    }

      /*  Insert a new note and observe row returned   */

    @Test
   public void insertNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<DataStateStatus<Integer>> returnedData = SingleToFlowable.just(DataStateStatus.success(insertedRow, INSERT_SUCCESS));
        when(localRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        DataStateStatus<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.insertNote());

        // Assert
        assertEquals(DataStateStatus.success(insertedRow, INSERT_SUCCESS), returnedValue);
    }

    /*  insert: dont return a new row without observer */
    @Test
   public void dontReturnInsertRowWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(localRepository, never()).insertNote(any(Note.class));
    }


    /*  set note, null title, throw exception   */

    @Test
   public void insertNote_returnNull_throwException() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        thrown.expect(Exception.class);
        thrown.expectMessage(NOTE_TITLE_NULL);

        // Assert
        noteViewModel.setNote(note);

    }

     /* update a note and observe row returned */

    @Test
    public void updateNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int updatedRow = 1;
        Flowable<DataStateStatus<Integer>> returnedData = SingleToFlowable.
                just(DataStateStatus.success(updatedRow, UPDATE_SUCCESS));
        when(localRepository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        DataStateStatus<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.updateNote());

        // Assert
        assertEquals(DataStateStatus.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }

    /* update: don't return a new row without observer   */
    @Test
   public void dontReturnUpdateRowNumWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(localRepository, never()).updateNote(any(Note.class));
    }

    @Test
    public void saveNote_should_allowSave_returnFalse() throws  Exception{
        //Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setContent(null);
        //act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);

        //Assert
        thrown.expect(Exception.class);
        thrown.expectMessage(NO_CONTENT_ERROR);

        // Assert
        noteViewModel.saveNote();

  /*      Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                noteViewModel.saveNote();
            }

        });

        assertEquals(NO_CONTENT_ERROR, thrown.expectMessage(NO_CONTENT_ERROR));*/
    }


}
