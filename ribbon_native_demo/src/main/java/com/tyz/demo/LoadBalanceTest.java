package com.tyz.demo;

import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import org.junit.Test;
import rx.Observable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by shinyruo on 2020/7/13 19:11.
 */
public class LoadBalanceTest {

    @Test
    public void run() {
        List<Server> serverList = Arrays.asList(new Server("localhost", 8081), new Server("localhost", 8083));

        ILoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);

        IntStream.rangeClosed(0, 5).forEach(i -> {

            String result = LoadBalancerCommand.<String>builder().withLoadBalancer(loadBalancer).build()
                    .submit(new ServerOperation<String>() {

                        public Observable<String> call(Server server) {
                            try {
                                String addr = "http://" + server.getHost() + ":" + server.getPort() + "user/hello";
                                System.out.println("调用地址为：" + addr);

                                URL url = new URL(addr);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("get");
                                connection.connect();
                                InputStream inputStream = connection.getInputStream();
                                byte[] data = new byte[inputStream.available()];
                                inputStream.read(data);

                                return Observable.just(new String(data));

                            } catch (Exception e) {
                                return Observable.error(e);
                            }

                        }
                    }).toBlocking().first();
            System.out.println("调用结果：" + result);
        });
    }

}
