// DB 위치 : /var/lib/mysql 안에 있으며, apt-get이나 yum같은 패키지 다운로더를 이용하면 Default Path로 깔리게 된다.

const db = require('mysql');    //mysql 모듈을 사용하기 위한 객체. DB값 파싱에 사용.
const fs = require('fs');       //fs 모듈을 사용하기 위한 객체. 온도값 파싱에 사용.

//db에 접근하기 위한 객체를 만든다.
const connection = db.createConnection({
	host	: 'localhost',      //호스트 아이피
	user	: 'root',           //Mysql 유저 이름
	password	: '1234',       //Mysql 비밀번호
	database	: 'Fishberry'   //사용하는 DB 이름
});

connection.connect();

// DB에 실행한 sql문을 넣고 해당 값으로 나온 인자들을 기반으로 함수를 만들어 프로그래밍한다.
// error : sql문 실행 도중 오류가 일어났을 경우의 변수
// results : 결과값 데이터를 담고 있는 변수
// fields : 결과의 필드값을 담고 있는 변수
connection.query('select * from TemperSetting', (error, results, fields) => {

    //에러가 일어난다면 에러 출력
	if(error)
		console.log(error);
	else {      //정상작동한다면
		var min = results[0].min;   //첫번째 결과값의 min에 해당하는 데이터를 담는다.
		var max = results[0].max;   //첫번째 결과값의 max에 해당하는 데이터를 담는다.
		var temper = 0.0;       //온도값을 담을 변수

        //fs를 이용해서 온도값 파싱을 한다.
		fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
			var text = data;    //첫번째 인자값의 경로에 담겨있는 파일의 데이터를 담는다.
			var first = text.substring(69,71);      //69번쨰 이상 71번째 미만의 글자를 잘라서 담는다.
			var second = text.substring(71,74);     //71번째 이상 74번째 미만의 글자를 잘라서 담는다.

			temper = first + '.' + second;  //온도 완성
			console.log(temper);

			if(temper < min || temper > max) {  //온도가 설정치 미만이거나 초과일경우
				if(temper < min){
					console.log('Temper warning! (lower temper than "setting min temper")');
					console.log('Current temper : ' + temper);
				}
				else if(temper > max) {
					console.log('Temper warning! (Higher temper than "setting max temper")');
					console.log('Current temper : ' + temper);
				}
            }
            //온도가 설정 범위에 있을 경우
			else
				console.log('Current temper : ' + temper);
		});
	}

});

// DB 객체 close. 이로써 DB객체는 소멸한다.
connection.end();

// 참고한 페이지
// https://opentutorials.org/course/3347/21185 - 생활코딩
