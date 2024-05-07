package equipo.flashcards.linguashake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.text.InputType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // Listener para el botón de mostrar/ocultar contraseña
        binding.showPasswordButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Cambiar la visibilidad de la contraseña y la confirmación según el estado del botón
                int passwordInputType;
                if (isChecked) {
                    // Mostrar la contraseña
                    passwordInputType = InputType.TYPE_CLASS_TEXT;
                } else {
                    // Ocultar la contraseña
                    passwordInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                binding.singupPassword.setInputType(passwordInputType);
                binding.singupConfirm.setInputType(passwordInputType);
            }
        });


        binding.singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // cuando se hace clic al btnregistro
            public void onClick(View v) {
                // obtenemos los valores que escribio el usuario
                String email = binding.singupEmail.getText().toString();
                String password = binding.singupPassword.getText().toString();
                String confirmPassword = binding.singupConfirm.getText().toString();

                if (!isValidEmail(email)) {
                    dialogoError("Formato de correo inválido");
                    return;
                }

                if (password.length() < 8) {
                    dialogoError("La contraseña debe tener al menos 8 caracteres.");
                    return;
                }

                String missingRequirements = getMissingRequirements(password);
                if (!missingRequirements.isEmpty()) {
                    dialogoError("La contraseña falta: " + missingRequirements);
                    return;
                }




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

    // Método para validar el formato de correo electrónico
    private boolean isValidEmail(String email) {
        String emailPattern =
                "[a-zA-Z0-9._-]+" + //Con esto pide lo normal: letrás mayus y minus, guión bajo...
                "@[a-z]+" + //con esto checa que haya una "@" y que después de esta haya una letra minusc
                "\\.+" +    //después debe haber almenos un punto
                "[a-z]+";   //Finalmente, caracteres para terminar el correo...
        return email.matches(emailPattern);
    }



    // Método para validar la complejidad de la contraseña
    private String getMissingRequirements(String password) {
        StringBuilder missingRequirements = new StringBuilder();

        if (!password.matches(".*[0-9].*")) {
            missingRequirements.append("al menos un número, ");
        }

        if (!password.matches(".*[a-z].*")) {
            missingRequirements.append("al menos una letra minúscula, ");
        }

        if (!password.matches(".*[A-Z].*")) {
            missingRequirements.append("al menos una letra mayúscula, ");
        }

        if (!password.matches(".*[@!#$%^&+=()*].*")) {
            missingRequirements.append("al menos un carácter especial, ");
        }

        if (password.contains(" ")) {
            missingRequirements.append("sin espacios en blanco, ");
        }

        // Elimina la coma y el espacio al final del mensaje
        if (missingRequirements.length() > 0) {
            missingRequirements.deleteCharAt(missingRequirements.length() - 1);
            missingRequirements.deleteCharAt(missingRequirements.length() - 1);
        }

        return missingRequirements.toString();
    }

    private void dialogoError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);

        TextView errorMessageTextView = dialogView.findViewById(R.id.errorMessageTextView);
        errorMessageTextView.setText(errorMessage);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



}