package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

public class Tarjeta extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isShaking = false;
    private long lastTimeShakeDetected = 0;
    // umbral para agitar
    private static final float SHAKE_THRESHOLD = 10f;
    // tiempo que espera despues de agitar, 1 segundo
    private static final int SHAKE_TIMEOUT = 1000;
    private CardView cardView;
    private Button respuestaButton;
    private TextView fraseTextView;
    private TextView respuestaTextView;
    private boolean isShowingAnswer = false;

    private DatabaseHelper databaseHelper;
    private TarjetaObj tarjetaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);
        // se inicia el acelerometro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            // si no encuentra el sensor
            Toast.makeText(this, "No se ha encontrado el sensor de acelerómetro.", Toast.LENGTH_SHORT).show();
        }
        // obtenemos los elementos del xml
        cardView = findViewById(R.id.cardView);
        respuestaButton = findViewById(R.id.respuesta_button);
        fraseTextView = findViewById(R.id.frase_textview);
        respuestaTextView = findViewById(R.id.respuesta_textview);

        // inicializar la base de datos
        databaseHelper = new DatabaseHelper(this);

        mostrarNuevaTarjeta();

        // si da clic en el btn respuesta
        respuestaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se llama al metodo para voltear la tarjeta
                flipCard();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // enciende el listener del sensor cuando la actividad está en primer plano
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // detiene el listener del sensor cuando la actividad pasa a segundo plano
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // esto se llama cuando el sensor cambia
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // toma el tiempo de la sacudida
            long currentTime = System.currentTimeMillis();
            // verifica que el haya pasado tiempo desde la ultima sacudida
            if ((currentTime - lastTimeShakeDetected) > SHAKE_TIMEOUT) {
                // valores de aceleración en los ejes X, Y, Z
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // calcula la aceleración menos la gravedad
                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                // si la aceleración es mayor al umbral de sacudida
                if (acceleration > SHAKE_THRESHOLD) {
                    // marcar como sacudida
                    if (!isShaking) {
                        isShaking = true;
                        // voltear la tarjeta
                        flipCard();
                    }
                    // cambiar el tiempo de la ultima sacudida
                    lastTimeShakeDetected = currentTime;
                } else {
                    isShaking = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // aquí no se si vayamos a usarlo más adelante, pero investigué y es x si
        // el sensor cambia la precisión o algo así
    }

    // para voltear la tarjeta
    private void flipCard() {
        if (isShowingAnswer) {
            // voltearla hacia atrás para ocultar la respuesta
            flip(cardView, R.drawable.flip_backward);
        } else {
            // voltearla hacia adelante para mostrar la respuesta
            flip(cardView, R.drawable.flip_forward);
        }
        // cambiar el estado de que comprueba si se esta mostrando
        isShowingAnswer = !isShowingAnswer;
    }

    // este método es para la animación que debe tener el giro
    private void flip(View view, int animation) {
        // el view es la card a girar y el animation es el xml de la animación
        view.animate()
                // girar 90 grados en y
                .rotationY(90)
                // dura la primera mitad
                .setDuration(250)
                .withEndAction(() -> {
                    // otros 90 grados
                    view.setRotationY(-90);
                    view.animate()
                            .rotationY(0)
                            .setDuration(250)
                            .start();
                })
                .start();
    }

    private void mostrarNuevaTarjeta() {
        // una nueva tarjeta de la base de datos
        tarjetaActual = databaseHelper.obtenerProximaTarjeta();

        if (tarjetaActual != null) {
            // mostrar la frase en el textview
            fraseTextView.setText(tarjetaActual.getFrase());
            // reiniciar el estado de mostrar la respuesta
            isShowingAnswer = false;
        } else {
            // si no hay más tarjetas
            fraseTextView.setText("No hay más tarjetas.");
            respuestaTextView.setText("");
        }
    }

}
