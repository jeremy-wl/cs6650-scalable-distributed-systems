class RequestLatency
  include Mongoid::Document

  field :response_time, type: Integer
  field :db_query_time, type: Integer
  field :error, type: Boolean
  field :host_name, type: String
  index({ host_name: 1 }, name: 'host_name_index')
end
