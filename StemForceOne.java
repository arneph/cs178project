// To run:
// javac StemForceOne.java
// java StemForceOne path/source.csv path/tweetStems.csv path/stems.csv

import java.io.*;
import java.lang.*;
import java.nio.file.*;
import java.util.*;

import opennlp.tools.tokenize.*;
import opennlp.tools.stemmer.*;
import opennlp.tools.stemmer.snowball.*;

class StemForceOne {
    static String fileToString(String path) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        }catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return content;
    }
    
    static void stringToFile(String path, String content) {
        try (PrintWriter out = new PrintWriter(path)) {
            out.print(content);
        }catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
    
    static class Stem {
        int index;
        String root;
        int count;
        HashMap<String, Integer> versions;
    }
    
    static class Tweet {
        int index;
        String content;
        int label;
        ArrayList<Stem> stems;
    }
    
    static ArrayList<Tweet> csvToTweets(String csv) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (String line : csv.split("\n")) {
            if (line.startsWith(",")) continue;
            if (line.equals("")) continue;
            int c = line.indexOf(",");
            int s = line.indexOf("\"") + 1;
            int t = line.lastIndexOf("\"");
            int l = line.lastIndexOf(",") + 1;
            
            Tweet tweet = new Tweet();
            tweet.index = Integer.parseInt(line.substring(0, c));
            tweet.content = line.substring(s, t);
            tweet.label = Integer.parseInt(line.substring(l));
            tweet.stems = new ArrayList<>();
            tweets.add(tweet);
        }
        return tweets;
    }
    
    static String tweetsToCSV(ArrayList<Tweet> tweets) {
        StringBuilder bob = new StringBuilder();
        bob.append(",Stems,Label\n");
        
        for (Tweet tweet : tweets) {
            bob.append(tweet.index);
            bob.append(",{");
            for (int i = 0; i < tweet.stems.size(); i++) {
                Stem stem = tweet.stems.get(i);
                
                if (i > 0) bob.append(",");
                bob.append(stem.index);
            }
            bob.append("},");
            bob.append(tweet.label);
            bob.append("\n");
        }
        
        return bob.toString();
    }
    
    static String stemsToCSV(ArrayList<Stem> stems) {
        StringBuilder bob = new StringBuilder();
        bob.append(",Root,Count,Versions\n");
        
        for (Stem stem : stems) {
            bob.append(stem.index);
            bob.append(",\"" + stem.root + "\"," + stem.count + ",{");
            int i = 0;
            for (String version : stem.versions.keySet()) {
                int count = stem.versions.get(version);
                
                if (i++ > 0) bob.append(",");
                bob.append("\"" + version + "\":" + count);
            }
            bob.append("}\n");
        }
        
        return bob.toString();
    }

    public static void main(String[] args) {
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        
        String inPath = args[0];
        String inContent = fileToString(inPath);
        
        ArrayList<Tweet> tweets = csvToTweets(inContent);
        
        ArrayList<Stem> orderedStems = new ArrayList<>();
        HashMap<String, Stem> stems = new HashMap<>();
        
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            
            for (String token : tokenizer.tokenize(tweet.content)) {
                if (token.length() < 1) continue;
                
                token = token.toLowerCase();
                
                if (token.equals("@")) continue;
                if (token.equals("#")) continue;
                if (token.equals("a")) continue;
                if (token.equals("the")) continue;
                if (token.equals("of")) continue;
                if (token.equals("is")) continue;
                if (token.equals("it")) continue;
                if (token.equals("in")) continue;
                if (token.equals("on")) continue;

                String stemString = stemmer.stem(token).toString();
                Stem stem = stems.get(stemString);
                
                if (stem == null) {
                    stem = new Stem();
                    stem.index = orderedStems.size();
                    stem.root = stemString;
                    stem.count = 0;
                    stem.versions = new HashMap<>();
                    
                    stems.put(stemString, stem);
                    orderedStems.add(stem);
                }
                
                stem.count++;
                stem.versions.putIfAbsent(token, 0);
                stem.versions.merge(token, 1, Integer::sum);
                
                tweet.stems.add(stem);
            }
        }
        
        System.out.println("Tweets: " + tweets.size());
        System.out.println("Stems: " + stems.size());
        
        String outTweetsPath = args[1];
        String outTweetsContent = tweetsToCSV(tweets);
        
        stringToFile(outTweetsPath, outTweetsContent);
        
        String outStemsPath = args[2];
        String outStemsContent = stemsToCSV(orderedStems);
        
        stringToFile(outStemsPath, outStemsContent);
    }
}


