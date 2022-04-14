package com.example.onlinechatapp.Adapter;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Message_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<Message_Model> messageModels;
    private Context context;
    int receiver_View_Type = 1;
    int sender_View_Type = 2;
    String API_KEY="";


    public ChatAdapter(ArrayList<Message_Model> messageModel, Context context) {
        this.messageModels = messageModel;
        this.context = context;

    }

    public int getItemViewType(int position) {
        if (messageModels.get(position).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return sender_View_Type;
        } else {
            return receiver_View_Type;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == sender_View_Type) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message_Model messageModel = messageModels.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            if (messageModel.getMsg().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")) {
                Glide.with(context).load(messageModel.getImageUrl()).into(((SenderViewHolder) holder).image1);
                ((SenderViewHolder) holder).img_layout.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).image1.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).location.setVisibility(View.GONE);
                ((SenderViewHolder) holder).cont_lay1.setVisibility(View.GONE);
                ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());

                /*((SenderViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotoView.class);
                        intent.putExtra("image", messageModel.getImageUrl());
                        context.startActivity(intent);
                    }
                });*/
            }
            else if(messageModel.getMsg().equals("$2y$10$4S0nmurvLkIkLbjnUZMrOu/IWViv87UzRB2v5hcBVzbGDUkw.3D..")){

            }
            else if(messageModel.getMsg().equals("$ncw$&nwcbwcwjdd!@cnwkcScwxj#5cjwc9qw8dw5cn")){
                ((SenderViewHolder) holder).img_layout.setVisibility(View.GONE);
                ((SenderViewHolder) holder).image1.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).cont_lay1.setVisibility(View.GONE);
                ((SenderViewHolder) holder).location.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());

                String url="https://maps.googleapis.com/maps/api/staticmap?center="+messageModel.getLat()+","+messageModel.getLon()+"&zoom=14&size=400x400&key=AIzaSyDRSh_tZk6KOKnk5-OJgsw_7yhCJ9kj4jE";
                Glide.with(context).load(url).into(((SenderViewHolder) holder).location);
                ((SenderViewHolder) holder).location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "http://maps.google.com/maps?daddr=" + messageModel.getLat() + "," + messageModel.getLon();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        context.startActivity(intent);
                    }
                });

            }
            else if(messageModel.getMsg().equals("%msjCkvjx08GH#mc0*mxvhvx4VHs13Nch!cnq-nss.uyCX7xvC")){
                ((SenderViewHolder) holder).img_layout.setVisibility(View.GONE);
                ((SenderViewHolder) holder).image1.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).location.setVisibility(View.GONE);
                ((SenderViewHolder) holder).cont_lay1.setVisibility(View.VISIBLE);

                ((SenderViewHolder) holder).disp_name1.setText(messageModel.getDisp_name());
                ((SenderViewHolder) holder).phone1.setText(messageModel.getNumber());
                ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());
            }
            else {
                ((SenderViewHolder) holder).img_layout.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).location.setVisibility(View.GONE);
                ((SenderViewHolder) holder).cont_lay1.setVisibility(View.GONE);
                ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());
                ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMsg());
            }

        } else {
            if (messageModel.getMsg().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")) {
                Glide.with(context).load(messageModel.getImageUrl()).into(((RecieverViewHolder) holder).image2);
                ((RecieverViewHolder) holder).img_layout2.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).image2.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).cont_lay2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).location1.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());
            }
            else if(messageModel.getMsg().equals("$ncw$&nwcbwcwjdd!@cnwkcScwxj#5cjwc9qw8dw5cn")){
                ((RecieverViewHolder) holder).img_layout2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).image2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).cont_lay2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).location1.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());
                String url="https://maps.googleapis.com/maps/api/staticmap?center="+messageModel.getLat()+","+messageModel.getLon()+"&zoom=14&size=400x400&key=AIzaSyDRSh_tZk6KOKnk5-OJgsw_7yhCJ9kj4jE";
                Glide.with(context).load(url).into(((RecieverViewHolder) holder).location1);
                ((RecieverViewHolder) holder).location1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "http://maps.google.com/maps?daddr=" + messageModel.getLat() + "," + messageModel.getLon();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        context.startActivity(intent);
                    }
                });
            }
            else if(messageModel.getMsg().equals("%msjCkvjx08GH#mc0*mxvhvx4VHs13Nch!cnq-nss.uyCX7xvC")){
                ((RecieverViewHolder) holder).img_layout2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).image2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).location1.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).cont_lay2.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());

                ((RecieverViewHolder) holder).disp_name2.setText(messageModel.getDisp_name());
                ((RecieverViewHolder) holder).phone2.setText(messageModel.getNumber());

                ((RecieverViewHolder) holder).add_btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent contactIntent = new Intent(ContactsContract.Intents.Insert. ACTION ) ;
                        contactIntent.setType(ContactsContract.RawContacts. CONTENT_TYPE ) ;
                        contactIntent
                                .putExtra(ContactsContract.Intents.Insert. NAME , ((RecieverViewHolder) holder).disp_name2.getText().toString())
                                .putExtra(ContactsContract.Intents.Insert. PHONE , ((RecieverViewHolder) holder).phone2.getText().toString()) ;
                        context.startActivity(contactIntent);
                    }
                });
            }
            else {
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).location1.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).cont_lay2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).img_layout2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());
                ((RecieverViewHolder) holder).recieverMsg.setText(messageModel.getMsg());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recievertime,disp_name2,phone2,add_btn2;
        CardView img_layout2;
        ImageView image2,location1,screenshot1;
        LinearLayout cont_lay2;



        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.rec_msg);
            recievertime = itemView.findViewById(R.id.rec_time);
            img_layout2= itemView.findViewById(R.id.image_card2);
            image2 = itemView.findViewById(R.id.image2);
            location1= itemView.findViewById(R.id.mylocation2);
            screenshot1= itemView.findViewById(R.id.screenshot2);
            disp_name2 = itemView.findViewById(R.id.display_name2);
            phone2 = itemView.findViewById(R.id.phone_number2);
            add_btn2 = itemView.findViewById(R.id.add_cont_btn2);
            cont_lay2 = itemView.findViewById(R.id.contact_layout_rec);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, sendertime,disp_name1,phone1,add_btn1;
        CardView img_layout;
        ImageView image1,location,screen_shot;
        LinearLayout cont_lay1;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.send_msg);
            sendertime = itemView.findViewById(R.id.send_time);
            img_layout= itemView.findViewById(R.id.image_card);
            image1 = itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.mylocation);
            screen_shot = itemView.findViewById(R.id.screenshot);
            disp_name1 = itemView.findViewById(R.id.display_name);
            phone1 = itemView.findViewById(R.id.phone_number);
            add_btn1 = itemView.findViewById(R.id.add_cont_btn);
            cont_lay1 = itemView.findViewById(R.id.contact_layout);
        }
    }
}
