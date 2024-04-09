package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import equipo.flashcards.linguashake.databinding.ActivitySingupBinding;

public class SingupActivity extends AppCompatActivity {
    ActivitySingupBinding binding;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        binding.singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // cuando se hace clic al btnregistro
            public void onClick(View v) {
                // obtenemos los valores que escribio el usuario
                String email = binding.singupEmail.getText().toString();
                String password = binding.singupPassword.getText().toString();
                String confirmPassword = binding.singupConfirm.getText().toString();

                //verificar campos vacios
                if(email.equals("")||password.equals("")||confirmPassword.equals(""))
                    Toast.makeText(SingupActivity.this, "All files are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    //veriicar si las contras son iguales
                    if(password.equals(confirmPassword)){
                        //verificar si el user ya esta en la base
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if(checkUserEmail==false){
                            // si no esta se agrega a la base
                            Boolean insert  = databaseHelper.insertData(email, password);

                            if ( insert == true){
                                // si sale bn se muestra un mensaje y se manda al login
                                Toast.makeText(SingupActivity.this, "Signup Succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                // si falla muestra un mensaje
                                Toast.makeText(SingupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // si el user ya existe le pide q inicie sesion
                            Toast.makeText(SingupActivity.this, "User already exists, please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // si las contras no coinciden le muestra un mensaje
                        Toast.makeText(SingupActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // x si quiere volver al login
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}