package io.dcloud.common.common_a;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.constant.AbsoluteConst;

/* compiled from: SNCHandler.java */
/* loaded from: classes.dex */
class c {
    AbsMgr a;

    boolean a(char c) {
        return c == '\r' || c == '\n';
    }

    boolean b(char c) {
        return c == '\t' || c == 11 || c == '\f' || c == ' ' || c == 160 || c == 12288;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(AbsMgr absMgr) {
        this.a = absMgr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<a> b(String str) {
        String trim = null;
        ArrayList<a> arrayList = new ArrayList<>(1);
        if (str.startsWith(AbsoluteConst.SOCKET_NATIVE_COMMAND)) {
            str = str.substring(4);
        }
        String trim2 = str.trim();
        int length = trim2.length();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2;
            while (i2 < length) {
                char charAt = trim2.charAt(i2);
                i2++;
                if (i2 == length || ((b(charAt) && arrayList2.size() % 2 == 0) || a(charAt))) {
                    trim = trim2.substring(i3, i2).trim();
                    if (!"".equals(trim)) {
                        break;
                    }
                }
            }
            arrayList2.add(trim);
        }
        while (i < arrayList2.size()) {
            a aVar = new a();
            aVar.a = (String) arrayList2.get(i);
            aVar.b = (String) arrayList2.get(i + 1);
            i += 2;
            arrayList.add(aVar);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(final String str) {
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.a.c.1
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                for (a aVar : c.this.b(str)) {
                    if ("update".equals(aVar.a)) {
                        String str2 = aVar.b;
                        if ("all".equals(str2)) {
                            c.this.a.processEvent(IMgr.MgrType.WindowMgr, 13, null);
                        } else if ("current".equals(str2)) {
                            c.this.a.processEvent(IMgr.MgrType.WindowMgr, 12, null);
                        } else {
                            c.this.a.processEvent(IMgr.MgrType.WindowMgr, 14, str2);
                        }
                    }
                }
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SNCHandler.java */
    /* loaded from: classes.dex */
    public class a {
        String a;
        String b;

        a() {
        }
    }
}
