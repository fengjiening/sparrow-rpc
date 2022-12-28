package test;

import com.fengjiening.sparrow.server.app.TcpServerApp;

/**
 * @ClassName: test
 * @Description: TODO
 * @Date: 2022/10/25 20:45
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class test_server {
    public static void main(String[] args) throws InterruptedException {
        new TcpServerApp(1234).start();
    }
}
