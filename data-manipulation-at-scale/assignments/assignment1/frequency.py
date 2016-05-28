import sys
import json


def hw(tweet_file):
    tweets = get_tweet_data(tweet_file)
    all_words = sum(tweets, [])
    total_words = len(all_words)
    set_all_words = set(all_words)

    for word in set_all_words:
        proportion = round(sum(map(lambda x: x == word, all_words)) / float(total_words), 4)
        print word+' '+str(proportion)

def get_tweet_data(fp):
    def parse(x):
        try:
            return json.loads(x)['text']
        except:
            return ""

    return map(lambda x: x.lower().split(), map(parse, fp))


def main():
    tweet_file = open(sys.argv[1])
    hw(tweet_file)


if __name__ == '__main__':
    main()
