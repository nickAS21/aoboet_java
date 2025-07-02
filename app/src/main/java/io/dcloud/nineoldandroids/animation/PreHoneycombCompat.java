package io.dcloud.nineoldandroids.animation;

import android.view.View;

import io.dcloud.nineoldandroids.util.FloatProperty;
import io.dcloud.nineoldandroids.util.IntProperty;
import io.dcloud.nineoldandroids.util.Property;
import io.dcloud.nineoldandroids.view.animation.AnimatorProxy;

/* loaded from: classes.dex */
final class PreHoneycombCompat {
    static Property<View, Float> ALPHA = new FloatProperty<View>("alpha") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.1
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setAlpha(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getAlpha());
        }
    };
    static Property<View, Float> PIVOT_X = new FloatProperty<View>("pivotX") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.2
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setPivotX(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getPivotX());
        }
    };
    static Property<View, Float> PIVOT_Y = new FloatProperty<View>("pivotY") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.3
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setPivotY(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getPivotY());
        }
    };
    static Property<View, Float> TRANSLATION_X = new FloatProperty<View>("translationX") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.4
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setTranslationX(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getTranslationX());
        }
    };
    static Property<View, Float> TRANSLATION_Y = new FloatProperty<View>("translationY") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.5
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setTranslationY(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getTranslationY());
        }
    };
    static Property<View, Float> ROTATION = new FloatProperty<View>("rotation") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.6
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotation(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotation());
        }
    };
    static Property<View, Float> ROTATION_X = new FloatProperty<View>("rotationX") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.7
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotationX(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotationX());
        }
    };
    static Property<View, Float> ROTATION_Y = new FloatProperty<View>("rotationY") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.8
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotationY(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotationY());
        }
    };
    static Property<View, Float> SCALE_X = new FloatProperty<View>("scaleX") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.9
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setScaleX(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getScaleX());
        }
    };
    static Property<View, Float> SCALE_Y = new FloatProperty<View>("scaleY") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.10
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setScaleY(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getScaleY());
        }
    };
    static Property<View, Integer> SCROLL_X = new IntProperty<View>("scrollX") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.11
        @Override // io.dcloud.nineoldandroids.util.IntProperty
        public void setValue(View view, int i) {
            AnimatorProxy.wrap(view).setScrollX(i);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Integer get(View view) {
            return Integer.valueOf(AnimatorProxy.wrap(view).getScrollX());
        }
    };
    static Property<View, Integer> SCROLL_Y = new IntProperty<View>("scrollY") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.12
        @Override // io.dcloud.nineoldandroids.util.IntProperty
        public void setValue(View view, int i) {
            AnimatorProxy.wrap(view).setScrollY(i);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Integer get(View view) {
            return Integer.valueOf(AnimatorProxy.wrap(view).getScrollY());
        }
    };
    static Property<View, Float> X = new FloatProperty<View>("x") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.13
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setX(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getX());
        }
    };
    static Property<View, Float> Y = new FloatProperty<View>("y") { // from class: io.dcloud.nineoldandroids.animation.PreHoneycombCompat.14
        @Override // io.dcloud.nineoldandroids.util.FloatProperty
        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setY(f);
        }

        @Override // io.dcloud.nineoldandroids.util.Property
        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getY());
        }
    };

    private PreHoneycombCompat() {
    }
}
