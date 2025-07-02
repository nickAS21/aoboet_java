package io.dcloud.feature.ui.nativeui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: WaitingView.java old b */
/* loaded from: classes.dex */
public class WaitingView implements PopupWindow.OnDismissListener, ISysEventListener {
    public static final String a = "b";
    private int A;
    private int B;
    private long D;
    private String E;
    private String G;
    private AnimationDrawable H;
    private Bitmap I;
    private int J;
    public String b;
    LinearLayout c;
    private Context d;
    private NativeUIFeatureImpl e;
    private IWebview f;
    private IApp g;
    private String h;
    private PopupWindow j;
    private TextView k;
    private View l;
    private ProgressBar m;
    private ImageView n;
    private String o;
    private String p;
    private LinearLayout q;
    private String r;
    private String s;
    private String u;
    private int v;
    private int w;
    private View y;
    private int z;
    private String i = AbsoluteConst.EVENTS_CLOSE;
    private boolean t = true;
    private int x = -872415232;
    private boolean C = false;
    private int F = -1;
    private int K = -2;
    private int L = -2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WaitingView(NativeUIFeatureImpl nativeUIFeatureImpl, IWebview iWebview, String str, JSONObject jSONObject, String str2) {
        this.e = nativeUIFeatureImpl;
        this.f = iWebview;
        this.g = iWebview.obtainApp();
        this.h = str2;
        this.d = iWebview.getContext();
        this.y = ((AdaFrameItem) iWebview.obtainFrameView()).obtainMainView();
        this.z = this.g.getInt(0);
        this.A = this.g.getInt(1);
        a(iWebview, jSONObject);
        c();
        a(this.q);
        b(str);
        d();
    }

    private void a(IWebview iWebview, JSONObject jSONObject) {
        if (!JSONUtil.isNull(jSONObject, "background")) {
            this.x = PdrUtil.stringToColor(JSONUtil.getString(jSONObject, "background"));
        }
        String modalValue = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_MODAL);
        if (!PdrUtil.isEmpty(modalValue)) {
            this.t = !PdrUtil.isEquals(AbsoluteConst.FALSE, modalValue);
        }
        float scale = iWebview.getScale();
        this.B = (int) (PdrUtil.parseInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_ROUND), 10) * scale);
        String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_PADLOCK);
        if (!PdrUtil.isEmpty(string)) {
            this.C = Boolean.valueOf(string).booleanValue() | this.C;
        }
        this.K = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_WIDTH), this.z, this.K, scale);
        this.L = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "height"), this.A, this.L, scale);
        String string2 = JSONUtil.getString(jSONObject, "back");
        if (!TextUtils.isEmpty(string2)) {
            this.i = string2;
        }
        this.u = JSONUtil.getString(jSONObject, "style");
        if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_COLOR)) {
            this.o = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_COLOR);
        }
        this.r = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_PADDIN);
        if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_PADDING)) {
            String string3 = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_PADDING);
            if (string3.indexOf(37) >= 0) {
                this.v = PdrUtil.convertToScreenInt(string3, this.z, this.v, scale);
                this.w = PdrUtil.convertToScreenInt(string3, this.A, this.w, scale);
            } else {
                int convertToScreenInt = PdrUtil.convertToScreenInt(string3, this.z, this.w, scale);
                this.w = convertToScreenInt;
                this.v = convertToScreenInt;
            }
        } else {
            int parseInt = PdrUtil.parseInt(this.r, this.z, PdrUtil.parseInt("3%", this.z, 0));
            this.w = parseInt;
            this.v = parseInt;
        }
        if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_TEXTALIGN)) {
            this.p = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_TEXTALIGN);
        }
        JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONObject, "loading");
        if (jSONObject2 != null) {
            this.s = JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_DISPLAY);
            this.D = JSONUtil.getLong(jSONObject2, AbsoluteConst.JSON_KEY_INTERVAL);
            this.E = JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_ICON);
            this.F = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "height"), this.z, -1, scale);
        }
        if (!TextUtils.isEmpty(this.E)) {
            this.G = this.g.convert2AbsFullPath(this.f.obtainFullUrl(), this.E);
        }
        this.J = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "size"), this.z, 0, scale);
    }

    private void c() {
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) this.d.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RInformation.LAYOUT_DIALOG_LAYOUT_LOADING_DCLOUD, (ViewGroup) null, false);
        this.c = linearLayout;
        LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(RInformation.DCLOUD_LOADING_LAYOUT_ROOT);
        this.q = linearLayout2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.width = this.K;
        layoutParams.height = this.L;
        this.q.setLayoutParams(layoutParams);
    }

    private void a(View view) {
        this.k = (TextView) view.findViewById(RInformation.ID_TEXT_LOADING_DCLOUD);
        this.m = (ProgressBar) view.findViewById(RInformation.ID_PROGRESSBAR_LOADING_DCLOUD);
        this.n = (ImageView) view.findViewById(RInformation.ID_IMAGE_LOADING_DCLOUD);
        this.l = view.findViewById(RInformation.ID_WAITING_SEPARATOR_DCLOUD);
    }

    private void b(String str) {
        f();
        e();
        g();
        j();
        a(str);
        h();
        i();
    }

    private void d() {
        int i = -1;
        int i2 = -2;
        if (Build.VERSION.SDK_INT < 23 || !this.t) {
            i = -2;
        } else {
            this.t = false;
            i2 = -1;
        }
        if (!this.t || (!TextUtils.isEmpty(this.i) && !AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(this.i))) {
            this.g.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
        }
        PopupWindow popupWindow = new PopupWindow(this.c, i, i2, this.t);
        this.j = popupWindow;
        popupWindow.showAtLocation(this.y, 17, 0, 0);
        this.j.setOnDismissListener(this);
        this.j.setBackgroundDrawable(new BitmapDrawable());
        this.j.setOutsideTouchable(true);
        this.j.setTouchInterceptor(new View.OnTouchListener() { // from class: io.dcloud.feature.ui.nativeui.b.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return WaitingView.this.t;
            }
        });
        AnimationDrawable animationDrawable = this.H;
        if (animationDrawable == null || animationDrawable.isRunning()) {
            return;
        }
        this.H.start();
    }

    private void e() {
        this.k.setTextColor(!PdrUtil.isEmpty(this.o) ? PdrUtil.stringToColor(this.o) : -1);
        if (!PdrUtil.isEmpty(this.p)) {
            if ("left".equals(this.p)) {
                this.k.setGravity(3);
            } else if ("right".equals(this.p)) {
                this.k.setGravity(5);
            } else {
                this.k.setGravity(17);
            }
        } else {
            this.k.setGravity(17);
        }
        int i = this.J;
        if (i > 0) {
            this.k.setTextSize(0, i);
        }
    }

    private void f() {
        if (AbsoluteConst.JSON_VALUE_BLOCK.equalsIgnoreCase(this.s)) {
            this.q.setOrientation(LinearLayout.VERTICAL);
            return;
        }
        if (AbsoluteConst.JSON_VALUE_INLINE.equalsIgnoreCase(this.s)) {
            this.q.setOrientation(LinearLayout.HORIZONTAL);
        } else if ("none".equalsIgnoreCase(this.s)) {
            this.l.setVisibility(View.GONE);
            this.m.setVisibility(View.GONE);
        }
    }

    private void g() {
        Drawable drawable;
        if (PdrUtil.isEquals(this.u, "black")) {
            drawable = this.d.getResources().getDrawable(RInformation.DRAWBLE_PROGRESSBAR_BLACK_DCLOUD);
        } else {
            drawable = this.d.getResources().getDrawable(RInformation.DRAWBLE_PROGRESSBAR_WHITE_DCLOUD);
        }
        if (this.F > 0) {
            ProgressBar progressBar = this.m;
            int i = this.F;
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(i, i));
        } else {
            int intrinsicHeight = (int) (drawable.getIntrinsicHeight() * 0.3d);
            this.m.setLayoutParams(new LinearLayout.LayoutParams(intrinsicHeight, intrinsicHeight));
        }
        this.m.setIndeterminateDrawable(drawable);
    }

    private void h() {
        this.q.setFocusable(true);
        this.q.setFocusableInTouchMode(true);
        this.q.setOnKeyListener(new View.OnKeyListener() { // from class: io.dcloud.feature.ui.nativeui.b.2
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == 0 && i == 4) {
                    if (!AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(WaitingView.this.i)) {
                        if ("transmit".equalsIgnoreCase(WaitingView.this.i)) {
                            if (WaitingView.this.f.canGoBack()) {
                                WaitingView.this.f.goBackOrForward(-1);
                            } else {
                                WaitingView.this.f.getActivity().onBackPressed();
                            }
                            return false;
                        }
                        if ("none".equalsIgnoreCase(WaitingView.this.i)) {
                            return true;
                        }
                    } else {
                        WaitingView.this.a();
                        WaitingView.this.b();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void i() {
        LinearLayout linearLayout = this.q;
        int i = this.v;
        int i2 = this.w;
        linearLayout.setPadding(i, i2, i, i2);
        GradientDrawable gradientDrawable = (GradientDrawable) this.q.getBackground();
        int i3 = this.B;
        if (i3 > 0) {
            gradientDrawable.setCornerRadius(i3);
        }
        gradientDrawable.setColor(this.x);
        if (this.C) {
            this.q.setOnClickListener(new View.OnClickListener() { // from class: io.dcloud.feature.ui.nativeui.b.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    WaitingView.this.a();
                    WaitingView.this.b();
                }
            });
        }
    }

    private int a(int i) {
        int min = Math.min(this.z, this.A);
        int i2 = this.v;
        int i3 = (min - i2) - i2;
        if (i3 <= 0 || i <= i3) {
            return 1;
        }
        return i / i3;
    }

    private void j() {
        if (TextUtils.isEmpty(this.G)) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bArr = new byte[0];
        try {
            bArr = a(this.g.obtainResInStream(this.G));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int i = options.outWidth;
        int i2 = options.outHeight;
        if (i == 0 || i2 == 0 || i % i2 != 0) {
            return;
        }
        options.inSampleSize = a(i2);
        options.inJustDecodeBounds = false;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        this.I = decodeByteArray;
        if (decodeByteArray != null) {
            int width = decodeByteArray.getWidth();
            int height = this.I.getHeight();
            int i3 = width / height;
            if (this.D <= 0) {
                this.D = 100L;
            }
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int i4 = 0; i4 < i3; i4++) {
                animationDrawable.addFrame(new BitmapDrawable(Bitmap.createBitmap(this.I, i4 * height, 0, height, height)), (int) this.D);
            }
            animationDrawable.setOneShot(false);
            ViewGroup.LayoutParams layoutParams = this.n.getLayoutParams();
            if (layoutParams != null) {
                int i5 = this.F;
                if (i5 > 0) {
                    height = i5;
                }
                layoutParams.width = height;
                layoutParams.height = height;
                this.n.setLayoutParams(layoutParams);
            }
            this.m.setVisibility(View.GONE);
            this.n.setVisibility(View.VISIBLE);
            this.n.setBackground(animationDrawable);
            this.H = (AnimationDrawable) this.n.getBackground();
        }
    }

    private byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                inputStream.close();
                byteArrayOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        PopupWindow popupWindow = this.j;
        if (popupWindow == null || !popupWindow.isShowing()) {
            return;
        }
        this.j.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str) {
        String trim = this.k.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            if (trim.length() == str.length()) {
                if (this.k.getLayoutParams() != null) {
                    this.k.setLayoutParams(new LinearLayout.LayoutParams(this.k.getWidth(), this.k.getHeight()));
                }
            } else {
                this.k.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            }
        }
        if (TextUtils.isEmpty(str)) {
            this.l.setVisibility(View.GONE);
            this.k.setVisibility(View.GONE);
        } else {
            this.k.setText(str);
        }
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        JSUtil.execCallback(this.f, this.h, null, JSUtil.OK, false, false);
        this.j = null;
        if (!this.t || (!TextUtils.isEmpty(this.i) && !AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(this.i))) {
            this.g.unregisterSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
        }
        Bitmap bitmap = this.I;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        this.I.recycle();
        System.gc();
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType != ISysEventListener.SysEventType.onKeyUp || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
            return false;
        }
        if ("none".equalsIgnoreCase(this.i)) {
            return true;
        }
        if ("transmit".equalsIgnoreCase(this.i)) {
            return false;
        }
        a();
        b();
        return true;
    }

    public void b() {
        this.e.showDatePicker(this.b);
        this.g.unregisterSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
    }
}
