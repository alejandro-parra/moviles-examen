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
    Button backButton,editAppointmentButton;
    TextView descriptionText,idText,nameText, ageText, genderText,dateText,allergiesText;
    Appointment appointment;
    public static AppointmentInfoFragment newInstance() {
        return new AppointmentInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.appointment_info_fragment, container, false);
        appointment = (Appointment) getArguments().getSerializable("appointment");
        backButton = vista.findViewById(R.id.backListButton);
        editAppointmentButton = vista.findViewById(R.id.editAppointmentButton);
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
                editAppointmentFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragments(editAppointmentFragment);
            }
        });
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
