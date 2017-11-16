require_relative 'mongo'
require_relative 'rabbitmq'

def reset_queue(host, queue_name)
  channel = get_queue_channel(host)
  channel.queue(queue_name, durable: true).delete
  channel.queue(queue_name, durable: true) # Declaring a queue is idempotent
end

def reset_collections_and_queue(db_host, q_host)
  reset_ski_records_db(db_host)
  reset_queue(q_host, 'GET')
  reset_queue(q_host, 'POST')
end

reset_collections_and_queue('localhost:27017', 'localhost')
