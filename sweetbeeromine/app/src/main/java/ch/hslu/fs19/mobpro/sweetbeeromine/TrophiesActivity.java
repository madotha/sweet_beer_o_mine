package ch.hslu.fs19.mobpro.sweetbeeromine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.Utilities;

/**
 * Class for the trophies activity.
 */
public class TrophiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        // Get Views from TrophiesActivity layout
        TextView beerCount = findViewById(R.id.trophies_beerCount);
        TextView countryCount = findViewById(R.id.trophies_countryCount);
        Button shareButton = findViewById(R.id.trophies_shareButton);

        // Set the SharedPreferences for Beer & Country Counter
        SharedPreferences sp = getSharedPreferences("counts", 0);
        beerCount.setText("" + sp.getInt(Utilities.getSharedPreferencesCountsBeer(), 0));
        countryCount.setText("" + sp.getInt(Utilities.getSharedPreferencesCountsCountry(), 0));

        // Set Intent for the Share Button
        // Sends a predefined text to a chosen Application
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, stringTrophiesStats());
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.txtTrophies_shareText)));
            }
        });
    }

    /**
     * Create the string which is shared when clicking on the Share Button.
     */
    public String stringTrophiesStats() {
        TextView statsBeerCount = findViewById(R.id.trophies_beerCount);
        TextView stats02 = findViewById(R.id.trophies_stats02);
        TextView statsCountryCount = findViewById(R.id.trophies_countryCount);
        TextView stats03 = findViewById(R.id.trophies_stats03);
        String trophiesStats = "";
        trophiesStats += getResources().getString(R.string.txtTrophies_shareStats01);
        trophiesStats += " ";
        trophiesStats += statsBeerCount.getText();
        trophiesStats += " ";
        trophiesStats += stats02.getText();
        trophiesStats += " ";
        trophiesStats += statsCountryCount.getText();
        trophiesStats += " ";
        trophiesStats += stats03.getText();
        trophiesStats += " ";
        trophiesStats += getResources().getString(R.string.txtTrophies_shareStatsExtra);
        return trophiesStats;
    }


}
