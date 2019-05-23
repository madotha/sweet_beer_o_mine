package ch.hslu.fs19.mobpro.sweetbeeromine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.StorageUtilities;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.Utilities;

/**
 * Adapter for the Cardview.
 */
public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeersViewHolder> {

    private List<BeerEntity> beersList;

    public BeersAdapter(List<BeerEntity> beersList) {
        this.beersList = beersList;
    }

    @Override
    public int getItemCount() {
        return beersList.size();
    }

    /**
     * Holds the Views for the CardView which should be shown in RecyclerView.
     */
    public static class BeersViewHolder extends RecyclerView.ViewHolder {
        protected TextView vBrewery;
        protected TextView vBeerName;
        protected TextView vCountry;
        protected ImageView vImage;
        public View view;

        public BeersViewHolder(View view) {
            super(view);
            this.view = view;
            vBrewery = view.findViewById(R.id.txtBrewery);
            vBeerName = view.findViewById(R.id.txtBeerName);
            vCountry = view.findViewById(R.id.txtCountry);
            vImage = view.findViewById(R.id.txtImageView);

        }
    }

    /**
     * Set Content of CardView when it is bound to the RecyclerView.
     * @param beersViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull final BeersViewHolder beersViewHolder, int i) {
        final BeerEntity bi = beersList.get(i);
        beersViewHolder.vBrewery.setText(bi.getBrewery());
        beersViewHolder.vBeerName.setText(bi.getBeerName());
        beersViewHolder.vCountry.setText(bi.getCountry());

        // load image for beer
        try {
            beersViewHolder.vImage.setImageBitmap(StorageUtilities.loadImageFromStorage(bi.getImagePath()));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        // Sets Activity which should be launched when clicked on a CardView in the RecyclerView
        beersViewHolder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString(Utilities.getBeerIdKey(), String.valueOf(bi.getId()));
                Intent intent = new Intent(context, DetailBeerActivity.class);
                //Here the id of the beer is passed to the DetailBeerActivity
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public BeersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                        inflate(R.layout.list_item, viewGroup, false);

        return new BeersViewHolder(itemView);
    }

}
