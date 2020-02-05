package raum.muchbeer.unittest.repos;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Single;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.roomdb.NoteDao;
import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.LiveDataTestUtil;
import raum.muchbeer.unittest.util.TestUtil;

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

@RunWith(JUnit4.class)
public class LocalRepositoryJUnit4Test {

    @Rule
public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    private LocalRepository localRepository;
    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Mock
    private NoteDao noteDao;

    @Before
    public void initEach() {
        // MockitoAnnotations.initMocks(this);
        noteDao = Mockito.mock(NoteDao.class);

        localRepository = new LocalRepository(noteDao);

    }


  /*  insert note  verify the correct method is called confirm observer is triggered
    confirm new row inserted*/

    @Test
   public void insertNote_returnRow() throws Exception {
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
        Assert.assertEquals(DataStateStatus.success(1, INSERT_SUCCESS), returnedValue);


//        // Or test using RxJava
//        noteRepository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1, INSERT_SUCCESS));
    }


/*   insert note
    Failure (return -1)*/

    @Test
   public void insertNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        Assert.assertEquals(DataStateStatus.error(null, UPDATE_FAILURE), returnedValue);
    }


/*   insert note null title  confirm throw exception*/

    @Test
   public void insertNote_returnNull_throwException() throws Exception {

        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        thrown.expect(Exception.class);
        thrown.expectMessage(NOTE_TITLE_NULL);

        //Assert
        localRepository.insertNote(note);

     }

     /*  update note verify correct method is called  confirm observer is trigger
     confirm number of rows updated  */

    @Test
   public void updateNote_returnNumRowsUpdated() throws Exception {
        // Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        Assert.assertEquals(DataStateStatus.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }

    /*   update note   Failure (-1)   */

    @Test
   public void updateNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final DataStateStatus<Integer> returnedValue = localRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        Assert.assertEquals(DataStateStatus.error(null, UPDATE_FAILURE), returnedValue);
    }

    /*  update note  null title  throw exception   */
    @Test
   public void updateNote_nullTitle_throwException() throws Exception {

        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        //Act
        thrown.expect(Exception.class);
        thrown.expectMessage(NOTE_TITLE_NULL);


        //Assert
        localRepository.updateNote(note);

    }

    /*        delete note          null id        throw exception     */
    @Test
   public void deleteBook_nullId_throwException() throws Exception {

        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setId(-1);

        //Act
        thrown.expect(Exception.class);
        thrown.expectMessage(INVALID_NOTE_ID);

        //Assert
      localRepository.deleteBook(note);  }


    /*  delete note  delete success  return Resource.success with deleted row */
    @Test
   public void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        // Arrange
        final int deletedRow = 1;
        DataStateStatus<Integer> successResponse = DataStateStatus.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();

        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        DataStateStatus<Integer> observedResponse = liveDataTestUtil.getValue(localRepository.deleteBook(NOTE1));

        // Assert
        Assert.assertEquals(successResponse, observedResponse);
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
        Assert.assertEquals(errorResponse, observedResponse);
    }
    @Test
   public void giannaDummyTest() throws Exception {

        Assert.assertNotNull(noteDao);
        Assert.assertNotNull(localRepository);
    }


}
