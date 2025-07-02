package io.dcloud.common.util;

import java.util.HashMap;

/* loaded from: classes.dex */
public class AppStreamUtil {
    public static HashMap<String, String> AppStreamGenuines;

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        AppStreamGenuines = hashMap;
        hashMap.put("H5EC86117", "36Kr资讯");
        AppStreamGenuines.put("H592E7F63", "6人游旅行网");
        AppStreamGenuines.put("H5C4E96B4", "爱美丽");
        AppStreamGenuines.put("H5709B599", "宝贝租车");
        AppStreamGenuines.put("chaoyingyue", "超盈越");
        AppStreamGenuines.put("H5BCD03E4", "点评外卖");
        AppStreamGenuines.put("H510E2B40", "枫桥居花卉");
        AppStreamGenuines.put("H5E9801E3", "好记密码");
        AppStreamGenuines.put("HelloH5", "Hello H5+");
        AppStreamGenuines.put("HelloMUI", "Hello MUI");
        AppStreamGenuines.put("H5F563BDD", "HiMall");
        AppStreamGenuines.put("H51700FE4", "京东秒杀");
        AppStreamGenuines.put("H5336E745", "蓝昊数码");
        AppStreamGenuines.put("H5605AB01", "柳州1号");
        AppStreamGenuines.put("H5291D2691", "迷你课表");
        AppStreamGenuines.put("H54D773AC", "期待乐");
        AppStreamGenuines.put("H522034E0", "人脉返现");
        AppStreamGenuines.put("H532A4BFF", "挑食火锅");
        AppStreamGenuines.put("H5FCEFA0C", "我的冲印");
        AppStreamGenuines.put("H57D443FC", "养车屋");
        AppStreamGenuines.put("H52203BEA", "弈客围棋");
        AppStreamGenuines.put("H5EF8A9C1", "艺人捧场");
        AppStreamGenuines.put("H55BDF6BE", "甬派");
        AppStreamGenuines.put("H56022FE5", "有道词典");
        AppStreamGenuines.put("H559D7DDA", "智慧防汛");
        AppStreamGenuines.put("H5B680757", "中旋");
        AppStreamGenuines.put("H554D94D4", "装修云管家助手");
        AppStreamGenuines.put("H56F0FF05", "走着服务者");
        AppStreamGenuines.put("H50608789", "点点壁纸");
    }

    public static boolean isAppStreamGenuine(String str) {
        return AppStreamGenuines.containsKey(str);
    }
}
