package ch.hslu.fs19.mobpro.sweetbeeromine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.data.DataRepository;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.Utilities;

/**
 * Class for the main activity.
 */
public class MainActivity extends AppCompatActivity {
    // Declare the data repository
    private DataRepository repository;

    /**
     * Override of onCreate Method
     *     What happens in here:
     *
     *     - setting up sample Data in database if database is empty
     *     - setting up observer of LiveData
     *     - set actions for the FABs
     *     - set show/hide for FABs on scroll
     *     - set SharedPreferences for Beer & Country Counter
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the first Sample Record in the database if none is present
        repository = new DataRepository(getApplicationContext());
        if(repository.numberOfBeers() == 0) {
            repository.setUpDataBase();
        }

        // get all the beers contained in the db
        LiveData<List<BeerEntity>> list = repository.getAllBeers();

        // We have to observe the live data, because the actual query is made asynchronously and
        // we don't know when its made and when the Live Data actually contains the data.
        list.observe(this, new Observer<List<BeerEntity>>() {
            @Override
            public void onChanged(@Nullable List<BeerEntity> entities) {
                List<BeerEntity> list = new ArrayList<>();
                for(BeerEntity entity: entities) {
                    list.add(entity);
                }
                initDataListForAdapter(list);
            }
        });

        // AddBeer FloatingActionButton & set Action to AddBeerActivity
        final Intent addbeerview = new Intent(this, AddBeerActivity.class);
        final FloatingActionButton addFloatingButton = findViewById(R.id.add_floating_button);
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(addbeerview);
            }
        });

        // Trophies FloatingActionButton & set Action to TrophiesActivity
        final Intent trophiesview = new Intent(this, TrophiesActivity.class);
        final FloatingActionButton trophiesFloatingButton = findViewById(R.id.trophies_floating_button);
        trophiesFloatingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(trophiesview);
            }
        });

        // configure OnScrollListener for the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // Show FABs when no Scrolling is detected
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    addFloatingButton.show();
                    trophiesFloatingButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
            // Hide FABs when Scrolling is detected
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && addFloatingButton.isShown() && trophiesFloatingButton.isShown()) {
                    addFloatingButton.hide();
                    trophiesFloatingButton.hide();
                }
            }
        });

        // Set count preferences, which are getting used by the TrophiesActivity
        setTrophiesPreferences();
    }



    // Before exiting MainActivity, set Shared Preferences
    // Reassures that Counters are always saved when switching to another activity
    @Override
    protected void onPause() {
        setTrophiesPreferences();
        super.onPause();
    }

    // Show the menu resource in ActionBar Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set Action for Button in ActionBar Options Menu to start CopyrightActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_copyright:
                Intent startCopyright = new Intent(this, CopyrightActivity.class);
                startActivity(startCopyright);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Generate the recycler view based on the db-entries.
     * @param entities
     */
    private void initDataListForAdapter(List<BeerEntity> entities) {
        // needed for initialization of the RecyclerView
        RecyclerView recList = findViewById(R.id.recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        // set the CardView for every beer as content of the RecyclerView
        BeersAdapter ca = new BeersAdapter(entities);
        recList.setAdapter(ca);
    }

    /**
     * Set Beer & Country Counter to SharedPreferences.
     */
    private void setTrophiesPreferences() {
        SharedPreferences sp = getSharedPreferences(Utilities.getSharedPreferencesCounts(), 0);

        SharedPreferences.Editor spedit = sp.edit();
        spedit.putInt(Utilities.getSharedPreferencesCountsBeer(), repository.numberOfBeers());
        spedit.putInt(Utilities.getSharedPreferencesCountsCountry(), repository.numberOfCountries());
        spedit.apply();
    }

}


