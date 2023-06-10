package com.example.lyricapp;


import android.os.Build;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.lyricapp.base.BaseActivity;
import com.example.lyricapp.utils.PermissionUtil;
import com.example.lyricapp.view.lyric.LyricView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String path = Environment.getExternalStorageDirectory().getPath() + "/Download";
    //    private static final String name = "514765154";
//    private static final String name = "28892408";
    private static final String name = "408332757";
//    private static final String name = "歌词.txt";

    private LyricView lyricView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        lyricView = findViewById(R.id.lyric_view);
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_restart).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (PermissionUtil.getPermissions(this, 0X00FF, PermissionUtil.READ_EXTERNAL_STORAGE, PermissionUtil.WRITE_EXTERNAL_STORAGE)) {
            lyricView.readFile(path, name);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {
            lyricView.startLyric();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0X00FF) {
            boolean read = false;
            boolean write = false;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];

                switch (permission) {
                    case PermissionUtil.READ_EXTERNAL_STORAGE:
                        read = grantResults[i] != -1;
                        break;
                    case PermissionUtil.WRITE_EXTERNAL_STORAGE:
                        write = grantResults[i] != -1;
                        break;
                }
            }
            if (read) {
                lyricView.readFile(path, name);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean show = shouldShowRequestPermissionRationale(PermissionUtil.READ_EXTERNAL_STORAGE);
                }
            }
        }
    }
}