# CS 178 Project

Our project was to predict emojis associated with tweets. The dataset is taken from Kaggle:

https://www.kaggle.com/hariharasudhanas/twitter-emoji-prediction

The Stemmer is based on Apache OpenNLP:

http://opennlp.apache.org

To run the joinrows.py script:

python joinrows.py [in_file] [out_file]

To run the TweetStemmer:

javac TweetStemmer.java

java TweetStemmer path/source.csv path/tweetStems.csv path/stems.csv
