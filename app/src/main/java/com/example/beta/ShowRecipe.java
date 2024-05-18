package com.example.beta;

import static com.example.beta.AddRecipe.refRecipes;
import static com.example.beta.LoginActivity.fbDB;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class ShowRecipe extends AppCompatActivity {
    private TextView instructions,ingredients,title;
    private TextToSpeech textToSpeech;
    private Button btn;

    @SuppressLint("MissingInflatedId") // dan was here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        refRecipes=fbDB.getReference("Recipes");
        instructions=(TextView) findViewById(R.id.textView6);
        ingredients =(TextView) findViewById(R.id.textView7);
        title = (TextView) findViewById(R.id.textView8);
        btn =(Button)findViewById(R.id.read);
        /*refRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                title.setText(recipe.getTitle());
                ingredients.setText(lstToStr(recipe.getIngredients()));
                instructions.setText(lstToStr(recipe.getInstructions()));
                System.out.println(recipe);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowRecipe.this, "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }

        });*/

       /* textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "please enter text", Toast.LENGTH_LONG).show();
                }
                else
                {
                    textToSpeech.speak(title.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);
                    textToSpeech.speak(ingredients.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);
                    textToSpeech.speak(instructions.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
        });*/
    }

    private String lstToStr(List<String> lst)
    {
        String str="";
        for(int i=0;i<lst.size();i++)
        {
            str+=lst.get(i);
        }
        return str;
    }

}