package equipo.flashcards.linguashake;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionarIdiomaCuestionario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_idioma_cuestionario);

        Button btnTarjeta = findViewById(R.id.ingles_button);
        btnTarjeta.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), Cuestionario.class);
            startActivity(intent);
        });


    }
}