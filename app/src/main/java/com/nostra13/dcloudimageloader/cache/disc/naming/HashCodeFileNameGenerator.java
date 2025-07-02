package com.nostra13.dcloudimageloader.cache.disc.naming;

/* loaded from: classes.dex */
public class HashCodeFileNameGenerator implements FileNameGenerator {
    @Override // com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator
    public String generate(String str) {
        return String.valueOf(str.hashCode());
    }
}
