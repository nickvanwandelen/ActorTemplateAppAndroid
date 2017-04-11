package task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Nick on 10-4-2017.
 */

public class ImageHelper {
    public String convertImageToPhotoString(BitmapDrawable drawable){
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        String image = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return image;
    }

    public Bitmap convertPhotoStringToImage(String pictureString){
        String transformedPictureString = pictureString.replace("data:image/jpeg;base64,", "");
        Log.e("STRING:", "something" + pictureString);

        byte[] byteArray = Base64.decode(transformedPictureString, Base64.DEFAULT);
        Log.e("COUNT:" , "Length of byte array: " + byteArray.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
}