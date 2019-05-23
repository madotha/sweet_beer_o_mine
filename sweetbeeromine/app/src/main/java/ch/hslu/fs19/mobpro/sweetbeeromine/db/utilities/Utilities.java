package ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities;

/**
 * Utilities class for common used strings.
 */
public class Utilities {
    private static final String BEER_ID_KEY = "beerID";
    private static final String SHARED_PREFERENCES_COUNTS = "counts";
    private static final String SHARED_PREFERENCES_COUNTS_BEER = "beercount";
    private static final String SHARED_PREFERENCES_COUNTS_COUNTRY = "countrycount";

    public static String getBeerIdKey() { return BEER_ID_KEY; }
    public static String getSharedPreferencesCounts() { return SHARED_PREFERENCES_COUNTS; }
    public static String getSharedPreferencesCountsBeer() { return SHARED_PREFERENCES_COUNTS_BEER; }
    public static String getSharedPreferencesCountsCountry() { return SHARED_PREFERENCES_COUNTS_COUNTRY; }
}
