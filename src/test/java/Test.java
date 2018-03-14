import com.sun.beans.editors.ByteEditor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author haoshijing
 * @version 2018年03月13日 11:41
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("39.104.52.84",8077);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String msg = "hello,world,12354656";
        int length = 4+ msg.getBytes().length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.putInt(msg.getBytes().length);
        byteBuffer.put(msg.getBytes());

        dataOutputStream.write(byteBuffer.array());

        int recvLen = dataInputStream.readInt();
        byte[]recvBuffer = new byte[recvLen-4];
        dataInputStream.read(recvBuffer);
        System.out.println("recvBuffer = [" + new String(recvBuffer) + "]");
        socket.close();
    }
}
