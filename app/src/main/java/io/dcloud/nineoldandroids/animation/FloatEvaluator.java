package io.dcloud.nineoldandroids.animation;

/* loaded from: classes.dex */
public class FloatEvaluator implements TypeEvaluator<Number> {
    @Override // io.dcloud.nineoldandroids.animation.TypeEvaluator
    public Float evaluate(float f, Number number, Number number2) {
        float floatValue = number.floatValue();
        return Float.valueOf(floatValue + (f * (number2.floatValue() - floatValue)));
    }
}
