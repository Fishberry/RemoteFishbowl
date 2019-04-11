const db = require('../../models/findDB');
let temperature = '';

function startCheckTemper(fs, connection) {
  fs.access('/sys/bus/w1/devices/28-020d9246133d/w1_slave', fs.constants.F_OK, (err) => {
    if (err) {
      temperature = '-999';
      console.log("온도 읽어올 수 없음.");
    }

    else {
      fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {

        //console.log('읽어온 온도 : ', data); // 전체 파일의 내용을 로그찍음

        if (err) throw err;
        var text = data;
        var first = text.substring(69, 71);
        var second = text.substring(71, 72);

        temperature = `${first}.${second}`;
        //console.log("Temperature : " + temperature);
        db.insertTvalue(temperature);

      });
    }
  });
}
module.exports = {
  startCheckTemper
};
