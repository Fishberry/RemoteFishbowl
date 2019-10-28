package com.example.shinj.navmain;

import io.socket.client.Socket;

//싱글톤 클래스입니다. 소켓이랑 주소를 담고 있으며 주소가 필요없을시에는 뺄 생각도 하고 있습니다.
public class IntentData {
    //이 변수에 new IntentData()라는 인스턴스가 담겨있냐없냐를 판단하는 변수.
    //이 변수 때문에, 새로 인스턴스를 만들지 않기 때문에, 전역 클래스로 활용할 수 있고, 변수에 담겨있는 정보를 그대로 활용할 수 있다.
    private static IntentData uniqueInstance;

    //다른 변수들 여기에
    Socket socket;
    String address;

    //생성자에 private를 둔 이유는, 누군가가 new IntentData()같이 직접 인스턴스를 생성하려고 하는 행위를 방지하기 위함.
    private IntentData() {}

    //다른 클래스에서 객체를 생성해서 해당 클래스의 변수를 받고 싶을 시에 사용하는 메소드.
    //예로 들면 IntentData intentData = IntentData.getInstance(); 라고 사용하면 된다.
    //절대 IntentData intentData = new IntentData();라고 사용하지 말 것.
    //사용하면 앞에 권한을 private에서 다른 것으로 바꾸라고 한다.
    //private에서 다른 것으로 생성자의 권한을 바꿔주면 절대 안 된다.
    public static IntentData getInstance() {
        //아무도 생성하지 않았다면
        if(uniqueInstance == null) {
            //동기화 한 후에 다시 확인 (근소한 차이로 동시에 접근하면 인스턴스가 2개가 생성될 수 있으므로)
            synchronized (IntentData.class) {
                //아무도 생성하지 않았다면
                if(uniqueInstance == null) {
                    //그제서야 생성.
                    uniqueInstance = new IntentData();
                }
            }
        }

        //아무도 생성하지 않았다면, new IntentData()가 담긴 uniqueInstance를 보내줄 것이고
        //생성이 이미 된 상태라면, 이미 각 정보가 저장되어 있는 uniqueInstance를 보내주게 될 것이다.
        return uniqueInstance;
    }

    //쓰고싶은 메소드 여기에


    //나머지는 getter/setter이기 때문에 별 문제 없습니다.
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getAddress() {
        return address;
    }
}
