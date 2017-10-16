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
######################################################
#                Creating a table                    #
######################################################

# db.create_table(
#   attribute_definitions: [{
#     attribute_name: 'recordId',
#     attribute_type: 'S'
#   }],
#   table_name: 'lift_records',
#   key_schema: [{
#     attribute_name: 'recordId',
#     key_type: 'HASH'
#   }],
#   provisioned_throughput: {
#     read_capacity_units: 5,
#     write_capacity_units: 5
#   },
#   stream_specification: {
#     stream_enabled: false
#   }
# )


######################################################
#                Deleting a table                    #
######################################################

# db.table('lift_records').delete
