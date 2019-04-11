const fs = require('fs');

exports.input = function(e, fd, data) {
  fs.write(fd, data, null, (err) => {
    if (err) throw err;
    console.log('아두이노로 write 완료');
    fs.close(fd, (err) => {
      if(err) console.log(err);
    });
  });
};
