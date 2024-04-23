package equipo.flashcards.linguashake;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Cuestionario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Button btnTarjeta = findViewById(R.id.guardar_button);
        btnTarjeta.setOnClickListener(view ->{
            if (todasLasOpcionesSeleccionadas()) {
                 Toast.makeText(this, "Cuestionario Terminado exitosamente", Toast.LENGTH_SHORT).show();
                 regresarPantallaInicio();
            } else {
                 Toast.makeText(this, "Por favor, seleccione una opción en todas las preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void regresarPantallaInicio() {
        Intent intent = new Intent(this, MenuPrincipal.class); // Reemplaza "PantallaInicio" con el nombre de tu actividad de inicio
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    private boolean todasLasOpcionesSeleccionadas() {
        RadioGroup opcionesGrupo1 = findViewById(R.id.opciones_radio_group);
        RadioGroup opcionesGrupo2 = findViewById(R.id.opciones_radio_group2);
        RadioGroup opcionesGrupo3 = findViewById(R.id.opciones_radio_group3);
        RadioGroup opcionesGrupo4 = findViewById(R.id.opciones_radio_group4);
        RadioGroup opcionesGrupo5 = findViewById(R.id.opciones_radio_group5);

        return opcionSeleccionadaEnGrupo(opcionesGrupo1) &&
                opcionSeleccionadaEnGrupo(opcionesGrupo2) &&
                opcionSeleccionadaEnGrupo(opcionesGrupo3) &&
                opcionSeleccionadaEnGrupo(opcionesGrupo4) &&
                opcionSeleccionadaEnGrupo(opcionesGrupo5);
    }
    private boolean opcionSeleccionadaEnGrupo(RadioGroup radioGroup) {
        int totalRadioButtons = radioGroup.getChildCount();
        for (int i = 0; i < totalRadioButtons; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.isChecked()) {
                return true;
            }
        }
        return false;
    }
}