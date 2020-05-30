package mx.itesm.hospitalcivil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EmergencyCallFragment extends Fragment {

    Button callButton;

    public static EmergencyCallFragment newInstance() {
        EmergencyCallFragment fragment = new EmergencyCallFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_emergency_call, container, false);
        callButton = vista.findViewById(R.id.callButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacer llamada de emergencia
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:3339424400"));
                startActivity(intent);

            }
        });
        return vista;
    }
}
