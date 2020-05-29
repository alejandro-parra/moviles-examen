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
import android.widget.EditText;

public class EditAppointmentFragment extends Fragment {
    Button backFromEditButton;


    public static EditAppointmentFragment newInstance() {
        return new EditAppointmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.edit_appointment_fragment, container, false);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText description = view.findViewById(R.id.descriptionEditText);
        backFromEditButton = view.findViewById(R.id.backFromEditButton);
        backFromEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().containsKey("father") && getArguments().getString("father").equals("appointmentInfo")){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("appointment",getArguments().getSerializable("appointment"));
                    AppointmentInfoFragment appointmentInfoFragment = new AppointmentInfoFragment();
                    appointmentInfoFragment.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragments(appointmentInfoFragment);
                } else if (getArguments().containsKey("scan")) {
                    ((MainActivity)getActivity()).replaceFragments(new CameraActivity());
                }
            }
        });
        if(getArguments().containsKey("scan")){
            description.setText("Creating");
        } else{
            description.setText("Editing");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
