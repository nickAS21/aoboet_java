package com.nostra13.dcloudimageloader.core.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import io.dcloud.common.util.net.RequestData;

/* loaded from: classes.dex */
public interface ImageDownloader {
    InputStream getStream(String str, Object obj) throws IOException;

    /* loaded from: classes.dex */
    public enum Scheme {
        HTTP(RequestData.URL_HTTP),
        HTTPS(RequestData.URL_HTTPS),
        FILE("file"),
        CONTENT("content"),
        ASSETS("assets"),
        DRAWABLE("drawable"),
        UNKNOWN("");

        private String scheme;
        private String uriPrefix;

        Scheme(String str) {
            this.scheme = str;
            this.uriPrefix = str + "://";
        }

        public static Scheme ofUri(String str) {
            if (str != null) {
                for (Scheme scheme : values()) {
                    if (scheme.belongsTo(str)) {
                        return scheme;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String str) {
            return str.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }

        public String wrap(String str) {
            return this.uriPrefix + str;
        }

        public String crop(String str) {
            if (!belongsTo(str)) {
                throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", str, this.scheme));
            }
            return str.substring(this.uriPrefix.length());
        }
    }
}
