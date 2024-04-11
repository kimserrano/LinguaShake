package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    }
}