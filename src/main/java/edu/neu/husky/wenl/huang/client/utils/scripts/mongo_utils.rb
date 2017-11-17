require 'mongo'

module MongoUtils
  def get_db(db_host, db_name)
    db_client = Mongo::Client.new([db_host])
    Mongo::Database.new(db_client, db_name)
  end

end
