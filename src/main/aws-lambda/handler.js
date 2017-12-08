// http://docs.aws.amazon.com/lambda/latest/dg/best-practices.html
'use strict'

const amqp = require('amqplib/callback_api')
const quri = 'amqp://admin:admin@34.216.61.206'
const mongouri = 'mongodb://34.216.159.222:27017/ski-records'
// const mongouri = 'mongodb://localhost:27017/ski-records'

const MongoClient = require('mongodb').MongoClient

// POST => https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev/records/load-lift-record
module.exports.loadLiftRecord = (event, context, callback) => {
  // http://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-context.html#nodejs-prog-model-context-properties
  // https://www.mongodb.com/blog/post/optimizing-aws-lambda-performance-with-mongodb-atlas-and-nodejs

  // This makes sure the db/q connection does not have to be closed (not closing it will result in
  // function execution timeout) before returning to caller, and the connection can be reused by subsequent calls
  context.callbackWaitsForEmptyEventLoop = false

  const resStart = new Date().getTime()

  // console.log(cachedDb)
  const data = event.body

  let error = 0; // TODO: promise catch => error++

  // TODO: replace nested callbacks with chaining Promises
  connectToDatabase(mongouri, db => {
    const dbQueryStart = new Date().getTime()
    return db
      .collection('lift-records')
      .insertOne(JSON.parse(data))
      .then(() => {  // TODO: should not open and close connections for each request, this slows
        db.close()   //       down performance dramatically
        const dbQueryTime = new Date().getTime() - dbQueryStart;

        return getQueueChannel(quri, (channel, conn) => {
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

            conn.close()  // TODO
            callback(null, res)
          })
        })
      })
  })
}

// GET => https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev/records/generate-skier-day-record
const skierIdRange = 40000
module.exports.generateSkierDayRecord = (event, context, callback) => {
  context.callbackWaitsForEmptyEventLoop = false;
  const day = parseInt(event.queryStringParameters['dayNum']);

  let dailySkiRecords = {};
  let mapReduceContext = {lastSkierId: 1,
                          batchSize: 50,
                          workerCount: 30,
                          finished: 0 };

  let reducer = (collection) => {
    let dailySkiDocuments = [];
    for (let skierId in dailySkiRecords) {
      dailySkiDocuments.push(dailySkiRecords[skierId]);
    }

    collection.insertMany(dailySkiDocuments)
      .then(
        () => {
          const res = {
            statusCode: 200,
            body: JSON.stringify({
              message: 'Your function executed successfully!',
            })
          };
          callback(null, res);
        }
      )
  };

  function mapper(collection) {
    if (mapReduceContext.lastSkierId >= skierIdRange){
      mapReduceContext.finished += 1;
      if (mapReduceContext.finished === mapReduceContext.workerCount){
        reducer(collection);
      }
      return;
    }

    console.log(mapReduceContext.lastSkierId);
    collection.find({skierId: {'$ge': mapReduceContext.lastSkierId,
                          '$lt': mapReduceContext.lastSkierId + mapReduceContext.batchSize},
                      day: day})
      .toArray()
      .then(
        (docs) =>{
          for (doc of docs) {
            let skierId = parseInt(doc['skierId']);
            let height = getVerticalByLiftId(parseInt(doc['liftId']));
            if (!(skierid in dailySkiRecords)) {
              dailySkiRecords[skierid] = {
                  skierId: skierId,
                  day: day,
                  liftRides: 0,
                  verticals: 0
              }
            }
            dailySkiRecords[skierId].verticals += height;
            dailySkiRecords[skierId].liftRides += 1;
          }

          mapper(collection);
        }
      );
  }

  function mapReduce(collection) {
    for(let i=0;i<mapReduceContext.workerCount;i++) {
      mapper(collection);
    }
  }

  connectToDatabase(mongouri, db => { mapReduce(db.collection('lift-records')) });
};


// GET => https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev/records/myvert?dayNum=X&skierId=Y
module.exports.getSkierDayRecord = (event, context, callback) => {
  context.callbackWaitsForEmptyEventLoop = false

  const resStart = new Date().getTime()
  let error = 0; // TODO: promise catch => error++

  // TODO: replace nested callbacks with chaining Promises
  connectToDatabase(mongouri, db => {
    const dbQueryStart = new Date().getTime()
    const skierId = parseInt(event.queryStringParameters['skierId']);
    const day = parseInt(event.queryStringParameters['dayNum']);

    return db
      .collection('daily-ski-records')
      .find({skierId: skierId, day: day})
      .toArray()
      .then(data => {
        const dbQueryTime = new Date().getTime() - dbQueryStart

        return getQueueChannel(quri, channel => {
          const qname = 'GET'
          const hostName = 'λ'
          const resTime = new Date().getTime() - resStart

          const qdata = resTime + ',' + dbQueryTime + ',' + error + ',' + qname + ',' + hostName
          return publishToQueue(channel, qname, qdata, () => {
            const res = {
              statusCode: 200,
              body: JSON.stringify({
                message: 'Your function executed successfully!',
                // input: event,
                data: data
              }),
            }

            callback(null, res)
          })
        })
      })
  })
}

function getVerticalByLiftId(liftId) {
  if (liftId >= 1  && liftId <= 10)  return 200;
  if (liftId >= 11 && liftId <= 20)  return 300;
  if (liftId >= 21 && liftId <= 30)  return 400;
  if (liftId >= 31 && liftId <= 40)  return 500;
  return -1;
}

function connectToDatabase(uri, callback) {
  return MongoClient
    .connect(uri, (err, db) => { return callback(db) })
}

function publishToQueue(channel, qname, message, callback) {
  channel.assertQueue(qname, {durable: true})
  channel.sendToQueue(qname, new Buffer(message), {}, callback)
}

function getQueueChannel(uri, callback) {
  return amqp.connect(uri, (err, conn) => {
    return conn.createConfirmChannel((err, channel) => {
      return callback(channel, conn)
    })
  })
}
