require '../scripts/mongo_utils'
include MongoUtils

db = get_db('35.166.195.89:27017', 'latency_data')


def get_nth_percentile(n, arr)
  i = (n / 100.0 * arr.size - 1).ceil
  arr[i]
end

def get_median(arr)
  arr[arr.size >> 1]
end

metrics = {}

db['request_latencies'].find({}).each do |record|
  host    = record[:host_name]
  request = record[:req_method]

  if metrics[host].nil?
    metrics[host] = {
      num_requests_get: 0,
      num_requests_post: 0,
      response_latencies_get:  [],
      db_query_latencies_get:  [],
      response_latencies_post: [],
      db_query_latencies_post: [],
      sum_response_latencies_get: 0,
      sum_db_query_latencies_get: 0,
      sum_response_latencies_post: 0,
      sum_db_query_latencies_post: 0,
      num_errors_get: 0,
      num_errors_post: 0
    }
  end

  data = metrics[host]

  if record[:error]
    request == 'GET' ? data[:num_errors_get] : data[:num_errors_post] += 1
    next
  end

  response_time = record[:response_time]
  db_query_time = record[:db_query_time]

  if request == 'GET'
    data[:num_requests_get] += 1
    data[:sum_response_latencies_get] += response_time
    data[:sum_db_query_latencies_get] += db_query_time
    data[:response_latencies_get] << response_time
    data[:db_query_latencies_get] << db_query_time
  else # POST
    data[:num_requests_post] += 1
    data[:sum_response_latencies_post] += response_time
    data[:sum_db_query_latencies_post] += db_query_time
    data[:response_latencies_post] << response_time
    data[:db_query_latencies_post] << db_query_time
  end
end

metrics.each do |host, data|
  [
    data[:db_query_latencies_get], data[:db_query_latencies_post],
    data[:response_latencies_get], data[:response_latencies_post]].map(&:sort!)

  puts
  puts "==== Metrics for #{host} ===="
  puts

  n_get = data[:response_latencies_get].size
  n_post = data[:response_latencies_post].size

  puts "GET Response mean: #{data[:sum_db_query_latencies_get] / n_get}"
  puts "GET Response median: #{get_nth_percentile(50, data[:response_latencies_get])}"
  puts "GET Response 95th percentile: #{get_nth_percentile(95, data[:response_latencies_get])}"
  puts "GET Response 99th percentile: #{get_nth_percentile(99, data[:response_latencies_get])}"

  puts

  puts "GET DB query mean: #{data[:sum_response_latencies_get] / n_get}"
  puts "GET DB query median: #{get_nth_percentile(50, data[:db_query_latencies_get])}"
  puts "GET DB query 95th percentile: #{get_nth_percentile(95, data[:db_query_latencies_get])}"
  puts "GET DB query 99th percentile: #{get_nth_percentile(99, data[:db_query_latencies_get])}"

  puts

  puts "POST Response mean: #{data[:sum_db_query_latencies_post] / n_post}"
  puts "POST Response median: #{get_nth_percentile(50, data[:response_latencies_post])}"
  puts "POST Response 95th percentile: #{get_nth_percentile(95, data[:response_latencies_post])}"
  puts "POST Response 99th percentile: #{get_nth_percentile(99, data[:response_latencies_post])}"

  puts

  puts "POST DB query mean: #{data[:sum_response_latencies_post] / n_post}"
  puts "POST DB query median: #{get_nth_percentile(50, data[:db_query_latencies_post])}"
  puts "POST DB query 95th percentile: #{get_nth_percentile(95, data[:db_query_latencies_post])}"
  puts "POST DB query 99th percentile: #{get_nth_percentile(99, data[:db_query_latencies_post])}"

  puts

  puts "Number of GET requests received:  #{data[:num_requests_get]}"
  puts "Number of POST requests received: #{data[:num_requests_post]}"
  puts "Number of errors in GET:  #{data[:num_errors_get]}"
  puts "Number of errors in POST: #{data[:num_errors_post]}"
end
