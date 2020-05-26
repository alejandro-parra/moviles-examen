package mx.itesm.hospitalcivil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    private long splashTime = 3000L;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainActivity();
            }
        },splashTime);

    }

    private void goToMainActivity(){
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
