package equipo.flashcards.linguashake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Cuestionario extends AppCompatActivity {
    private int respuestasCorrectas = 0;
    private int r1 = 0;
    private int r2 = 0;
    private int r3 = 0;
    private int r4 = 0;
    private int r5 = 0;
    private String idi= "";
    private int difi = 0;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Button btnTarjeta = findViewById(R.id.guardar_button);
        Button btnSalir = findViewById(R.id.salir);
        Button btnReintento = findViewById(R.id.reintento);
         idi = getIntent().getStringExtra("idioma");
         difi = getIntent().getIntExtra("dificultad",1);

        databaseHelper = new DatabaseHelper(this);
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
        crearCuestionario();
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
    private void crearCuestionario(){
        List<Pregunta> listaPreguntas= databaseHelper.obtenerPreguntasAleatorias(idi,difi);
        int vuelta=1;
        for (Pregunta a :listaPreguntas){
            if (vuelta==1){
                if(a!=null) {
                    TextView pregunta1 = findViewById(R.id.pregunta1);
                    pregunta1.setText(a.getTextoPregunta());
                    RadioButton op1 = findViewById(R.id.opcion1_radio_button);
                    op1.setText(a.getOpcion1());
                    RadioButton op2 = findViewById(R.id.opcion2_radio_button);
                    op2.setText(a.getOpcion2());
                    RadioButton op3 = findViewById(R.id.opcion3_radio_button);
                    op3.setText(a.getOpcion3());
                    r1 = a.getRespuesta();
                }
            }
            if (vuelta==2){
                if(a!=null) {
                    TextView pregunta2 = findViewById(R.id.pregunta2);
                    pregunta2.setText(a.getTextoPregunta());
                    RadioButton op1 = findViewById(R.id.opcion1_radio_button2);
                    op1.setText(a.getOpcion1());
                    RadioButton op2 = findViewById(R.id.opcion2_radio_button2);
                    op2.setText(a.getOpcion2());
                    RadioButton op3 = findViewById(R.id.opcion3_radio_button2);
                    op3.setText(a.getOpcion3());
                    r2 = a.getRespuesta();
                }
            }
            if (vuelta==3){
                if(a!=null) {
                    TextView pregunta3 = findViewById(R.id.pregunta3);
                    pregunta3.setText(a.getTextoPregunta());
                    RadioButton op1 = findViewById(R.id.opcion1_radio_button3);
                    op1.setText(a.getOpcion1());
                    RadioButton op2 = findViewById(R.id.opcion2_radio_button3);
                    op2.setText(a.getOpcion2());
                    RadioButton op3 = findViewById(R.id.opcion3_radio_button3);
                    op3.setText(a.getOpcion3());
                    r3 = a.getRespuesta();
                }
            }if (vuelta==4){
                if(a!=null){
                TextView pregunta4= findViewById(R.id.pregunta4);
                pregunta4.setText(a.getTextoPregunta());
                RadioButton op1 = findViewById(R.id.opcion1_radio_button4);
                op1.setText(a.getOpcion1());
                RadioButton op2 = findViewById(R.id.opcion2_radio_button4);
                op2.setText(a.getOpcion2());
                RadioButton op3 = findViewById(R.id.opcion3_radio_button4);
                op3.setText(a.getOpcion3());
                r4=a.getRespuesta();
                }
            }if (vuelta==5){
                TextView pregunta5= findViewById(R.id.pregunta5);
                if(a!=null){
                    pregunta5.setText(a.getTextoPregunta());
                    RadioButton op1 = findViewById(R.id.opcion1_radio_button5);
                    op1.setText(a.getOpcion1());
                    RadioButton op2 = findViewById(R.id.opcion2_radio_button5);
                    op2.setText(a.getOpcion2());
                    RadioButton op3 = findViewById(R.id.opcion3_radio_button5);
                    op3.setText(a.getOpcion3());
                    r5=a.getRespuesta();
                }

            }
            vuelta++;
        }
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
        verificarRespuestaCorrecta(r1, R.id.opcion1_radio_button, R.id.opcion2_radio_button, R.id.opcion3_radio_button);

        // Verificar respuestas correctas para la pregunta 2
        verificarRespuestaCorrecta(r2, R.id.opcion1_radio_button2, R.id.opcion2_radio_button2, R.id.opcion3_radio_button2);

        // Verificar respuestas correctas para la pregunta 3
        verificarRespuestaCorrecta(r3, R.id.opcion1_radio_button3, R.id.opcion2_radio_button3, R.id.opcion3_radio_button3);

        // Verificar respuestas correctas para la pregunta 4
        verificarRespuestaCorrecta(r4, R.id.opcion1_radio_button4, R.id.opcion2_radio_button4, R.id.opcion3_radio_button4);

        // Verificar respuestas correctas para la pregunta 5
        verificarRespuestaCorrecta(r5, R.id.opcion1_radio_button5, R.id.opcion2_radio_button5, R.id.opcion3_radio_button5);
    }
    private void verificarRespuestaCorrecta(int correcta, int... radioButtonIds) {
        int vuelta=1;
        for (int id : radioButtonIds) {
            RadioButton radioButton = findViewById(id);
            if (radioButton.isChecked()) {
                if (vuelta == correcta) {
                    respuestasCorrectas++; // Incrementa el contador si la respuesta es correcta
                    radioButton.setBackgroundColor(Color.GREEN); // Cambia el color de fondo a verde si es correcta
                } else {
                    radioButton.setBackgroundColor(Color.RED); // Cambia el color de fondo a rojo si es incorrecta
                }
                break;
            }
            vuelta++;
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