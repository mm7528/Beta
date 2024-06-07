package com.example.beta;

import static com.example.beta.FBDB.fbuser;
import static com.example.beta.FBDB.storageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The type Scan recipe.
 */
public class ScanRecipe extends AppCompatActivity {


    private Button takePic,recognizeText;
    private AlertDialog.Builder adb;
    private ImageView iv;
    private String warning="The recipe should be in the next format:\n" +
            "English letters only\n"+
            "Title(write however you want)\n" +
            "Ingredients:\n" +
            "1\n" +
            "2\n" +
            "3\n" +
            "Instructions:\n" +
            "1\n" +
            "2\n" +
            "3\n" +
            "please note that the recipe must include all the mentioned characters\n" +
            "such as the words 'Instructions' and 'Ingredients' with capital I's";
    private final int OPEN_CAMERA_CODE = 1234;
    private final int TAKE_A_PIC_CODE = 2345;
    private TextRecognizer textRecognizer;
    private String currentPath, lastFull;
    private Uri imageUri;
    private BroadcastReceiver broadcastReceiver;
    private byte[] bytes;
    private static int count=0;
    private StorageReference ref;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_recipe);
        takePic =(Button) findViewById(R.id.button11);
        recognizeText = (Button) findViewById(R.id.button10);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        broadcastReceiver=new InternetReceiver();
        adb=new AlertDialog.Builder(this);
        iv=(ImageView) findViewById(R.id.iv3);
        adb.setTitle("Attention");
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv.setText(warning);
        adb.setView(tv);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkCameraPermission();
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                {
                    count++;
                    adb.show();
                }
                else {
                    checkCameraPermission();
                }



            }


        });

        recognizeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(imageUri==null)
                {

                    Toast.makeText(getApplicationContext(), "Take a picture first.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    recognizeTextFromImage();
                }
            }

        });

    }


    /**
     * input:none.
     * output:none.
     * The function uses the google ml-kit to recognize the text in the picture and then
     * forwards the text of the recipe into an activity where the user can edit it
     */
    private void recognizeTextFromImage() {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String recognizedText = text.getText();
                            if(!recognizedText.isEmpty())
                            {
                                ref.putBytes(bytes)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(ScanRecipe.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                                            }

                                        });
                                Intent si=new Intent(ScanRecipe.this,AddRecipe.class);
                                //seperate the fields of the recipe!
                                si.putExtra("title",getSeparatedText(recognizedText).get(0));
                                si.putExtra("ingredients",getSeparatedText(recognizedText).get(1));
                                si.putExtra("instructions",getSeparatedText(recognizedText).get(2));
                                startActivity(si);

                            }
                            else {
                                Toast.makeText(ScanRecipe.this, "failed to recognize text", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed to recognize text" , Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "failed to recognize text " , Toast.LENGTH_LONG).show();
        }
    }


    /**
     * the function checks permissions and forwards to camera
     */
    private void checkCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(ScanRecipe.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ScanRecipe.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)

        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScanRecipe.this,
                    Manifest.permission.CAMERA) ||ActivityCompat.shouldShowRequestPermissionRationale(ScanRecipe.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // This is Case 4.

                ActivityCompat.requestPermissions(ScanRecipe.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_CAMERA_CODE);
            } else {
                // This is Case 3. Request for permission here
                ActivityCompat.requestPermissions(ScanRecipe.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_CAMERA_CODE);
            }

        }
        else {
            takeApic();
        }


    }

    /**
     * checks permissions by the requestCode
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            takeApic();
            // This is Case 2 (Permission is now granted)
        } else {
            // This is Case 1 again as Permission is not granted by user

            //Now further we check if used denied permanently or not
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScanRecipe.this,
                    Manifest.permission.CAMERA) ||ActivityCompat.shouldShowRequestPermissionRationale(ScanRecipe.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // case 4 User has denied permission but not permanently
                ActivityCompat.requestPermissions(ScanRecipe.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_CAMERA_CODE);

            } else {
                // case 5. Permission denied permanently.
                // You can open Permission setting's page from here now.

                Toast.makeText(this, "can't open camera, please check permissions", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * the function creates an internal file where the pic will be stored and
     * forwards the user to a function that opens the camera
     */
    private void takeApic() {

        // creating local temporary file to store the full resolution photo
        String filename = "tempfile";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imgFile = File.createTempFile(filename, ".jpg", storageDir);
            currentPath = imgFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(ScanRecipe.this, "com.example.beta.fileprovider", imgFile);
            Intent takePicIntent = new Intent();
            takePicIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (takePicIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicIntent, TAKE_A_PIC_CODE);
            }

        } catch (IOException e) {
            Toast.makeText(ScanRecipe.this, "Failed to create temporary file", Toast.LENGTH_LONG);
            throw new RuntimeException(e);
        }
    }


    /**
     * the functions opens the camera to take a picture
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TAKE_A_PIC_CODE&&resultCode==RESULT_OK)
        {

            uploadImage(data);
        }
    }

    /**
     * the function uploads the scanned recipe to Firebase Storage
     * @param data
     */
    private void uploadImage(Intent data)
    {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        lastFull = dateFormat.format(date);
        ref = storageReference.child(fbuser.getUid()+"/"+"scanned/"+lastFull+".jpg");
        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bytes = baos.toByteArray();
        iv.setImageBitmap(imageBitmap);


    }

    /**
     * the function separates the text of the recipe by format
     * @param text
     * @return
     */
    private List<String> getSeparatedText(String text)
    {
        List<String> recipe = new ArrayList<>();
        String s = "Ingredients";
        int place =text.indexOf(s);
        String substring = text.substring(0,place-1);
        recipe.add(substring);
        int lastPlace = place+s.length();
        s="Instructions";
        place = text.indexOf(s);
        substring = text.substring(lastPlace+1,place);
        recipe.add(substring);
        substring =text.substring(place+13,text.length()-1);
        recipe.add(substring);
        return recipe;
    }

    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}

