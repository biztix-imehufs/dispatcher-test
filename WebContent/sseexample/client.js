var EventSource = require('eventsource');
var es = new EventSource('http://localhost:8087/DispatcherTest/sseexample');
es.onmessage = function(e) {
  console.log(e.data);
};