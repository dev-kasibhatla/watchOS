package kasibhatla.dev.watchOS;

import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<String> data;

    public CustomAdapter(ArrayList<String> dataSet){
        data=dataSet;
    }

    //Arranging Views
    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout itemView;
        public CustomViewHolder(ConstraintLayout a){
            super(a);
            itemView = a;
        }
    }

    //Create required views
    @Override
    @NonNull
    public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create a new view
        ConstraintLayout a = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.file_recycler_layout,parent,false);
        CustomViewHolder vh = new CustomViewHolder(a);
        return vh;
    }

    public void onBindViewHolder(CustomViewHolder holder, int position) {
        TextView n =holder.itemView.findViewById(R.id.txtFileTitle);
        n.setText(data.get(position));
        //gethost only works after resolution
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

}
