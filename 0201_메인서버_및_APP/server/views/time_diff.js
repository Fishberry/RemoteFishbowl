function diff(current, inputHour, inputMinute){
    var time_diff = {
        hour: 0,
        minute: 0,
	second: 0,
	gaps: 0
    };
    var standard_date = new Date();
    var standard_hour = (parseInt(current.getHours())+inputHour); 

    //standard_date.setHours(standard_hour, 0);
    if(parseInt(current.getHours())>11) {
        standard_date.setHours(inputHour+12, 0);
    }
    else {
        standard_date.setHours(inputHour, 0);
    }
    standard_date.setMinutes(inputMinute, 0);

    //var gap = (standard_date.getTime() - current.getTime())/60000;
    let gap = 28788;

    time_diff.hour = parseInt(gap/60/60);
    time_diff.minute = parseInt(gap/60%60);
    time_diff.second = parseInt(gap%60);

    return time_diff;
}

module.exports = diff;
