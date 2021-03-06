package raum.muchbeer.unittest.repos;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import net.bytebuddy.asm.Advice;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Single;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.roomdb.NoteDao;
import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.InstantExecutorExtension;
import raum.muchbeer.unittest.util.LiveDataTestUtil;
import raum.muchbeer.unittest.util.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static raum.muchbeer.unittest.repo.LocalRepository.DELETE_FAILURE;
import static raum.muchbeer.unittest.repo.LocalRepository.DELETE_SUCCESS;
import static raum.muchbeer.unittest.repo.LocalRepository.INSERT_SUCCESS;
import static raum.muchbeer.unittest.repo.LocalRepository.INVALID_NOTE_ID;
import static raum.muchbeer.unittest.repo.LocalRepository.NOTE_TITLE_NULL;
import static raum.muchbeer.unittest.repo.LocalRepository.UPDATE_FAILURE;
import static raum.muchbeer.unittest.repo.LocalRepository.UPDATE_SUCCESS;

@ExtendWith(InstantExecutorExtension.class)

public class LocalRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    private LocalRepository localRepository;

    @Mock
    private NoteDao noteDao;

    @BeforeEach
    public void initEach() {
        // MockitoAnnotations.initMocks(this);
        noteDao = Mockito.mock(NoteDao.class);

        localRepository = new LocalRepository(noteDao);

    }


  /*  insert note
    verify the correct method is called
    confirm observer is triggered
    confirm new row inserted*/

    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.insertNote(NOTE1).blockingSingle();

        // Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        System.out.println("Returned value: " + returnedValue.data);
        assertEquals(DataStateStatus.success(1, INSERT_SUCCESS), returnedValue);


//        // Or test using RxJava
//        noteRepository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1, INSERT_SUCCESS));
    }


/*   insert note
    Failure (return -1)*/

    @Test
    void insertNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(DataStateStatus.error(null, UPDATE_FAILURE), returnedValue);
    }
/*   insert note
null title
    confirm throw exception*/

    @Test
    void insertNote_returnNull_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note  = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                localRepository.insertNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

     /*
        update note
        verify correct method is called
        confirm observer is trigger
        confirm number of rows updated
     */

    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {
        // Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        Assertions.assertEquals(DataStateStatus.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }

    /*   update note   Failure (-1)   */

    @Test
    void updateNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        Assertions.assertEquals(DataStateStatus.error(null, UPDATE_FAILURE), returnedValue);
    }

    /*  update note  null title  throw exception   */
    @Test
    void updateNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note  = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                localRepository.updateNote(note);
            }
        });

        Assertions.assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }


    /*        delete note          null id        throw exception     */
    @Test
    void deleteBook_nullId_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                localRepository.deleteBook(note);
            }
        });

        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }


    /*  delete note  delete success  return Resource.success with deleted row */
    @Test
     void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        // Arrange
        final int deletedRow = 1;
        DataStateStatus<Integer> successResponse = DataStateStatus.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();

        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        DataStateStatus<Integer> observedResponse = liveDataTestUtil.getValue(localRepository.deleteBook(NOTE1));

        // Assert
        assertEquals(successResponse, observedResponse);
    }

    /*
       delete note   delete failure   return Resource.error
    */
    @Test
    public void deleteNote_deleteFailure_returnResourceError() throws Exception {
        // Arrange
        final int deletedRow = -1;
        DataStateStatus<Integer> errorResponse = DataStateStatus.error(null, DELETE_FAILURE);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        DataStateStatus<Integer> observedResponse = liveDataTestUtil.getValue(localRepository.deleteBook(NOTE1));

        // Assert
        assertEquals(errorResponse, observedResponse);
    }
    @Test
    void giannaDummyTest() throws Exception {

        Assertions.assertNotNull(noteDao);
        Assertions.assertNotNull(localRepository);
    }
}