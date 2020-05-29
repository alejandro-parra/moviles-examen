package mx.itesm.hospitalcivil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.emergencyCallFragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case(R.id.emergencyCallFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new EmergencyCallFragment()).commit();
                break;
            case(R.id.appointmentListFragment):
                AppointmentListFragment appointmentListFragment = new AppointmentListFragment();
                Bundle bundle = new Bundle();
                appointmentListFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,appointmentListFragment).commit();
                break;
            case(R.id.mapsFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new MapsFragment()).commit();
                break;
            case(R.id.myInfoFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new MyInfoFragment()).commit();
                break;
        }

        return true;
    }

    public void replaceFragments(Fragment fragmentClass){
        //Fragment fragment = null;
        /*try{
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e){
            e.printStackTrace();
        }*/

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentClass).commit();
    }
}
