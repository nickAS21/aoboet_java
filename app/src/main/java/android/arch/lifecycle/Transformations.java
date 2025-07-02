package android.arch.lifecycle;

import android.arch.core.util.Function;

public class Transformations {

    private Transformations() {
    }

    public static <X, Y> LiveData<Y> map(final LiveData<X> source, final Function<X, Y> function) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            @Override
            public void onChanged(X x) {
                result.setValue(function.apply(x));
            }
        });
        return result;
    }

    public static <X, Y> LiveData<Y> switchMap(final LiveData<X> source, final Function<X, LiveData<Y>> switchMapFunction) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            LiveData<Y> currentSource;

            @Override
            public void onChanged(X x) {
                LiveData<Y> newLiveData = switchMapFunction.apply(x);

                if (currentSource == newLiveData) {
                    return;
                }

                if (currentSource != null) {
                    result.removeSource(currentSource);
                }

                currentSource = newLiveData;

                if (newLiveData != null) {
                    result.addSource(newLiveData, new Observer<Y>() {
                        @Override
                        public void onChanged(Y y) {
                            result.setValue(y);
                        }
                    });
                }
            }
        });
        return result;
    }
}
