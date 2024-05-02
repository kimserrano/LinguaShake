package equipo.flashcards.linguashake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast; // Importar Toast
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    private static final String PREF_SELECTED_LANGUAGE = "selected_language";
    private RadioGroup radioGroupLanguages;
    private RadioButton radioButtonEnglish, radioButtonSpanish;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        radioGroupLanguages = findViewById(R.id.radioGroupLanguages);
        radioButtonEnglish = findViewById(R.id.radioButtonEnglish);
        radioButtonSpanish = findViewById(R.id.radioButtonSpanish);
        buttonSave = findViewById(R.id.buttonSave);

        // Configurar el listener para el botón "Guardar cambios"
        buttonSave.setOnClickListener(view -> {
            // Obtener el SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Obtener el ID del RadioButton seleccionado
            int selectedRadioButtonId = radioGroupLanguages.getCheckedRadioButtonId();

            // Guardar la selección del idioma
            if (selectedRadioButtonId == R.id.radioButtonEnglish) {
                editor.putString(PREF_SELECTED_LANGUAGE, "English");
                Log.d("ConfigActivity", "Se guardó el idioma: English");
                showToast("Idioma guardado: English"); // Mostrar un Toast
            } else if (selectedRadioButtonId == R.id.radioButtonSpanish) {
                editor.putString(PREF_SELECTED_LANGUAGE, "Spanish");
                Log.d("ConfigActivity", "Se guardó el idioma: Spanish");
                showToast("Idioma guardado: Spanish"); // Mostrar un Toast
            }
            editor.apply();
        });

        // Verificar si hay una selección previa y establecer el estado de los radio buttons en consecuencia
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString(PREF_SELECTED_LANGUAGE, null);
        if (selectedLanguage != null) {
            if (selectedLanguage.equals("English")) {
                radioButtonEnglish.setChecked(true);
                Log.d("ConfigActivity", "Se cargó el idioma previamente guardado: English");
            } else if (selectedLanguage.equals("Spanish")) {
                radioButtonSpanish.setChecked(true);
                Log.d("ConfigActivity", "Se cargó el idioma previamente guardado: Spanish");
            }
        }
    }

    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

