package io.dcloud.nineoldandroids.util;

/* loaded from: classes.dex */
public abstract class FloatProperty<T> extends Property<T, Float> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.dcloud.nineoldandroids.util.Property
    public /* bridge */ /* synthetic */ void set(Object obj, Float f) {
        set2((T) obj, f);
    }

    public abstract void setValue(T t, float f);

    public FloatProperty(String str) {
        super(Float.class, str);
    }

    /* renamed from: set, reason: avoid collision after fix types in other method */
    public final void set2(T t, Float f) {
        setValue(t, f.floatValue());
    }
}
