package ch.hslu.fs19.mobpro.sweetbeeromine.db.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.AppDatabase;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerQuotesEntity;

/**
 * This class acts as a single access point to the beer data, hence the room db. Interaction with
 * data should only be made using this class, not directly using the AppDatabase App.
 */
public class DataRepository {
    private AppDatabase appDatabase;

    public DataRepository(final Context context) {
        this.appDatabase = AppDatabase.getInstance(context);
    }

    /**
     * Make sure db gets created and at least one beer element is contained, and also set up quotes.
     */
    public void setUpDataBase() {
        BeerEntity entity = new BeerEntity("SampleBrewery", "SampleBeerName", "SampleBeerType", "SampleCountry", "SampleNotes");
        List<BeerEntity> beerList = new ArrayList<>();
        beerList.add(entity);
        appDatabase.beerDao().insertAll(beerList);
        List<BeerQuotesEntity> quotesList = new ArrayList<>();
        BeerQuotesEntity quote1 = new BeerQuotesEntity("Milk is for babies. When you grow up you have to drink beer.", "- Arnold Schwarzenegger");
        BeerQuotesEntity quote2 = new BeerQuotesEntity("Some people wanted champagne and caviar when they should have had beer and hot dogs.",
                "- Dwight D. Eisenhower");
        BeerQuotesEntity quote3 = new BeerQuotesEntity("Not all chemicals are bad. Without chemicals such as hydrogen and oxygen, for example, there would be no way to make water, a vital ingredient in beer.", "- Dave Barry");
        BeerQuotesEntity quote4 = new BeerQuotesEntity("Give me a woman who loves beer and I will conquer the world.", "- Kaiser Wilhelm");
        BeerQuotesEntity quote5 = new BeerQuotesEntity("Beer, it’s the best damn drink in the world.","- Jack Nicholson");
        BeerQuotesEntity quote6 = new BeerQuotesEntity("Give a man a beer, waste an hour. Teach a man to brew, and waste a lifetime!", "- Bill Owen");
        BeerQuotesEntity quote7 = new BeerQuotesEntity("Prohibition makes you want to cry into your beer and denies you the beer to cry into.","- Don Marquis");
        BeerQuotesEntity quote8 = new BeerQuotesEntity("If you guys are going to be throwing beer bottles at us, at least make sure they’re full.","– Dave Mustaine");
        BeerQuotesEntity quote9 = new BeerQuotesEntity("Whiskey’s to tough, Champagne costs too much, Vodka puts my mouth in gear. I hope this refrain, Will help me explain, As a matter of fact, I like beer.", "– Tom T. Hall");
        BeerQuotesEntity quote10 = new BeerQuotesEntity("There is no such thing as a bad beer. It’s that some taste better than others.","– Billy Carter");
        quotesList.add(quote1);
        quotesList.add(quote2);
        quotesList.add(quote3);
        quotesList.add(quote4);
        quotesList.add(quote5);
        quotesList.add(quote6);
        quotesList.add(quote7);
        quotesList.add(quote8);
        quotesList.add(quote9);
        quotesList.add(quote10);
        appDatabase.quotesDao().insertAll(quotesList);
    }

    /**
     * Get all beers.
     * @return
     */
    public LiveData<List<BeerEntity>> getAllBeers() {
        return appDatabase.beerDao().getAll();
    }

    /**
     * Get Beer by id.
     * @param id of beer.
     * @return
     */
    public BeerEntity getBeerById(String id) {
        return appDatabase.beerDao().findById(id);
    }

    /**
     * Get the number of beers contained in the database.
     * @return
     */
    public Integer numberOfBeers() {
        return appDatabase.beerDao().getNumberOfBeers();
    }

    /**
     * Insert given list to the db.
     * @param entities
     */
    public void insertBeers(List<BeerEntity> entities) {
        appDatabase.beerDao().insertAll(entities);
    }

    /**
     * Get the number of different countries.
     * @return
     */
    public Integer numberOfCountries() {
        return appDatabase.beerDao().getNumberOfCountries();
    }

    /**
     * Update the beer entitiy.
     * @param entity to update
     */
    public void updateBeer(BeerEntity entity) {
        appDatabase.beerDao().upDateEntity(entity);
    }

    /**
     * Used to delete beer.
     * @param entity to delete
     */
    public void deleteBeeer(BeerEntity entity) {
        appDatabase.beerDao().deleteBeer(entity);
    }

    /**
     * Get all quotes fro the db.
     * @return all quotes
     */
    public List<BeerQuotesEntity> getAllQuotes() {
        return appDatabase.quotesDao().getAll();
    }

}
