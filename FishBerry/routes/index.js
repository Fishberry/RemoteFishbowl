var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', (req, res, next) => {
  // DB에서 데이터를 찾은 후 보내줄 수도 있다.
  res.render('index', {
    title: 'Express'
    // 이곳에 렌더링 할때 동봉할 객체들을 기술한다
  });
});

module.exports = router;
