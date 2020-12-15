package test;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Map<String, Object> map = new ConcurrentHashMap<>();

        //10 接口, 每个接口 5个方法

        for (int i = 0; i < 1000; i++) {
            map.put("com.bruce.fastrpc.spring.server.InitRpcBeanMediator.method" + 1, "哈哈哈" + 1);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            int i1 = ThreadLocalRandom.current().nextInt(50);
            Object o = map.get("com.bruce.fastrpc.spring.server.InitRpcBeanMediator.method" + i1);
        }
        System.out.println(System.currentTimeMillis() - start);

    }
}
