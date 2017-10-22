# http://docs.aws.amazon.com/sdkforruby/api/Aws/DynamoDB/Table.html
require 'aws-sdk-dynamodb'

# def create_table(db, table_name, hash_key = { id: :number },
#                  read_capacity = 5, write_capacity = 5)
#   db.tables.create(table_name, read_capacity, write_capacity, hash_key: hash_key)
# end
#
# def delete_table(db, table_name)
#   db.tables[table_name].delete
# end

db = Aws::DynamoDB::Resource.new(
  access_key_id:     ENV['AWS_ACCESS_KEY'],
  secret_access_key: ENV['AWS_SECRET_KEY'],
  region:            'us-west-2'
)

##############################################################################
#                               Deleting tables                              #
##############################################################################


db.table('lift_records').delete
db.table('daily_ski_records').delete

#-----------------------------------------------------------------------------

##############################################################################
#                               Creating tables                              #
##############################################################################


#######################################################
#                     lift_records                    #
#######################################################

db.create_table(
  attribute_definitions: [{
    attribute_name: 'skierId',
    attribute_type: 'N'        # number
  }, {
    attribute_name: 'day',     # sort key (day + time), to form a unique combination with hash key
    attribute_type: 'S'        # string
  }],
  table_name: 'lift_records',
  key_schema: [{
    attribute_name: 'skierId', # partition key
    key_type: 'HASH'
  }, {
    attribute_name: 'day',     # sort key
    key_type: 'RANGE'
  }],
  provisioned_throughput: {
    read_capacity_units: 13,
    write_capacity_units: 13
  },
  stream_specification: {
    stream_enabled: false
  }
)

#######################################################
#                daily_ski_records                    #
#######################################################

db.create_table(
  attribute_definitions: [{
    attribute_name: 'skierId',
    attribute_type: 'N'        # number
  }, {
    attribute_name: 'day',     # sort key
    attribute_type: 'S'
  }],
  table_name: 'daily_ski_records',
  key_schema: [{
    attribute_name: 'skierId', # partition key
    key_type: 'HASH'
  }, {
    attribute_name: 'day',     # sort key
    key_type: 'RANGE'
  }],
  provisioned_throughput: {
    read_capacity_units: 12,
    write_capacity_units: 12
  },
  stream_specification: {
    stream_enabled: false
  }
)
