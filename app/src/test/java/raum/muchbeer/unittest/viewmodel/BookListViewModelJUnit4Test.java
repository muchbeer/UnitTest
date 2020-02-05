package raum.muchbeer.unittest.viewmodel;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.LiveDataTestUtil;
import raum.muchbeer.unittest.util.TestUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static raum.muchbeer.unittest.repo.LocalRepository.DELETE_FAILURE;
import static raum.muchbeer.unittest.repo.LocalRepository.DELETE_SUCCESS;

@RunWith(JUnit4.class)
public class BookListViewModelJUnit4Test {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private BookListViewModel viewModel;

    @Mock
    private LocalRepository localRepository;


    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        viewModel = new BookListViewModel(localRepository);
    }

    /* Retrieve list of books observe list return list */

    @Test
   public void retrieveNotes_returnNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;


        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
                MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(localRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        Assert.assertEquals(returnedData, observedData);
    }

     /* retrieve list of book  observe the list return empty list */

    @Test
   public void retrieveNotes_returnEmptyNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = new ArrayList<>();

        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(localRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        Assert.assertEquals(returnedData, observedData);
    }

     /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
   public void deleteNote_observeResourceSuccess() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        DataStateStatus<Integer> returnedData = DataStateStatus.success(1, DELETE_SUCCESS);


        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<DataStateStatus<Integer>> returnedValue = new MutableLiveData<>();

        returnedValue.setValue(returnedData);
        when(localRepository.deleteBook(any(Note.class))).thenReturn(returnedValue);

        // Act
        DataStateStatus<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));


        // Assert
        Assert.assertEquals(returnedData, observedValue);
    }

    /*
        delete note
        observe Resource.error
        return Resource.error
     */
    @Test
   public void deleteNote_observeResourceError() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        DataStateStatus<Integer> returnedData = DataStateStatus.error(null, DELETE_FAILURE);
        LiveDataTestUtil<DataStateStatus<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<DataStateStatus<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(localRepository.deleteBook(any(Note.class))).thenReturn(returnedValue);

        // Act
        DataStateStatus<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));


        // Assert
        Assert.assertEquals(returnedData, observedValue);
    }

}
