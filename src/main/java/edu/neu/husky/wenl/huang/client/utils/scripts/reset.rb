require_relative 'mongo_utils'
require_relative 'rabbitmq_utils'
include MongoUtils
include RabbitmqUtils

def reset_queue(host, queue_name)
  channel = get_queue_channel(host)
  channel.queue(queue_name, durable: true).purge
  channel.queue(queue_name, durable: true) # Declaring a queue is idempotent
end

def reset_ski_records_db(db_host)
  db = get_db(db_host, 'ski-records')
  db.drop

  db['lift-records'].indexes.create_many(
    [{ key: { skierId: 1 } },
     { key: { day: 1 } }]
  )
  db['daily-ski-records'].indexes.create_many(
    [{ key: { skierId: 1 } },
     { key: { day: 1 } }]
  )
end

def reset_latency_db(db_host)
  get_db(db_host, 'latency_data').drop
end

def reset_collections_and_queues(db_host_ski, db_host_latency, q_host)
  reset_ski_records_db(db_host_ski)
  reset_latency_db(db_host_latency)
  reset_queue(q_host, 'GET')
  reset_queue(q_host, 'POST')
end

# reset_collections_and_queues('localhost:27017',
#                              'localhost:27017',
#                              'localhost')
reset_collections_and_queues('54.149.160.144:27017',
                             '34.216.61.206',
                             '34.216.61.206')
