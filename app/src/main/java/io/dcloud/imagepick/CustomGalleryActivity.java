package io.dcloud.imagepick;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import io.dcloud.RInformation;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.js.gallery.GalleryFeatureImpl;
import io.src.dcloud.adapter.DCloudBaseActivity;

/* loaded from: classes.dex */
public class CustomGalleryActivity extends DCloudBaseActivity {
    GridView a;
    b c;
    TextView d;
    ImageView e;
    TextView f;
    private ImageLoader i;
    private ArrayList<Integer> j;
    private String n;
    Handler b = new Handler() { // from class: io.dcloud.imagepick.CustomGalleryActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            CustomGalleryActivity.this.c.a((ArrayList<a>) message.obj, CustomGalleryActivity.this.k);
            if (CustomGalleryActivity.this.j == null || CustomGalleryActivity.this.j.isEmpty()) {
                if (!CustomGalleryActivity.this.k.isEmpty()) {
                    CustomGalleryActivity.this.f.setVisibility(0);
                    CustomGalleryActivity.this.d.setText("已选择" + CustomGalleryActivity.this.k.size() + "张图片");
                }
            } else {
                CustomGalleryActivity.this.f.setVisibility(0);
                CustomGalleryActivity.this.d.setText("已选择" + CustomGalleryActivity.this.j.size() + "张图片");
                CustomGalleryActivity.this.c.a(CustomGalleryActivity.this.j);
            }
            CustomGalleryActivity.this.c();
        }
    };
    private ArrayList<String> k = new ArrayList<>();
    private ArrayList<String> l = new ArrayList<>();
    private int m = -1;
    View.OnClickListener g = new View.OnClickListener() { // from class: io.dcloud.imagepick.CustomGalleryActivity.3
        @Override // android.view.View.OnClickListener
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
    AdapterView.OnItemClickListener h = new AdapterView.OnItemClickListener() { // from class: io.dcloud.imagepick.CustomGalleryActivity.4
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (CustomGalleryActivity.this.m > 0 && CustomGalleryActivity.this.c.b() >= CustomGalleryActivity.this.m && !CustomGalleryActivity.this.c.getItem(i).b) {
                if (TextUtils.isEmpty(CustomGalleryActivity.this.n)) {
                    return;
                }
                GalleryFeatureImpl.a(CustomGalleryActivity.this.n);
                return;
            }
            CustomGalleryActivity.this.c.a(view, i);
            ArrayList<a> c = CustomGalleryActivity.this.c.c();
            if (c.isEmpty()) {
                CustomGalleryActivity.this.d.setText("选择图片");
                CustomGalleryActivity.this.f.setVisibility(8);
            } else {
                CustomGalleryActivity.this.d.setText("已选择" + c.size() + "张图片");
                CustomGalleryActivity.this.f.setVisibility(0);
            }
        }
    };

    @Override // android.app.Activity
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
        this.a = (GridView) findViewById(RInformation.ID_IMAGE_PICK_GRID_GALLERY);
        b bVar = new b(this.that, this.i, PlatformUtil.SCREEN_WIDTH(this.that));
        this.c = bVar;
        bVar.a(true);
        this.a.setAdapter((ListAdapter) this.c);
        this.a.setOnItemClickListener(this.h);
        this.e = (ImageView) findViewById(RInformation.ID_IMAGE_PICK_NO_MEDIA);
        TextView textView = (TextView) findViewById(RInformation.ID_IMAGE_PICK_BTN_OK);
        this.f = textView;
        textView.setOnClickListener(this.g);
        TextView textView2 = (TextView) findViewById(RInformation.ID_IMAGE_PICK_TITLE);
        this.d = textView2;
        textView2.setText("选择图片");
        new Thread(new Runnable() { // from class: io.dcloud.imagepick.CustomGalleryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                ArrayList d = CustomGalleryActivity.this.d();
                ArrayList arrayList = new ArrayList();
                Iterator it = CustomGalleryActivity.this.k.iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
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
            }
        }).start();
    }

    private void b() {
        String stringExtra = getIntent().getStringExtra("selected");
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        this.k.clear();
        try {
            JSONArray jSONArray = new JSONArray(stringExtra);
            for (int i = 0; i < jSONArray.length(); i++) {
                this.k.add(jSONArray.getString(i).replace(DeviceInfo.FILE_PROTOCOL, ""));
                if (this.m > 0 && this.k.size() == this.m) {
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.c.isEmpty()) {
            this.e.setVisibility(0);
        } else {
            this.e.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<a> d() {
        ArrayList<a> arrayList = new ArrayList<>();
        try {
            Cursor query = this.that.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name", "_size"}, null, null, null);
            if (query != null && query.getCount() > 0) {
                while (query.moveToNext()) {
                    if (query.getInt(query.getColumnIndex("_size")) >= 10240) {
                        a aVar = new a();
                        aVar.a = query.getString(query.getColumnIndex("_data"));
                        this.l.add(aVar.a);
                        arrayList.add(aVar);
                    }
                }
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ArrayList<Integer> a = this.c.a();
        if (a.isEmpty()) {
            return;
        }
        bundle.putIntegerArrayList("selectedPositions", a);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        b bVar = this.c;
        if (bVar != null) {
            bVar.d();
            this.c = null;
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        RuningAcitvityUtil.putRuningActivity(this.that);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        RuningAcitvityUtil.removeRuningActivity(this.that.getComponentName().getClassName());
    }
}
