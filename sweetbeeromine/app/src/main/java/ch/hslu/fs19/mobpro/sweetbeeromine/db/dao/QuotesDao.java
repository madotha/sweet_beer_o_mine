package ch.hslu.fs19.mobpro.sweetbeeromine.db.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerQuotesEntity;

/**
 * Dao Interface for the beer quotes entity. It declares the operations possible on the quotes entity.
 */
@Dao
public interface QuotesDao {
    @Query("SELECT * FROM quotes")
    List<BeerQuotesEntity> getAll();

    @Insert
    void insertAll(List<BeerQuotesEntity> entity);
}
