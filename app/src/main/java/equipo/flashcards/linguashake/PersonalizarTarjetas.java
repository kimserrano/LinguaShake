package equipo.flashcards.linguashake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PersonalizarTarjetas extends AppCompatActivity {

    EditText temaEditText, fraseEditText, respuestaEditText;
    Spinner temaSpinner;
    Button guardarButton, otroTema;

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizar_tarjetas);

        // agarrar los elementos de la vista
        temaSpinner = findViewById(R.id.tema_spinner);
        fraseEditText = findViewById(R.id.frase);
        respuestaEditText = findViewById(R.id.respuesta);
        guardarButton = findViewById(R.id.guardar_button);
        otroTema = findViewById(R.id.nuevo_Tema);

        databaseHelper = new DatabaseHelper(this);
        List<String> idiomas = databaseHelper.obtenerTemasIdiomas();
        // Crear un ArrayAdapter usando la lista de temas y un diseño predeterminado
        ArrayAdapter<String> temaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idiomas);

        // Especificar el diseño a utilizar cuando se despliegan las opciones
        temaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temaSpinner.setAdapter(temaAdapter);


        otroTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoNuevoTema();
            }
        });

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarFlashcard();
            }
        });
    }

    private void guardarFlashcard() {
        //los valores de los EditText
        String tema = temaSpinner.getSelectedItem().toString().trim();
        String frase = fraseEditText.getText().toString().trim();
        String respuesta = respuestaEditText.getText().toString().trim();

        // campos llenos
        if (tema.isEmpty() || frase.isEmpty() || respuesta.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // flashcard en la base de datos
        boolean insertado = databaseHelper.insertDataFlashcard(tema, frase, respuesta);

        if (insertado) {
            Toast.makeText(this, "Flashcard guardada exitosamente", Toast.LENGTH_SHORT).show();
            // Limpiar los EditText después de guardar
            temaEditText.getText().clear();
            fraseEditText.getText().clear();
            respuestaEditText.getText().clear();
        } else {
            Toast.makeText(this, "Error al guardar flashcard", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // cerrar la base de datos cuando la actividad es destruida
        databaseHelper.close();
        super.onDestroy();
    }

    private void mostrarDialogoNuevoTema() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Tema");

        // EditText para que el usuario ingrese el nuevo tema
        final EditText nuevoTemaEditText = new EditText(this);
        builder.setView(nuevoTemaEditText);

        // Botón "Aceptar" en el cuadro de diálogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoTema = nuevoTemaEditText.getText().toString().trim();
                if (!nuevoTema.isEmpty()) {
                    // Agregar el nuevo tema al Spinner
                    ((ArrayAdapter<String>) temaSpinner.getAdapter()).add(nuevoTema);
                    // Seleccionar el nuevo tema en el Spinner
                    temaSpinner.setSelection(((ArrayAdapter<String>) temaSpinner.getAdapter()).getPosition(nuevoTema));
                }
            }
        });

        // Botón "Cancelar" en el cuadro de diálogo
        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
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


        // Cambiar el texto del botón "Personalizar" según el idioma seleccionado
        if (idiomaSeleccionado.equals("English")) {
            otroTema.setText(R.string.temaTarjetas_en);
            fraseEditText.setText(R.string.preguntaTarjeta_en);
            respuestaEditText.setText(R.string.respuestaTarjeta_en);
            guardarButton.setText(R.string.guardar_en);

        } else if (idiomaSeleccionado.equals("Spanish")) {
            otroTema.setText(R.string.temaTarjetas_es);
            fraseEditText.setText(R.string.preguntaTarjeta_es);
            respuestaEditText.setText(R.string.respuestaTarjeta_es);
            guardarButton.setText(R.string.guardar_es);
        }
    }





}