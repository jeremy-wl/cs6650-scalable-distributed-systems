'use strict'
const amqp = require('amqplib/callback_api')
const username = 'admin'
const password = 'admin'
const queueHost = '34.216.61.206'
const uri = ''.concat('amqp://', username, ':', password, '@', queueHost)

module.exports.load = (event, context, callback) => {
  amqp.connect(uri, function(err, conn) {
    conn.createConfirmChannel(function(err, channel) {
      const q = 'GET'

      channel.assertQueue(q, {durable: true})
      channel.sendToQueue(q, new Buffer('Hello World!'), {}, function () {
        conn.close()
      })
    })
  })

  const response = {
    statusCode: 200,
    body: JSON.stringify({
      message: 'Go Serverless v1.0! Your function executed successfully!',
      input: event,
    }),
  }

  callback(null, response)
}
