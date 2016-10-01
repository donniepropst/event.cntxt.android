package hackathon.eventcntxt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private static int GOOGLE_ACTIVITY_RESULT_CODE = 1001;

    //Use butterknife view binding for ease of readability
    @BindView(R.id.registration_signup_button) Button signInButton;

    @BindView(R.id.register_form_email_layout) TextInputLayout emailLayout;
    @BindView(R.id.register_form_email) EditText emailTextView;

    @BindView(R.id.register_form_password_layout) TextInputLayout passwordLayout;
    @BindView(R.id.register_form_password) EditText passwordTextView;
    @BindView(R.id.register_form_password_confirm) EditText passwordConfirmTextView;
    @BindView(R.id.register_form_password_confirm_layout) TextInputLayout passwordConfirmLayout;

    //@BindView(R.id.register_form_first_name)EditText firstNameText;
    //@BindView(R.id.register_form_last_name)EditText lastNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We are using Firebase for user managemtent

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }

        //This loads a view from the res/layout folder(where you design the screens)
        setContentView(R.layout.activity_registration);


        //This binds the view we configured above to the @BindView annotations specified at the top of the file
        ButterKnife.bind(this);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String passwordConfirm = passwordConfirmTextView.getText().toString();

                if(validate(email, password, passwordConfirm)) {
                    loginEmailPassword(email, password);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //after activity starts bind Firebase auth state to a listener method
        firebaseAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent i = new Intent(RegistrationActivity.this, HomeActivity.class);
                startActivity(i);
                Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d("AUTH", "onAuthStateChanged:signed_out");
            }
        }
    };


    private void loginEmailPassword(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            System.out.println("added user");
                        }
                    }
                });
    }



    private boolean validate(String email, String password, String passwordConfirm){
        boolean valid = true;
        emailLayout.setError("");
        passwordLayout.setError("");
        passwordConfirmLayout.setError("");
        if(!validateEmail(email)){
            emailLayout.setError("Invalid email");
            valid = false;
        }
        if(!validatePassword(password)){
            passwordLayout.setError("Password must be at least 6 characters");
            valid = false;
        }
        if(!validatePasswordConfirm(password, passwordConfirm)){
            passwordConfirmLayout.setError("Passwords do not match");
            valid = false;
        }
        return valid;
    }
    private boolean validateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validatePassword(String password){
        return (password.length() >= 6);
    }
    private boolean validatePasswordConfirm(String password, String confirmPassword){
        return (password.equals(confirmPassword));
    }


}
