package ch.hslu.fs19.mobpro.sweetbeeromine.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Class for the beer entity.
 */
@Entity(tableName = "beer")
public class BeerEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String brewery;
    private String beerName;
    private String beerType;
    private String country;
    private String notes;
    private String imagePath;

    public BeerEntity(String brewery, String beerName, String beerType, String country, String notes) {
        this.brewery = brewery;
        this.beerName = beerName;
        this.beerType = beerType;
        this.country = country;
        this.notes = notes;
        this.imagePath = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String name) {
        this.beerName = name;
    }

    public String getBeerType() {
        return beerType;
    }

    public void setBeerType(String type) {
        this.beerType = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
