package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;


public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // si da clic en tarjeta que se pase a esa pantalla
        Button btnTarjeta = findViewById(R.id.tarjetas_button);
        btnTarjeta.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), SeleccionarIdioma.class);
            startActivity(intent);

        });
        Button btnCuestionario = findViewById(R.id.cuestionarios_button);
        btnCuestionario.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), SeleccionarIdiomaCuestionario.class);
            startActivity(intent);
        });

        // Botón para abrir el config de idioma
        Button btnConfig = findViewById(R.id.config_button);
        btnConfig.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTextoSegunIdioma();
    }

    private void cargarTextoSegunIdioma() {
        // Obtener las preferencias de idioma guardadas
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String idiomaSeleccionado = sharedPreferences.getString("selected_language", "default");

        // Obtener el texto del idioma seleccionado en el HEader
        String textoPregunta;
        if (idiomaSeleccionado.equals("English")) {
            textoPregunta = getString(R.string.menuprincipal_header_en);

        } else {
            textoPregunta = getString(R.string.menuprincipal_header_es);
        }

        // Obtener el texto del idioma seleccionado para "Con tarjetas"
        String textoTarjetas;
        if (idiomaSeleccionado.equals("English")) {
            textoTarjetas = getString(R.string.menuprincipal_tarjetas_en);
        } else {
            textoTarjetas = getString(R.string.menuprincipal_tarjetas_es);
        }

        // Obtener el texto del idioma seleccionado para "Con cuestionarios"
        String textoCuestionarios;
        if (idiomaSeleccionado.equals("English")) {
            textoCuestionarios = getString(R.string.menuprincipal_cuestionarios_en);
        } else {
            textoCuestionarios = getString(R.string.menuprincipal_cuestionarios_es);
        }

        // Establecer el texto en los TextView correspondientes
        TextView textViewPregunta = findViewById(R.id.pregunta);
        TextView textViewTarjetas = findViewById(R.id.tarjetas_button);
        TextView textViewCuestionarios = findViewById(R.id.cuestionarios_button);

        textViewPregunta.setText(textoPregunta);
        textViewTarjetas.setText(textoTarjetas);
        textViewCuestionarios.setText(textoCuestionarios);
    }


}
