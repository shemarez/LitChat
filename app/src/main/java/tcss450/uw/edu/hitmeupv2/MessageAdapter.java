package tcss450.uw.edu.hitmeupv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyalbira.loadingdots.LoadingDots;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import tcss450.uw.edu.hitmeupv2.WebService.ChatMessage;

/**
 * Created by Shema on 4/20/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";


    private final List<ChatMessage> chatMessages;
    private Activity context;
    private LoadingDots mDots;

    public MessageAdapter(Activity context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
        mDots = new LoadingDots(context);
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check to simulate whether it me or other sender
        boolean isTyping = chatMessage.getRecipientTyping();
        boolean isPhoto = chatMessage.isPhotoMsg();
        String photoSrc = chatMessage.getPhotoSrc();
        File photoFile = chatMessage.getPhotoFile();
        setAlignment(holder, myMsg);

        if(isTyping) {
            holder.loadingDots.setVisibility(View.VISIBLE);
        } else {
            holder.loadingDots.setVisibility(View.GONE);

        }

        holder.txtMessage.setText(chatMessage.getMessage());
        holder.txtInfo.setText(chatMessage.getDate());
        holder.txtMonthDay.setText((chatMessage.getMonthDay()));


        // if it is a url do this
        if(photoSrc != null && isPhoto) {
            System.out.println("in photosrc");
            System.out.println("THE URI " + BASE_URL +"public/" +photoSrc);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(BASE_URL + "public/" + photoSrc).into(holder.sentPhoto);
            holder.txtMessage.setText("");
            holder.sentPhoto.setVisibility(View.VISIBLE);
            photoSrc = null;
        } else {
            holder.sentPhoto.setVisibility(View.GONE);
        }


        // if it is a file from within android dir do this
        if(photoFile != null && isPhoto) {
            System.out.println("in photofile");
            Picasso.with(context).load(photoFile).into(holder.sentPhoto);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(photoFile).into(holder.sentPhoto);
            holder.txtMessage.setText("");
            holder.sentPhoto.setVisibility(View.VISIBLE);

        }
        else if(!isPhoto) {
            holder.sentPhoto.setVisibility(View.GONE);

        }
        return convertView;
    }


    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void remove(ChatMessage message) {
        chatMessages.remove(chatMessages.size() - 1);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe) {

        if (isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setTextColor(Color.parseColor("#b34700"));
            holder.txtInfo.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMonthDay.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMonthDay.setLayoutParams(layoutParams);

        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
            holder.txtInfo.setTextColor(Color.parseColor("#606060"));
            layoutParams = (LinearLayout.LayoutParams) holder.txtMonthDay.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMonthDay.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.loadingDots = (LoadingDots) v.findViewById(R.id.myLoadingDots);
        holder.txtMonthDay = (TextView) v.findViewById(R.id.monthDay);
        holder.sentPhoto = (ImageView)v.findViewById(R.id.sentPhoto);
        return holder;
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public TextView txtMonthDay;
        public ImageView sentPhoto;
        public LoadingDots loadingDots;
    }


}