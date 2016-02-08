import sys
import json


def calculate_weight_score(x):  # dummy rule
    if x < 0:
        return -1
    elif x > 0:
        return 1
    else:
        return 0


def hw(sent_file, tweet_file):
    precomp_scores = get_precomp_scores(sent_file)
    tweets = get_tweet_data(tweet_file)
    all_words = sum(tweets, [])
    not_scored_words = set(all_words) - set(precomp_scores.keys())
    dict_word_tweets = {}

    for tweet in tweets:
        inter_words = set(tweet).intersection(not_scored_words)
        for word in inter_words:
            dict_word_tweets[word] = dict_word_tweets.get(word, []) + tweet

    dict_scores_per_word = map(lambda (k, v): (k, map(lambda x: precomp_scores.get(x, 0), v)),
                               dict_word_tweets.iteritems())
    dict_scores_per_word = map(lambda (k, v): (k, map(calculate_weight_score, v)), dict_scores_per_word)
    dict_scores_per_word = map(lambda (k, v): (k, sum(v) / float(len(v))), dict_scores_per_word)

    for (k, v) in dict_scores_per_word:
        print k + ' ' + str(round(v, 3))


def get_precomp_scores(fp):
    precomp_scores = {}

    for line in fp:
        term, score = line.split("\t")
        precomp_scores[term] = int(score)

    return precomp_scores


def get_tweet_data(fp):
    def parse(x):
        try:
            return json.loads(x)['text']
        except:
            return ""

    return map(lambda x: x.lower().split(), map(parse, fp))


def main():
    sent_file = open(sys.argv[1])
    tweet_file = open(sys.argv[2])
    hw(sent_file, tweet_file)


if __name__ == '__main__':
    main()
