package raum.muchbeer.unittest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NoteTest {

    public static final String TIMESTAMP1 = "24-2019";
    public static final String TIMESTAMP2= "26-2016";
  /*  Compare two notes*/

    @Test
    void isNotes_identicalProperties_returnTrue() throws Exception {
        //Arrange
Note note1 = new Note("gianna","baby girl", TIMESTAMP1);
note1.setId(1);
Note note2 = new Note("gianna", "baby girl", TIMESTAMP1);
note2.setId(1);
        //Action

        //Assert
        Assertions.assertEquals(note1, note2);
        System.out.println("The notes are identical");
    }


/*
Compare notes with two different ID's
     */

    @Test
    void isNotes_twoDifferentID_returnFalse() throws Exception {
        //Arrange
        Note note1 = new Note("gianna","baby girl", TIMESTAMP1);
        note1.setId(1);
        Note note2 = new Note("gianna", "baby girl", TIMESTAMP1);
        note2.setId(2);
        //Action

        //Assert
        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes are not identical");
    }

/*
        Compare notes with two different Timestamp
    */

    @Test
    void isNoteEqual_differentTimeStamp_returnTrue() throws Exception {
        Note note1 = new Note("gianna","baby girl", TIMESTAMP1);
        note1.setId(1);
        Note note2 = new Note("gianna", "baby girl", TIMESTAMP2);
        note2.setId(1);
        //Action

        //Assert
        Assertions.assertEquals(note1, note2);
        System.out.println("The notes are equal");
    }
/*
        Compare notes with two different title
    */

    @Test
    void isNoteEqual_differentTitle_returnFalse() throws Exception {
        Note note1 = new Note("gianna","baby girl", TIMESTAMP1);
        note1.setId(1);
        Note note2 = new Note("gadiel", "baby girl", TIMESTAMP2);
        note2.setId(1);
        //Action

        //Assert
        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes title are not equal ");
    }
/*
      Compare notes with two different content
    */

    @Test
    void isNoteEqual_differentContent_returnFalse() throws Exception {
        Note note1 = new Note("gianna","baby girl", TIMESTAMP1);
        note1.setId(1);
        Note note2 = new Note("gadiel", "baby boy", TIMESTAMP2);
        note2.setId(1);
        //Action

        //Assert
        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes title are not equal ");
    }
}
