import matplotlib.pyplot as plt
from sortedcontainers import SortedListWithKey
from math import ceil


def get_nth_percentile(n, lst):
    i = ceil(n / 100.0 * len(lst) - 1)
    return lst[i]


def get_median(lst):
    return lst[len(lst) >> 1]


# https://www.elastic.co/blog/averages-can-dangerous-use-percentile
if __name__ == '__main__':
    pairs, percentile_99th, means, medians = [], [], [], []

    with open('../data/test_results/results2.csv') as fp:
        for line in fp:
            time, latency = line.split(',')
            time, latency = float(time) / 1000, int(latency)
            pairs.append((time, latency))

    pairs_sort_by_time = sorted(pairs, key=lambda p: p[0])
    start_time = pairs_sort_by_time[0][0]
    pairs_sort_by_time = list(map(lambda x: (x[0] - start_time, x[1]), pairs_sort_by_time))
    timestamps = list(map(lambda x: x[0], pairs_sort_by_time))

    # Self-ordering list data structure
    # http://www.grantjenks.com/docs/sortedcontainers/sortedlistwithkey.html#id1
    pairs_sort_by_latency = SortedListWithKey(key=lambda x: x[1])

    latency_sum = 0.0
    for pair in pairs_sort_by_time:      # for each timestamp (from earliest to latest):
        pairs_sort_by_latency.add(pair)  #   add the pair to another self-sorted (by latency) list
        latency_sum += pair[1]           #   and grab the 99th percentile element/median from it

        percentile_99th.append(get_nth_percentile(99, pairs_sort_by_latency)[1])
        medians.append(get_median(pairs_sort_by_latency)[1])
        means.append(latency_sum / len(pairs_sort_by_latency))

    y_axis = plt.gca().yaxis
    y_axis.grid(True, linestyle='--', alpha=0.5)

    plt.scatter(timestamps, means, c='blue', s=3, alpha=0.5, marker='o', label='Mean')
    plt.scatter(timestamps, medians, c='red', s=3, alpha=0.5, marker='o', label='Medians')
    plt.scatter(timestamps, percentile_99th, c='green', s=3, alpha=0.5, marker='o', label='99th Percentile')

    plt.xlabel('Timestamp (s)')
    plt.ylabel('Latency (ms)')
    plt.legend(scatterpoints=3)
    plt.show()
