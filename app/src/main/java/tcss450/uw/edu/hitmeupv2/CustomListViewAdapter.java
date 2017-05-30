package tcss450.uw.edu.hitmeupv2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
     * checks if it needs a profile image.
     */

    private boolean hasProfilePic = false;

    /**
     * For setting image resource.
     */
    private Picasso.Builder builder;

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
        this.builder = new Picasso.Builder(context);
    }

    /**
     * Sets the title of the rowitem.
     * @param title the resource id
     */
    public void setmTitle(int title) {
        this.mTitle = title;
    }

    /**
     * Sets the subtitle of the rowitem.
     * @param subTitle the resource id
     */
    public void setmSubtitleTitle(int subTitle) {
        this.mSubtitle = subTitle;
    }

    /**
     * Sets the image resource id of the rowitem
     * @param imgView the resource id
     */
    public void setmImg(int imgView) { this.mImg = imgView;  };

    /**
     * Sets a flag to see if the rowitem needs an image
     * @param hasProfilePic a true/false value
     */
    public void setHasProfilePic(boolean hasProfilePic) { this.hasProfilePic = hasProfilePic; }

    /*private view holder class*/
    private class ViewHolder {
        CircleImageView imageView;
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
                holder.imageView = (CircleImageView) convertView.findViewById(mImg);
            } else {
                holder.txtTitle = (TextView) convertView.findViewById(mTitle);
                holder.imageView = (CircleImageView) convertView.findViewById(mImg);

            }

//            holder.imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if(this.hasSubtitle) {
            holder.txtDesc.setText(rowItem.getmSubItem());
            holder.txtTitle.setText(rowItem.getmTitle());
            if(rowItem.getImgPath() != null) {
                builder.build().load(rowItem.getmImgPath()).placeholder(R.drawable.avatar).into(holder.imageView);
            }


        } else {
            holder.txtTitle.setText(rowItem.getmTitle());

            if(rowItem.getImgPath() != null) {
                builder.build().load(rowItem.getmImgPath()).placeholder(R.drawable.avatar).into(holder.imageView);
            } else {
                builder.build().load(R.drawable.avatar).into(holder.imageView);
            }

        }

        return convertView;
    }
}
