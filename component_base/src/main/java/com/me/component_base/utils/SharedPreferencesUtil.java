package com.me.component_base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences 工具类
 * @author yang
 */
public class SharedPreferencesUtil {
    private static SharedPreferencesUtil spUtil;
    private static SharedPreferences sp;

    private SharedPreferencesUtil(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void getInstance(Context context, String name) {
        if (spUtil == null) {
            spUtil = new SharedPreferencesUtil(context, name);
        }
    }

    public static boolean putValue(String key, Object value) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = gson.toJson(value);
                    editor.putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 获取SharedPreferences中保存的数据
     *
     * @param key          键
     * @param defaultValue 获取失败默认值
     * @return 从SharedPreferences读取的数据
     */
    public static Object getValue(String key, Object defaultValue) {
        Object result;
        String type = defaultValue.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    result = sp.getBoolean(key, (Boolean) defaultValue);
                    break;
                case "Long":
                    result = sp.getLong(key, (Long) defaultValue);
                    break;
                case "Float":
                    result = sp.getFloat(key, (Float) defaultValue);
                    break;
                case "String":
                    result = sp.getString(key, (String) defaultValue);
                    break;
                case "Integer":
                    result = sp.getInt(key, (Integer) defaultValue);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = sp.getString(key, "");
                    if (!"".equals(json) && json.length() > 0) {
                        result = gson.fromJson(json, defaultValue.getClass());
                    } else {
                        result = defaultValue;
                    }
                    break;
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public static <T> boolean putListData(String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        SharedPreferences.Editor editor = sp.edit();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public static <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于获取集合
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = sp.getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        Log.e("SharedPreferencesUtil", obj.toString());
        return map;
    }

    /**
     * 存放实体类以及任意类型
     */
    public static void putBean(String key, Object obj) {
        // obj必须实现Serializable接口，否则会出问题
        if (obj instanceof Serializable) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(key, string64).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("the obj must implement Serializable");
        }

    }

    public static Object getBean(String key) {
        Object obj = null;
        try {
            String base64 = sp.getString(key, "");
            if ("".equals(base64)) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 删除所有数据
     */
    public static void removeAll() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 删除指定key
     * @param key key
     */
    public static void removeKey(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}