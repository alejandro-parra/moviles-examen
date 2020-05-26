package mx.itesm.hospitalcivil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import mx.itesm.hospitalcivil.R;

public class FamilyRVAdapter extends RecyclerView.Adapter<FamilyRVAdapter.MemberViewHolder> {
    private Context context;
    private ArrayList<String> familyNames;
    private OnMemberListener mOnMemberListener;

    public FamilyRVAdapter(ArrayList<String> familyNames, OnMemberListener onMemberListener,Context context){
        this.mOnMemberListener = onMemberListener;
        this.context = context;
        this.familyNames = familyNames;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_family,parent,false),mOnMemberListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        String member = familyNames.get(position);
        //Picasso.with(context).load(appointment.getURL()).placeholder(R.mipmap.ic_launcher_round).into(holder.bookImage);
        //holder.bookImage.setImageResource(R.drawable.ic_launcher_background);//se tiene que cambiar a la imagen del evento
        holder.familyMemberName.setText(member);


    }

    @Override
    public int getItemCount() {
        return familyNames.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView familyMemberName;
        OnMemberListener onMemberListener;

        public MemberViewHolder(@NonNull View itemView, OnMemberListener onMemberListener) {
            super(itemView);
            familyMemberName = (TextView) itemView.findViewById(R.id.familyNameText);
            itemView.setOnClickListener(this);
            this.onMemberListener= onMemberListener;
        }

        @Override
        public void onClick(View v) {
            onMemberListener.onMemberClick(getAdapterPosition());
        }
    }

    public interface OnMemberListener {
        void onMemberClick(int position);
    }
}


