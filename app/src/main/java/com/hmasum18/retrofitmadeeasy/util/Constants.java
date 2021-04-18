package com.hmasum18.retrofitmadeeasy.util;

import java.util.HashMap;

public class Constants {
    public static final String BASE_URL_FCM = "https://fcm.googleapis.com/";
    public static final String FCM_SERVER_KEY = "AAAAAOJHYwA:APA91bHGSc-f-w7KY6RiML6lJS6o9JqMWACuCjmhWeSmmqCL0nm5llHF6vx1wX9vMLLuj2pEZOZ-7GfozNsrdIqtuGMIIafjQrIgSrRlTaQAzwgoQLVa59IpHl7L7A1mCOYi9UUUnS6r";

    public static final HashMap<String,String> fcmHeaderMap = new HashMap<>();
    static {
        fcmHeaderMap.put("Authorization","key="+ FCM_SERVER_KEY);
        fcmHeaderMap.put("content-type","application/json");
    }
}
