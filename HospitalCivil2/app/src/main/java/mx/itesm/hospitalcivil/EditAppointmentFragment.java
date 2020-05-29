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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAppointmentFragment extends Fragment {
    private Button backFromEditButton, saveButton;
    private TextView status;
    private EditText description;

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        description = view.findViewById(R.id.descriptionEditText);
        backFromEditButton = view.findViewById(R.id.backFromEditButton);
        status = view.findViewById(R.id.appointmentStatus);
        saveButton = view.findViewById(R.id.saveAppointment);

        // Save info
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().containsKey("scan")){
                    String[] scan = getArguments().getString("scan").split("/");
                    // format DD MM YYY
                    String[] date = scan[2].split(" ");
                    int day = Integer.parseInt(date[0]);
                    int month = Integer.parseInt(date[1]);
                    int year = Integer.parseInt(date[2]);
                    Date birthDate = new Date(year - 1900, month, day);
                    List<String> allergies = Arrays.asList(scan[4].split(","));
                    Map<String, Object> appointment = new HashMap<>();
                    appointment.put("id", scan[0]);
                    appointment.put("patientName", scan[1]);
                    appointment.put("birthDate", new Timestamp(birthDate));
                    appointment.put("gender", scan[3]);
                    appointment.put("allergic", allergies);
                    appointment.put("description", description.getText().toString());
                    appointment.put("createdBy", user.getEmail().substring(0, user.getEmail().length()-9).toUpperCase());
                    db.collection("appointment").add(appointment)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                   if(task.isSuccessful()) {
                                       AppointmentListFragment appointmentListFragment = new AppointmentListFragment();
                                       ((MainActivity) getActivity()).replaceFragments(appointmentListFragment);
                                   }
                                   else {
                                       Toast.makeText(getContext(), "There was a problem, contact administration", Toast.LENGTH_SHORT);
                                   }
                                }
                            });
                } else {
                    final Appointment appointment = (Appointment) getArguments().getSerializable("appointment");
                    db.collection("appointment").document(appointment.getDocID())
                            .update("description", description.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Bundle bundle = new Bundle();
                                    Appointment app = (Appointment) getArguments().getSerializable("appointment");
                                    app.setDescription(description.getText().toString());
                                    bundle.putSerializable("appointment", app);
                                    AppointmentInfoFragment appointmentInfoFragment = new AppointmentInfoFragment();
                                    appointmentInfoFragment.setArguments(bundle);
                                    ((MainActivity) getActivity()).replaceFragments(appointmentInfoFragment);
                                }
                            });
                }
            }
        });


        // Back Button functionality
        backFromEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().containsKey("father") && getArguments().getString("father").equals("appointmentInfo")) {
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
            status.setText(R.string.creatingStatus);
        } else {
            status.setText(R.string.editStatus);
            Appointment myAppointment = (Appointment) getArguments().getSerializable("appointment");
            description.setText(myAppointment.getDescription());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
