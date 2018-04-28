package projects.etrkk5.employeer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import projects.etrkk5.employeer.Profiles.Ad;
import projects.etrkk5.employeer.R;

/**
 * Created by EsrefTurkok on 28.04.2018.
 */

public class AdAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Ad> mAdList;

    public AdAdapter(Activity activity, List<Ad> adList) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mAdList = adList;
    }

    @Override
    public int getCount() {
        return mAdList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View positionView;

        positionView = mInflater.inflate(R.layout.ad_layout, null);
        TextView textViewTitle = (TextView)positionView.findViewById(R.id.textViewTitle);

        Ad ad = mAdList.get(position);
        textViewTitle.setText(ad.getTitle());

        return  positionView;
    }
}
