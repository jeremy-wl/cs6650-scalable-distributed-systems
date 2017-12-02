// http://docs.aws.amazon.com/lambda/latest/dg/best-practices.html
'use strict'

const amqp = require('amqplib/callback_api')
const quri = 'amqp://admin:admin@34.216.61.206'
const mongouri = 'mongodb://54.149.160.144:27017/ski-records'

const MongoClient = require('mongodb').MongoClient

let cachedDb = null
let cachedChannel = null

module.exports.load = (event, context, callback) => {
  // http://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-context.html#nodejs-prog-model-context-properties
  // https://www.mongodb.com/blog/post/optimizing-aws-lambda-performance-with-mongodb-atlas-and-nodejs

  // This makes sure the db/q connection does not have to be closed (not closing it will result in
  // function execution timeout) before returning to caller, and the connection can be reused by subsequent calls
  context.callbackWaitsForEmptyEventLoop = false

  const data = event.body
  console.log(cachedDb)

  connectToDatabase(mongouri, db => {
    db.collection('lift-records')
      .insertOne(JSON.parse(data))
      .then(function (doc) {
        return Promise.resolve()
      })
  })

  getQueueChannel(quri, channel => {
    publishToQueue(channel, 'GET', data)
  })

  const response = {
    statusCode: 200,
    body: JSON.stringify({
      message: 'Go Serverless v1.0! Your function executed successfully!',
      // input: event,
    }),
  }

  callback(null, response)
}

function connectToDatabase(uri, callback) {
  if (cachedDb && cachedDb.serverConfig.isConnected()) {
    console.log('=> using cached database instance');
    return Promise.resolve(cachedDb);
  }

  return MongoClient
    .connect(uri, (err, db) => {
      cachedDb = db
      return callback(cachedDb)
    })
}

function publishToQueue(channel, qname, message) {
  channel.assertQueue(qname, {durable: true})
  channel.sendToQueue(qname, new Buffer(message))
}

function getQueueChannel(uri, callback) {
  if (cachedChannel) {
    console.log('=> using cached q instance');
    return Promise.resolve(cachedChannel);
  }

  return amqp.connect(uri, function(err, conn) {
    return conn.createConfirmChannel(function(err, channel) {
      cachedChannel = channel
      return callback(channel)
    })
  })
}
