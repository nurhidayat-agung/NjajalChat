package nurhidayat.agung;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    public String userName , chatRoom;
    private EditText edtSendMsg;
    public Button btnSendMsg;
    public ScrollView svChatContent;
    public TextView tvContentMsg;
    private DatabaseReference root;
    private String temp_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtSendMsg = (EditText) findViewById(R.id.edtSendChat);
        btnSendMsg = (Button) findViewById(R.id.btn_sendmsg);
        svChatContent = (ScrollView) findViewById(R.id.sv_chatcontent);
        tvContentMsg = (TextView) findViewById(R.id.tv_contentmsg);


        userName = getIntent().getExtras().get("user_name").toString();
        chatRoom = getIntent().getExtras().get("room_name").toString();

        setTitle("Room - " + chatRoom);

        root = FirebaseDatabase.getInstance().getReference().child(chatRoom);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = "agungnur";
                String msg = edtSendMsg.getText().toString();
                String encryptedMsg = null;
                try {
                    encryptedMsg = AESCrypt.encrypt(pass,msg);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference msgRoot = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",userName);
                map2.put("msg",encryptedMsg);
                msgRoot.updateChildren(map2);
                //Toast.makeText(getApplicationContext(),""+userName,Toast.LENGTH_SHORT).show();
                edtSendMsg.setText(null);



            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    append_chat_conversation(dataSnapshot);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    append_chat_conversation(dataSnapshot);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chatMsg,chatUsername;
    private void append_chat_conversation(DataSnapshot dataSnapshot) throws GeneralSecurityException {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chatMsg = (String) ((DataSnapshot)i.next()).getValue();
            String pass = "agungnur";
            String decrypt = "";
            decrypt = AESCrypt.decrypt(pass,chatMsg);
            chatUsername = (String) ((DataSnapshot)i.next()).getValue();
            if (chatUsername.equals(userName)){
                tvContentMsg.append("\nSaya" + " : " +decrypt + "\n");
                tvContentMsg.append("chiper text : \n" + chatMsg + "\n");
            }else {
                tvContentMsg.append("\n"+chatUsername + " : " +decrypt + "\n");
                tvContentMsg.append("chiper text : \n" + chatMsg + "\n");
            }
        }

    }

}
