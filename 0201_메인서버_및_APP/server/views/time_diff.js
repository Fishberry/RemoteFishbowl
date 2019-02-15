function diff(current, inputHour, inputMinute){
    var time_diff = {
        hour: 0,
        minute: 0,
	second: 0
    };
    var standard_date = new Date();
    var standard_hour = (parseInt(current.getHours())+inputHour);

    standard_date.setHours(standard_hour, 0);
    standard_date.setMinutes(inputMinute, 0);

    var gap = (standard_date.getTime() - current.getTime())/60000;

    time_diff.hour = parseInt(gap/60);
    time_diff.minute = parseInt(gap%60);
    time_diff.second = (60-parseInt(current.getSeconds()));

    return time_diff;
}

module.exports = diff;
