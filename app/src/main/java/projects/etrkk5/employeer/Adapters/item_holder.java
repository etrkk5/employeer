package projects.etrkk5.employeer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import projects.etrkk5.employeer.R;

/**
 * Created by EsrefTurkok on 26.04.2018.
 */

public class item_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView textViewName;
    public TextView textViewLocation;

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"LOG "+getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public item_holder(final View view) {
        super(view);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
        view.setOnClickListener(this);
    }
}

