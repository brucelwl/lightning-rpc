package test;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class FutureTest2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        Map<String, Map<String, Object>> map = new ConcurrentHashMap<>();

        //10 接口, 每个接口 5个方法

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> methods = new HashMap<>();
            for (int j = 0; j < 50; j++) {
                methods.put("method" + j, "哈哈哈" + 1);
            }
            map.put("com.bruce.fastrpc.spring.server.InitRpcBeanMediator" + i, methods);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            int i1 = ThreadLocalRandom.current().nextInt(10);
            Map<String, Object> stringObjectMap = map.get("com.bruce.fastrpc.spring.server.InitRpcBeanMediator" + i1);
            Object o = stringObjectMap.get("method" + i1);
        }
        System.out.println(System.currentTimeMillis() - start);

    }
}
