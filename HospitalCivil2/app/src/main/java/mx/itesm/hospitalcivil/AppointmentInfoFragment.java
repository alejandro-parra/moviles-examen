package mx.itesm.hospitalcivil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AppointmentInfoFragment extends Fragment {
    private Button backButton,editAppointmentButton;
    private Appointment appointment;
    public static AppointmentInfoFragment newInstance() {
        return new AppointmentInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.appointment_info_fragment, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appointment = (Appointment) getArguments().getSerializable("appointment");
        setValues(view, appointment);
        backButton = view.findViewById(R.id.backListButton);
        editAppointmentButton = view.findViewById(R.id.editAppointmentButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragments(new AppointmentListFragment());
            }
        });
        editAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAppointmentFragment editAppointmentFragment = new EditAppointmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("father","appointmentInfo");
                bundle.putSerializable("appointment", appointment);
                editAppointmentFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragments(editAppointmentFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public void setValues(View v, Appointment ap){
        ((TextView)v.findViewById(R.id.appointmentID)).setText(ap.getPatientID());
        ((TextView)v.findViewById(R.id.appointmentName)).setText(ap.getName());
        ((TextView)v.findViewById(R.id.appointmentAge)).setText(""+ap.getAge());
        ((TextView)v.findViewById(R.id.appointmentGender)).setText(ap.getGender());
        String allergies = "";
        String[] arr = ap.getAllergies();
        if(arr.length == 0){
            allergies = "No allergies";
        }
        else {
            for (int i = 0; i < arr.length - 1; i++) {
                allergies += arr[i] + ", ";
            }
            allergies += arr[arr.length - 1];
        }
        ((TextView)v.findViewById(R.id.appointmentAllergies)).setText(allergies);
        ((TextView)v.findViewById(R.id.appointmentDesc)).setText(ap.getDescription());
    }
}
