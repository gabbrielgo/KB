package com.kbbukopin.biller.Arranet;

//import static com.kbbukopin.biller.ArranetRequestKey.arranet_trace_number;
import static com.kbbukopin.biller.Arranet.ArranetSignOn.asciiToHex;
import com.kbbukopin.biller.util.Constants;
import static com.kbbukopin.biller.util.Constants.ARRANET_PRIVATE_KEY;
import com.kbbukopin.biller.util.ConvertUtils;
import static com.kbbukopin.biller.util.ConvertUtils.HexToDecimal;
import static com.kbbukopin.biller.util.ConvertUtils.asciiToHex;
import static com.kbbukopin.biller.util.ConvertUtils.hexToAscii;
import static com.kbbukopin.biller.util.ConvertUtils.readVariableFromFile;
import com.kbbukopin.biller.util.EncryptionUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ArranetInquiry { //arranet_private_key
        public static String mti;
        public static String bit2;
        public static String bit11;
        public static String bit12;
        public static String bit26;
        public static String bit32;
        public static String bit33;
        //public static String bit40;
        public static String bit41;
        public static String bit48;
        public static String bitmap;
        private static String dev_arranet_partner_id;
        public static String arranet_trace_number;
        private static String decrypt_session_key;
        private static String arranet_key;
        
        
	public static void main(String[] args) throws Exception
	{
               
//                byte[] arranet_key = ArranetRequestKey.getArranetKey();
		String filePath = "arranetKey.txt"; 
                mti= "2100";
                //key =  "{{arranet_key}}" ; //bit53
                bitmap = "40300041808100000000000000000000";
                bit2 =  "99501"; //LLVAR "99501";
                bit11 =  "";//arranet_trace_number
                bit12 =  "";//localTime
                bit26 =  "6021";
                bit32 =  "4410010"; //LLVAR "4410010"; 074410010
                bit33 =  "";//dev_arranet_partner_id
                bit41 =  "1391013909607231";
                bit48 =  "441CA01530000000001"; 
                Object arranetRequestKey = readVariableFromFile(filePath);
                System.out.println("readValue : " + arranetRequestKey);

                //String dev_arranet_partner_id = System.getenv("dev_arranet_partner_id_iso");
                dev_arranet_partner_id = "4500001";
                Date mydate = new Date();
                System.out.println("mydate: " + mydate);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String localTime = sdf.format(Calendar.getInstance().getTime());
                bit12 = localTime;
                System.out.println("localTime : " + localTime);

                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS");
                arranet_trace_number = sdf2.format(Calendar.getInstance().getTime());
                bit11 = arranet_trace_number;
                System.out.println("arranet_trace_number: " + arranet_trace_number);

                String sendStr = mti + bitmap + bit2 + bit11 + bit12 + bit26 + bit32 +  dev_arranet_partner_id + bit41 + bit48 + arranetRequestKey ;
                //String sendStr = "2800001000008100000020231114102234074500001101";

                System.out.println("request: " + sendStr);

		int MsgLenth =	sendStr.length();
		
		String hexheader = Integer.toHexString(MsgLenth);
		String MsgHeader = hexToAscii(hexheader);
		
		byte byte1 = (byte)0x00;
		byte byte2 = (byte)0xd0;
		
		char Test= (char)Integer.parseInt("d0", 16);
		System.out.println("MsgLenth : " + MsgLenth);
		System.out.println("hexheader : " + hexheader);
		System.out.println("MsgHeader : " + MsgHeader);
		
		
		System.out.println("asciiToHex : " + ConvertUtils.asciiToHex(MsgHeader));
		
		String WriteText = (char) byte1 + MsgHeader + sendStr;
		
		System.out.println("asciiToHex WriteText : " + ConvertUtils.asciiToHex(WriteText));
		
		try 
		{
			createfile(WriteText);
			SendServer(WriteText);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void SendServer(String SendMsg) throws UnknownHostException, IOException, Exception {
		
		Socket socket = null;
//		String ServerIP ="13.67.36.65";
//		int ServerPort =10038;
		
                
                String ServerIP ="10.30.210.37";
		int ServerPort =45101;

		BufferedReader in = null;
		PrintStream out = null;
		
		
		socket = new Socket(ServerIP, ServerPort);
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintStream(socket.getOutputStream());
		
		
		out.println(SendMsg);
		out.flush();
		
		System.out.println("## START Sever Info  : " + ServerIP + ", " + ServerPort);
		System.out.println("## START SEND Message : (" + SendMsg + ")");
		
		System.out.println("## START RECEIVE Message");
		
		
		StringBuffer buffer = new StringBuffer();
		
		char[] HeadBuffer	=	new char[2];
		
		int HeadBufferCnt = in.read(HeadBuffer, 0, 2);
		System.out.println("## START RECEIVE HEAD CNT : " +  HeadBufferCnt);
		System.out.println("## START RECEIVE HEAD  : " +  new String(HeadBuffer));
		System.out.println("## START RECEIVE HEAD HEX : " +  ConvertUtils.asciiToHex(new String(HeadBuffer)));
		
		String HeadHEX = ConvertUtils.asciiToHex(new String(HeadBuffer));
		if ( HeadHEX.length() > 3 )
		{
			HeadHEX	=	HeadHEX.substring(0,3);
		}
		System.out.println("## START RECEIVE MESSAGE LENTH (HEAD Value) : " +  HexToDecimal(HeadHEX));
		int	ReceiveMessageLenth = HexToDecimal(HeadHEX);
		
		
		char[] cbuf	=	new char[ReceiveMessageLenth +  2];
		
		int readcnt = in.read(cbuf, 2, ReceiveMessageLenth);
		System.out.println("## START RECEIVE Message CNT : " +  readcnt);

		/*
		int checklen = 0;
		while(true) 
		{
			int ch = in.read();
			if((ch<0) || (ch == '\n')) {
				break;
			}
			buffer.append((char) ch);
		}
		String line = buffer.toString();
		*/
		
		String line = new String (cbuf);
                String myResponse = new String (cbuf, 2, ReceiveMessageLenth);
		System.out.println(line);
                
                int lineLenth =	myResponse.length();
                System.out.println("MsgLenth: " + lineLenth);
                String arranetSessionKey = myResponse.substring(65, 113);
                System.out.println("arranetSessionKey:" + arranetSessionKey);
                
                      // Get current timestamp in milliseconds
                long mydate = System.currentTimeMillis();
                System.out.println("mydate: " + mydate);

                // Format current local time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String localTime = now.format(formatter);
                System.out.println("localTime: " + localTime);

                // Format current time for arranet_trace_number
                DateTimeFormatter traceNumberFormatter = DateTimeFormatter.ofPattern("HHmmssSSSSSS");
                String arranetTraceNumber = now.format(traceNumberFormatter);

               
                String decryptedData = EncryptionUtils.decryptData(arranetSessionKey, ARRANET_PRIVATE_KEY);
                arranet_key = EncryptionUtils.encryptData(decryptedData, arranetTraceNumber);
                
		in.close();
		out.close();
		socket.close();
                
//               byte[] arranet_session_key = ConvertUtils.ConvertToBit48(line);
//               String trace_number = arranet_trace_number + "FFFF";		
		
	}
        
        public static String getArranetKey(){
            return arranet_key;
        }

	private static void createfile(String TextMsg) throws InterruptedException
	{
		 try {
			 
	            File file = new File("c:\\Temp\\writeFile.txt");
	 
	            if (!file.exists()) {
	                file.createNewFile();
	            }else
	            {
	            	file.delete();
	            	
	            	Thread.sleep(500);
	            	
	            	file.createNewFile();
	            }
	 

	            FileWriter fw = new FileWriter(file);
	            BufferedWriter writer = new BufferedWriter(fw);
	 

	            writer.write(TextMsg);
	 
	            // 5. BufferedWriter close
	            writer.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
        /////
        
       


}
