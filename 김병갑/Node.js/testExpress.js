const express = require('express'); //express 모듈
const path = require('path');       //fs로 파일을 찾는거와는 달리, path모듈을 사용
const app = express();              //express 활성화

app.set('views', __dirname);        //상대적 경로의 루트 디렉토리에 EJS 탬플릿이 있다고 알려준다.
app.set('view engine', 'ejs');      //EJS 탬플릿을 사용하겠다 선언. '.ejs'를 제외 시키기 위해 사용.

//루트 디렉토리를 찾았다면
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'writeNow.html'));  //상대적 경로의 루트 디렉토리/writeNow.html의 파일을 웹 브라우저에 보낸다.
});

// 경로가 /result 라면
app.get('/result', (req, res) => {
    const id = req.query.ID;    //name이 ID인 쿼리값 받기
    const pw = req.query.PW;    //name이 PW인 쿼리값 받기

    res.render('result', {      //응답 객체에 바로 ejs기법 적용. result.ejs에 ID에 id변수값을, PW에 pw변수값 적용.
        ID: id,
        PW: pw
    });
});

app.listen(3000, () => {        //3000번 포트로 받고, 콘솔창에 서버 시작 알림글 출력.
    console.log('시작합니드앙!');
});