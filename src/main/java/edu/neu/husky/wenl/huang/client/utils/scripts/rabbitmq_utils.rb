require 'bunny'

module RabbitmqUtils
  def get_queue_channel(host)
    conn = Bunny.new(hostname: host, username: 'admin', password: 'admin')
    conn.start
    conn.create_channel
  end

  def publish_to_queue(host, queue_name, message)
    channel = get_queue_channel(host)
    channel.default_exchange.publish(message, routing_key: queue_name)
  end
end
