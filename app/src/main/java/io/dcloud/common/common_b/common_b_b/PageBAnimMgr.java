package io.dcloud.common.common_b.common_b_b;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import io.dcloud.common.DHInterface.ITypeofAble;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import android.animation.Animator;
import android.animation.ObjectAnimator;

/* compiled from: PageBAnimMgr.java */
/* loaded from: classes.dex  old j*/
public class PageBAnimMgr {
    public static View view = null;
    public static DHImageView b = null;
    private static boolean c = false;

    public static void a(DHFrameView dVar, int i) {
        String str = dVar.getAnimOptions().mAnimType;
        String str2 = dVar.getAnimOptions().mAnimType_close;
        DHFrameView dVar2 = (DHFrameView) dVar.l.findFrameViewB(dVar);
        if (dVar2 == null) {
            return;
        }
        if (i == 1) {
            String closeAnimType = AnimOptions.getCloseAnimType(str2);
            if (dVar.mAccelerationType.equals("auto") && !PdrUtil.isEquals(closeAnimType, AnimOptions.ANIM_POP_OUT) && !BaseInfo.isDefaultAim && dVar2.mSnapshot == null) {
                return;
            }
            if (dVar.mAccelerationType.equals("none") && !PdrUtil.isEquals(closeAnimType, AnimOptions.ANIM_POP_OUT) && dVar2.mSnapshot == null) {
                return;
            }
            if (!dVar.mAccelerationType.equals("none") && dVar2.mSnapshot == null) {
                BaseInfo.sOpenedCount--;
            }
        } else {
            if (dVar.mAccelerationType.equals("auto") && !PdrUtil.isEquals(str, AnimOptions.ANIM_POP_IN) && !BaseInfo.isDefaultAim && dVar2.mSnapshot == null) {
                return;
            }
            if (dVar.mAccelerationType.equals("none") && !PdrUtil.isEquals(str2, AnimOptions.ANIM_POP_IN) && dVar2.mSnapshot == null) {
                return;
            }
            if (!dVar.mAccelerationType.equals("none") && dVar2.mSnapshot == null) {
                BaseInfo.sOpenedCount++;
                c = BaseInfo.sOpenedCount > 1;
            }
        }
        if (dVar2 != null) {
            view = dVar2.obtainMainView();
            dVar2.l.a(dVar2, dVar);
            dVar2.chkUseCaptureAnimation(true, dVar2.hashCode(), dVar2.mSnapshot != null);
            if (dVar2.mAnimationCapture && BaseInfo.sAnimationCaptureB && !a(dVar2)) {
                Logger.e("mabo", "B页面是否启用截图动画方案:true | " + dVar2.getAnimOptions().mAnimType);
                a(i, dVar2, dVar);
            } else {
                Logger.e("mabo", "B页面是否启用截图动画方案:false | " + dVar2.getAnimOptions().mAnimType);
                b(i, dVar2, dVar);
            }
        }
        if (BaseInfo.sOpenedCount == 0) {
            c = false;
        }
    }

    public static boolean a(DHFrameView dVar) {
        ViewGroup viewGroup = (ViewGroup) dVar.obtainMainView();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewGroup.getChildAt(i) instanceof ITypeofAble) {
                return true;
            }
        }
        return false;
    }

    private static void a(int i, DHFrameView dVar, DHFrameView dVar2) {
        final ObjectAnimator ofFloat;
        int i2 = dVar2.obtainApp().getInt(0);
        int size = dVar.m.mChildArrayList.size();
        if (i == 0 || c) {
            dVar.m.setScrollIndicator("none");
            if (size != 0) {
                for (int i3 = 0; i3 < size; i3++) {
                    if (dVar.m.mChildArrayList.get(i3) instanceof DHFrameView) {
                        ((DHFrameView) dVar.m.mChildArrayList.get(i3)).m.setScrollIndicator("none");
                    }
                }
            }
        }
        DHImageView a2 = dVar2.l.a(dVar, i, c);
        b = a2;
        if (a2 == null) {
            b(i, dVar, dVar2);
            return;
        }
        if (i == 0 || c) {
            if (dVar.p != null) {
                dVar.m.setScrollIndicator(dVar.p.getScrollIndicator());
            }
            if (size != 0) {
                for (int i4 = 0; i4 < size; i4++) {
                    if (dVar.m.mChildArrayList.get(i4) instanceof DHFrameView) {
                        DHFrameView dVar3 = (DHFrameView) dVar.m.mChildArrayList.get(i4);
                        dVar3.m.setScrollIndicator(dVar3.p.getScrollIndicator());
                    }
                }
            }
        }
        if (i == 0) {
            b.setTag(Integer.valueOf(dVar.hashCode()));
            if (PdrUtil.isEquals(dVar2.getAnimOptions().mAnimType, AnimOptions.ANIM_POP_IN)) {
                Object obj = b;
                if (obj == null) {
                    obj = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj, "translationX", dVar.obtainFrameOptions().left, (-i2) / 6);
            } else {
                Object obj2 = b;
                if (obj2 == null) {
                    obj2 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj2, "translationX", 0.0f);
            }
            ofFloat.setDuration(dVar2.getAnimOptions().duration_show);
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.j.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (PageBAnimMgr.b != null) {
                        PageBAnimMgr.b.b(true);
                    }
                    BaseInfo.sDoingAnimation = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BaseInfo.sDoingAnimation = false;
                    if (PageBAnimMgr.b != null) {
                        PageBAnimMgr.b.b(false);
                        PageBAnimMgr.b.clearAnimation();
                        PageBAnimMgr.b.setVisibility(View.INVISIBLE);
                        PageBAnimMgr.b.setImageBitmap(null);
                    } else {
                        PageBAnimMgr.view.clearAnimation();
                    }
                    animator.removeListener(this);
                }
            });
        } else {
            if (PdrUtil.isEquals(AnimOptions.getCloseAnimType(dVar2.getAnimOptions().mAnimType_close), AnimOptions.ANIM_POP_OUT)) {
                Object obj3 = b;
                if (obj3 == null) {
                    obj3 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj3, "translationX", (-i2) / 6, dVar2.obtainFrameOptions().left);
            } else {
                Object obj4 = b;
                if (obj4 == null) {
                    obj4 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj4, "translationX", 0.0f);
            }
            ofFloat.setDuration(dVar2.getAnimOptions().duration_close);
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.j.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (PageBAnimMgr.b != null) {
                        PageBAnimMgr.b.b(true);
                    }
                    BaseInfo.sDoingAnimation = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PageBAnimMgr.b != null) {
                        PageBAnimMgr.b.b(false);
                    }
                    BaseInfo.sDoingAnimation = false;
                    PageBAnimMgr.view.postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.j.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (PageBAnimMgr.b != null) {
                                PageBAnimMgr.b.clearAnimation();
                                PageBAnimMgr.b.setVisibility(View.INVISIBLE);
                                PageBAnimMgr.b.setImageBitmap(null);
                                PageBAnimMgr.b.setTag(0);
                                return;
                            }
                            PageBAnimMgr.view.clearAnimation();
                        }
                    }, 320L);
                    animator.removeListener(this);
                }
            });
        }
        ofFloat.start();
        dVar.l.h(dVar);
    }

    private static void b(int i, final DHFrameView dVar, DHFrameView dVar2) {
        final ObjectAnimator ofFloat;
        int i2 = dVar2.obtainApp().getInt(0);
        if (i == 0) {
            if (PdrUtil.isEquals(dVar2.getAnimOptions().mAnimType, AnimOptions.ANIM_POP_IN)) {
                Object obj = b;
                if (obj == null) {
                    obj = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj, "translationX", dVar.obtainFrameOptions().left, (-i2) / 6);
            } else {
                Object obj2 = b;
                if (obj2 == null) {
                    obj2 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj2, "translationX", 0.0f);
            }
            ofFloat.setDuration(dVar2.getAnimOptions().duration_show);
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.j.3
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    BaseInfo.sDoingAnimation = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PageBAnimMgr.view.setX(DHFrameView.obtainFrameOptions().left);
                    PageBAnimMgr.view.setY( DHFrameView.obtainFrameOptions().top);
                    BaseInfo.sDoingAnimation = false;
                    ofFloat.removeListener(this);
                }
            });
        } else {
            if (PdrUtil.isEquals(AnimOptions.getCloseAnimType(dVar2.getAnimOptions().mAnimType_close), AnimOptions.ANIM_POP_OUT)) {
                Object obj3 = b;
                if (obj3 == null) {
                    obj3 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj3, "translationX", (-i2) / 6, dVar2.obtainFrameOptions().left);
            } else {
                Object obj4 = b;
                if (obj4 == null) {
                    obj4 = view;
                }
                ofFloat = ObjectAnimator.ofFloat(obj4, "translationX", 0.0f);
            }
            ofFloat.setDuration(dVar2.getAnimOptions().duration_close);
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.j.4
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    BaseInfo.sDoingAnimation = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PageBAnimMgr.view.setX(DHFrameView.obtainFrameOptions().left);
                    PageBAnimMgr.view.setY(DHFrameView.obtainFrameOptions().top);
                    BaseInfo.sDoingAnimation = false;
                    ofFloat.removeListener(this);
                }
            });
        }
        ofFloat.start();
        dVar.l.h(dVar);
    }
}
