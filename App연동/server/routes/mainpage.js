const express = require('express');
const router = express.Router();

router.get('/', (req, res) => {
	res.status(200).render('main.ejs');
});

router.get('/main/:id', (req, res) => {
	console.log(req.params.id);
	//com6.write(req.params.id);
	res.status(200).send('Serial Controll OK!!');
});

module.exports = router;