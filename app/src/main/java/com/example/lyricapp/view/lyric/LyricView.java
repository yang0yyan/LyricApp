package com.example.lyricapp.view.lyric;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LyricView extends ListView implements BaseLyric {

    private static final String TAG = "LyricView";
    private List<LyricBean> dataList = new ArrayList<>();
    private LyricBaseAdapter mAdapter;

    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0X00FF00) {
                mAdapter.setChooseIndex((Integer) msg.obj);
                smoothScrollToPositionFromTop((Integer) msg.obj, 570, 1000);
            }
        }
    };

    public LyricView(Context context) {
        super(context);
        initView(context);
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        mAdapter = new LyricBaseAdapter(context);
        setAdapter(mAdapter);
    }

    @Override
    public void setLyricList(List<LyricBean> lyricList) {

    }

    @Override
    public void startLyric() {
        createThread(mAdapter.getDataList());
    }

    @Override
    public void pauseLyric() {

    }

    private void createThread(List<LyricBean> lyricList) {
        if (null == lyricList || lyricList.size() == 0) return;
        long time = System.currentTimeMillis();  // milliseconds
        Thread thread = new Thread(() -> {
            int length = lyricList.size();
            int index = 0;
            while (index < length) {
                LyricBean lyricBean = lyricList.get(index);
                long millis = lyricBean.getTimeMillis();
                Message message = new Message();
                message.what = 0X00FF00;
                message.obj = index;

//                while (true){
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(millis <= System.currentTimeMillis() - time)break;
//                }
                Log.d(TAG, "createThread: " + millis);
                if (millis > System.currentTimeMillis() - time) {
                    try {
                        Thread.sleep(millis - (System.currentTimeMillis() - time));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }




                handler.sendMessage(message);
                index++;
            }
            Log.d(TAG, "createThread: 结束");
        });
        thread.start();
    }

    public void readFile(String path, String name) {
        File file = new File(path, name);
        if (!file.exists()) {
            Toast.makeText(getContext(), "文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String len = "";
            while ((len = br.readLine()) != null) {
                builder.append(len);
            }
            is.close();
            br.close();
            String asd = builder.toString();
            Gson gson = new Gson();
            LyricFileBean bean = gson.fromJson(asd, LyricFileBean.class);

            String[] str = bean.getKaraokeLyric().split("\n");
            String[] str2 = bean.getLyric().split("\n");
            Log.d(TAG, "readFile: asdsad");
            handleLyric(str2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLyric(String[] lyrics) {
        Gson gson = new Gson();
        List<LyricBean> lyricList = new ArrayList<>();
        for (int i = 0; i < lyrics.length; i++) {
            String item = lyrics[i];
            LyricBean lyricBean = new LyricBean();
            int start = item.indexOf("[");
            int end = item.indexOf("]");
            if (start == 0 && end > 0) {
                lyricBean.setTimeClock(item.substring(start + 1, end));
                lyricBean.setLyric(item.substring(end + 1).trim());
            }
            if (item.indexOf("{") == 0) {
                TestBean bean = gson.fromJson(item, TestBean.class);
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < bean.getC().size(); j++) {
                    str.append(bean.getC().get(j).getTx());
                }
                lyricBean.setTimeClock("00:00." + bean.getT());
                lyricBean.setLyric(String.valueOf(str));
            }
            lyricBean.setTimeMillis(getTimeMillis(lyricBean.getTimeClock()));
            lyricList.add(lyricBean);
        }
        mAdapter.setDataList(lyricList);
//        createThread(lyricList);
        Log.d(TAG, "handleLyric: 123213");
    }

    private long getTimeMillis(String time) {
        if (null == time) return 0L;
        long millis = 0L;
        String[] str = time.split(":");
        if (str.length == 2) {
            String[] str2 = str[1].split("\\.");
            int val1 = Integer.parseInt(str[0]);
            int val2 = Integer.parseInt(str2[0]);
            int val3 = Integer.parseInt(str2[1]);
            for (int i = str2[1].length(); i < 3; i++) {
                val3 *= 10;
            }
            millis = (val1 * 60L + val2) * 1000 + val3;
        }
        return millis;
    }

    static class TestBean {
        private Integer t;
        private List<CDTO> c;

        public Integer getT() {
            return t;
        }

        public void setT(Integer t) {
            this.t = t;
        }

        public List<CDTO> getC() {
            return c;
        }

        public void setC(List<CDTO> c) {
            this.c = c;
        }

        public static class CDTO {
            private String tx;
            private String li;
            private String or;

            public String getTx() {
                return tx;
            }

            public void setTx(String tx) {
                this.tx = tx;
            }

            public String getLi() {
                return li;
            }

            public void setLi(String li) {
                this.li = li;
            }

            public String getOr() {
                return or;
            }

            public void setOr(String or) {
                this.or = or;
            }
        }
    }
}

