class GETMetricsConsumer < ActiveJob::Base
  include Sneakers::Worker
  include MetricsConsumer
  queue_as 'GET'
end
