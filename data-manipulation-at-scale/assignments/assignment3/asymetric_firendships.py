import MapReduce
import sys

mr = MapReduce.MapReduce()


def mapper(record):
    set_friendship = set(record)
    key = str(set_friendship)
    mr.emit_intermediate(key, set_friendship)


def reducer(key, list_of_values):
    if len(list_of_values)==1:
        list_of_values = list(list_of_values[0])
        mr.emit((list_of_values[0], list_of_values[1]))
        mr.emit((list_of_values[1], list_of_values[0]))

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
