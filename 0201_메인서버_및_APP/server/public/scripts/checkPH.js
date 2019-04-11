const db = require('../../models/findDB');
let phValue = '';

function startCheckPH(fs) {
  fs.readFile('/home/pi/Desktop/FishberryServer/background/pH_log', 'utf8', (err, data) => {
    if (err) throw err;
    var text = data;
    var ph = text.substring(0, 4);
    phValue = `${ph}`;
    //console.log("phValue : " + phValue);
    db.insertPvalue(phValue);

  });
}

module.exports = {
  startCheckPH
};
