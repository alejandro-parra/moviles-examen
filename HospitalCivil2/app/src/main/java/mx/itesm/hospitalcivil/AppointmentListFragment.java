package mx.itesm.hospitalcivil;

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

import java.util.ArrayList;


public class AppointmentListFragment extends Fragment implements RVAdapter.OnAppointmentListener{
    Button newRequestButton;
    private ArrayList<Appointment> appointments;
    private String patientID;
    private RecyclerView rv;
    private View vista;
    private LinearLayoutManager linearLayoutManager;
    private RVAdapter adapter;


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


        newRequestButton = vista.findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragments(new CameraActivity());
            }
        });


        rv = (RecyclerView) vista.findViewById(R.id.appointmentsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        appointments = new ArrayList<>();
        //Llenar la lista de appointments, hacer queries para hacer retrieve con el ID
        appointments.add(new Appointment("blablabla","219387123987","juan perez", 57, "male",  new String[]{"naproxeno","ibuprofeno"}, "123"));
        appointments.add(new Appointment("blablabla","219387123987","gabriela sanchez", 57, "male",  new String[]{"naproxeno","ibuprofeno"}, "123"));
        appointments.add(new Appointment("blablabla","219387123987","juan perez", 57, "male",  new String[]{"naproxeno","ibuprofeno"}, "123"));
        appointments.add(new Appointment("blablabla","219387123987","martin perez", 57, "male",  new String[]{"naproxeno","ibuprofeno"}, "123"));
        appointments.add(new Appointment("blablabla","219387123987","juan perez", 57, "male",  new String[]{"naproxeno","ibuprofeno"}, "123"));


        final AppointmentListFragment appointmentListener = this;
        adapter = new RVAdapter(appointments, appointmentListener,getContext());
        rv.setAdapter(adapter);

    }

    @Override
    public void onAppointmentClick(int position) {
        Appointment tmpAppointent = appointments.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("appointment",tmpAppointent);
        AppointmentInfoFragment appointmentInfoFragment = new AppointmentInfoFragment();
        appointmentInfoFragment.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragments(appointmentInfoFragment);
    }
}
