import MapReduce
import sys

mr = MapReduce.MapReduce()

def mapper(record):
    trim10 = record[1][0:-10]
    mr.emit_intermediate(trim10, None)

def reducer(key, list_of_values):
    mr.emit(key)


# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)






