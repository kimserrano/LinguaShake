package equipo.flashcards.linguashake;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionarIdiomaCuestionario extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_idioma_cuestionario);
        TextView facilInglesTextView = findViewById(R.id.facil_ingles_textview);
        TextView dificilInglesTextView = findViewById(R.id.dificil_ingles_textview);

        TextView facilChinoTextView = findViewById(R.id.facil_chino_textview);
        TextView dificilChinoTextView = findViewById(R.id.dificil_chino_textview);
        facilInglesTextView.setOnClickListener(view -> {
            startCuestionarioActivity("ingles", 1); // English language, difficulty level 1
        });

        // Set OnClickListener for Difficult English TextView
        dificilInglesTextView.setOnClickListener(view -> {
            startCuestionarioActivity("ingles", 2); // English language, difficulty level 2
        });

        // Set OnClickListener for Easy Chinese TextView
        facilChinoTextView.setOnClickListener(view -> {
            startCuestionarioActivity("chino", 1); // Chinese language, difficulty level 1
        });

        // Set OnClickListener for Difficult Chinese TextView
        dificilChinoTextView.setOnClickListener(view -> {
            startCuestionarioActivity("chino", 2); // Chinese language, difficulty level 2
        });


    }

    private void startCuestionarioActivity(String idioma, int dificultad) {
        Intent intent = new Intent(getApplicationContext(), Cuestionario.class);
        intent.putExtra("idioma", idioma);
        intent.putExtra("dificultad", dificultad);
        startActivity(intent);
    }
}