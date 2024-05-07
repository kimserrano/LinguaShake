package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;


import java.util.List;

public class SeleccionarIdioma extends AppCompatActivity {
    LinearLayout botonesLayout;
    TextView preguntaTextView;
    ImageView loginLogoImageView;
    Button inglesButton, personalizarButton;

    String temaSeleccionado;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_idioma);
        //agarrar elementos de la vista
        botonesLayout = findViewById(R.id.botones_layout);
        preguntaTextView = findViewById(R.id.pregunta);
        loginLogoImageView = findViewById(R.id.login_logo);
        personalizarButton = findViewById(R.id.personalizar_button);

        databaseHelper = new DatabaseHelper(this);


        personalizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalizarTarjetas.class);
                startActivity(intent);
            }
        });

        // mostrar los botones al crear la actividad
        mostrarBotones();
    }


    //para mostrar los botones en el LinearLayout
    private void mostrarBotones() {
        // Obtener las preferencias de idioma guardadas
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String idiomaSeleccionado = sharedPreferences.getString("selected_language", "default");

        // lista actualizada de temas o idiomas
        List<String> temasIdiomas = databaseHelper.obtenerTemasIdiomas();

        // limpiar botónes previos
        botonesLayout.removeAllViews();


        // agregar botones para cada tema o idioma en la lista
        for (String tema : temasIdiomas) {
            Button button = new Button(this);
            // Establecer los parámetros de diseño
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.margin_top), 0, 0); // 10dp de margen superior
            button.setLayoutParams(params);

            button.setText(tema);
            button.setTextSize(18);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setAllCaps(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button.setTextAppearance(R.style.ButtonTheme_RosaBajito);
            } else {
                button.setTextAppearance(this, R.style.ButtonTheme_RosaBajito);
            }



            // Establecer el radio de esquina (cornerRadius)
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadii(new float[] {30, 30, 30, 30, 30, 30, 30, 30}); // 30dp para todos los bordes
            drawable.setColor(getResources().getColor(R.color.rosaBajito)); // Color de fondo

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackground(drawable);
            } else {
                button.setBackgroundDrawable(drawable);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temaSeleccionado = ((Button) v).getText().toString();
                    Intent intent = new Intent(getApplicationContext(), Tarjeta.class);
                    intent.putExtra("tema", temaSeleccionado);
                    startActivity(intent);
                }
            });
            botonesLayout.addView(button);
        }

        // Cambiar el texto del botón "Personalizar" según el idioma seleccionado
        if (idiomaSeleccionado.equals("English")) {
            personalizarButton.setText(R.string.botonAprender_en);
        } else if (idiomaSeleccionado.equals("Spanish")) {
            personalizarButton.setText(R.string.botonAprender_es);
        }
    }





    @Override
    protected void onDestroy() {
        // cerrar la base de datos cuando la actividad es destruida
        databaseHelper.close();
        super.onDestroy();
    }
}