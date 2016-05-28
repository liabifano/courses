import MapReduce
import sys

mr = MapReduce.MapReduce()


def our_groupby(alist):
    d = {}
    for x, y in alist:
        d.setdefault(x, []).append(y)
    return d


def mapper(record):
    if record[0] == 'a':
        for k in range(0, 4 + 1):
            mr.emit_intermediate((record[1], k), (record[2], record[3]))
    elif record[0] == 'b':
        for k in range(0, 4 + 1):
            mr.emit_intermediate((k, record[2]), (record[1], record[3]))


def reducer(key, list_of_values):
    element = sum(map(lambda x: 0 if len(x) == 1 else x[0] * x[1], our_groupby(list_of_values).values()))
    mr.emit((key[0], key[1], element))

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
