package mx.itesm.hospitalcivil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectMemberFragment extends Fragment implements FamilyRVAdapter.OnMemberListener{
    private ArrayList<String> familyNames;
    private ArrayList<String> familyMembersIDs;
    private Map<String, String> family;
    private String patientID;
    private RecyclerView rv;
    private View vista;
    private LinearLayoutManager linearLayoutManager;
    private FamilyRVAdapter adapter;
    Button backToListButton;


    public static SelectMemberFragment newInstance() {
        SelectMemberFragment fragment = new SelectMemberFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.select_member_fragment, container, false);
        Bundle bundle = getArguments();
        patientID = bundle.getString("id");
        backToListButton = vista.findViewById(R.id.backToListButton);
        backToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                AppointmentListFragment appointmentListFragment = new AppointmentListFragment();
                appointmentListFragment.setArguments(bundle);
                ((MainActivity) getActivity()).replaceFragments(appointmentListFragment);
            }
        });

        family = new HashMap<String, String>();
        familyNames = new ArrayList<>();
        familyNames.add("Juan perez");
        familyNames.add("chuchita martinez");
        familyMembersIDs = bundle.getStringArrayList("familymembers");
        //con el ID de sus familiares inicialziar la lista de nombres que se le pasa al recyclerview y hacer el map para mantener una relacion entre ID y nombre
        for (int i = 0; i < familyMembersIDs.size(); i++){
            //Llenar la lista de miembros de familia, hacer queries para hacer retrieve con el ID
            family.put(familyMembersIDs.get(i),familyNames.get(i));
        }



        rv = (RecyclerView) vista.findViewById(R.id.familyRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);





        final SelectMemberFragment memberListener = this;
        adapter = new FamilyRVAdapter(familyNames, memberListener, getContext());
        rv.setAdapter(adapter);



        return vista;
    }

    @Override
    public void onMemberClick(int position) {
        String memberID = family.get(familyNames.get(position));
        Bundle bundle = new Bundle();
        bundle.putString("memberID",memberID);
        bundle.putString("father","selectMember");
        EditAppointmentFragment editAppointmentFragment= new EditAppointmentFragment();
        editAppointmentFragment.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragments(editAppointmentFragment);
    }
}
