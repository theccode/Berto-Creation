package com.android.berto;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ImageActivity extends MasterFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ImageFragment();
    }
}