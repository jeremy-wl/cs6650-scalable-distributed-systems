require '../scripts/mongo_utils'
include MongoUtils

# INPUT_FILE = '../../data/test_results/writer/1510965744950_59999.csv'
INPUT_FILE = '../../data/test_results/reader/1510966240725_40000.csv'

def get_nth_percentile(n, arr)
  i = (n / 100.0 * arr.size - 1).ceil
  arr[i]
end

def get_median(arr)
  arr[arr.size >> 1]
end


start_timestamps, end_timestamps, latencies = [], [], []
sum_latencies = 0

File.open(INPUT_FILE).read.each_line do |line|
  start_timestamp, latency = line.split(',').map(&:to_i)
  start_timestamps << start_timestamp
  latencies << latency
  end_timestamps << start_timestamp + latency
  sum_latencies += latency
end

n_requests = latencies.count
wall_time = (end_timestamps.max - start_timestamps.min) / 1000.0
latencies.sort!

puts '=============================================='
puts "latency Mean            : #{sum_latencies / n_requests}s"
puts "latency Median          : #{get_nth_percentile(50, latencies)}s"
puts "latency 95th percentile : #{get_nth_percentile(95, latencies)}s"
puts "latency 99th percentile : #{get_nth_percentile(99, latencies)}s"
puts '----------------------------------------------'
puts "Requests sent           : #{n_requests}"
puts "Total wall time         : #{wall_time}s"
puts "Throughput              : #{(n_requests / wall_time).round(3)} requests/s"
puts '=============================================='
