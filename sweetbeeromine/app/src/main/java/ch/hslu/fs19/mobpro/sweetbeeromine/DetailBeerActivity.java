package ch.hslu.fs19.mobpro.sweetbeeromine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.data.DataRepository;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerQuotesEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.StorageUtilities;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.Utilities;

/**
 * Activity for the detail screen.
 */
public class DetailBeerActivity extends AppCompatActivity {
    private String beerId;
    private BeerEntity beerEntity;
    private DataRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_beer);
        // initialize repository
        this.repository = new DataRepository(getApplicationContext());

        // get the id of the beer
        Intent intent = getIntent();

        // for some fucking unknown reason, this line has to be called twice to work
        this.beerId = intent.getStringExtra(Utilities.getBeerIdKey());
        this.beerId = intent.getStringExtra(Utilities.getBeerIdKey());

        if(this.beerId != null && !this.beerId.isEmpty()) {
            this.beerEntity = repository.getBeerById(this.beerId);

            // set values of text fields
            TextView brewery = findViewById(R.id.detailView_content_brewery);
            brewery.setText(beerEntity.getBrewery());
            TextView beerName = findViewById(R.id.detailView_content_beername);
            beerName.setText(beerEntity.getBeerName());
            TextView beerType = findViewById(R.id.detailView_content_beertype);
            beerType.setText(beerEntity.getBeerType());
            TextView beerCountry = findViewById(R.id.detailView_content_country);
            beerCountry.setText(beerEntity.getCountry());
            TextView beerNotes = findViewById(R.id.detailView_content_notes);
            beerNotes.setText(beerEntity.getNotes());
            ImageView beerImage = findViewById(R.id.detailView_image);

            // set image
            try {
                if (!beerEntity.getImagePath().isEmpty()) {
                    beerImage.setImageBitmap(StorageUtilities.loadImageFromStorage(beerEntity.getImagePath()));
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Failed to get image from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to get Beer-Details from database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.beerEntity = repository.getBeerById(this.beerId);
        // set values of text fields
        TextView brewery = findViewById(R.id.detailView_content_brewery);
        brewery.setText(beerEntity.getBrewery());
        TextView beerName = findViewById(R.id.detailView_content_beername);
        beerName.setText(beerEntity.getBeerName());
        TextView beerType = findViewById(R.id.detailView_content_beertype);
        beerType.setText(beerEntity.getBeerType());
        TextView beerCountry = findViewById(R.id.detailView_content_country);
        beerCountry.setText(beerEntity.getCountry());
        TextView beerNotes = findViewById(R.id.detailView_content_notes);
        beerNotes.setText(beerEntity.getNotes());
        ImageView beerImage = findViewById(R.id.detailView_image);

        // set image
        try {
            if (!beerEntity.getImagePath().isEmpty()) {
                beerImage.setImageBitmap(StorageUtilities.loadImageFromStorage(beerEntity.getImagePath()));
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Failed to get image from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // make notes content scrollable
        beerNotes.setMovementMethod(new ScrollingMovementMethod());

        // fill random quote to textview
        TextView quotesView = findViewById(R.id.detailView_quotes);
        TextView authorView = findViewById(R.id.detailView_quoteAuthor);

        List<BeerQuotesEntity> entityList = repository.getAllQuotes();
        Random randomIndex = new Random();
        int index = randomIndex.nextInt(entityList.size() - 1);

        quotesView.setText(entityList.get(index).getQuote());
        authorView.setText(entityList.get(index).getAuthor());


    }

    // Show the menu resource in ActionBar Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set Action for Button in ActionBar Options Menu to start EditBeerActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.detail_edit:
                Intent editbeerview = new Intent(this, EditBeerActivity.class);
                editbeerview.putExtra(Utilities.getBeerIdKey(), beerId);
                startActivity(editbeerview);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
