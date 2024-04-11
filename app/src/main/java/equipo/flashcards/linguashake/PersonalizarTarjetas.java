package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalizarTarjetas extends AppCompatActivity {

    EditText temaEditText, fraseEditText, respuestaEditText;
    Button guardarButton;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizar_tarjetas);

        // agarrar los elementos de la vista
        temaEditText = findViewById(R.id.tema);
        fraseEditText = findViewById(R.id.frase);
        respuestaEditText = findViewById(R.id.respuesta);
        guardarButton = findViewById(R.id.guardar_button);

        databaseHelper = new DatabaseHelper(this);

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarFlashcard();
            }
        });
    }

    private void guardarFlashcard() {
        //los valores de los EditText
        String tema = temaEditText.getText().toString().trim();
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
}
