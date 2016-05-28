import sys
import json


def hw(sent_file, tweet_file):
    precomp_scores = get_precomp_scores(sent_file)
    tweets = get_tweet_data(tweet_file)

    for tweet in tweets:
        print sum(map(lambda x: precomp_scores.get(x, 0), tweet))


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
