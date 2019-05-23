package ch.hslu.fs19.mobpro.sweetbeeromine.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.dao.BeerDao;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.dao.QuotesDao;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerQuotesEntity;

/**
 * This is the actual room database. It is implemented as a singleton.
 */
@Database(entities = {BeerEntity.class, BeerQuotesEntity.class}, version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDB;

    public abstract BeerDao beerDao();

    public abstract QuotesDao quotesDao();

    /**
     * Make sure only on instance of the room db is present, because an instance is memory expensive.
     * @param appContext context of activity
     * @return
     */
    public static AppDatabase getInstance(final Context appContext) {
        if (appDB == null) {
            synchronized (AppDatabase.class) {
                appDB = Room.databaseBuilder(appContext, AppDatabase.class, "sweet-beer-of-mine").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
        }
        return appDB;
    }

}
