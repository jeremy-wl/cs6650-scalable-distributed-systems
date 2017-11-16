class RawMetricsConsumer < ActiveJob::Base
  include Sneakers::Worker
  queue_as 'POST'

  def work(msg)
    puts msg
    ack! # we need to let queue know that message was received
  end
end
