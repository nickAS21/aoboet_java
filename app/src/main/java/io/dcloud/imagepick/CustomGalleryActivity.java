package io.dcloud.imagepick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nostra13.dcloudimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import io.dcloud.RInformation;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.js.gallery.GalleryFeatureImpl;
import io.src.dcloud.adapter.DCloudBaseActivity;

public class CustomGalleryActivity extends DCloudBaseActivity {
    GridView a;
    b c;
    TextView d;
    ImageView e;
    TextView f;
    private ImageLoader i;
    private ArrayList<Integer> j;
    private String n;
    private final Handler b = new MyHandler(this);
    private ArrayList<String> k = new ArrayList<>();
    private ArrayList<String> l = new ArrayList<>();
    private int m = -1;

    private static class MyHandler extends Handler {
        private final WeakReference<CustomGalleryActivity> activityRef;

        MyHandler(CustomGalleryActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            CustomGalleryActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return;

            activity.c.a((ArrayList<a>) message.obj, activity.k);

            if (activity.j == null || activity.j.isEmpty()) {
                if (!activity.k.isEmpty()) {
                    activity.f.setVisibility(View.VISIBLE);
                    activity.d.setText("已选择" + activity.k.size() + "张图片");
                }
            } else {
                activity.f.setVisibility(View.VISIBLE);
                activity.d.setText("已选择" + activity.j.size() + "张图片");
                activity.c.a(activity.j);
            }
            activity.c();
        }
    }

    View.OnClickListener g = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<a> c = CustomGalleryActivity.this.c.c();
            int size = c.size();
            String[] strArr = new String[size];
            for (int i = 0; i < size; i++) {
                strArr[i] = c.get(i).a;
            }
            CustomGalleryActivity.this.that.setResult(-1, new Intent().putExtra("all_path", strArr));
            CustomGalleryActivity.this.finish();
        }
    };

    AdapterView.OnItemClickListener h = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (CustomGalleryActivity.this.m > 0 && CustomGalleryActivity.this.c.b() >= CustomGalleryActivity.this.m && !CustomGalleryActivity.this.c.getItem(i).b) {
                if (TextUtils.isEmpty(CustomGalleryActivity.this.n)) return;
                GalleryFeatureImpl.a(CustomGalleryActivity.this.n);
                return;
            }
            CustomGalleryActivity.this.c.a(view, i);
            ArrayList<a> c = CustomGalleryActivity.this.c.c();
            if (c.isEmpty()) {
                CustomGalleryActivity.this.d.setText("选择图片");
                CustomGalleryActivity.this.f.setVisibility(View.GONE);
            } else {
                CustomGalleryActivity.this.d.setText("已选择" + c.size() + "张图片");
                CustomGalleryActivity.this.f.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.j = bundle.getIntegerArrayList("selectedPositions");
        }
        requestWindowFeature(1);
        setContentView(RInformation.LAYOUT_IMAGE_PICK_GALLERY);
        this.m = getIntent().getIntExtra("maximum", this.m);
        this.n = getIntent().getStringExtra("_onMaxedId");
        b();
        a();
    }

    private void a() {
        this.a = findViewById(RInformation.ID_IMAGE_PICK_GRID_GALLERY);
        b bVar = new b(this.that, this.i, PlatformUtil.SCREEN_WIDTH(this.that));
        this.c = bVar;
        bVar.a(true);
        this.a.setAdapter((ListAdapter) this.c);
        this.a.setOnItemClickListener(this.h);
        this.e = findViewById(RInformation.ID_IMAGE_PICK_NO_MEDIA);
        TextView textView = findViewById(RInformation.ID_IMAGE_PICK_BTN_OK);
        this.f = textView;
        textView.setOnClickListener(this.g);
        TextView textView2 = findViewById(RInformation.ID_IMAGE_PICK_TITLE);
        this.d = textView2;
        textView2.setText("选择图片");

        new Thread(() -> {
            ArrayList<a> d = CustomGalleryActivity.this.d();
            ArrayList<String> arrayList = new ArrayList<>();
            for (String str : CustomGalleryActivity.this.k) {
                if (CustomGalleryActivity.this.l.contains(str)) {
                    arrayList.add(str);
                }
            }
            CustomGalleryActivity.this.k.clear();
            CustomGalleryActivity.this.l.clear();
            CustomGalleryActivity.this.k.addAll(arrayList);
            Message message = new Message();
            message.obj = d;
            CustomGalleryActivity.this.b.sendMessage(message);
        }).start();
    }

    private void b() {
        String stringExtra = getIntent().getStringExtra("selected");
        if (TextUtils.isEmpty(stringExtra)) return;

        this.k.clear();
        try {
            JSONArray jSONArray = new JSONArray(stringExtra);
            for (int i = 0; i < jSONArray.length(); i++) {
                this.k.add(jSONArray.getString(i).replace(DeviceInfo.FILE_PROTOCOL, ""));
                if (this.m > 0 && this.k.size() == this.m) return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void c() {
        if (this.c.isEmpty()) {
            this.e.setVisibility(View.VISIBLE);
        } else {
            this.e.setVisibility(View.GONE);
        }
    }

    @SuppressLint("Range")
    private ArrayList<a> d() {
        ArrayList<a> arrayList = new ArrayList<>();
        try (Cursor query = this.that.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name", "_size"}, null, null, null)) {
            if (query != null) {
                int sizeIdx = query.getColumnIndex("_size");
                int dataIdx = query.getColumnIndex("_data");
                while (query.moveToNext()) {
                    if (sizeIdx >= 0 && query.getInt(sizeIdx) >= 10240) {
                        a aVar = new a();
                        if (dataIdx >= 0) {
                            aVar.a = query.getString(dataIdx);
                            this.l.add(aVar.a);
                            arrayList.add(aVar);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ArrayList<Integer> a = this.c.a();
        if (!a.isEmpty()) {
            bundle.putIntegerArrayList("selectedPositions", a);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.c != null) {
            this.c.d();
            this.c = null;
        }
        this.b.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        RuningAcitvityUtil.putRuningActivity(this.that);
    }

    @Override
    public void onPause() {
        super.onPause();
        RuningAcitvityUtil.removeRuningActivity(this.that.getComponentName().getClassName());
    }
}
