package ertong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static final String PINYIN_TABLE = "拼音汉字表.txt";
    public static final String HANZI_TABLE = "一二级汉字表.txt";
    public static final String HANZI_MAP = "map1.txt";
    public static final String TWO_HANZI_MAP = "map2.txt";
    public static final String PINYIN_MAP = "map3.txt";
    public static final String[] NEWS_DATE = {"02", "04", "05", "06", "07", "08", "09", "10", "11"};
    public static final double  ALPHA = 0.9999;
    public static final int MAX_SIZE = 1000;
    public static final int OUT_SIZE = 5;
    public static final int INIT_POSSIBILITY = 1000000;
    public static final double RATIO = 100000.0;

    public static final String INPUT_FILE = "input.txt";

    public static String readStrFromFile(String path){
        String s = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    public static List<String> readListFromFile(String path){
        List<String> list = new ArrayList<String>();
        String s = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            while((s = br.readLine()) != null){
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}