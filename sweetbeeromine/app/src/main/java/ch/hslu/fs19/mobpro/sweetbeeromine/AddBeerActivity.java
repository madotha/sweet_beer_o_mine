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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.hslu.fs19.mobpro.sweetbeeromine.db.data.DataRepository;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.entity.BeerEntity;
import ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities.StorageUtilities;

/**
 * Activity to add beers.
 */
public class AddBeerActivity extends AppCompatActivity {
    private String beerImagePath = "";
    private BeerEntity beerEntity;
    private DataRepository repository;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        // Initialize Repository
        this.repository = new DataRepository(getApplicationContext());

        // Set 'Edit' Button onClick Action
        Button editimagebutton = findViewById(R.id.addView_editImageButton);
        editimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        // Set 'Take Picture' Button onClick Action
        Button takepicbutton = findViewById(R.id.addView_takePicButton);
        takepicbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });

        // Set 'Save' Button onClick Action
        Button saveButton = findViewById(R.id.addView_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBeer();
                finish();
            }
        });

        // Set 'Cancel' Button onClick Action
        Button cancelButton = findViewById(R.id.addView_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onTouchListener for non-text box views to hide keyboard
        View view = findViewById(R.id.addView_constraint_layout);
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AddBeerActivity.this);
                    return false;
                }
            });
        }

    }

    /**
     * Saving method to create a BeerEntity.
     */
    public void saveBeer() {
        // Get Strings from EditViews which should be saved in BeerEntity
        TextView brewery = findViewById(R.id.addView_edit_brewery);
        String breweryText = brewery.getText().toString();
        TextView beerName = findViewById(R.id.addView_edit_beername);
        String beerNameText = beerName.getText().toString();
        TextView beerType = findViewById(R.id.addView_edit_beertype);
        String beerTypeText = beerType.getText().toString();
        TextView beerCountry = findViewById(R.id.addView_edit_country);
        String beerCountryText = beerCountry.getText().toString();
        TextView beerNotes = findViewById(R.id.addView_edit_notes);
        String beerNotesText = beerNotes.getText().toString();

        // Create the BeerEntity
        beerEntity = new BeerEntity(breweryText,beerNameText,beerTypeText,beerCountryText,beerNotesText);
        beerEntity.setImagePath(this.beerImagePath);
        // Add the BeerEntity to the Repository
        List<BeerEntity> newBeers = new ArrayList<>();
        newBeers.add(beerEntity);

        repository.insertBeers(newBeers);
    }

    /**
     * Function to start Camera Action.
     */
    public void getImageFromCamera() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }

    /**
     * Function to start Gallery Action.
     */
    public void getImageFromAlbum() {
        try {
            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            startActivityForResult(imagePickerIntent, RESULT_LOAD_IMAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while trying to fetch image", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Defines what happens if a picture from Camera or Gallery is received.
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = findViewById(R.id.addView_image);

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
