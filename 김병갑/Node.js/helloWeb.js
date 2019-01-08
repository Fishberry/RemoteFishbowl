const http = require('http');   //http 모듈 사용

//서버 생성
http.createServer((req, res) => {
    res.writeHead(200, { 'Content-Type': 'text/html' });  //html 코드번호 : 200(정상), http-equiv 설정
    res.write('Hello Web!');    //웹 브라우저에 출력
    res.end();    //웹 종료를 나타냄. 6번과 7번을 합쳐서 'res.end('Hello Web!');' 이라고 사용해도 된다.
}).listen(3000, () => {     //3000번 포트로 요청받을 수 있음.
    console.log('Server Start');    //서버 콘솔창에 출력.
});