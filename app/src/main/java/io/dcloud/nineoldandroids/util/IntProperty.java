package io.dcloud.nineoldandroids.util;

/* loaded from: classes.dex */
public abstract class IntProperty<T> extends Property<T, Integer> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.dcloud.nineoldandroids.util.Property
    public /* bridge */ /* synthetic */ void set(Object obj, Integer num) {
        set2((T) obj, num);
    }

    public abstract void setValue(T t, int i);

    public IntProperty(String str) {
        super(Integer.class, str);
    }

    /* renamed from: set, reason: avoid collision after fix types in other method */
    public final void set2(T t, Integer num) {
        set2((T) t, Integer.valueOf(num.intValue()));
    }
}
