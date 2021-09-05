package com.example.gapshup.Activity;


import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapshup.Adapter.MessagesAdapter;
import com.example.gapshup.ModelClass.Messages;
import com.example.gapshup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatActivity extends AppCompatActivity {
    TextView receiverName;
    String ReceiverName, ReceiverUID, SenderUID;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    String senderRoom, receiverRoom;
    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    ImageView sendBtn;
    EditText editMessage;
    MessagesAdapter adapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverName = findViewById(R.id.receiverName);
        ReceiverName = getIntent().getStringExtra("name");
        ReceiverUID = getIntent().getStringExtra("uid");
        receiverName.setText(ReceiverName);
        sendBtn = findViewById(R.id.imgSendButton);
        editMessage = findViewById(R.id.editMessage);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        SenderUID = FirebaseAuth.getInstance().getUid();
        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(chatActivity.this,messagesArrayList);
        messageAdapter.setAdapter(adapter);

        senderRoom = SenderUID+ReceiverUID;
        receiverRoom = ReceiverUID+SenderUID;

        messagesArrayList = new ArrayList<>();
//        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(chatActivity.this, "No Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                editMessage.setText("");
                Date date = new Date();

                Messages message1 = new Messages(message, SenderUID,date.getTime());
                database.getReference()
                        .child("Chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            }
                        });
            }
        });

    }
}
