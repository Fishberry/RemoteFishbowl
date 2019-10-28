const fs = require('fs');

exports.input = function(fd, data, msg) {
  fs.write(fd, data, null, (err) => {
    if (err) throw err;
    console.log(msg);
    fs.close(fd, (err) => {
      if(err) console.log(err);
    });
  });
};
