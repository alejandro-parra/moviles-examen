package mx.itesm.hospitalcivil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private LinkedList<MyFragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        if(savedInstanceState == null) {
            fragments = new LinkedList<>();
            bottomNavigationView.setSelectedItemId(R.id.emergencyCallFragment);
        } else {
            fragments = (LinkedList<MyFragment>) savedInstanceState.getSerializable("fragments");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragments = new LinkedList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        switch (item.getItemId()){
            case(R.id.emergencyCallFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new EmergencyCallFragment()).commit();
                break;
            case(R.id.appointmentListFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new AppointmentListFragment()).commit();
                break;
            case(R.id.mapsFragment):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new MapsFragment()).commit();
                break;
        }

        return true;
    }

    private void replaceFragments(Fragment fragmentClass){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                fragmentClass).commit();
    }
    public void replaceFragments(Fragment fragmentClass, String oldFragment, Object oldBundle){
        fragments.add(new MyFragment(oldFragment, oldBundle));
        System.out.println(fragments.toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                fragmentClass).commit();
    }
    public void replaceFragmentsAndReset(Fragment fragmentClass){
        fragments = new LinkedList<>();
        replaceFragments(fragmentClass);
    }

    // Go back button
    public boolean goBack(){
        if(fragments.size() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            MyFragment last = fragments.removeLast();
            if (last.name.equals("list")) {
                replaceFragments(new AppointmentListFragment());
            } else if (last.name.equals("info")) {
                AppointmentInfoFragment fragment = new AppointmentInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("appointment", (Appointment) last.bundle);
                fragment.setArguments(bundle);
                replaceFragments(fragment);
            } else if (last.name.equals("camera")) {
                replaceFragments(new CameraActivity());
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        goBack();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("fragments", fragments);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(!goBack()){
            super.onBackPressed();
        }
    }
}
class MyFragment {
    public String name;
    public Object bundle;
    public MyFragment(String name, Object bundle){
        this.name = name;
        this.bundle = bundle;
    }
}
