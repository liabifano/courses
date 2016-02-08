import sys
import json
import operator

dict_states = {
    'AK': 'Alaska',
    'AL': 'Alabama',
    'AR': 'Arkansas',
    'AS': 'American Samoa',
    'AZ': 'Arizona',
    'CA': 'California',
    'CO': 'Colorado',
    'CT': 'Connecticut',
    'DC': 'District of Columbia',
    'DE': 'Delaware',
    'FL': 'Florida',
    'GA': 'Georgia',
    'GU': 'Guam',
    'HI': 'Hawaii',
    'IA': 'Iowa',
    'ID': 'Idaho',
    'IL': 'Illinois',
    'IN': 'Indiana',
    'KS': 'Kansas',
    'KY': 'Kentucky',
    'LA': 'Louisiana',
    'MA': 'Massachusetts',
    'MD': 'Maryland',
    'ME': 'Maine',
    'MI': 'Michigan',
    'MN': 'Minnesota',
    'MO': 'Missouri',
    'MP': 'Northern Mariana Islands',
    'MS': 'Mississippi',
    'MT': 'Montana',
    'NA': 'National',
    'NC': 'North Carolina',
    'ND': 'North Dakota',
    'NE': 'Nebraska',
    'NH': 'New Hampshire',
    'NJ': 'New Jersey',
    'NM': 'New Mexico',
    'NV': 'Nevada',
    'NY': 'New York',
    'OH': 'Ohio',
    'OK': 'Oklahoma',
    'OR': 'Oregon',
    'PA': 'Pennsylvania',
    'PR': 'Puerto Rico',
    'RI': 'Rhode Island',
    'SC': 'South Carolina',
    'SD': 'South Dakota',
    'TN': 'Tennessee',
    'TX': 'Texas',
    'UT': 'Utah',
    'VA': 'Virginia',
    'VI': 'Virgin Islands',
    'VT': 'Vermont',
    'WA': 'Washington',
    'WI': 'Wisconsin',
    'WV': 'West Virginia',
    'WY': 'Wyoming'
}


def hw(sent_file, tweet_file):
    tweets = filter(lambda x: x is not None, get_tweet_data(tweet_file))
    states = set(map(lambda x: x[1], tweets))
    precomp_scores = get_precomp_scores(sent_file)
    list_states_scores = []

    for state in states:
        filted_tweets = sum(map(lambda x: x[0], filter(lambda x: x[1]==state, tweets)),[])
        score_per_state = sum(map(lambda x: precomp_scores.get(x, 0), filted_tweets)) / float(len(filted_tweets))
        list_states_scores.append((state, score_per_state))

    happiest_state = sorted(dict(list_states_scores).items(), key=operator.itemgetter(1))[-1][0]

    print dict_states.keys()[dict_states.values().index(happiest_state)]


def get_tweet_data(fp):
    def parse(x):
        try:
            return (json.loads(x)['text'], json.loads(x)['user']['location'])
        except:
            return None

    return filter(lambda x: x[1] is not None and x[1] in dict_states.values(),
                  map(lambda x: (x[0].lower().split(), x[1]),
                    filter(lambda x: x is not None,
                      map(parse, fp))))



def get_precomp_scores(fp):
    precomp_scores = {}

    for line in fp:
        term, score = line.split("\t")
        precomp_scores[term] = int(score)

    return precomp_scores


def main():
    sent_file = open(sys.argv[1])
    tweet_file = open(sys.argv[2])
    hw(sent_file, tweet_file)


if __name__ == '__main__':
    main()
