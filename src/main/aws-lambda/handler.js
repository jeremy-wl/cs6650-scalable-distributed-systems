// http://docs.aws.amazon.com/lambda/latest/dg/best-practices.html
'use strict'

const amqp = require('amqplib/callback_api')
const quri = 'amqp://admin:admin@34.216.61.206'
const mongouri = 'mongodb://54.149.160.144:27017/ski-records'
// const mongouri = 'mongodb://localhost:27017/ski-records'

const MongoClient = require('mongodb').MongoClient

let cachedDb = null
let cachedChannel = null

// POST => https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev/records/load-lift-records
module.exports.loadLiftRecord = (event, context, callback) => {
  // http://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-context.html#nodejs-prog-model-context-properties
  // https://www.mongodb.com/blog/post/optimizing-aws-lambda-performance-with-mongodb-atlas-and-nodejs

  // This makes sure the db/q connection does not have to be closed (not closing it will result in
  // function execution timeout) before returning to caller, and the connection can be reused by subsequent calls
  context.callbackWaitsForEmptyEventLoop = false

  const resStart = new Date().getTime()

  // console.log(cachedDb)
  const data = event.body

  let error = 0;

  // TODO: replace nested callbacks with chaining Promises
  connectToDatabase(mongouri, db => {
    const dbQueryStart = new Date().getTime()
    return db
      .collection('lift-records')
      .insertOne(JSON.parse(data))
      .then(() => {
        const dbQueryTime = new Date().getTime() - dbQueryStart

        return getQueueChannel(quri, channel => {
          const qname = 'POST'
          const hostName = 'λ'
          const resTime = new Date().getTime() - resStart

          const qdata = resTime + ',' + dbQueryTime + ',' + error + ',' + qname + ',' + hostName
          return publishToQueue(channel, qname, qdata, () => {
            const res = {
              statusCode: 200,
              body: JSON.stringify({
                message: 'Your function executed successfully!',
                // input: event,
              }),
            }

            callback(null, res)
          })
        })
      })
  })
}

// GET => https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev/records/generate-daily-skier-records
// invoke this handler locally (with serverless) with care
// - db connection is not closed, so it will crash db very quickly
const skierIdRange = 40000
module.exports.generateDailySkierRecords = (event, context, callback) => {
  context.callbackWaitsForEmptyEventLoop = false

  for (let skierId = 1; skierId <= skierIdRange; skierId++) {
    const day = parseInt(event.queryStringParameters['day']);

    let liftRides = 0, verticals = 0;

    connectToDatabase(mongouri, db => {
      return db.collection('lift-records')
        .find({skierId: skierId, day: day})
        .toArray()
        .then(doc => {

          for (let i = 0; i < doc.length; i++) {
            const liftId = parseInt(doc[i]['liftId'])
            verticals += getVerticalByLiftId(liftId)
            liftRides++
          }

          return db
            // .collection('lift-records')
            .collection('lift-records-tmp')
            .insertOne({
              skierId: skierId,
              day: day,
              liftRides: liftRides,
              verticals: verticals
            })
        })
    })
  }

  const res = {
    statusCode: 200,
    body: JSON.stringify({
      message: 'Your function executed successfully!',
      // input: event,
    }),
  }

  callback(null, res)
}

function getVerticalByLiftId(liftId) {
  if (liftId >= 1  && liftId <= 10)  return 200;
  if (liftId >= 11 && liftId <= 20)  return 300;
  if (liftId >= 21 && liftId <= 30)  return 400;
  if (liftId >= 31 && liftId <= 40)  return 500;
  return -1;
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

function publishToQueue(channel, qname, message, callback) {
  channel.assertQueue(qname, {durable: true})
  channel.sendToQueue(qname, new Buffer(message), {}, callback)
}

function getQueueChannel(uri, callback) {
  if (cachedChannel) {
    console.log('=> using cached q instance');
    return Promise.resolve(cachedChannel);
  }

  return amqp.connect(uri, (err, conn) => {
    return conn.createConfirmChannel((err, channel) => {
      cachedChannel = channel
      return callback(channel)
    })
  })
}
