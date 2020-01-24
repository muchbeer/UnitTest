package raum.muchbeer.unittest.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import raum.muchbeer.unittest.repo.LocalRepository;
import raum.muchbeer.unittest.roomdb.NoteDao;
import raum.muchbeer.unittest.roomdb.NoteDatabase;

import static raum.muchbeer.unittest.roomdb.NoteDatabase.DATABASE_NAME;

@Module
class AppModule {

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(
                application,
                NoteDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase noteDatabase){
        return noteDatabase.getNoteDao();
    }

    @Singleton
    @Provides
    static LocalRepository provideLocalRepository(NoteDao noteDao) {
        return new LocalRepository(noteDao);
    }
}
