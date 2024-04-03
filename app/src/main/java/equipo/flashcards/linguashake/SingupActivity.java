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
            public void onClick(View v) {
                String email = binding.singupEmail.getText().toString();
                String password = binding.singupPassword.getText().toString();
                String confirmPassword = binding.singupConfirm.getText().toString();

                if(email.equals("")||password.equals("")||confirmPassword.equals(""))
                    Toast.makeText(SingupActivity.this, "All files are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    if(password.equals(confirmPassword)){
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if(checkUserEmail==false){
                            Boolean insert  = databaseHelper.insertData(email, password);

                            if ( insert == true){
                                Toast.makeText(SingupActivity.this, "Signup Succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SingupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SingupActivity.this, "User already exists, please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SingupActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}