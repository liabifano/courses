import json
import operator
import sys
from collections import Counter


def hw(tweet_file):
    tags = sum(get_tags_data(tweet_file), [])
    dict_freqs = Counter(tags)
    top_ten = sorted(dict_freqs.items(), key=operator.itemgetter(1), reverse=True)[0:10]

    for tag in top_ten:
        print tag[0] + ' ' + str(tag[1])


def get_tags_data(fp):
    def parse(x):
        try:
            return json.loads(x)['entities']["hashtags"]
        except:
            return ""

    def extract_text(list_tags):
        all_tags = []
        for item in list_tags:
            all_tags.append(item['text'])

        return all_tags

    return map(extract_text,
               filter(lambda x: x,
                      map(parse, fp)))


def main():
    tweet_file = open(sys.argv[1])
    hw(tweet_file)


if __name__ == '__main__':
    main()
