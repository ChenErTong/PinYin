package ertong;

import java.util.*;

public class Predictor {
    private Map<Character, Integer> c_map;
    private Map<Character, Map<Character, Integer>> w_map;
    private Map<String, List<Character>> p_map;

    Predictor(Trainer trainer) {
        this.c_map = trainer.getC_map();
        this.w_map = trainer.getW_map();
        this.p_map = trainer.getP_map();
    }

    public String[] predict(String pinyin){
        String[] ps = pinyin.split(" ");
        Queue<Possibility> queue = new PriorityQueue<Possibility>();
        queue.add(new Possibility("#", Utility.INIT_POSSIBILITY));
        return dynamicPlanning(ps, 0, queue);
    }

    private void show(Map map){
        System.out.println(map.toString());
    }

    private String[] dynamicPlanning(String[] ps, int index, Queue<Possibility> queue){
        if(index == ps.length){
            String[] result = new String[Math.min(queue.size(), Utility.OUT_SIZE)];
            for (int i = 0; i < result.length; ++i){
                result[i] = queue.poll().str.substring(1);
            }
            return result;
        }
        Possibility[] before = new Possibility[Math.min(queue.size(), Utility.MAX_SIZE)];
        double norm = queue.peek().p;
        for (int i = 0; i < before.length; ++i){
            before[i] = queue.poll();
            before[i].p /= norm;
        }
        queue.clear();
        List<Character> cs = p_map.get(ps[index]);
        for (Possibility s : before) {
            for (Character c : cs) {
                Possibility possibility = s.generateNext(c);
                if(!Double.isNaN(possibility.p)){
                    queue.add(possibility);
                }
            }
        }
        return dynamicPlanning(ps, index + 1, queue);
    }

    class Possibility implements Comparable<Possibility> {
            String str;
            double p;

        Possibility(String str, double p) {
            this.str = str;
            this.p = p;
        }

        Possibility generateNext(char next){
            char before = str.charAt(str.length() - 1);
            return new Possibility(str + next, p * getPossibility(before, next));
        }

        private double getPossibility(char before, char next){
            int n_count = c_map.get(next);
            if(before == '#'){
                return n_count / Utility.RATIO;
            }else {
                int b_count = c_map.get(before);
                Integer integer = w_map.get(before).get(next);
                int bn_count = integer == null ? 0 : integer;
//                System.out.println(before + " " + next + ": " + Utility.ALPHA * bn_count / b_count  + "+" + (1 - Utility.ALPHA) * n_count / Utility.RATIO);
                return Utility.ALPHA * bn_count / b_count + (1 - Utility.ALPHA) * n_count / Utility.RATIO;
            }
        }

        void output(){
            System.out.println(str + ": " + p);
        }

        public int compareTo(Possibility o) {
            return Double.compare(o.p, p);
        }
    }
}