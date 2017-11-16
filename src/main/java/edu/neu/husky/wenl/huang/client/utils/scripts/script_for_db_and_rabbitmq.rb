require 'mongo'
require 'bunny'

# def delete_records_from_collection(collection, filter = {})
#   collection.delete_many(filter)
# end
#
# delete_records_from_collection(db['lift-records'], day: 1)

def get_queue_channel(host)
  conn = Bunny.new(hostname: host)
  conn.start
  conn.create_channel
end

def publish_to_queue(host, queue_name, message)
  channel = get_queue_channel(host)
  channel.default_exchange.publish(message, routing_key: queue_name)
end

def reset_queue(host, queue_name)
  channel = get_queue_channel(host)
  channel.queue(queue_name, durable: true).delete
  channel.queue(queue_name, durable: true) # Declaring a queue is idempotent
end

def reset_db(db_host, db_name)
  db_client = Mongo::Client.new([db_host])
  db = Mongo::Database.new(db_client, db_name)
  db.drop

  db['lift-records'].indexes.create_one(skierId: 1)
  db['daily-ski-records'].indexes.create_one(skierId: 1)
end

def reset_collections_and_queue(db_host, db_name, q_host)
  reset_db(db_host, db_name)
  reset_queue(q_host, 'GET')
  reset_queue(q_host, 'POST')
end

reset_collections_and_queue('localhost:27017', 'ski-records', 'localhost')
