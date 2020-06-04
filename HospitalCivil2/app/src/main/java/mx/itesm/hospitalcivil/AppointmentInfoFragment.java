package mx.itesm.hospitalcivil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentInfoFragment extends Fragment {
    private Button editAppointmentButton, deleteAppointmentButton;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        appointment = (Appointment) getArguments().getSerializable("appointment");
        setValues(view, appointment);
        editAppointmentButton = view.findViewById(R.id.editAppointmentButton);
        deleteAppointmentButton = view.findViewById(R.id.deleteAppointmentButton);


        // Edit button
        editAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAppointmentFragment editAppointmentFragment = new EditAppointmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("father","appointmentInfo");
                bundle.putSerializable("appointment", appointment);
                editAppointmentFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragments(editAppointmentFragment, "info", appointment);
            }
        });

        // Delete button
        deleteAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.Confirmation);
                // Add the buttons
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("appointment").document(appointment.getDocID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getApplicationContext(), "Appointment deleted succesfully", Toast.LENGTH_SHORT);
                                ((MainActivity)getActivity()).replaceFragmentsAndReset(new AppointmentListFragment());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error deleting document", e);
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                dialog.show();
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
