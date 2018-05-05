package sample;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.*;

public class Solver {
    private String inputString;
    private static final String SMALL_SPACE = "small.wav";
    private static final String SPACE = "space.wav";
    public Solver() {
    }

    public Solver(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public void createResult() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        List<String> parts = buildSequenceOfFiles();
        if (parts.size()==0){
            return;
        }
        if (parts.size()==1){
            AudioInputStream clip = AudioSystem.getAudioInputStream(new File("records\\"+parts.get(0)));
            AudioSystem.write(clip, AudioFileFormat.Type.WAVE, new File("records\\result.wav"));
            Sound.playSound("records\\result.wav").join();
            return;
        }
        AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File("records\\"+parts.get(0)));
        AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File("records\\"+parts.get(1)));
        AudioInputStream appendedFiles =
                new AudioInputStream(
                        new SequenceInputStream(clip1, clip2),
                        clip1.getFormat(),
                        clip1.getFrameLength() + clip2.getFrameLength());

        AudioSystem.write(appendedFiles,
                AudioFileFormat.Type.WAVE,
                new File("result\\result1.wav"));
        for (int i=2;i<parts.size();i++){
            clip1 = AudioSystem.getAudioInputStream(new File("result\\result"+(i-1)+".wav"));
            clip2 = AudioSystem.getAudioInputStream(new File("records\\"+parts.get(i)));
            appendedFiles =
                    new AudioInputStream(
                            new SequenceInputStream(clip1, clip2),
                            clip1.getFormat(),
                            clip1.getFrameLength() + clip2.getFrameLength());

            AudioSystem.write(appendedFiles,
                    AudioFileFormat.Type.WAVE,
                    new File("result\\result" + i + ".wav"));
        }
        Sound.playSound("result\\result"+(parts.size()-1)+".wav").join();
    }

    public List<String> buildSequenceOfFiles() throws IOException, UnsupportedAudioFileException {
        String[] words = inputString.split(" ");
        Set<String> records = getRecords("records");
        LinkedList<String> res = new LinkedList<>();
        for (String word:words){
            word = word.toLowerCase();
            if (word.equals(".")||word.equals(",")||word.equals("-")){
                res.add(SPACE);
            }
            if (records.contains(word+".wav")){
                res.add(word+".wav");
            }
            else{
                int i=0;
                a:while(i<word.length()) {
                    while (i < word.length()) {
                        String newWord = word.substring(0, word.length() - i - 1);
                        if (records.contains(newWord + ".wav")) {

                            res.add(newWord + ".wav");
                            res.add(SMALL_SPACE);
                            word = word.substring(word.length()-i-1);
                            if (records.contains(word+".wav")){
                                res.add(word+".wav");
                                break a;
                            }
                            i=0;
                            break;
                        }
                        i++;
                    }
                }

            }
            res.add(SPACE);
        }
        for(String s:res){
            System.out.println(s);
        }
        return res;
    }

    public Set<String> getRecords(String path) throws IOException, UnsupportedAudioFileException {
        Set<String> result = new HashSet<>();
        for (String str:new File(path).list()){
            result.add(str);
        }
        return result;
    }
}
//Слава Україні Героям слава . Синтезатор написаний, лабораторна робота виконана Факультет кібернетики, кафедра ТК