package com.example.beta;

import static com.example.beta.LoginActivity.fbuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
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
    private String warning="The recipe should be in the next format:\n" +
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
            "such as ':' and the words 'Instructions' and 'Ingredients' with capital I's";
    private final int OPEN_CAMERA_CODE = 1234;
    private final int TAKE_A_PIC_CODE = 2345;
    private TextRecognizer textRecognizer;
    private FirebaseStorage storage;
    private String currentPath, lastFull;
    private Uri imageUri;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_recipe);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        takePic =(Button) findViewById(R.id.button11);
        recognizeText = (Button) findViewById(R.id.button10);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        adb=new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Attention!");
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
                adb.show();


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
     * output:none. The function uses the google ml-kit to recognize the text in the picture and then
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
                            Intent si=new Intent(ScanRecipe.this,AddRecipe.class);
                            //seperate the fields of the recipe!
                            si.putExtra("title",getSeparatedText(recognizedText).get(0));
                            si.putExtra("ingredients",getSeparatedText(recognizedText).get(1));
                            si.putExtra("instructions",getSeparatedText(recognizedText).get(2));
                            startActivity(si);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed recognizing text due to:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed preparing image due to: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void checkCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(ScanRecipe.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(ScanRecipe.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED)
        {
                ActivityCompat.requestPermissions(ScanRecipe.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_CAMERA_CODE);
        }
        else {
                takeApic();
            }

    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==OPEN_CAMERA_CODE&&grantResults[1]== PackageManager.PERMISSION_GRANTED)
        {
            takeApic();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TAKE_A_PIC_CODE&&resultCode==RESULT_OK)
        {

            uploadImage(data);
        }
    }

    private void uploadImage(Intent data)
    {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        lastFull = dateFormat.format(date);
        StorageReference ref = storageReference.child(fbuser.getUid()+"/"+"scanned/"+lastFull+".jpg");
        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        ref.putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ScanRecipe.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ScanRecipe.this, "Upload failed", Toast.LENGTH_LONG).show();
                    }
                });

    }
    
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
}

