package raum.muchbeer.unittest.repo;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import raum.muchbeer.unittest.roomdb.NoteDao;

@Singleton
public class LocalRepository {

    //inject
    @NonNull
    private static NoteDao noteDao;


    @Inject
    public LocalRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
