package io.dcloud.feature.barcode.barcode_b;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* compiled from: InactivityTimer.java */
/* loaded from: classes.dex old h */
public final class InactivityTimer {
    private final Activity b;
    private final ScheduledExecutorService a = Executors.newSingleThreadScheduledExecutor(new a());
    private ScheduledFuture<?> c = null;

    public InactivityTimer(Activity activity) {
        this.b = activity;
        a();
    }

    public void a() {
        c();
        this.c = this.a.schedule(new FinishListener(this.b), 300L, TimeUnit.SECONDS);
    }

    private void c() {
        ScheduledFuture<?> scheduledFuture = this.c;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.c = null;
        }
    }

    public void b() {
        c();
        this.a.shutdown();
    }

    /* compiled from: InactivityTimer.java */
    /* loaded from: classes.dex */
    private static final class a implements ThreadFactory {
        private a() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }
}
