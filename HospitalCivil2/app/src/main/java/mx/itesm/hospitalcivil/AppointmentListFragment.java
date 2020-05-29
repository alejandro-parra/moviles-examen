package mx.itesm.hospitalcivil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AppointmentListFragment extends Fragment implements RVAdapter.OnAppointmentListener{
    Button newRequestButton;
    private ArrayList<Appointment> appointments;
    private String patientID;
    private RecyclerView rv;
    private View vista;
    private LinearLayoutManager linearLayoutManager;
    private RVAdapter adapter;
    private FirebaseUser user;


    public static AppointmentListFragment newInstance() {
        AppointmentListFragment fragment = new AppointmentListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_appointment_list, container, false);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        newRequestButton = vista.findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragments(new CameraActivity());
            }
        });


        rv = vista.findViewById(R.id.appointmentsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        appointments = new ArrayList<>();
        final AppointmentListFragment appointmentListener = this;
        db.collection("appointment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()) {
                                if(user.getEmail().startsWith(document.getString("id").toLowerCase())
                                || user.getEmail().startsWith(document.getString("createdBy").toLowerCase())) {
                                    List<String> tmp = (List<String>) document.get("allergic");
                                    String[] allergies = new String[tmp.size()];
                                    tmp.toArray(allergies);
                                    appointments.add(new Appointment(
                                            document.getString("description"),
                                            document.getString("createdBy"),
                                            document.getString("patientName"),
                                            document.getDate("birthDate"),
                                            document.getString("gender"),
                                            allergies,
                                            document.getString("id")
                                    ));
                                }
                            }
                            adapter = new RVAdapter(appointments, appointmentListener,getContext());
                            rv.setAdapter(adapter);
                        } else{
                            System.out.println("Valio el query");
                        }
                    }
                });



    }

    @Override
    public void onAppointmentClick(int position) {
        Appointment tmpAppointment = appointments.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("appointment",tmpAppointment);
        AppointmentInfoFragment appointmentInfoFragment = new AppointmentInfoFragment();
        appointmentInfoFragment.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragments(appointmentInfoFragment);
    }
}
