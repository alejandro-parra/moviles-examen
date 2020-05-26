package mx.itesm.hospitalcivil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString() != "" && passwordEditText.getText().toString() != "") {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("id",usernameEditText.getText().toString());
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),"You should enter a name/password!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
