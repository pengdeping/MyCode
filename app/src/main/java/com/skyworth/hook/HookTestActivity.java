package com.skyworth.hook;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skyworth.myapp.R;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/26 18:03
 */
public class HookTestActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtInput;
    private Button mBtnCopy;
    private Button mBtnShowPaste;
    private LinearLayout mActivityMain;
    WindowManager windowManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook);
        initView();
        ClipboardHook.hookService(this);
        WindowManagerHook.hookService(this);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        requestAlertWindowPermission();
    }

    //申请权限
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);
    }

    private void initView() {
        mEtInput = (EditText) findViewById(R.id.et_input);
        mBtnCopy = (Button) findViewById(R.id.btn_copy);
        mBtnShowPaste = (Button) findViewById(R.id.btn_show_paste);
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);

        mBtnCopy.setOnClickListener(this);
        mBtnShowPaste.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:

                Dialog dialog = new Dialog(HookTestActivity.this);
                dialog.setTitle("Dadfadfa");
                dialog.show();

                String input = mEtInput.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(this, "input不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

//                //复制
//                ClipData clip = ClipData.newPlainText("simple text", mEtInput.getText().toString());
//                clipboard.setPrimaryClip(clip);
                break;
            case R.id.btn_show_paste:
                //黏贴
//                clip = clipboard.getPrimaryClip();
//                if (clip != null && clip.getItemCount() > 0) {
//                    Toast.makeText(this, clip.getItemAt(0).getText(), Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }
}
