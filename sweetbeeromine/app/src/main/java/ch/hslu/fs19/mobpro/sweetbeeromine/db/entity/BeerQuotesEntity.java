package ch.hslu.fs19.mobpro.sweetbeeromine.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Class for the quotes entity.
 */
@Entity(tableName = "quotes")
public class BeerQuotesEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String quote;
    private String author;

    public BeerQuotesEntity(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
