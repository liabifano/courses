import pandas as pd

def merge_and_count(left, right):
    merged = []
    acc_inv = 0
    while (len(left)>0 and len(right)>0):
        if left[0] <= right[0]:
            merged.append(left[0])
            left = left[1:]
        else:
            acc_inv = acc_inv + len(left)
            merged.append(right[0])
            right = right[1:]

    if len(left)>0: merged = merged + left
    if len(right)>0: merged = merged + right

    return (merged, acc_inv)

def sort_and_count(v):
    count_inv = 0
    if len(v) <= 1: return (v, count_inv)

    left, countright = sort_and_count(v[0:len(v)/2])
    right, count_right = sort_and_count(v[len(v)/2:])

    sorted_, splited_inv = merge_and_count(left, right)

    count_inv = countright + count_right + splited_inv

    return (sorted_, count_inv)

def main():
    values = list(pd.read_csv('_bcb5c6658381416d19b01bfc1d3993b5_IntegerArray.txt', header=None)[0])
    (sorted_, count_inv) = sort_and_count(values)
    print 'Number of inversions: %d' %count_inv

if __name__ == '__main__':
    main()