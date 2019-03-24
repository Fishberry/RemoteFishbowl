Mysql 설치 하는 법
	sudo apt-get install mysql-server mysql-client

설치 후 최초 Mysql 접근
	sudo mysql -u root (처음은 패스워드가 없기 때문에 -p 옵션을 주지 않는다. 현재는 MariaDB로 개명되었다.)

처음일 경우 설정하는 법

1. 비밀번호 설정
	use mysql; (mysql이라는 이름의 데이터베이스 사용)

	1.1) 자신의 Mysql이 최근것이고 데몬이랑 버전을 비교했을 경우, 하위가 아니라면
		UPDATE user SET password=PASSWORD('원하는 비밀번호') where user='root'; (루트의 비밀번호를 설정해준다.)

	1.2) 1.1의 조건에 부합하지 않는 경우
		아무것도 모른 채로 DB에서 값을 가져와 서버를 출력하려고 하면 이러한 에러를 볼 수 있게 된다.
		Error: ER_NOT_SUPPORTED_AUTH_MODE: Client does not support authentication protocol requested by server; consider upgrading MariaDB client. 와 비슷한 에러를 출력한다. 이는, 서버의 Authentication Method의 설정 단계를 recommanded가 아닌 legacy로 설정을 해 주어야 한다. 만약 자신이 GUI로 mysql installer를 볼 수 있다면

	mysql installer > server 옆에 reconfigure > Auth 설정에서 legacy 선택 후 완료를 누르면 된다.

	하지만 CUI라면 이렇게 진행한다.

	use mysql;
	update user set authentication_string=password('원하는 비밀번호'), plugin='mysql_native_password' where user='root';

	위 과정을 거쳐야 정상적으로 작동을 하고 그렇지 않는다면 1.2에서 나온 에러가 발생할 것이다.

=============================

<에러가 발생해서 완전히 제거하고 싶은 경우>

예를 들어, mysqld.sock을 찾을 수 없다거나, 권한 거부가 나서 열고 싶어도 열 수가 없어서 재설치를 해도 곤란한 경우가 온다. 이런 경우, 완벽하게 제거를 한 후에 재설치를 해야 정상작동을 하게 된다.

완전 제거법

sudo apt-get remove --purge mysql*  
sudo apt-get remove --purge mysql  
sudo apt-get remove --purge mariadb  
sudo apt-get remove --purge mariadb*  
sudo apt-get --purge remove mysql-server  
sudo apt-get --purge remove python-software-properties  
