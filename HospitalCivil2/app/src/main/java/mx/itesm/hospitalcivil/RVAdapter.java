package mx.itesm.hospitalcivil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.AppointmentViewHolder> {
    private Context context;
    private ArrayList<Appointment> appointments;
    private OnAppointmentListener mOnAppointmentListener;

    public RVAdapter(ArrayList<Appointment> appointments, OnAppointmentListener onAppointmentListener,Context context){
        this.appointments = appointments;
        this.mOnAppointmentListener = onAppointmentListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_appointment,parent,false),mOnAppointmentListener);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        //Picasso.with(context).load(appointment.getURL()).placeholder(R.mipmap.ic_launcher_round).into(holder.bookImage);
        //holder.bookImage.setImageResource(R.drawable.ic_launcher_background);//se tiene que cambiar a la imagen del evento
        holder.appointmentName.setText(appointment.getName());
        holder.appointmentDesc.setText(appointment.getDescription());

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView appointmentName;
        TextView appointmentDesc;
        OnAppointmentListener onAppointmentListener;

        public AppointmentViewHolder(@NonNull View itemView, OnAppointmentListener onAppointmentListener) {
            super(itemView);
            appointmentName = itemView.findViewById(R.id.patientNameText);
            appointmentDesc = itemView.findViewById(R.id.appointmentDescText);
            itemView.setOnClickListener(this);
            this.onAppointmentListener= onAppointmentListener;
        }

        @Override
        public void onClick(View v) {
            onAppointmentListener.onAppointmentClick(getAdapterPosition());
        }
    }

    public interface OnAppointmentListener {
        void onAppointmentClick(int position);
    }
}


