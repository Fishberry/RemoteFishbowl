# 서버 실행 전, 꼭 해야할 것

* /home/pi/Desktop/FishberryServer 디렉토리의 program.sh 파일을 sh program.sh & 명령으로 실행
* 위 작업은 자동실행 쉘 스크립트 제작으로 대체함(아래 참고)
* 자동실행 쉘 스크립트 적용을 위해서는 init-fishberry 파일을 /etc/init.d 디렉토리에 넣어야 하며, 이 파일을 chmod 744 init-fishberry 명령으로 루트권한을 부여한 후 chown root.sys init-fishberry 명령으로 루트 소유자로 변경해준다. 이후 파일을 실행할 때와 종료할 때를 run레벨로 하드링크 시키기 위해 ln /etc/init.d/init-fishberry /etc/rc3.d/S83init-fishberry 명령과 ln /etc/init.d/init-fishberry /etc/rc0.d/K83init-fishberry 명령을 실행한다. 이는 스크립트 실행의 우선순위를 83순위로 놓는다. 겹치면 다른 숫자를 적용해야 한다.  
만약 라즈베리를 재설치 했을 때 쉘 스크립트를 윈도우에서 그냥 가져왔다면 스크립트에 ^M 문자가 붙는다. 이걸 지워주기 위해서 vi -b로 그 파일을 열어서 없애줘야 한다.  
* 이후 서버를 nodemon app 명령으로 실행( 이것도 자동실행으로 전환 가능 )
* 서버 실행시 nodemon -> forever start app.js로 실행하면 더욱 좋음

여기서 program.sh 파일에는 mjpg-streamer 실행, serialmonitor 실행, ph값 얻어오는것을 백그라운드로 한다.  
[sudo nohup 실행할 명령 &] 처럼 사용하면 터미널 끊겨도 멈춤없이 프로그램이 백그라운드 실행된다.  

### 2019-02-22 추가

서버를 자동으로 실행시키기 위해 program.sh에 /usr/bin/forever start /home/pi/Desktop/FishberryServer/app.js 명령을 추가  
forever 명령어는 서버를 자동으로 재시작해주는 nodemon 모듈과 비슷하며,   
start / stop / restart 처럼 프로세스를 관리해 줄 수 있다.  
http://todactodac.blogspot.com/2016/06/nodejs-forever.html 페이지를 참고  

### 서버의 용량이 100%로 꽉 찼을 때 해결방법  

df -h명령어로 /dev/root의 Use%가 100% 꽉차서 용량을 더이상 못 사용할 때  
[1] su 명령어로 관리자 모드로 들어간다.  
[2] sudo du -h --max-depth=1 / 명령어로 꽉차보이는 디렉토리를 찾는다  
[3] 만약 /var/log 폴더가 1기가 이상으로 꽉찼다면, 비워주러 간다  
[4] /var/log 디렉토리로 가서 ls -alh 명령으로 어떤 파일이 200Mbyte 이상인지 확인한다  
[5] 만약 daemon.log 파일이 200M 이상이라면 아래와 같은 명령어로 0M를 만들며 파일은 지우지 않도록 한다. (rm 사용금지- 파일 뻑남)  
[5-1] cat /dev/null > /var/log/daemon.log (관리자 모드에서 해야함)  
[5-2] 나머지 200M 이상인 로그!!들만 비운다.   
https://sisiblog.tistory.com/24 <- 로그에 대한 정보  
[6] 로그가 아니라면 다른 꽉찬 부분을 확인 후 비워준다.  
