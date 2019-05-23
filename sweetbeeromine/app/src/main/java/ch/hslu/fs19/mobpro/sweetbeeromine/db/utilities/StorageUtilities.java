package ch.hslu.fs19.mobpro.sweetbeeromine.db.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * This class is used to save and load images to the internal memory.
 */
public class StorageUtilities {

    /**
     * Save bitmap to internal storage.
     * @param bitmapImage
     * @return path of file
     */
    public static String saveToInternalStorage(Context context, Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, UUID.randomUUID().toString() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    /**
     * Load image from storage.
     * @param path of file
     * @return bitmap
     * @throws FileNotFoundException
     */
    public static Bitmap loadImageFromStorage(String path) throws FileNotFoundException {
            File f=new File(path);
            return  BitmapFactory.decodeStream(new FileInputStream(f));
    }
}
