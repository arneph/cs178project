# CS 178 Project

Our project was to predict emojis associated with tweets. The dataset is taken from Kaggle:

https://www.kaggle.com/hariharasudhanas/twitter-emoji-prediction

The Stemmer is based on Apache OpenNLP:

http://opennlp.apache.org

The joinrows.py script is used to remove linebreaks in each tweet in the original dataset.
To run the joinrows.py script:

python joinrows.py [in_file] [out_file]

The TweetStemmer is used to replace words in tweets by indexed stems (common roots of words).
To run the TweetStemmer:

javac TweetStemmer.java

java TweetStemmer path/source.csv path/tweetStems.csv path/stems.csv

The main work including data analysis, Bayesian prediction and a neural network trained with Keras/TensorFlow is contained within twitter_emoji_prediction.ipynb.
