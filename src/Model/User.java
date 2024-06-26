package Model;

/*
접속한 클라이언트의 정보를 담기 위한 클래스
 */
public class User {
    private String threadName; //스레드 이름 저장
    private String id; //클라이언트 id 저장

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
