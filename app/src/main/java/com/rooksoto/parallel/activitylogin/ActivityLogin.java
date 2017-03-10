package com.rooksoto.parallel.activitylogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rooksoto.parallel.BaseView;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.activitylogin.login.FragmentLoginLogin;
import com.rooksoto.parallel.activitylogin.splash.FragmentLoginSplash;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.CustomToast;

public class ActivityLogin extends AppCompatActivity implements BaseView{
    private ActivityLoginPresenter activityLoginPresenter = new ActivityLoginPresenter();
    private int containerID = R.id.activity_login_fragment_container;
    private CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog();
    private CustomToast mCustomToast = new CustomToast();
    private ImageView logoViewLeft;
    private ImageView logoViewRight;
    private boolean logoVisible = false;
    private FragmentLoginLogin mFragmentLoginLogin;
    private boolean isNew = true;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    public void initialize () {
        activityLoginPresenter.giveActivity(this);
        setViews();
    }

    @Override
    public void setViews () {
        loadFragmentSplash();
    }

    private void loadFragmentSplash () {
        FragmentLoginSplash mFragmentLoginSplash = new FragmentLoginSplash();
        activityLoginPresenter.loadFragment(mFragmentLoginSplash,
                R.animator.animator_nothing, R.animator.animator_fade_out, containerID, "Splash");
    }

    @Override
    public void onBackPressed () {
    }

    public void onClickToActivityStart (View view) {
        activityLoginPresenter.intentToActivityStart();
    }
}
