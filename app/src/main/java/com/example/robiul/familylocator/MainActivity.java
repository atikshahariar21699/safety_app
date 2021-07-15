package com.example.robiul.familylocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1,b2;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference databaseReference;
    String email="";
    String code="";
    String name;
    String olddata = "";
    PermissionManager manager;
    private FirebaseAuth mAuth;
    EditText e1,e2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("message");

        databaseReference.keepSynced(true);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user==null)
        {
            setContentView(R.layout.activity_main);

            e1=(EditText) findViewById(R.id.log_email);
            e2=(EditText) findViewById(R.id.password);
            mAuth = FirebaseAuth.getInstance();
            email=e1.getText().toString();
            db = FirebaseDatabase.getInstance();
            databaseReference = db.getReference("message");
            databaseReference.keepSynced(true);


            b1=(Button) findViewById(R.id.login);
            b2=(Button) findViewById(R.id.registerlog);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login();
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRegisterActivity();
                }
            });

        }
        else
        {
            email=user.getEmail();
            loadFirebaseUser();

        }

    }


    public void Login()
    {
        email=e1.getText().toString();
        mAuth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("robiul", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Log is successfull !!",Toast.LENGTH_SHORT).show();
                            loadFirebaseUser();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("robiul", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Wrong email address or password",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    public void loadFirebaseUser() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildAdded", "addChildEventListener" + s);
                String olddata = "";
                String usersEmail= null;
                olddata = dataSnapshot.getKey();
                usersEmail = dataSnapshot.child("emil").getValue(String.class);
                Log.i("getKey: ",email+ "getChildrenCount: " + usersEmail);
                if (usersEmail != null && usersEmail.equals(email)) {
                    code = dataSnapshot.child("id").getValue(String.class);
                    name=dataSnapshot.child("name").getValue(String.class);
                    Intent myintent=new Intent(MainActivity.this,Mynavigation.class);
                    myintent.putExtra("Code",code);
                    myintent.putExtra("Name",name);
                    startActivity(myintent);
                    Log.i("size : ", " countUser " + code);
                    finish();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildChanged", "addChildEventListener" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("onChildRemoved", "addChildEventListener");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildMoved", "addChildEventListener" + s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("onCalcnelle", databaseError.toString() + "addChildEventListener");
            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);

        ArrayList<String> denied_permission=manager.getStatus().get(0).denied;
        if(denied_permission.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Permission enabled",Toast.LENGTH_SHORT).show();
        }

    }

    public void openSigninActivity()
    {


    }

    public void openRegisterActivity()
    {
        Intent myintent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(myintent);
        finish();

    }


}

