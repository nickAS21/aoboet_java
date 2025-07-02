package io.dcloud.feature.nativeObj;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.FrameBitmapView;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/* compiled from: NativeView.java */
/* loaded from: classes.dex old e*/
public class NativeView extends FrameLayout implements View.OnClickListener, INativeView, IWaiter {
    private int A;
    private float B;
    private RectF C;
    private JSONObject D;
    private int E;
    private int F;
    private int G;
    private int H;
    private boolean I;
    private int J;
    boolean a;
    String b;
    String c;
    JSONObject d;
    IApp e;
    Paint f;
    ArrayList<a> g;
    HashMap<String, Integer> h;
    boolean i;
    float j;
    float k;
    RectF l;
    JSONObject m;
    IWebview n;
    HashMap<String, HashMap<String, IWebview>> o;
    boolean p;
    boolean q;
    IFrameView r;
    IEventCallback s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;

    @Override // io.dcloud.common.DHInterface.INativeView
    public View obtanMainView() {
        return this;
    }

    public NativeView(Context context, IWebview iWebview, String str, String str2, JSONObject jSONObject) {
        super(context);
        this.a = false;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = new Paint();
        this.g = new ArrayList<>();
        this.h = new HashMap<>();
        this.B = 1.0f;
        this.i = false;
        this.I = false;
        this.l = null;
        this.m = null;
        this.J = 0;
        this.o = new HashMap<>(2);
        this.p = false;
        this.q = false;
        this.r = null;
        this.s = null;
        setWillNotDraw(false);
        this.e = iWebview.obtainApp();
        this.n = iWebview;
        this.B = iWebview.getScale();
        this.c = str;
        this.b = str2;
        this.d = jSONObject;
        c(1);
        setOnClickListener(this);
        setClickable(false);
        try {
            float optDouble = (float) jSONObject.optDouble(AbsoluteConst.JSON_KEY_OPACITY, 1.0d);
            if (Build.VERSION.SDK_INT >= 11) {
                setAlpha(optDouble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                if (this.d == null) {
                    this.d = jSONObject;
                } else {
                    Iterator<String> keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        this.d.put(next, jSONObject.getString(next));
                    }
                }
                requestLayout();
                invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", this.b);
            jSONObject.put("uuid", this.c);
            JSONObject jSONObject2 = this.d;
            if (jSONObject2 != null) {
                jSONObject.put("styles", jSONObject2.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public String getViewId() {
        return this.b;
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public String getViewUUId() {
        return this.c;
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public void setStyleLeft(int i) {
        try {
            this.d.put("left", (Float.valueOf(String.valueOf(i)).floatValue() / this.B) + "px");
            requestLayout();
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public int getStyleLeft() {
        return PdrUtil.convertToScreenInt(this.d.optString("left"), this.z, 0, this.B);
    }

    @Override // io.dcloud.common.DHInterface.INativeView
    public int getStyleWidth() {
        return this.x;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.l.contains(this.j, this.k)) {
            a("click");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str, IWebview iWebview, String str2) {
        HashMap<String, IWebview> hashMap = this.o.get(str);
        if (hashMap == null) {
            hashMap = new HashMap<>(2);
            this.o.put(str, hashMap);
        }
        boolean z = true;
        if (TextUtils.equals(str, "click")) {
            setClickable(true);
        }
        Iterator<String> it = this.o.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            HashMap<String, IWebview> hashMap2 = this.o.get(next);
            if (next != null && hashMap2.containsValue(iWebview)) {
                z = false;
                break;
            }
        }
        if (z) {
            iWebview.obtainFrameView().addFrameViewListener(new IEventCallback() { // from class: io.dcloud.feature.nativeObj.e.1
                @Override // io.dcloud.common.DHInterface.IEventCallback
                public Object onCallBack(String str3, Object obj) {
                    if (str3 != AbsoluteConst.EVENTS_CLOSE && str3 != AbsoluteConst.EVENTS_WINDOW_CLOSE) {
                        return null;
                    }
                    for (String str4 : NativeView.this.o.keySet()) {
                        HashMap<String, IWebview> hashMap3 = NativeView.this.o.get(str4);
                        if (!hashMap3.isEmpty()) {
                            Set<String> keySet = hashMap3.keySet();
                            int size = keySet.size();
                            String[] strArr = new String[size];
                            keySet.toArray(strArr);
                            for (int i = size - 1; i >= 0; i--) {
                                String str5 = strArr[i];
                                if (hashMap3.get(str5) == obj) {
                                    hashMap3.remove(str5);
                                }
                            }
                        }
                        if (hashMap3.isEmpty() && TextUtils.equals(str4, "click")) {
                            NativeView.this.setClickable(false);
                        }
                    }
                    ((IWebview) obj).obtainFrameView().removeFrameViewListener(this);
                    return null;
                }
            });
        }
        hashMap.put(str2, iWebview);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        this.i = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(JSONObject jSONObject) {
        this.m = jSONObject;
        a();
    }

    void a() {
        RectF rectF = new RectF(a(this, this.m));
        this.l = rectF;
        if (rectF.left == -2.14748365E9f || this.l.top == -2.14748365E9f) {
            float f = this.l.left;
            float f2 = this.l.left;
            float f3 = this.l.top;
            float f4 = this.l.right;
            float f5 = this.l.bottom;
            if (f == -2.14748365E9f) {
                float f6 = this.x;
                float f7 = this.l.right;
                int i = this.t;
                f2 = i + ((f6 - (f7 - i)) / 2.0f);
                f4 = f2 + this.l.right;
            }
            if (this.l.top == -2.14748365E9f) {
                float f8 = this.y;
                float f9 = this.l.bottom;
                int i2 = this.u;
                f3 = ((f8 - (f9 - i2)) / 2.0f) + i2;
                f5 = f3 + this.l.bottom;
            }
            this.l = new RectF(f2, f3, f4, f5);
        }
    }

    private boolean i() {
        HashMap<String, IWebview> hashMap = this.o.get("click");
        return (hashMap == null || hashMap.isEmpty()) ? false : true;
    }

    private boolean a(String str) {
        HashMap<String, IWebview> hashMap = this.o.get(str);
        boolean z = false;
        if (hashMap != null) {
            for (String str2 : hashMap.keySet()) {
                z = true;
                JSUtil.execCallback(hashMap.get(str2), str2, b(), JSUtil.OK, true, true);
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        this.t = PdrUtil.convertToScreenInt(this.d.optString("left"), this.z, 0, this.B);
        this.u = PdrUtil.convertToScreenInt(this.d.optString("top"), this.A, 0, this.B);
        String optString = this.d.optString(AbsoluteConst.JSON_KEY_WIDTH);
        int i = this.z;
        this.v = PdrUtil.convertToScreenInt(optString, i, i, this.B) + this.t;
        String optString2 = this.d.optString("height");
        int i2 = this.A;
        int convertToScreenInt = PdrUtil.convertToScreenInt(optString2, i2, i2, this.B);
        int i3 = this.u;
        int i4 = convertToScreenInt + i3;
        this.w = i4;
        this.x = this.v - this.t;
        this.y = i4 - i3;
        JSONObject jSONObject = this.d;
        if (jSONObject != null) {
            try {
                if (jSONObject.has("backgroudColor")) {
                    this.J = Color.parseColor(this.d.optString("backgroudColor"));
                } else if (this.d.has("backgroundColor")) {
                    this.J = Color.parseColor(this.d.optString("backgroundColor"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        a();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.I) {
            this.I = false;
        }
        postDelayed(new Runnable() { // from class: io.dcloud.feature.nativeObj.e.2
            @Override // java.lang.Runnable
            public void run() {
                NativeView eVar = NativeView.this;
                eVar.z = eVar.e.getInt(0);
                NativeView eVar2 = NativeView.this;
                eVar2.A = eVar2.e.getInt(1);
                NativeView.this.j();
                NativeView.this.l();
                NativeView.this.requestLayout();
            }
        }, 100L);
    }

    String b() {
        return String.format(Locale.ENGLISH, "{clientX:%d,clientY:%d,pageX:%d,pageY:%d,screenX:%d,screenY:%d}", Integer.valueOf((int) ((this.j - this.t) / this.B)), Integer.valueOf((int) ((this.k - this.u) / this.B)), Integer.valueOf((int) (this.j / this.B)), Integer.valueOf((int) (this.k / this.B)), Integer.valueOf((int) (this.j / this.B)), Integer.valueOf((int) (this.k / this.B)));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        this.j = motionEvent.getX();
        this.k = motionEvent.getY();
        if (2 == motionEvent.getAction() && (z = this.p)) {
            if (z) {
                a("touchmove");
            }
        } else if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.p = false;
        }
        if (!this.l.contains(this.j, this.k)) {
            return false;
        }
        if (this.i) {
            if (motionEvent.getAction() == 0) {
                this.p = true;
                a(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START);
            }
        } else if (1 == motionEvent.getAction()) {
            a("touchend");
        }
        if (this.i) {
            return i() ? super.onTouchEvent(motionEvent) : this.i;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(IFrameView iFrameView) {
        if (this.q && this.r != null) {
            if (iFrameView instanceof AdaFrameView) {
                ((AdaFrameView) iFrameView).removeNativeViewChild(this);
            }
            iFrameView.removeFrameViewListener(this.s);
            String optString = this.d.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE);
            ViewGroup viewGroup = null;
            if (TextUtils.equals(optString, "dock")) {
                viewGroup = (ViewGroup) ((AdaFrameItem) this.r).obtainMainView();
                ViewGroup viewGroup2 = (ViewGroup) this.r.obtainWebviewParent().obtainMainView();
                ViewGroup.LayoutParams layoutParams = viewGroup2.getLayoutParams();
                ViewOptions obtainFrameOptions = ((AdaFrameItem) this.r).obtainFrameOptions();
                int i = obtainFrameOptions.left;
                int i2 = obtainFrameOptions.top;
                int i3 = obtainFrameOptions.width;
                int i4 = obtainFrameOptions.height;
                if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
                    AbsoluteLayout.LayoutParams layoutParams2 = (AbsoluteLayout.LayoutParams) layoutParams;
                    layoutParams2.x = i;
                    layoutParams2.y = i2;
                }
                layoutParams.width = -1;
                layoutParams.height = -1;
                viewGroup2.layout(i, i2, i3 + i, i4 + i2);
            } else if (TextUtils.equals(optString, AbsoluteConst.JSON_VALUE_POSITION_STATIC)) {
                viewGroup = this.r.obtainWebView().obtainWebview();
            } else if (TextUtils.equals(optString, AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE)) {
                viewGroup = (ViewGroup) ((AdaFrameItem) this.r).obtainMainView();
            }
            if (viewGroup != null) {
                viewGroup.removeView(this);
            } else {
                ViewParent parent = getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this);
                }
            }
            this.q = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(IFrameView iFrameView) {
        if (this.q) {
            return;
        }
        this.r = iFrameView;
        if (iFrameView instanceof AdaFrameView) {
            ((AdaFrameView) iFrameView).addNativeViewChild(this);
        }
        final String obtainAppId = iFrameView.obtainApp().obtainAppId();
        IEventCallback iEventCallback = new IEventCallback() { // from class: io.dcloud.feature.nativeObj.e.3
            @Override // io.dcloud.common.DHInterface.IEventCallback
            public Object onCallBack(String str, Object obj) {
                if (TextUtils.equals(str, AbsoluteConst.EVENTS_CLOSE)) {
                    FeatureImpl.a(obtainAppId, NativeView.this);
                    return null;
                }
                if (!TextUtils.equals(str, AbsoluteConst.EVENTS_FRAME_ONRESIZE)) {
                    return null;
                }
                NativeView.this.c(0);
                NativeView.this.b(false);
                return null;
            }
        };
        this.s = iEventCallback;
        iFrameView.addFrameViewListener(iEventCallback);
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
            setVisibility(View.VISIBLE);
        }
        b(true);
        this.a = true;
        this.q = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        if (this.r == null) {
            return;
        }
        String optString = this.d.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE);
        if (TextUtils.equals(optString, "dock")) {
            ViewGroup viewGroup = (ViewGroup) ((AdaFrameItem) this.r).obtainMainView();
            ViewGroup viewGroup2 = (ViewGroup) this.r.obtainWebviewParent().obtainMainView();
            ViewGroup.LayoutParams layoutParams = viewGroup2.getLayoutParams();
            ViewOptions obtainFrameOptions = ((AdaFrameItem) this.r).obtainFrameOptions();
            int i = obtainFrameOptions.left;
            if (getTag() != null && "NavigationBar".equals(getTag().toString())) {
                i = 0;
            }
            int i2 = obtainFrameOptions.top;
            int i3 = z ? obtainFrameOptions.width : this.z;
            int i4 = z ? obtainFrameOptions.height : this.A;
            String optString2 = this.d.optString("dock");
            if (TextUtils.equals(optString2, "left")) {
                i = this.x;
                this.v = i;
                this.t = 0;
                i3 -= i;
            } else if (TextUtils.equals(optString2, "top")) {
                i2 = this.y;
                this.w = i2;
                this.u = 0;
                i4 -= i2;
                if (obtainFrameOptions.mJsonViewOption.has("top")) {
                    i4 = (i4 - obtainFrameOptions.bottom) - obtainFrameOptions.top;
                }
            } else if (TextUtils.equals(optString2, "right")) {
                int right = viewGroup.getRight();
                this.v = right;
                int i5 = this.x;
                this.t = right - i5;
                i3 -= i5;
            } else if (TextUtils.equals(optString2, "bottom")) {
                int bottom = viewGroup.getBottom();
                this.w = bottom;
                if (bottom == 0) {
                    this.w = obtainFrameOptions.top + obtainFrameOptions.height;
                }
                int i6 = this.w;
                int i7 = this.y;
                this.u = i6 - i7;
                i4 -= i7;
            }
            if (z) {
                viewGroup.addView(this);
            }
            if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
                AbsoluteLayout.LayoutParams layoutParams2 = (AbsoluteLayout.LayoutParams) layoutParams;
                layoutParams2.x = i;
                layoutParams2.y = i2;
            }
            layoutParams.width = i3;
            layoutParams.height = i4;
            viewGroup2.layout(i, i2, i3 + i, i4 + i2);
            return;
        }
        if (TextUtils.equals(optString, AbsoluteConst.JSON_VALUE_POSITION_STATIC)) {
            if (z) {
                this.r.obtainWebView().obtainWebview().addView(this);
            }
        } else if (TextUtils.equals(optString, AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) && z) {
            ((ViewGroup) ((AdaFrameItem) this.r).obtainMainView()).addView(this);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        c(1);
        b(false);
        if (this.n != null) {
            Iterator<a> it = this.g.iterator();
            while (it.hasNext()) {
                it.next().a(this.n);
            }
        }
        setMeasuredDimension(this.z, this.A);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Typeface create;
        canvas.save();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.clipRect(this.t, this.u, this.v, this.w);
        canvas.drawColor(this.J);
        RectF rectF = this.C;
        if (rectF != null) {
            canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        }
        Iterator<a> it = this.g.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.r.equals("clear")) {
                canvas.clipRect(next.h, Region.Op.DIFFERENCE);
            }
        }
        Iterator<a> it2 = this.g.iterator();
        while (it2.hasNext()) {
            a next2 = it2.next();
            this.f.reset();
            canvas.save();
            if (next2.f != null && next2.f.getBitmap() != null && !next2.f.d() && !next2.f.a()) {
                if (next2.h.left == Integer.MIN_VALUE || next2.h.top == Integer.MIN_VALUE) {
                    int i = next2.h.left;
                    int i2 = next2.h.left;
                    int i3 = next2.h.top;
                    int i4 = next2.h.right;
                    int i5 = next2.h.bottom;
                    if (i == Integer.MIN_VALUE) {
                        i2 = (((next2.h.right - this.t) - next2.g.width()) / 2) + this.t;
                        i4 = i2 + next2.g.width();
                    }
                    if (next2.h.top == Integer.MIN_VALUE) {
                        i3 = this.u + (((next2.h.bottom - this.u) - next2.g.height()) / 2);
                        i5 = i3 + next2.g.height();
                    }
                    canvas.drawBitmap(next2.f.getBitmap(), next2.g, new Rect(i2, i3, i4, i5), this.f);
                } else {
                    canvas.clipRect(next2.h);
                    canvas.drawBitmap(next2.f.getBitmap(), next2.g, next2.h, this.f);
                }
            } else if (next2.e != null) {
                canvas.clipRect(next2.h);
                this.f.reset();
                this.f.setAntiAlias(true);
                if (next2.j != 0) {
                    this.f.setColor(next2.j);
                }
                if (next2.i != 0.0f) {
                    this.f.setTextSize(next2.i);
                }
                if (!TextUtils.isEmpty(next2.o)) {
                    Typeface a2 = NativeTypefaceFactory.a(this.e, next2.o);
                    if (a2 != null) {
                        this.f.setTypeface(a2);
                    }
                } else if (!TextUtils.isEmpty(next2.m) && (create = Typeface.create(next2.m, Typeface.NORMAL)) != null) {
                    this.f.setTypeface(create);
                }
                this.f.setFakeBoldText(next2.k.equals(FrameBitmapView.BOLD));
                if (next2.l.equals(FrameBitmapView.ITALIC)) {
                    this.f.setTextSkewX(-0.5f);
                }
                float measureText = this.f.measureText(next2.e);
                float fontHeight = FrameBitmapView.getFontHeight(this.f);
                int width = next2.h.left + (((int) (next2.h.width() - measureText)) / 2);
                int height = next2.h.top + (((int) (next2.h.height() - fontHeight)) / 2);
                if (next2.n.equals("right")) {
                    width = (int) (next2.h.right - measureText);
                } else if (next2.n.equals("left")) {
                    width = next2.h.left;
                }
                CanvasHelper.drawString(canvas, next2.e, width, height, 17, this.f);
            } else if (next2.r.equals("rect")) {
                canvas.save();
                if (next2.h.left == Integer.MIN_VALUE || next2.h.top == Integer.MIN_VALUE) {
                    int i6 = next2.h.left;
                    int i7 = next2.h.left;
                    int i8 = next2.h.top;
                    int i9 = next2.h.right;
                    int i10 = next2.h.bottom;
                    if (i6 == Integer.MIN_VALUE) {
                        i7 = (this.x - (next2.h.right - this.t)) / 2;
                        i9 = i7 + next2.h.right;
                    }
                    if (next2.h.top == Integer.MIN_VALUE) {
                        i8 = (this.y - (next2.h.bottom - this.u)) / 2;
                        i10 = i8 + next2.h.bottom;
                    }
                    canvas.clipRect(i7, i8, i9, i10);
                    canvas.drawColor(next2.q);
                } else {
                    canvas.clipRect(next2.h);
                    canvas.drawColor(next2.q);
                }
                canvas.restore();
            }
            canvas.restore();
        }
        canvas.restore();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        RectF rectF = this.C;
        if (rectF != null) {
            canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        }
        super.dispatchDraw(canvas);
    }

    private int a(int i) {
        return this.t + i;
    }

    private int b(int i) {
        return this.u + i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a a(IWebview iWebview, NativeBitmap aVar, String str, int i, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, String str2, String str3) {
        return a(iWebview, aVar, str, i, jSONObject, jSONObject2, jSONObject3, str2, str3, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a a(IWebview iWebview, NativeBitmap aVar, String str, int i, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, String str2, String str3, boolean z) {
        a aVar2 = new a();
        this.n = iWebview;
        aVar2.a = this;
        aVar2.b = jSONObject;
        aVar2.c = jSONObject2;
        aVar2.d = jSONObject3;
        aVar2.f = aVar;
        aVar2.e = str;
        aVar2.q = i;
        aVar2.r = str3;
        if (!PdrUtil.isEmpty(str2)) {
            if (this.h.containsKey(str2)) {
                this.g.set(this.h.get(str2).intValue(), aVar2);
            } else {
                this.g.add(aVar2);
                this.h.put(str2, Integer.valueOf(this.g.indexOf(aVar2)));
            }
        } else {
            this.g.add(aVar2);
        }
        aVar2.a(this.n);
        if (str3.equals("img") && aVar != null && aVar.a()) {
            a(aVar2);
        }
        if (z) {
            c();
        }
        return aVar2;
    }

    public void c() {
        if (this.a) {
            requestLayout();
            postInvalidate();
        }
    }

    static Rect a(NativeView eVar, JSONObject jSONObject) {
        Rect rect = new Rect();
        if (jSONObject != null) {
            if ("auto".equals(jSONObject.optString("left"))) {
                rect.left = Integer.MIN_VALUE;
                String optString = jSONObject.optString(AbsoluteConst.JSON_KEY_WIDTH);
                int i = eVar.x;
                rect.right = PdrUtil.convertToScreenInt(optString, i, i, eVar.B);
            } else {
                rect.left = eVar.a(PdrUtil.convertToScreenInt(jSONObject.optString("left"), eVar.x, 0, eVar.B));
                String optString2 = jSONObject.optString(AbsoluteConst.JSON_KEY_WIDTH);
                int i2 = eVar.x;
                rect.right = PdrUtil.convertToScreenInt(optString2, i2, i2, eVar.B) + rect.left;
            }
            if ("auto".equals(jSONObject.optString("top"))) {
                rect.top = Integer.MIN_VALUE;
                String optString3 = jSONObject.optString("height");
                int i3 = eVar.y;
                rect.bottom = PdrUtil.convertToScreenInt(optString3, i3, i3, eVar.B);
            } else {
                rect.top = eVar.b(PdrUtil.convertToScreenInt(jSONObject.optString("top"), eVar.y, 0, eVar.B));
                String optString4 = jSONObject.optString("height");
                int i4 = eVar.y;
                rect.bottom = PdrUtil.convertToScreenInt(optString4, i4, i4, eVar.B) + rect.top;
            }
            int i5 = rect.right;
            int i6 = eVar.v;
            if (i5 <= i6) {
                i6 = rect.right;
            }
            rect.right = i6;
            int i7 = rect.bottom;
            int i8 = eVar.w;
            if (i7 <= i8) {
                i8 = rect.bottom;
            }
            rect.bottom = i8;
        } else {
            rect.left = eVar.t;
            rect.top = eVar.u;
            rect.right = eVar.v;
            rect.bottom = eVar.w;
        }
        return rect;
    }

    static Rect a(NativeView eVar, JSONObject jSONObject, NativeBitmap aVar) {
        Rect rect = new Rect();
        int width = aVar.getBitmap().getWidth();
        int height = aVar.getBitmap().getHeight();
        if (jSONObject != null) {
            rect.left = PdrUtil.convertToScreenInt(jSONObject.optString("left"), width, 0, eVar.B);
            rect.top = PdrUtil.convertToScreenInt(jSONObject.optString("top"), height, 0, eVar.B);
            rect.right = PdrUtil.convertToScreenInt(jSONObject.optString(AbsoluteConst.JSON_KEY_WIDTH), width, width, eVar.B) + rect.left;
            if (rect.right <= width) {
                width = rect.right;
            }
            rect.right = width;
            rect.bottom = PdrUtil.convertToScreenInt(jSONObject.optString("height"), height, height, eVar.B) + rect.top;
            if (rect.bottom <= height) {
                height = rect.bottom;
            }
            rect.bottom = height;
        } else {
            rect.left = 0;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
        }
        return rect;
    }

    public void a(IWebview iWebview, String str, String str2) throws Exception {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (getParent() == null) {
            a(iWebview, str2);
            return;
        }
        this.I = true;
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("type");
        int optInt = jSONObject.optInt(AbsoluteConst.TRANS_DURATION, 200);
        int optInt2 = jSONObject.optInt("frames", 12);
        this.D = jSONObject.optJSONObject("region");
        k();
        int i = optInt / optInt2;
        int i2 = this.w;
        int i3 = this.G;
        int i4 = this.u;
        int i5 = this.H;
        int i6 = (i2 - ((i3 + i4) + i5)) / optInt2;
        int i7 = (i2 - ((i3 + i4) + i5)) - (i6 * optInt2);
        if (TextUtils.isEmpty(optString) || !optString.equals("shrink")) {
            return;
        }
        a(iWebview, str2, this.E, this.F, this.u + this.G, this.w - this.H, i, i6, optInt2, i7, 1);
    }

    private void k() {
        JSONObject jSONObject = this.D;
        if (jSONObject != null) {
            this.E = PdrUtil.convertToScreenInt(jSONObject.optString("left"), this.x, 0, this.B);
            this.F = PdrUtil.convertToScreenInt(this.D.optString("right"), this.x, 0, this.B);
            this.G = PdrUtil.convertToScreenInt(this.D.optString("top"), this.y, 0, this.B);
            this.H = PdrUtil.convertToScreenInt(this.D.optString("bottom"), this.y, 0, this.B);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        if (this.C != null) {
            k();
            this.C.left = this.E;
            this.C.right = this.z - this.F;
            this.C.top = this.G + this.u;
            this.C.bottom = this.w - this.H;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(final IWebview iWebview, final String str, final int i, final int i2, final int i3, final int i4, final int i5, final int i6, final int i7, final int i8, final int i9) {
        if (!this.I) {
            a(iWebview, str);
            return;
        }
        if (this.C == null) {
            this.C = new RectF();
        }
        this.C.left = i;
        this.C.right = this.z - i2;
        this.C.top = i3;
        if (i9 == i7) {
            this.C.bottom = (i6 * i9) + i3 + i8;
        } else {
            this.C.bottom = (i6 * i9) + i3;
        }
        postDelayed(new Runnable() { // from class: io.dcloud.feature.nativeObj.e.4
            @Override // java.lang.Runnable
            public void run() {
                NativeView.this.invalidate();
                int i10 = i9;
                int i11 = i7;
                if (i10 == i11) {
                    NativeView.this.a(iWebview, str);
                } else {
                    NativeView.this.a(iWebview, str, i, i2, i3, i4, i5, i6, i11, i8, i10 + 1);
                }
            }
        }, i5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IWebview iWebview, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        JSUtil.execCallback(iWebview, str, null, JSUtil.OK, false, false);
    }

    @Override // io.dcloud.common.DHInterface.IWaiter
    public Object doForFeature(String str, Object obj) {
        if (str.equals("clearAnimate")) {
            f();
            return null;
        }
        if (!str.equals("checkTouch")) {
            return null;
        }
        MotionEvent motionEvent = (MotionEvent) obj;
        RectF rectF = this.l;
        if (rectF != null && rectF.contains(motionEvent.getX(), motionEvent.getY())) {
            return Boolean.valueOf(this.i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: NativeView.java */
    /* loaded from: classes.dex */
    public static class a {
        NativeView a;
        JSONObject b;
        JSONObject c;
        JSONObject d;
        String e;
        NativeBitmap f;
        Rect g;
        Rect h;
        float i;
        int j = -16777216;
        String k = FrameBitmapView.NORMAL;
        String l = FrameBitmapView.NORMAL;
        String m = "";
        String n = AbsoluteConst.JSON_VALUE_CENTER;
        String o = null;
        int p = 0;
        int q;
        String r;

        a() {
        }

        void a(IWebview iWebview) {
            NativeBitmap aVar = this.f;
            if (aVar != null) {
                this.g = NativeView.a(this.a, this.b, aVar);
            }
            this.h = NativeView.a(this.a, this.c);
            JSONObject jSONObject = this.d;
            if (jSONObject != null) {
                String optString = jSONObject.optString(AbsoluteConst.JSON_KEY_COLOR);
                if (!TextUtils.isEmpty(optString) && !optString.equals("null")) {
                    this.j = PdrUtil.stringToColor(optString);
                }
                String optString2 = this.d.optString("size");
                if (TextUtils.isEmpty(optString2)) {
                    optString2 = "16px";
                }
                this.i = PdrUtil.convertToScreenInt(optString2, this.a.x, 0, this.a.B);
                this.k = this.d.optString("weight", this.k);
                String optString3 = this.d.optString("style", this.l);
                this.l = optString3;
                this.m = this.d.optString("family", optString3);
                this.n = this.d.optString(AbsoluteConst.JSON_KEY_ALIGN, this.n);
                if (this.d.has(IApp.ConfigProperty.CONFIG_SRC)) {
                    this.o = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), this.d.optString(IApp.ConfigProperty.CONFIG_SRC, null));
                }
                int convertToScreenInt = PdrUtil.convertToScreenInt(this.d.optString(AbsoluteConst.JSON_KEY_MARGIN, "0px"), this.a.x, 0, this.a.B);
                this.p = convertToScreenInt;
                Rect rect = this.h;
                if (rect == null || convertToScreenInt == 0) {
                    return;
                }
                rect.left += this.p;
                this.h.top += this.p;
                this.h.right -= this.p;
                this.h.bottom -= this.p;
            }
        }
    }

    public void d() {
        if (getTag() != null && "NavigationBar".equals(getTag().toString())) {
            e();
        } else {
            h();
            f();
        }
    }

    public void e() {
        IFrameView iFrameView = this.r;
        if (iFrameView != null) {
            try {
                String valueOf = String.valueOf(iFrameView.obtainWebView().obtainWebview().hashCode());
                int i = -1;
                for (String str : this.h.keySet()) {
                    if (!valueOf.equals(str)) {
                        this.h.remove(str);
                    } else {
                        i = this.h.get(str).intValue();
                    }
                }
                if (-1 != i) {
                    a aVar = this.g.get(i);
                    Iterator<a> it = this.g.iterator();
                    while (it.hasNext()) {
                        a next = it.next();
                        if (next != aVar) {
                            if (next.f != null) {
                                next.f.a(true);
                            }
                            it.remove();
                        }
                    }
                } else {
                    h();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            h();
        }
        f();
    }

    public void f() {
        this.I = false;
        this.C = null;
        this.D = null;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(int i) {
        int i2;
        int i3;
        IApp iApp = this.e;
        if (iApp != null) {
            View obtainMainView = iApp.obtainWebAppRootView() != null ? this.e.obtainWebAppRootView().obtainMainView() : null;
            if (obtainMainView == null) {
                Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "isTitlebarVisible", null, new Class[]{Activity.class, String.class}, new Object[]{this.e.getActivity(), this.e.obtainAppId()});
                if (invokeMethod instanceof Boolean ? Boolean.valueOf(invokeMethod.toString()).booleanValue() : false) {
                    Object invokeMethod2 = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "getTitlebarHeightPx", null, new Class[]{Activity.class}, new Object[]{this.e.getActivity()});
                    if (invokeMethod2 instanceof Integer) {
                        i3 = Integer.valueOf(invokeMethod2.toString()).intValue();
                        i2 = i3;
                        obtainMainView = this.e.getActivity().getWindow().getDecorView().findViewById(R.id.content);
                    }
                }
                i3 = 0;
                i2 = i3;
                obtainMainView = this.e.getActivity().getWindow().getDecorView().findViewById(R.id.content);
            } else {
                i2 = 0;
            }
            if (obtainMainView != null) {
                this.e.getActivity().getWindowManager();
                int i4 = this.e.getInt(0);
                this.A = this.e.getInt(1) - i2;
                this.z = i4 - i2;
                j();
            }
        }
    }

    public void g() {
        setVisibility(View.GONE);
        h();
    }

    public void h() {
        Iterator<a> it = this.g.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.r.equals("img") && next.f != null && next.f.getBitmap() != null && !next.f.d()) {
                next.f.a(true);
            }
        }
        this.g.clear();
        this.h.clear();
    }

    public void a(a aVar) {
        GifDrawable g = aVar.f.g();
        if (g != null) {
            View gifImageView = new GifImageView(this.e.getActivity());
            ((GifImageView)gifImageView).setImageDrawable(g);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(aVar.h.width(), aVar.h.height());
            if (aVar.h.left != Integer.MIN_VALUE) {
                layoutParams.leftMargin = aVar.h.left;
            } else {
                layoutParams.width = aVar.h.right;
                layoutParams.leftMargin = ((this.x - aVar.h.right) + this.t) / 2;
            }
            if (aVar.h.top != Integer.MIN_VALUE) {
                layoutParams.topMargin = aVar.h.top;
            } else {
                layoutParams.height = aVar.h.bottom;
                layoutParams.topMargin = ((this.y - aVar.h.bottom) + this.u) / 2;
            }
            addView(gifImageView, layoutParams);
        }
        requestLayout();
        invalidate();
    }
}
