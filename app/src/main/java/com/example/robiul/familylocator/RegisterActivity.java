package com.example.robiul.familylocator;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    EditText e4,e5,e1,e2;
    private FirebaseAuth mAuth;
    Button bt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e1=(EditText) findViewById(R.id.registration_name);
        e2=(EditText) findViewById(R.id.phone_number);
        e4=(EditText) findViewById(R.id.Email_id);
        e5=(EditText) findViewById(R.id.log_password);
        bt=(Button) findViewById(R.id.registration);

        mAuth = FirebaseAuth.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordactivity();

            }
        });

    }


    public void passwordactivity()
    {
        mAuth.createUserWithEmailAndPassword(e4.getText().toString(), e5.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("robiul", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);

                            gotonameactivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("robiul", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                    }
                });

    }

    public void gotonameactivity()
    {

        Random r=new Random();
        int n=100000+r.nextInt(900000);
        String code=String.valueOf(n);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cycle");
        String id=myRef.push().getKey();
        myRef.child(code).child(code).setValue(code);
        myRef.child(code).child(code).setValue(code);

                MemberDetails M=new MemberDetails(e1.getText().toString(),code,e4.getText().toString(),e5.getText().toString());



                DatabaseReference myRef1 = database.getReference("message");
                myRef1.child(code).setValue(M);

                Toast.makeText(getApplicationContext(),"Registration successfull !",Toast.LENGTH_SHORT).show();

        Intent myintent=new Intent(RegisterActivity.this,Mynavigation.class);

        myintent.putExtra("Code",code);
        myintent.putExtra("Name",e1.getText().toString());
        startActivity(myintent);

        finish();

    }



}
