package tcss450.uw.edu.hitmeupv2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;

/**
 * Created by Shema on 5/9/2017.
 *
 * Creates a custom adapter for the listviews used in the project.
 * Extends an ArrayAdapter of RowItems. Can display a profile picture,
 * a Title and a subtitle.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
    /**
     * Context.
     */
    private Context mContext;

    /**
     * Constructor
     * @param context the activity
     * @param resourceId the resource id
     * @param items the items
     */
    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.mContext = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    /**
     * Overrided method for a custom view, which shows
     * the profile picture, the title and subtitle.
     * @param position index
     * @param convertView m
     * @param parent m
     * @return a view
     */
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.content_homepage_list, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.lastConvo);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.friendLabel);
//            holder.imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
//
        holder.txtDesc.setText(rowItem.getmSubItem());
        holder.txtTitle.setText(rowItem.getmTitle());
//        holder.imageView.setImageResource(rowItem.getmImgId());

        return convertView;
    }
}
