package ch.hslu.fs19.mobpro.sweetbeeromine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.data.DataRepository;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.StorageUtilities;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.Utilities;

/**
 * Class for the edit beer activity.
 */
public class EditBeerActivity extends AppCompatActivity {
    private String beerImagePath = "";
    private String beerId;
    private BeerEntity beerEntity;
    private DataRepository repository;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_beer);

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
            TextView brewery = findViewById(R.id.editView_edit_brewery);
            brewery.setText(beerEntity.getBrewery());
            TextView beerName = findViewById(R.id.editView_edit_beername);
            beerName.setText(beerEntity.getBeerName());
            TextView beerType = findViewById(R.id.editView_edit_beertype);
            beerType.setText(beerEntity.getBeerType());
            TextView beerCountry = findViewById(R.id.editView_edit_country);
            beerCountry.setText(beerEntity.getCountry());
            TextView beerNotes = findViewById(R.id.editView_edit_notes);
            beerNotes.setText(beerEntity.getNotes());
            ImageView beerImage = findViewById(R.id.editView_image);
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

        // image button on click listener
        Button editimagebutton = findViewById(R.id.editView_editImageButton);
        editimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        // cancel button on click listener
        Button cancelButton = findViewById(R.id.editView_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // save button on click listener
        Button saveButton = findViewById(R.id.editView_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBeer();
                finish();
            }
        });

        // takepicbutton on click listener
        Button takepicbutton = findViewById(R.id.editView_takePicButton);
        takepicbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });

        // Set up touch listener for non-text box views to hide keyboard.
        View view = findViewById(R.id.editView_constraint_layout);

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EditBeerActivity.this);
                    return false;
                }
            });
        }
    }

    // Show Delete button in DetailView on ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set action to delete entry when clicked on delete option in ActionBar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_delete:
                repository.deleteBeeer(beerEntity);
                // return to main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Saving Action to update the BeerEntity.
     */
    public void updateBeer() {
        // Get Strings from EditViews which should be saved in BeerEntity
        TextView brewery = findViewById(R.id.editView_edit_brewery);
        String breweryText = brewery.getText().toString();
        TextView beerName = findViewById(R.id.editView_edit_beername);
        String beerNameText = beerName.getText().toString();
        TextView beerType = findViewById(R.id.editView_edit_beertype);
        String beerTypeText = beerType.getText().toString();
        TextView beerCountry = findViewById(R.id.editView_edit_country);
        String beerCountryText = beerCountry.getText().toString();
        TextView beerNotes = findViewById(R.id.editView_edit_notes);
        String beerNotesText = beerNotes.getText().toString();

        // Set the new values to the BeerEntity
        beerEntity.setBrewery(breweryText);
        beerEntity.setBeerName(beerNameText);
        beerEntity.setBeerType(beerTypeText);
        beerEntity.setCountry(beerCountryText);
        beerEntity.setNotes(beerNotesText);
        beerEntity.setImagePath(!this.beerImagePath.equals("") ? this.beerImagePath : beerEntity.getImagePath());

        // Update BeerEntity
        repository.updateBeer(beerEntity);
    }

    // Function which starts the Activity to load an image taken by a Camera app
    public void getImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Function which starts the Activity to load an image from the gallery
    public void getImageFromAlbum() {
        try {
            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            startActivityForResult(imagePickerIntent, RESULT_LOAD_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while picking image", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the Results from either getting an image from the gallery or the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = findViewById(R.id.editView_image);

        // When an image from the Camera is received
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                // set size of bitmap
                if (bitmap.getWidth() > 800 && bitmap.getHeight() > 800) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/ 5, bitmap.getHeight() / 5, false);
                } else {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                }

                // round bitmap
                bitmap = getCircularBitmapFrom(bitmap);

                // set bitmap to ImageView of Activity
                imageView.setImageBitmap(bitmap);
                this.beerImagePath = StorageUtilities.saveToInternalStorage(getApplicationContext(), bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong while taking pic from camera", Toast.LENGTH_SHORT).show();
            }
        }
        // When an image from the Gallery is received
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                // set size of bitmap
                if (bitmap.getWidth() > 800 && bitmap.getHeight() > 800) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/ 5, bitmap.getHeight() / 5, false);
                } else {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                }

                // round bitmap
                bitmap = getCircularBitmapFrom(bitmap);

                // set bitmap to ImageView of Activity
                imageView.setImageBitmap(bitmap);
                this.beerImagePath = StorageUtilities.saveToInternalStorage(getApplicationContext(), bitmap);

            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error while trying to fetch image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts bitmap to circular bitmap.
     * @param bitmap bitmap to convert
     * @return circular bitmap
     */
    public static Bitmap getCircularBitmapFrom(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        float radius = bitmap.getWidth() > bitmap.getHeight() ? ((float) bitmap
                .getHeight()) / 2f : ((float) bitmap.getWidth()) / 2f;
        Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                radius, paint);

        return canvasBitmap;
    }

    /**
     * Hide software keyboard.
     * @param activity to apply
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
