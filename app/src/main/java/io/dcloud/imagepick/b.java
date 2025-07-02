package io.dcloud.imagepick;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.RInformation;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* compiled from: GalleryAdapter.java */
/* loaded from: classes.dex */
public class b extends BaseAdapter {
    private LayoutInflater b;
    private ArrayList<String> e;
    private boolean f;
    private AbsListView.LayoutParams g;
    private DisplayImageOptions h;
    private int a = 3;
    private ArrayList<io.dcloud.imagepick.a> c = new ArrayList<>();
    private ArrayList<io.dcloud.imagepick.a> d = new ArrayList<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public b(Context context, ImageLoader imageLoader, int i) {
        this.b = (LayoutInflater) context.getSystemService("layout_inflater");
        a(context, i);
        this.h = new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(DCloudAdapterUtil.getImagePickNoMediaId(context)).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void a(Context context, int i) {
        int dip2px = CanvasHelper.dip2px(context, 2.0f);
        int i2 = this.a;
        int i3 = (i - (dip2px * (i2 - 1))) / i2;
        this.g = new AbsListView.LayoutParams(i3, i3);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.c.size();
    }

    @Override // android.widget.Adapter
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public io.dcloud.imagepick.a getItem(int i) {
        return this.c.get(i);
    }

    public void a(boolean z) {
        this.f = z;
    }

    public ArrayList<Integer> a() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < this.d.size(); i++) {
            if (this.d.get(i).b) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    public int b() {
        return this.d.size();
    }

    public ArrayList<io.dcloud.imagepick.a> c() {
        ArrayList<io.dcloud.imagepick.a> arrayList = new ArrayList<>();
        for (int i = 0; i < this.d.size(); i++) {
            if (this.d.get(i).b) {
                arrayList.add(this.d.get(i));
            }
        }
        return arrayList;
    }

    public void a(ArrayList<Integer> arrayList) {
        Iterator<Integer> it = arrayList.iterator();
        while (it.hasNext()) {
            io.dcloud.imagepick.a aVar = this.c.get(it.next().intValue());
            if (aVar != null) {
                aVar.b = true;
                this.d.add(aVar);
            }
        }
    }

    public void a(ArrayList<io.dcloud.imagepick.a> arrayList, ArrayList<String> arrayList2) {
        try {
            this.c.clear();
            this.c.addAll(arrayList);
            this.e = arrayList2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetInvalidated();
    }

    public boolean a(View view, int i) {
        io.dcloud.imagepick.a aVar = this.c.get(i);
        if (aVar.b) {
            aVar.b = false;
            if (this.d.contains(aVar)) {
                this.d.remove(aVar);
            }
        } else {
            aVar.b = true;
            this.d.add(aVar);
        }
        a aVar2 = (a) view.getTag();
        if (this.c.get(i).b) {
            aVar2.b.setVisibility(0);
        } else {
            aVar2.b.setVisibility(8);
        }
        return aVar.b;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        a aVar;
        if (view == null) {
            view = this.b.inflate(RInformation.LAYOUT_IMAGE_PICK_GALLERY_ITEM, (ViewGroup) null);
            aVar = new a();
            aVar.a = (ImageView) view.findViewById(RInformation.ID_IMAGE_PICK_IMG_QUEUE);
            aVar.b = (ImageView) view.findViewById(RInformation.ID_IMAGE_PICK_MASK);
            view.setTag(aVar);
            view.setLayoutParams(this.g);
        } else {
            aVar = (a) view.getTag();
        }
        aVar.a.setTag(Integer.valueOf(i));
        try {
            Log.d("shutao", this.c.get(i).a);
            a(this.c.get(i));
            ImageLoader.getInstance().displayImage(DeviceInfo.FILE_PROTOCOL + this.c.get(i).a, aVar.a, this.h);
            if (this.f) {
                if (this.c.get(i).b) {
                    aVar.b.setVisibility(0);
                } else {
                    aVar.b.setVisibility(8);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /* compiled from: GalleryAdapter.java */
    /* loaded from: classes.dex */
    public class a {
        ImageView a;
        ImageView b;

        public a() {
        }
    }

    public void d() {
        e();
    }

    public void e() {
        ArrayList<io.dcloud.imagepick.a> arrayList = this.c;
        if (arrayList != null) {
            arrayList.clear();
            this.d.clear();
            notifyDataSetChanged();
        }
    }

    public void a(io.dcloud.imagepick.a aVar) {
        ArrayList<String> arrayList;
        if (aVar.b || this.d.contains(aVar) || (arrayList = this.e) == null) {
            return;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (aVar.a.equals(next)) {
                aVar.b = true;
                this.e.remove(next);
                this.d.add(aVar);
                return;
            }
        }
    }
}
