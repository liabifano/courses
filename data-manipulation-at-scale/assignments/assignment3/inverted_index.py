import MapReduce
import sys

mr = MapReduce.MapReduce()


def mapper(record):
    key = record[0]
    words = record[1]
    words = words.split()
    for w in words:
        mr.emit_intermediate(w, key)


def reducer(key, list_of_values):
    mr.emit((key, list(set(list_of_values))))

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
