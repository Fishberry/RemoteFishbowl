# 서버 실행 전, 꼭 해야할 것

* ~/Desktop/FishberryServer 디렉토리의 program.sh 파일을 sh program.sh & 명령으로 실행  
* 이후 서버를 nodemon app 명령으로 실행  

여기서 program.sh 파일에는 mjpg-streamer 실행과 serialmonitor 실행을 백그라운드로 한다.  
[sudo nohup 실행할 명령 &] 처럼 사용하면 터미널 끊겨도 멈춤없이 프로그램이 백그라운드 실행된다.  
