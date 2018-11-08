package ertong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Trainer trainer = new Trainer();
//        trainer.train();
        Predictor predictor = new Predictor(trainer);

        List<String> pinyins = Utility.readListFromFile(Utility.INPUT_FILE);
        for (String pinyin: pinyins) {
            System.out.println(pinyin);
            String[] result = predictor.predict(pinyin);
            for (String r: result) {
                System.out.println(r);
            }
        }

        System.out.println("Waiting for input...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            while((line = br.readLine()) != null || line.length() > 0){
                String[] result = predictor.predict(line);
                for (String r: result) {
                    System.out.println(r);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}