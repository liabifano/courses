import MapReduce
import sys

mr = MapReduce.MapReduce()


def mapper(record):
    mr.emit_intermediate(record[1], {'database': record[0], 'record': record})


def reducer(key, list_of_values):
    databases = list(set(map(lambda x: x['database'], list_of_values)))
    # xunxo detected, it gonna break when it have more than 2 datasets
    one_database = filter(lambda x: x['database'] == databases[0], list_of_values)
    other_database = filter(lambda x: x['database'] == databases[1], list_of_values)
    for data in one_database:
        mr.emit(other_database[0]['record'] + data['record'])

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
