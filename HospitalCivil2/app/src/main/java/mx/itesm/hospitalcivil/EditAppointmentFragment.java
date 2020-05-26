package mx.itesm.hospitalcivil;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditAppointmentFragment extends Fragment {
    Button backFromEditButton;


    public static EditAppointmentFragment newInstance() {
        return new EditAppointmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.edit_appointment_fragment, container, false);
        backFromEditButton = vista.findViewById(R.id.backFromEditButton);
        backFromEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getString("father") == "appointmentInfo"){
                    ((MainActivity)getActivity()).replaceFragments(new AppointmentInfoFragment());
                } else if (getArguments().getString("father") == "selectMember") {
                    ((MainActivity)getActivity()).replaceFragments(new SelectMemberFragment());
                }
            }
        });
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
