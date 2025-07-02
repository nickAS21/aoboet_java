package android.support.v4.util;

/* loaded from: classes.dex */
public class ArrayMap_Old<K, V>  {
//public class ArrayMap_Old<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
//    MapCollections<K, V> mCollections;
//
//    public ArrayMap_Old() {
//    }
//
//    public ArrayMap_Old(int i) {
//        super(i);
//    }
//
//    public ArrayMap_Old(SimpleArrayMap simpleArrayMap_Old) {
//        super(simpleArrayMap_Old);
//    }
//
//    private MapCollections<K, V> getCollection() {
//        if (this.mCollections == null) {
//            this.mCollections = new MapCollections<K, V>() { // from class: android.support.v4.util.ArrayMap_Old.1
//                @Override // android.support.v4.util.MapCollections
//                protected int colGetSize() {
//                    return ArrayMap_Old.this.mSize;
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected Object colGetEntry(int i, int i2) {
//                    return ArrayMap_Old.this.mArray[(i << 1) + i2];
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected int colIndexOfKey(Object obj) {
//                    return ArrayMap_Old.this.indexOfKey(obj);
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected int colIndexOfValue(Object obj) {
//                    return ArrayMap_Old.this.indexOfValue(obj);
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected Map<K, V> colGetMap() {
//                    return ArrayMap_Old.this;
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected void colPut(K k, V v) {
//                    ArrayMap_Old.this.put(k, v);
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected V colSetValue(int i, V v) {
//                    return ArrayMap_Old.this.setValueAt(i, v);
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected void colRemoveAt(int i) {
//                    ArrayMap_Old.this.removeAt(i);
//                }
//
//                @Override // android.support.v4.util.MapCollections
//                protected void colClear() {
//                    ArrayMap_Old.this.clear();
//                }
//            };
//        }
//        return this.mCollections;
//    }
//
//    public boolean containsAll(Collection<?> collection) {
//        return MapCollections.containsAllHelper(this, collection);
//    }
//
//    @Override // java.util.Map
//    public void putAll(Map<? extends K, ? extends V> map) {
//        ensureCapacity(this.mSize + map.size());
//        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
//            put(entry.getKey(), entry.getValue());
//        }
//    }
//
//    public boolean removeAll(Collection<?> collection) {
//        return MapCollections.removeAllHelper(this, collection);
//    }
//
//    public boolean retainAll(Collection<?> collection) {
//        return MapCollections.retainAllHelper(this, collection);
//    }
//
//    @Override // java.util.Map
//    public Set<Map.Entry<K, V>> entrySet() {
//        return getCollection().getEntrySet();
//    }
//
//    @Override // java.util.Map
//    public Set<K> keySet() {
//        return getCollection().getKeySet();
//    }
//
//    @Override // java.util.Map
//    public Collection<V> values() {
//        return getCollection().getValues();
//    }
}
