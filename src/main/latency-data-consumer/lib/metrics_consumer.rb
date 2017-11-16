module MetricsConsumer
  def work(request_latency_info)
    response_time, db_query_time, error, req_method, host_name = request_latency_info.split(',')
    # insert raw data to db
    RequestLatency.create(
      response_time: response_time,
      db_query_time: db_query_time,
      error: error,
      req_method: req_method,
      host_name: host_name
    )
    ack! # we need to let queue know that message was received
  end
end
