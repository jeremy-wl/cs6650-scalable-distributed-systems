class RequestLatency
  include Mongoid::Document

  field :response_time, type: Integer
  field :db_query_time, type: Integer
  field :req_method, type: String # 'GET' or 'POST'
  field :error, type: Boolean
  field :host_name, type: String
  index({ host_name: 1 }, name: 'host_name_index')
  index({ error: 1 }, name: 'error_index')
  index({ req_method: 1 }, name: 'req_method_index')
end
