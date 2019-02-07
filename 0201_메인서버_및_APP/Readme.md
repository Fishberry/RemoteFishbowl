# 서버 실행 전, 꼭 해야할 것

* /home/pi/Desktop/FishberryServer 디렉토리의 program.sh 파일을 sh program.sh & 명령으로 실행 ~ 
* 위 작업은 자동실행 쉘 스크립트 제작으로 대체함(아래 참고)
* 자동실행 쉘 스크립트 적용을 위해서는 init-fishberry 파일을 /etc/init.d 디렉토리에 넣어야 하며, 이 파일을 chmod 744 init-fishberry 명령으로 루트권한을 부여한 후 chown root.sys init-fishberry 명령으로 루트 소유자로 변경해준다. 이후 파일을 실행할 때와 종료할 때를 run레벨로 하드링크 시키기 위해 ln /etc/init.d/init-fishberry /etc/rc3.d/S83init-fishberry 명령과 ln /etc/init.d/init-fishberry /etc/rc0.d/K83init-fishberry 명령을 실행한다. 이는 스크립트 실행의 우선순위를 83순위로 놓는다. 겹치면 다른 숫자를 적용해야 한다.
* 이후 서버를 nodemon app 명령으로 실행( 이것도 자동실행으로 전환 가능 )

여기서 program.sh 파일에는 mjpg-streamer 실행과 serialmonitor 실행을 백그라운드로 한다.  
[sudo nohup 실행할 명령 &] 처럼 사용하면 터미널 끊겨도 멈춤없이 프로그램이 백그라운드 실행된다.  
