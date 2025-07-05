package io.dcloud.feature.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class DeviceFeatureImpl implements IFeature, ISysEventListener {
    private SensorManager b;
    private Sensor d;
    private Context f;
    private PowerManager.WakeLock c = null;
    private boolean e = false;
    int a = -1;
    private final SensorEventListener g = new SensorEventListener() { // from class: io.dcloud.feature.device.DeviceFeatureImpl.1
        private final float[] b = {2.0f, 2.5f, 0.5f};
        private float[] c = new float[3];
        private long d;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] fArr = new float[3];
            boolean z = false;
            for (int i = 0; i < 3; i++) {
                fArr[i] = Math.round(this.b[i] * (sensorEvent.values[i] - this.c[i]) * 0.45f);
                if (Math.abs(fArr[i]) > 0.0f) {
                    z = true;
                }
                this.c[i] = sensorEvent.values[i];
            }
            if (z) {
                Logger.i("sensorChanged " + sensorEvent.sensor.getName() + " (" + sensorEvent.values[0] + ", " + sensorEvent.values[1] + ", " + sensorEvent.values[2] + ") diff(" + fArr[0] + " " + fArr[1] + " " + fArr[2] + ")");
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis - this.d > 1000) {
                this.d = 0L;
                float f = fArr[0];
                float f2 = fArr[1];
                boolean z2 = Math.abs(f) > 3.0f;
                boolean z3 = Math.abs(f2) > 3.0f;
                if (z2 || z3) {
                    if (z2 && z3) {
                        return;
                    }
                    if (z2) {
                        if (f < 0.0f) {
                            Logger.i("test", "<<<<<<<< LEFT <<<<<<<<<<<<");
                        } else {
                            Logger.i("test", ">>>>>>>>> RITE >>>>>>>>>>>");
                        }
                    } else if (f2 < -2.0f) {
                        Logger.i("test", "<<<<<<<< UP <<<<<<<<<<<<");
                    } else {
                        Logger.i("test", ">>>>>>>>> DOWN >>>>>>>>>>>");
                    }
                    this.d = uptimeMillis;
                }
            }
        }
    };

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("getCurrentType")) {
            return DeviceInfo.getNetWorkType();
        }
        if ("unlockOrientation".equals(str)) {
            iWebview.obtainApp().setRequestedOrientation((String) null);
        } else {
            if ("lockOrientation".equals(str)) {
                iWebview.obtainApp().setRequestedOrientation(strArr[0]);
            } else {
                int i = 1;
                if ("dial".equals(str)) {
                    a(iWebview, strArr[0], PdrUtil.parseBoolean(strArr[1], true, false));
                } else {
                    long j = 500;
                    if ("beep".equals(str)) {
                        ToneGenerator toneGenerator = new ToneGenerator(5, 100);
                        try {
                            int parseInt = Integer.parseInt(strArr[0]);
                            if (parseInt > 0) {
                                i = parseInt;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        for (int i2 = 0; i2 < i; i2++) {
                            toneGenerator.startTone(88);
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                        }
                    } else if ("setWakelock".equals(str)) {
                        if (PdrUtil.parseBoolean(strArr[0], false, false)) {
                            this.c.acquire();
                        } else {
                            this.c.release();
                        }
                    } else {
                        if ("isWakelock".equals(str)) {
                            return JSUtil.wrapJsVar(String.valueOf(this.c.isHeld()), false);
                        }
                        if ("vibrate".equals(str)) {
                            try {
                                long parseLong = Long.parseLong(strArr[0]);
                                if (parseLong > 0) {
                                    j = parseLong;
                                }
                            } catch (NumberFormatException e3) {
                                e3.printStackTrace();
                            }
                            ((Vibrator) this.f.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(j);
                        } else if ("setVolume".equals(str)) {
                            float parseFloat = Float.parseFloat(strArr[0]);
                            AudioManager audioManager = (AudioManager) iWebview.getContext().getSystemService(Context.AUDIO_SERVICE);
                            int a = a(parseFloat);
                            audioManager.setStreamVolume(4, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(8, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(3, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(5, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(2, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(1, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            audioManager.setStreamVolume(0, a, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        } else {
                            if ("getVolume".equals(str)) {
                                return JSUtil.wrapJsVar(String.valueOf(((AudioManager) iWebview.getContext().getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(3) / this.a), false);
                            }
                            if ("s.resolutionHeight".equals(str)) {
                                IApp obtainApp = iWebview.obtainApp();
                                return JSUtil.wrapJsVar(String.valueOf(obtainApp.getInt(2) / iWebview.getScale()), false);
                            }
                            if ("s.resolutionWidth".equals(str)) {
                                IApp obtainApp2 = iWebview.obtainApp();
                                return JSUtil.wrapJsVar(String.valueOf(obtainApp2.getInt(0) / iWebview.getScale()), false);
                            }
                            if ("d.resolutionHeight".equals(str)) {
                                IApp obtainApp3 = iWebview.obtainApp();
                                return JSUtil.wrapJsVar(String.valueOf(obtainApp3.getInt(1) / iWebview.getScale()), false);
                            }
                            if ("d.resolutionWidth".equals(str)) {
                                IApp obtainApp4 = iWebview.obtainApp();
                                return JSUtil.wrapJsVar(String.valueOf(obtainApp4.getInt(0) / iWebview.getScale()), false);
                            }
                            if ("setBrightness".equals(str)) {
                                a(iWebview, Float.parseFloat(strArr[0]));
                            } else {
                                if ("getBrightness".equals(str)) {
                                    return JSUtil.wrapJsVar(String.valueOf(a(iWebview.getActivity()) / 255.0f), false);
                                }
                                if (str.equals("getCurrentAPN")) {
                                    String currentAPN = DeviceInfo.getCurrentAPN();
                                    if (TextUtils.isEmpty(currentAPN)) {
                                        return null;
                                    }
                                    try {
                                        return JSUtil.wrapJsVar(new JSONObject("{name:" + currentAPN + "}"));
                                    } catch (JSONException e4) {
                                        e4.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private int a(Activity activity) {
        int i;
        float f = activity.getWindow().getAttributes().screenBrightness * 255.0f;
        if (f < 0.0f) {
            try {
                if (Build.VERSION.SDK_INT > 17) {
                    i = Settings.Global.getInt(activity.getContentResolver(), "screen_brightness");
                } else {
                    i = Settings.System.getInt(activity.getContentResolver(), "screen_brightness");
                }
                f = i;
            } catch (Exception unused) {
            }
        }
        if (f < 0.0f) {
            f = 125.0f;
        }
        try {
            f = Settings.System.getInt(this.f.getContentResolver(), "screen_brightness");
        } catch (Exception unused2) {
        }
        return (int) f;
    }

    private void a(IWebview iWebview, float f) {
        Window window = iWebview.getActivity().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.screenBrightness = f;
        window.setAttributes(attributes);
        Settings.System.putInt(this.f.getContentResolver(), "screen_brightness", (int) (f * 255.0f));
    }

    private int a(float f) {
        if (f > 1.0f || f < 0.0f) {
            return 0;
        }
        return (int) (f * this.a);
    }

    protected void a(IWebview iWebview, String str, boolean z) {
        iWebview.getActivity().startActivity(new Intent(!z ? "android.intent.action.CALL" : "android.intent.action.DIAL", Uri.parse("tel:" + str)));
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        SensorManager sensorManager = (SensorManager) absMgr.getContext().getSystemService(Context.SENSOR_SERVICE);
        this.b = sensorManager;
        this.d = sensorManager.getDefaultSensor(1);
        Context context = absMgr.getContext();
        this.f = context;
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(10, "My Lock");
        this.c = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        this.a = ((AudioManager) absMgr.getContext().getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(3);
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onResume) {
            this.b.registerListener(this.g, this.d, 0);
        } else if (sysEventType == ISysEventListener.SysEventType.onStop) {
            this.b.unregisterListener(this.g);
        }
        return false;
    }
}
