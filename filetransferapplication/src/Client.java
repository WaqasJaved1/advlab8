import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client {
    private DatagramSocket socket = null;
    private FileEvent event = null;
    private String sourceFilePath = "C:/Users/User/Downloads/waqas.zip";
    private String destinationPath = "C:/tmp/downloads/";
    private String hostName = "localHost";
//
    public Client() {

    }

    public void createConnection() {
        try {

            socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(hostName);
            byte[] incomingData = new byte[1024];
            event = getFileEvent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(event);

            byte[] data = outputStream.toByteArray();
            
            System.out.println(data.length);
            int totalPacket = data.length/60000;
            
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(totalPacket);
            
            
            byte[] result = b.array();

            DatagramPacket sendPacket1 = new DatagramPacket(result, result.length, IPAddress, 9876);

            int StartPoint = 0;
            int EndPoint = 0;
            for(int i=0; i< totalPacket; i++){
                if((i-1) != totalPacket){
                    EndPoint = i *60000;
                }else{
                    EndPoint = data.length;
                }
                
                byte[] arr2 = Arrays.copyOfRange(data, StartPoint,EndPoint);
                DatagramPacket sendPacket = new DatagramPacket(arr2, arr2.length, IPAddress, 9876);
                socket.send(sendPacket);

                StartPoint = EndPoint+1;
            }
            System.out.println("File sent from client");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);
            Thread.sleep(2000);
            System.exit(0);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public FileEvent getFileEvent() {
        FileEvent fileEvent = new FileEvent();
        String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
        String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
        fileEvent.setDestinationDirectory(destinationPath);
        fileEvent.setFilename(fileName);
        fileEvent.setSourceDirectory(sourceFilePath);
        File file = new File(sourceFilePath);
        if (file.isFile()) {
            try {
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setFileSize(len);
                fileEvent.setFileData(fileBytes);
                fileEvent.setStatus("Success");
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("path specified is not pointing to a file");
            fileEvent.setStatus("Error");
        }
        return fileEvent;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.createConnection();
    }
}