package equipo.flashcards.linguashake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Cuestionario extends AppCompatActivity {
    private int respuestasCorrectas = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Button btnTarjeta = findViewById(R.id.guardar_button);
        Button btnSalir = findViewById(R.id.salir);
        Button btnReintento = findViewById(R.id.reintento);
        btnTarjeta.setOnClickListener(view ->{
            if (todasLasOpcionesSeleccionadas()) {
                // Calculamos las respuestas correctas antes de mostrar el resultado
                calcularRespuestasCorrectas();
                // Mostramos el resultado al usuario
                mostrarResultado();
            } else {
                // Mostrar un mensaje de error indicando que todas las opciones deben ser seleccionadas
                Toast.makeText(this, "Por favor, seleccione una opción en todas las preguntas", Toast.LENGTH_SHORT).show();
            }
        });
        btnSalir.setOnClickListener(view -> {
            // Volver a la pantalla principal
            finish();
        });

        btnReintento.setOnClickListener(view -> {
            // Deseleccionar todos los RadioButtons y restablecer el color
            deseleccionarRadioButtons();
        });
    }
    private void mostrarResultado() {
        String mensaje = "Respuestas correctas: " + respuestasCorrectas;
        respuestasCorrectas=0;
        mostrarAlerta(mensaje);
    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();

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
    private void calcularRespuestasCorrectas() {
        // Verificar respuestas correctas para la pregunta 1
        RadioButton radioButtonCorrectoPregunta1 = findViewById(R.id.opcion2_radio_button); // Segundo RadioButton
        verificarRespuestaCorrecta(radioButtonCorrectoPregunta1, R.id.opcion1_radio_button, R.id.opcion2_radio_button, R.id.opcion3_radio_button);

        // Verificar respuestas correctas para la pregunta 2
        RadioButton radioButtonCorrectoPregunta2 = findViewById(R.id.opcion2_radio_button2); // Primer RadioButton
        verificarRespuestaCorrecta(radioButtonCorrectoPregunta2, R.id.opcion1_radio_button2, R.id.opcion2_radio_button2, R.id.opcion3_radio_button2);

        // Verificar respuestas correctas para la pregunta 3
        RadioButton radioButtonCorrectoPregunta3 = findViewById(R.id.opcion2_radio_button3); // Tercer RadioButton
        verificarRespuestaCorrecta(radioButtonCorrectoPregunta3, R.id.opcion1_radio_button3, R.id.opcion2_radio_button3, R.id.opcion3_radio_button3);

        // Verificar respuestas correctas para la pregunta 4
        RadioButton radioButtonCorrectoPregunta4 = findViewById(R.id.opcion3_radio_button4); // Segundo RadioButton
        verificarRespuestaCorrecta(radioButtonCorrectoPregunta4, R.id.opcion1_radio_button4, R.id.opcion2_radio_button4, R.id.opcion3_radio_button4);

        // Verificar respuestas correctas para la pregunta 5
        RadioButton radioButtonCorrectoPregunta5 = findViewById(R.id.opcion2_radio_button5); // Tercer RadioButton
        verificarRespuestaCorrecta(radioButtonCorrectoPregunta5, R.id.opcion1_radio_button5, R.id.opcion2_radio_button5, R.id.opcion3_radio_button5);
    }
    private void verificarRespuestaCorrecta(RadioButton radioButtonCorrecto, int... radioButtonIds) {
        for (int id : radioButtonIds) {
            RadioButton radioButton = findViewById(id);
            if (radioButton.isChecked()) {
                if (radioButton == radioButtonCorrecto) {
                    respuestasCorrectas++; // Incrementa el contador si la respuesta es correcta
                    radioButton.setBackgroundColor(Color.GREEN); // Cambia el color de fondo a verde si es correcta
                } else {
                    radioButton.setBackgroundColor(Color.RED); // Cambia el color de fondo a rojo si es incorrecta
                }
                break;
            }
        }
    }
    private void deseleccionarRadioButtons() {
        deseleccionarGrupo(R.id.opciones_radio_group);
        deseleccionarGrupo(R.id.opciones_radio_group2);
        deseleccionarGrupo(R.id.opciones_radio_group3);
        deseleccionarGrupo(R.id.opciones_radio_group4);
        deseleccionarGrupo(R.id.opciones_radio_group5);
    }

    private void deseleccionarGrupo(int groupId) {
        RadioGroup radioGroup = findViewById(groupId);
        int totalRadioButtons = radioGroup.getChildCount();
        for (int i = 0; i < totalRadioButtons; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setChecked(false); // Deseleccionar RadioButton
            radioButton.setBackgroundColor(Color.TRANSPARENT); // Restablecer el color de fondo
        }
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