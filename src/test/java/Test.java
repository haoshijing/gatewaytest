import java.io.DataOutputStream;
import java.net.Socket;

/**
 * @author haoshijing
 * @version 2018年03月20日 17:12
 **/
public class Test {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",8077);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.write(22);
    }
}
