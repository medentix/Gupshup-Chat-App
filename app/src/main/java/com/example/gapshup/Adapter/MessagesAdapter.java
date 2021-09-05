package com.example.gapshup.Adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapshup.ModelClass.Messages;
import com.example.gapshup.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MessagesAdapter extends RecyclerView.Adapter {
    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    Context context;
    ArrayList<Messages> messagesArrayList = new ArrayList<Messages>();
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }
        else{

            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
             SenderViewHolder viewHolder = (SenderViewHolder) holder;
             viewHolder.txtmessage.setText(messages.getMessage());
        }
        else{

            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message)){
            return ITEM_SEND;
        }
        return ITEM_RECEIVE;
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView txtmessage;
        public SenderViewHolder(@NonNull View itemView){
            super(itemView);
            txtmessage = itemView.findViewById(R.id.txtMessages);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView txtmessage;
        public ReceiverViewHolder(@Nonnull View itemView){
            super(itemView);
            txtmessage = itemView.findViewById(R.id.txtMessages);
        }
    }
}
