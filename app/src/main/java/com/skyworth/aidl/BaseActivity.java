package com.skyworth.aidl;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/25 15:55
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
