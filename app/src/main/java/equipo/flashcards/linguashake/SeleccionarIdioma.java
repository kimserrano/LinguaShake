package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SeleccionarIdioma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_idioma);

        // si da clic en ingles que se pase a esa pantalla
        Button btnTarjeta = findViewById(R.id.ingles_button);
        btnTarjeta.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), Tarjeta.class);
            startActivity(intent);
        });

        Button btnPersonalizar = findViewById(R.id.personalizar_button);
        btnPersonalizar.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), PersonalizarTarjetas.class);
            startActivity(intent);
        });
    }
}