package Client;

import java.io.PrintWriter;

public class Main {
    private static PrintWriter writer;
    public static void main(String[] args) {
        //EnterGui 실행 - 5명 동시 실행 가능
        new EnterGui();
        new EnterGui();
        new EnterGui();
        new EnterGui();
        new EnterGui();
    }
}
