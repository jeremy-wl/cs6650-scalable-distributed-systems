class RawMetricsConsumer < ActiveJob::Base
  include Sneakers::Worker
  queue_as 'POST'

  def work(request_latency_info)
    response_time, db_query_time, error, host_name = request_latency_info.split(',')
    # insert raw data to db
    RequestLatency.create(
      response_time: response_time,
      db_query_time: db_query_time,
      error: error,
      host_name: host_name
    )
    ack! # we need to let queue know that message was received
  end
end
