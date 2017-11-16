require 'mongo'

module Mongo
  def reset_ski_records_db(db_host)
    db_client = Mongo::Client.new([db_host])
    db = Mongo::Database.new(db_client, 'ski-records')
    db.drop

    db['lift-records'].indexes.create_one(skierId: 1)
    db['daily-ski-records'].indexes.create_one(skierId: 1)
  end
end
