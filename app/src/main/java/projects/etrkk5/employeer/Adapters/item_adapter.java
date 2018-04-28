package projects.etrkk5.employeer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import projects.etrkk5.employeer.Profiles.Item;
import projects.etrkk5.employeer.R;

/**
 * Created by EsrefTurkok on 26.04.2018.
 */

public class item_adapter extends RecyclerView.Adapter<item_holder> {
    private List<Item> itemList;

    public item_adapter(List<Item> itemList){
        this.itemList = itemList;
    }

    @Override
    public item_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new item_holder(itemView);
    }

    @Override
    public void onBindViewHolder(item_holder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewLocation.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
