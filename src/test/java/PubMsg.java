
import io.netty.util.concurrent.DefaultThreadFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 发送数据到mqtt服务器
 * @author：涂有
 * @date 2017年8月16日 下午11:15:22
 */
public class PubMsg {
    private static int qos = 2; //只有一次
    private static String broker = "tcp://39.104.52.84:8077";
    private static String userName = "admin";
    private static String passWord = "admin";


    private static MqttClient connect(String clientId,String userName,
                                      String password) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(password.toCharArray());
        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(20);
//		String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
//		connOpts.setServerURIs(uris);  //起到负载均衡和高可用的作用
        MqttClient mqttClient = new MqttClient(broker, clientId, persistence);

        mqttClient.setCallback(new PushCallback("test"));
        mqttClient.connect(connOpts);

        if(mqttClient.isConnected()){
            mqttClient.subscribe("counter",new IMqttMessageListener(){

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    byte[] datas = message.getPayload();
                    System.out.println("topic = [" + topic + "], counter = [" + new String(datas) + "]");
                }
            });
        }
        return mqttClient;
    }

    private static void pub(MqttClient sampleClient, String msg,String topic)
            throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage("测试发送消息".getBytes());
        message.setQos(qos);
        message.setRetained(false);
        sampleClient.publish(topic, message);
    }

    private static void publish(String str,String clientId,String topic) throws MqttException{
        MqttClient mqttClient = connect(clientId,userName,passWord);

        if (mqttClient != null) {
            pub(mqttClient, str, topic);
            System.out.println("pub-->" + str);
        }

    }

    public static void main(String[] args) throws MqttException ,Exception{
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("Test"));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    publish("hello,world", "client-id-0", "test");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },1000,1000, TimeUnit.MILLISECONDS);

    }
}

class PushCallback implements MqttCallback {
    private String threadId;
    public PushCallback(String threadId){
        this.threadId = threadId;
    }

    public void connectionLost(Throwable cause) {

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
//       System.out.println("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        System.out.println(threadId + " " + msg);
    }
}

