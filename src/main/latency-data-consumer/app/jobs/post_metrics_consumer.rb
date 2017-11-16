class POSTMetricsConsumer < ActiveJob::Base
  include Sneakers::Worker
  include MetricsConsumer
  queue_as 'POST'
end
