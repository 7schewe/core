package io.schewe.core.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ImageLoader {

    private final static String IMAGE_DIRECTORY = "";
    private final static String SUB_DIRECTORY = "locpics/";
    private final String JPEG_EXTENSION = ".jpg";
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void saveImageToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper cw = new ContextWrapper(this.context);
        File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, SUB_DIRECTORY + fileName + JPEG_EXTENSION);

        FileOutputStream fos = null;
        try {
            Objects.requireNonNull(mypath.getParentFile()).mkdirs();
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(fos).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap loadImageFromInternalStorage(String fileName) {
        Bitmap b = null;
        try {
            ContextWrapper cw = new ContextWrapper(this.context);
            File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
            File f = new File(directory, SUB_DIRECTORY + fileName + JPEG_EXTENSION);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public Bitmap loadImageFromAssets(String fileName) {
        Bitmap b = null;
        try(InputStream open = this.context.getAssets().open("locpics/" + fileName + JPEG_EXTENSION)) {
            b = BitmapFactory.decodeStream(open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void takePicture(View view, String title) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) bgDrawable.draw(canvas);
        else canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(view.getContext(), returnedBitmap));
        view.getContext().startActivity(Intent.createChooser(i, title));
    }

    private static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "", null);
        return Uri.parse(path);
    }

}
