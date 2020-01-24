package raum.muchbeer.unittest.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import raum.muchbeer.unittest.model.Note;


@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    public abstract NoteDao getNoteDao();
}
