package jetsetapp.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Save {

    private static Context TheThis;

    private static String NameOfOverwrittenFile = "Overwritten";
    private static String NameOfFolder = "/KidsPaint";
    private static String NameOfFile = "KidsPaint";
    private static String file_path = "/data/user/0/jetsetapp.paint/app_imageDir";
//            Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;

    public static String getNameOfFolder() {
        return NameOfFolder;
    }

    public static String getFile_path() {
        return file_path;
    }

    public static String getNameOfOverwrittenFile() {
        return NameOfOverwrittenFile;
    }

    private static void UnableToSave() {

        Toast.makeText(TheThis, "Picture failed to save", Toast.LENGTH_SHORT).

                show();
    }

    private static void AbleToSave() {

        Toast.makeText(TheThis, "Picture saved to Gallery", Toast.LENGTH_SHORT).
                show();
    }

//    private static void AbleToSaveToInternal(String FileName) {
//
//        Toast.makeText(TheThis, " Picture saved ", Toast.LENGTH_SHORT).
//                show();
//    }

    private static void UnableToSaveIO() {

        Toast.makeText(TheThis, "Picture failed to save - IO exception", Toast.LENGTH_SHORT).

                show();
    }

    public void SaveImage(Context context, Bitmap ImageToSave) {
        TheThis = context;

        String currentDateAndTime = getCurrentDateAndTime();

        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, NameOfFile + currentDateAndTime + ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            ImageToSave.recycle();
            //addImageToGallery(file.getAbsolutePath(), context);
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        } catch (FileNotFoundException e) {
            UnableToSave();
        } catch (IOException e) {
            UnableToSaveIO();
        }
    }

    public void writeFileOnInternalStorage(Context mcoContext, Bitmap ImageToSave, String FileName) {
        Log.i("filename", FileName);
        //scale image first , otherwise FloodFill not work
        Bitmap ImageToSave2 = Bitmap.createScaledBitmap(ImageToSave, MainActivity.getNewBitmap().getWidth(),
                MainActivity.getNewBitmap().getHeight(), false);

        //check if the overwritten file already exists
        if (!FileName.contains(NameOfOverwrittenFile)) {
            FileName = NameOfOverwrittenFile + FileName;
        }
        TheThis = mcoContext;

        File dir = mcoContext.getDir("imageDir", Context.MODE_PRIVATE);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }

        FileOutputStream fOut = null;
        try {
            File file = new File(dir, FileName + ".png");
//            FileWriter writer = new FileWriter(file);
//            writer.append(sBody);
//            writer.flush();
//            writer.close();
            fOut = new FileOutputStream(file);
            ImageToSave2.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        } catch (FileNotFoundException e) {
            UnableToSave();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
                if (fOut != null) {
                    fOut.flush();
                }
//                AbleToSaveToInternal(FileName);
//                Log.i("path", dir.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageToSave2.recycle();
//            MakeSureFileWasCreatedThenMakeAvabile(file);
//            AbleToSaveToInternal(FileName);
        }

    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return df.format(c.getTime());
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("Internal Storage","Scanned " + path + ":");

                    }

                });

    }
}