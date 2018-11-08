package ertong;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trainer {
    private Map<Character, Integer> c_map;
    private Map<Character, Map<Character, Integer>> w_map;
    private Map<String, List<Character>> p_map;

    Trainer(){
        c_map = readMap(Utility.HANZI_MAP);
        w_map = readMap(Utility.TWO_HANZI_MAP);
        p_map = readMap(Utility.PINYIN_MAP);
    }

    public void train(){
        initHanzi();
        initPinyin();
        System.out.println("succeed in initing");
        for (int i = 1; i < Utility.NEWS_DATE.length; ++i){
            String path = "sina_news_gbk/2016-" + Utility.NEWS_DATE[i] + ".txt";
            updateHanzi(path);
            System.out.println("succeed in updating " + path);
        }
    }

    private void initPinyin(){
        char[] cs = Utility.readStrFromFile(Utility.HANZI_TABLE).toCharArray();
        List<String> ls = Utility.readListFromFile(Utility.PINYIN_TABLE);
        String[] ps = new String[ls.size()];
        String[] hs = new String[ls.size()];
        for (int i = 0; i < ls.size(); ++i){
            int border = ls.get(i).indexOf(" ");
            ps[i] = ls.get(i).substring(0, border);
            hs[i] = ls.get(i).substring(border + 1);
        }
        p_map = new HashMap<String, List<Character>>();
        for (int i = 1; i < cs.length; ++i){
            for (int j = 0; j < hs.length; ++j){
                if(hs[j].indexOf(cs[i]) != -1){
                    if(!p_map.containsKey(ps[j])){
                        p_map.put(ps[j], new ArrayList<Character>());
                    }
                    p_map.get(ps[j]).add(cs[i]);
                }
            }
        }

        writeMap(p_map, Utility.PINYIN_MAP);
    }

    private void updateHanzi(String news_file) {
        List<String> ss = Utility.readListFromFile(news_file);
        c_map = readMap(Utility.HANZI_MAP);
        w_map = readMap(Utility.TWO_HANZI_MAP);
        for (String s: ss) {
            s = s.substring(s.indexOf("{\"html\":"));
            JSONObject object = new JSONObject(s);
            String content = object.getString("html");
            char[] hanzi = content.toCharArray();
            char before = '#';
            for (char h : hanzi) {
                if (isChinese(h)) {
                    Integer count = c_map.get(h);
                    if (count != null) {
                        c_map.replace(h, count + 1);
                        if ((count = w_map.get(before).get(h)) != null) {
                            w_map.get(before).replace(h, count + 1);
                        } else {
                            w_map.get(before).put(h, 1);
                        }
                        before = h;
                    } else {
                        before = '#';
                    }
                } else {
                    before = '#';
                }
            }
        }
        writeMap(c_map, Utility.HANZI_MAP);
        writeMap(w_map, Utility.TWO_HANZI_MAP);
    }

    private void initHanzi(){
        char[] cs = Utility.readStrFromFile(Utility.HANZI_TABLE).toCharArray();
        c_map = new HashMap<Character, Integer>();
        w_map = new HashMap<Character, Map<Character, Integer>>();
        for (int i = 1; i < cs.length; ++i){
            c_map.put(cs[i], 0);
            w_map.put(cs[i], new HashMap<Character, Integer>());
        }
        w_map.put('#', new HashMap<Character, Integer>());

        writeMap(c_map, Utility.HANZI_MAP);
        writeMap(w_map, Utility.TWO_HANZI_MAP);
    }

    private Map readMap(String path){
        Map map = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            map = (Map) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    private void writeMap(Map map, String path){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(map);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isChinese(char c){
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    public Map<Character, Integer> getC_map() {
        return c_map;
    }

    public Map<Character, Map<Character, Integer>> getW_map() {
        return w_map;
    }

    public Map<String, List<Character>> getP_map() {
        return p_map;
    }

}