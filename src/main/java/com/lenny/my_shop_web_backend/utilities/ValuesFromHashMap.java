/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.utilities;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author lenny
 */
public class ValuesFromHashMap {

    public static String getStringValue_FromHashMap(HashMap hm, String key) {
        String res = null;
        try {
            if (hm.get(key) != null) {
                res = hm.get(key).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static Integer getIntegerValue_FromHashMap(HashMap hm, String key) {
        Integer res = null;
        try {
            if (hm.get(key) != null) {
                res = (Integer) hm.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static Boolean getBooleanValue_FromHashMap(HashMap hm, String key) {
        Boolean res = null;
        try {
            if (hm.get(key) != null) {
                res = (Boolean) hm.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static Long getLongValue_FromHashMap(HashMap hm, String key) {
        Long res = null;
        try {
            if (hm.get(key) != null) {
                res = (Long) hm.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static Date getDateValue_FromHashMap(HashMap hm, String key) {
        Date res = null;
        try {
            if (hm.get(key) != null) {
                SimpleDateFormat parseFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                DateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
                String date = (String) hm.get(key);
                Date parseDate = parseFormat.parse(date);
                String formatDate = simpleDateFormat.format(parseDate);
                res = (Date) simpleDateFormat.parse(formatDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static Date getTimeValue_FromHashMap(HashMap hm, String key) {
        Date res = null;
        try {
            if (hm.get(key) != null) {
                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
                DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                String time = (String) hm.get(key);
                Date parseTime = parseFormat.parse(time);
                String formatTime = simpleDateFormat.format(parseTime);
                res = (Date) simpleDateFormat.parse(formatTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public static HashMap getHashMapValue_FromHashMap(HashMap hm, String key) {
        HashMap res = null;
        try {
            if (hm.get(key) != null) {
                res = (HashMap) hm.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
