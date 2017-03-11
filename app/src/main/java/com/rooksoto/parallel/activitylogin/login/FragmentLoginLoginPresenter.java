package com.rooksoto.parallel.activitylogin.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rooksoto.parallel.BasePresenter;
import com.rooksoto.parallel.BuildConfig;
import com.rooksoto.parallel.R;

public class FragmentLoginLoginPresenter implements BasePresenter, GoogleApiClient.OnConnectionFailedListener {
    private static final String CLIENTID = BuildConfig.OAUTHCLIENTID;
    private static final int RC_SIGN_IN = 9001;
    private View view;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String TAG = "Fragment Login Login";

    @Override
    public void start () {
    }

    @Override
    public void onBackPressedOverride (View viewP) {
    }

    public void checkFirebaseAuth () {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Intent intent = new Intent(getActivity(), ActivityStart.class);
                    //startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void checkGoogleSignIn (View viewP, Activity activityP) {
        this.view = viewP;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENTID)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(view.getContext())
                .enableAutoManage((FragmentActivity) activityP, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void addAuthStateListener () {
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    public void removeAuthStateListener () {
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    public void checkLoginID (int code, int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void handleSignInResult (GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            Log.d(TAG, "Did not sign in correctly :/");
        }
    }

    public void firebaseAuthWithGoogle (GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task <AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(view.getContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
    }

    public GoogleApiClient getGoogleAPI () {
        return googleApiClient;
    }

    public void disconnectGoogleAPI () {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) view.getContext());
            googleApiClient.disconnect();
        }
    }

    public void setOnClickReplace (Fragment fragmentP, View viewP, int containerID, String id) {
        ((Activity) viewP.getContext()).getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.animator_fade_in_right, R.animator.animator_fade_out_right)
                .replace(containerID, fragmentP, id)
                .commit();
    }

    public void checkLoginInfo (String usernameP, String passwordP) {
        // // TODO: 3/9/17 Check username/password for Firebase authentication
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleAPI());
        ((Activity) view.getContext()).startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}