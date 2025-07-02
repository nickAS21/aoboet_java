package io.dcloud.feature.ui.nativeui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;

/* compiled from: ActionSheet.java */
/* loaded from: classes.dex old a */
public class ActionSheet extends Dialog implements View.OnClickListener {

    private static final int ID_VIEW_10 = 10;
    private static final int ID_VIEW_100 = 100;
    private static final int ID_VIEW_200 = 200;
    private static final int ID_TEXT_VIEW_100 = 100;
    private static final int ID_TEXT_VIEW_200 = 200;
    private Context a;
    private IActionSheet listenerSelected;
    private View veiw_c;
    private LinearLayout d;
    private View view;
    private List<C0009a> f;
    private String g;
    private String h;
    private boolean i;
    private boolean j;
    private boolean k;
    private IWebview l;

    /* compiled from: ActionSheet.java */
    /* loaded from: classes.dex  old b*/
    public interface IActionSheet {
        void a(int i);
    }

    public ActionSheet(Context context, IWebview iWebview, int i) {
        super(context, i);
        this.g = "";
        this.h = "";
        this.j = true;
        this.k = true;
        this.a = context;
        this.l = iWebview;
        a();
        getWindow().setGravity(80);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(0);
        getWindow().setBackgroundDrawable(colorDrawable);
        setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: io.dcloud.feature.ui.nativeui.a.1
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                ActionSheet.this.d();
                if (ActionSheet.this.listenerSelected != null) {
                    ActionSheet.this.listenerSelected.a(-1);
                }
                ActionSheet.this.k = false;
            }
        });
    }

    public void a() {
        View currentFocus;
        InputMethodManager inputMethodManager = (InputMethodManager) this.a.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive() && (currentFocus = ((Activity) this.a).getCurrentFocus()) != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        this.veiw_c = i();
        this.view.startAnimation(f());
        this.d.startAnimation(e());
    }

    private Animation e() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setDuration(300L);
        return translateAnimation;
    }

    private Animation f() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300L);
        return alphaAnimation;
    }

    private Animation g() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        translateAnimation.setDuration(300L);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private Animation h() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    private View i() {
        FrameLayout frameLayout = new FrameLayout(this.a);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 80;
        frameLayout.setLayoutParams(layoutParams);
        View view = new View(this.a);
        this.view = view;
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.view.setBackgroundColor(Color.argb(136, 0, 0, 0));
        this.view.setId(ID_VIEW_10);
        this.view.setOnClickListener(this);
        this.d = new LinearLayout(this.a);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        layoutParams2.gravity = 80;
        this.d.setLayoutParams(layoutParams2);
        this.d.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(this.view);
        frameLayout.addView(this.d);
        return frameLayout;
    }

    private void j() {
        boolean z;
        LinearLayout linearLayout;
        int intrinsicHeight = k().getIntrinsicHeight();
        int size = this.f.size() * intrinsicHeight;
        String str = this.g;
        if (str != null) {
            size += intrinsicHeight;
        }
        if (this.h != null) {
            size += intrinsicHeight;
        }
        if (str != null) {
            TextView textView = new TextView(this.a);
            textView.setGravity(17);
            textView.setId(ID_TEXT_VIEW_200);
            textView.setOnClickListener(this);
            textView.setBackgroundDrawable(k());
            textView.setText(this.g);
            textView.setTextColor(Color.parseColor("#8C8C8C"));
            textView.setTextSize(2, 16.0f);
            LinearLayout.LayoutParams b2 = b();
            b2.topMargin = 0;
            this.d.addView(textView, b2);
            z = true;
        } else {
            z = false;
        }
        ScrollView scrollView = null;
        if (size > this.l.obtainApp().getInt(1)) {
            scrollView = new ScrollView(this.a);
            linearLayout = new LinearLayout(this.a);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            scrollView.addView(linearLayout, new LinearLayout.LayoutParams(-1, -2));
        } else {
            linearLayout = this.d;
        }
        List<C0009a> list = this.f;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < this.f.size(); i++) {
                TextView textView2 = new TextView(this.a);
                textView2.setGravity(17);
                textView2.setId(i + 100 + 1);
                textView2.setOnClickListener(this);
                textView2.setBackgroundDrawable(a(this.f.size(), i, z));
                textView2.setText(this.f.get(i).a);
                if (this.f.get(i).b.equals("destructive")) {
                    textView2.setTextColor(Color.parseColor("#E8484A"));
                } else {
                    textView2.setTextColor(Color.parseColor("#000000"));
                }
                textView2.setTextSize(2, 16.0f);
                if (i > 0) {
                    LinearLayout.LayoutParams b3 = b();
                    b3.topMargin = 0;
                    linearLayout.addView(textView2, b3);
                } else {
                    linearLayout.addView(textView2);
                }
            }
        }
        if (this.h != null) {
            TextView textView3 = new TextView(this.a);
            textView3.setGravity(17);
            textView3.getPaint().setFakeBoldText(true);
            textView3.setTextSize(2, 16.0f);
            textView3.setId(ID_TEXT_VIEW_100);
            textView3.setBackgroundDrawable(this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_CANCEL_BT));
            textView3.setText(this.h);
            textView3.setTextColor(Color.parseColor("#000000"));
            textView3.setOnClickListener(this);
            LinearLayout.LayoutParams b4 = b();
            b4.topMargin = a(10);
            if (size > this.l.obtainApp().getInt(1)) {
                int i2 = this.l.obtainApp().getInt(1);
                if (this.g != null) {
                    i2 -= k().getIntrinsicHeight();
                }
                if (this.h != null) {
                    i2 -= this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_CANCEL_BT).getIntrinsicHeight() + (a(10) * 3);
                }
                this.d.addView(scrollView, new LinearLayout.LayoutParams(-1, i2));
            }
            this.d.addView(textView3, b4);
        } else if (size > this.l.obtainApp().getInt(1)) {
            this.d.addView(scrollView, new LinearLayout.LayoutParams(-1, -1));
        }
        int a = a(10);
        this.d.setBackgroundDrawable(new ColorDrawable(0));
        this.d.setPadding(a, a, a, a);
    }

    private int a(int i) {
        return (int) TypedValue.applyDimension(1, i, this.a.getResources().getDisplayMetrics());
    }

    public LinearLayout.LayoutParams b() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private Drawable k() {
        return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_TITLE);
    }

    private Drawable a(int i, int i2, boolean z) {
        if (i == 1) {
            if (z) {
                return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_BOTTOM);
            }
            return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_SINGLE);
        }
        if (i == 2) {
            if (i2 != 0) {
                if (i2 != 1) {
                    return null;
                }
                return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_BOTTOM);
            }
            if (z) {
                return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_MIDDLE);
            }
            return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_TOP);
        }
        if (i <= 2) {
            return null;
        }
        if (i2 == 0) {
            if (z) {
                return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_MIDDLE);
            }
            return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_TOP);
        }
        if (i2 == i - 1) {
            return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_BOTTOM);
        }
        return this.a.getResources().getDrawable(RInformation.SLT_AS_IOS7_OTHER_BT_MIDDLE);
    }

    public void c() {
        if (this.j) {
            show();
            getWindow().setContentView(this.veiw_c);
            this.j = false;
        }
    }

    public void d() {
        if (this.j) {
            return;
        }
        dismiss();
        l();
        this.j = true;
    }

    private void l() {
        this.d.startAnimation(g());
        this.view.startAnimation(h());
    }

    public ActionSheet a(String str) {
        this.h = str;
        return this;
    }

    public ActionSheet b(String str) {
        this.g = str;
        return this;
    }

    public ActionSheet a(boolean z) {
        this.i = z;
        return this;
    }

    public ActionSheet a(JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.length() != 0) {
            this.f = new ArrayList();
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    if (jSONObject != null) {
                        this.f.add(new C0009a(jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE), jSONObject.optString("style")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            j();
        }
        return this;
    }

    public ActionSheet a(IActionSheet bVar) {
        this.listenerSelected = bVar;
        return this;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if ((view.getId() != ID_VIEW_10 || this.i) && view.getId() != ID_VIEW_200) {
            d();
            if (view.getId() != ID_VIEW_10) {
                IActionSheet bVar = this.listenerSelected;
                if (bVar != null) {
                    bVar.a(view.getId() - ID_VIEW_100);
                }
                this.k = false;
                return;
            }
            IActionSheet bVar2 = this.listenerSelected;
            if (bVar2 != null) {
                bVar2.a(-1);
            }
            this.k = false;
        }
    }

    /* compiled from: ActionSheet.java */
    /* renamed from: io.dcloud.feature.ui.nativeui.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0009a {
        public String a;
        public String b;

        public C0009a(String str, String str2) {
            if (str2 == null) {
                this.b = IApp.ConfigProperty.CONFIG_RUNMODE_NORMAL;
            } else {
                this.b = str2;
            }
            this.a = str;
        }
    }
}
