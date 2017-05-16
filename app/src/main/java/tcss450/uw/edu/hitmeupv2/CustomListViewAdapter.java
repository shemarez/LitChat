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
     * Sets layout for the view being used.
     */
    private int mLayout;
    /**
     * Label for title
     */
    private int mTitle;
    /**
     * Label for subtitle
     */
    private int mSubtitle;
    /**
     * holds image.
     */
    private int mImg;
    /**
     * Context.
     */
    private Context mContext;

    /**
     * checks if it needs a subtitle
     */

    private boolean hasSubtitle;
    /**
     * Constructor
     * @param context the activity
     * @param resourceId the resource id
     * @param items the items
     */
    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items, boolean hasSubtitle) {
        super(context, resourceId, items);
        this.mContext = context;
        this.mLayout = resourceId;
        this.hasSubtitle = hasSubtitle;
    }

    public void setmTitle(int title) {
        this.mTitle = title;
    }
    public void setmSubtitleTitle(int subTitle) {
        this.mSubtitle = subTitle;
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
            convertView = mInflater.inflate(mLayout, null);
            holder = new ViewHolder();
            if(this.hasSubtitle) {
                holder.txtDesc = (TextView) convertView.findViewById(mSubtitle);
                holder.txtTitle = (TextView) convertView.findViewById(mTitle);

            } else {
                holder.txtTitle = (TextView) convertView.findViewById(mTitle);

            }

//            holder.imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if(this.hasSubtitle) {
            holder.txtDesc.setText(rowItem.getmSubItem());
            holder.txtTitle.setText(rowItem.getmTitle());

        } else {
            holder.txtTitle.setText(rowItem.getmTitle());
        }

//        holder.imageView.setImageResource(rowItem.getmImgId());

        return convertView;
    }
}
