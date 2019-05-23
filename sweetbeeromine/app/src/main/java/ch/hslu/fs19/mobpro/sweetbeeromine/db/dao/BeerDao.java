package ch.hslu.fs19.mobpro.sweetbeeromine.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;

/**
 * Dao Interface for the BeerEntity. This interface declares all possible interactions with the entity.
 */
@Dao
public interface BeerDao {
    @Query("SELECT * FROM beer")
    LiveData<List<BeerEntity>> getAll();

    @Query("SELECT * FROM beer WHERE id = :id")
    BeerEntity findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BeerEntity> entities);

    /**
     * Does the update based on the @PrimaryKey-Attribute
     */
    @Update
    void upDateEntity(BeerEntity entity);

    @Query("SELECT COUNT(*) FROM beer")
    Integer getNumberOfBeers();

    @Query("SELECT COUNT(DISTINCT country) FROM beer")
    Integer getNumberOfCountries();

    @Delete
    void deleteBeer(BeerEntity entity);
}
