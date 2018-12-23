package com.cetpainfotech.googlesignin;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    ImageView image;
    TextView name, email;
    private FirebaseAuth mAuth;
    Button signout;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signout=findViewById(R.id.signout);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        image=findViewById(R.id.image);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        googleSignInClient = GoogleSignIn
                .getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                // Google sign out
                googleSignInClient.signOut()
                        .addOnCompleteListener(HomeActivity.this,
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                    }
                                });

            }
        });
        //to get the current user data
        FirebaseUser user = mAuth.getCurrentUser();
        //to display the user name
        name.setText(user.getDisplayName());
        //to display the user email
        email.setText(user.getEmail());

        //to display the user profile pic
        Glide.with(this).load(user.getPhotoUrl()).into(image);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            finish();
            Intent intent= new Intent(HomeActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
    }
}
