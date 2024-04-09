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
import android.widget.Toast;
import androidx.cardview.widget.CardView;

public class Tarjeta extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isShaking = false;
    private long lastTimeShakeDetected = 0;
    //umbral para agitar
    private static final float SHAKE_THRESHOLD = 10f;
    //tiempo que espera despues de agitar, 1 segundo
    private static final int SHAKE_TIMEOUT = 1000;
    private CardView cardView;
    private Button respuestaButton;
    private boolean isShowingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);
        //se inicia el acelerometro
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

    //esto se llama cuando el sensor cambia
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //toma el tiempo de la sacudida
            long currentTime = System.currentTimeMillis();
            // verifica que el haya pasado tiempo desde la ultima sacudida
            if ((currentTime - lastTimeShakeDetected) > SHAKE_TIMEOUT) {
                // valores de aceleración en los ejes X, Y, Z
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // calculas la aceleracion menos la gravedad
                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
               // si la aceleracion es mayor al umbral de sacudida
                if (acceleration > SHAKE_THRESHOLD) {
                    // marcar como sacudida
                    if (!isShaking) {
                        isShaking = true;
                        // volteas la tarjeta
                        flipCard();
                    }
                    // cambias el tiempo de la ultima sacudida
                    lastTimeShakeDetected = currentTime;
                } else {
                    isShaking = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
       // aqui no se si vayamos a usarlo mas adelante, pero investigue y es x si
        // el sensor cambia la precision o algo asi
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

    // este metodo es para la animacion que debe tener el giro
    private void flip(View view, int animation) {
        // el view es la card a girar y el animation es el xml de la animacion
        view.animate()
                // girar 90 grds en y
                .rotationY(90)
                // dura la primera mitad
                .setDuration(250)
                .withEndAction(() -> {
                    // otros 90 grds
                    view.setRotationY(-90);
                    view.animate()
                            .rotationY(0)
                            .setDuration(250)
                            .start();
                })
                .start();
    }
}
