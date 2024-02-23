package com.kbbukopin.biller.Arranet;

import com.kbbukopin.biller.util.ConvertUtils;
import static com.kbbukopin.biller.util.ConvertUtils.HexToDecimal;
import static com.kbbukopin.biller.util.ConvertUtils.asciiToHex;
import static com.kbbukopin.biller.util.ConvertUtils.saveVariableToFile;
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

public class ArranetRequestKey {
	private static String arranet_private_key; //arranet_private_key
        public static String mti;
        public static String bit40;
        public static String bit48;
        public static String bitmap;
        private static String dev_arranet_partner_id;
        public static String arranet_trace_number;
        private static String decrypt_session_key;
        private static String arranet_key;
        
	public static void main(String[] args) throws Exception
	{
		
		arranet_private_key = "6C2AA680508B62F055BF74F62430614DDF6647F0D38E744C";
		mti = "2800";
                bit40 = "101"; //101 for request_key, 001 for sign on
                bit48 = "0070000000";
                bitmap = "0030000081010000";

                //String dev_arranet_partner_id = System.getenv("dev_arranet_partner_id_iso");
                dev_arranet_partner_id = "4500001";
                Date mydate = new Date();
                System.out.println("mydate: " + mydate);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String localTime = sdf.format(Calendar.getInstance().getTime());
                System.out.println("localTime : " + localTime);

                SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS");
                arranet_trace_number = sdf2.format(Calendar.getInstance().getTime());
                System.out.println("arranet_trace_number: " + arranet_trace_number);

                String sendStr = mti + bitmap + arranet_trace_number + localTime + "07" + dev_arranet_partner_id + bit40 + bit48;
                //String sendStr = "28000010000081010000202311141022340745000010010070000000";
                System.out.println("request: " + sendStr);

		int MsgLenth =	sendStr.length();
		
		String hexheader = Integer.toHexString(MsgLenth);
		String MsgHeader = ConvertUtils.hexToAscii(hexheader);
		
		byte byte1 = (byte)0x00;
		byte byte2 = (byte)0xd0;
		
		char Test= (char)Integer.parseInt("d0", 16);
		System.out.println("MsgLenth : " + MsgLenth);
		System.out.println("hexheader : " + hexheader);
		System.out.println("MsgHeader : " + MsgHeader);
		
		
		System.out.println("asciiToHex : " + asciiToHex(MsgHeader));
		
		String WriteText = (char) byte1 + MsgHeader + sendStr;
		
		System.out.println("asciiToHex WriteText : " + asciiToHex(WriteText));
		
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
                
		String ServerIP ="13.67.36.65";
		int ServerPort =10038;
		

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
		System.out.println("## START RECEIVE HEAD HEX : " +  asciiToHex(new String(HeadBuffer)));
		
		String HeadHEX = asciiToHex(new String(HeadBuffer));
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
                
                ///String bit48 = substr(response, 63,114); 
                //String bit48 = line.substring(63,115); 
                //String str_arranet_session_key = bit48.substring (4,52); //arranet_session_key
                int lineLenth =	myResponse.length();
                System.out.println("MsgLenth: " + lineLenth);
                String arranetSessionKey = myResponse.substring(65, 113);
                
                System.out.println("arranetSessionKey:" + arranetSessionKey);
                //decrypt_session_key

                String decryptedData = EncryptionUtils.decryptData(arranetSessionKey, arranet_private_key);
                
                arranet_key = EncryptionUtils.encryptData(decryptedData, arranet_trace_number);
                saveVariableToFile(arranet_key);
                // Encrypt arranet_key
               
             
		
		
		in.close();
		out.close();
		socket.close();
		
		
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

    
}
