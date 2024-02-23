package com.kbbukopin.biller.Silverlake;

//import static com.kbbukopin.biller.ArranetRequestKey.arranet_trace_number;
import com.kbbukopin.biller.Arranet.*;
import com.kbbukopin.biller.util.Constants;
import static com.kbbukopin.biller.util.Constants.ARRANET_PRIVATE_KEY;
import com.kbbukopin.biller.util.ConvertUtils;
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

public class MIPSPSignOn { //arranet_private_key
        public static String mti;

        public static String bitmap;
        private static String dev_arranet_partner_id;
        public static String arranet_trace_number;
        private static String decrypt_session_key;
        private static String arranet_key;
        
        
	public static void main(String[] args) throws Exception
	{
               
//                byte[] arranet_key = ArranetRequestKey.getArranetKey();
                String filePath = "arranetKey.txt";
                mti= "0800";
                //key =  "{{arranet_key}}" ; //bit53
                bitmap = "B2204000880180040000000010000000";
                String bit7 = "0110094448";
                String bit11 = "";//arranetKey, terisi setelah trigger arranet_trace_number
//                String bit32 = "4410010";//LLVAR "4410010";
                String bit33 = "4500001";//dev_arranet_partner_id = "4500001"
                String bit37 = "011000012360";
//                String bit40o = "";
//                String bit41 = "1391001390960721";
//                String bit42 = "";
//                String bit43 = "";
                String bit48= "0000000004202013404";
                String bit49 = "360";
//                String bit52o = "";
//                String bit55c = "";
//                String bit60 = "";
//                String bit61 = "";
                String bit62 = "00000000";
//                String bit63o = "";
//                String bit98 = "";
                String bit100 = "441        ";
                String bit102o = "";
                String bit103o = "";
                Object readValue = readVariableFromFile(filePath);
                

                Date mydate = new Date();
                System.out.println("mydate: " + mydate);
                
                SimpleDateFormat sdf =  new SimpleDateFormat("HHmmssSSSSSS");
                arranet_trace_number = sdf.format(Calendar.getInstance().getTime());
                bit11 = arranet_trace_number; //bit11

                SimpleDateFormat sdf2= new SimpleDateFormat("HHmmss");
                String localTime = sdf2.format(Calendar.getInstance().getTime());
//                bit12 = localTime; //bit12
//                System.out.println("localTime : " + localTime);

                SimpleDateFormat sdf3 = new SimpleDateFormat("ddMM");
                String dateLocal = sdf3.format(Calendar.getInstance().getTime());
//                bit13 = dateLocal;
                
                
                System.out.println("arranet_trace_number: " + arranet_trace_number);

                String sendStr = mti + bitmap + bit7 + bit11 + bit33 + bit37 + bit48 + bit49 + bit62 + readValue;
                //String sendStr = "28000010000081010000202311141022340745000010010070000000";

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
		
		
		System.out.println("asciiToHex : " + asciiToHex(MsgHeader));
		
		String WriteText = sendStr;
		
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
		String ServerIP ="10.0.28.110";
		int ServerPort =8362;
		

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
	
    public static String asciiToHex(String ascii) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }
    
    
    

    public static String stringToHex(String s) {
      String result = "";

      for (int i = 0; i < s.length(); i++) {
        result += String.format("%02X ", (int) s.charAt(i));
      }

      return result;
    }



    public static String stringToHex0x(String s) {
      String result = "";

      for (int i = 0; i < s.length(); i++) {
        result += String.format("0x%02X ", (int) s.charAt(i));
      }

      return result;
    }
    
    
    /**
     * Convert a byte array to hex string
     * {0x00, 0x01, (byte)0xff}   -> "00 01 ff"
     */
    public static String ByteArrayToHexString(byte[] data) {
        final int len = 3 * data.length;
        final StringBuilder stringBuilder = new StringBuilder(len);
        for (byte b : data) {
            stringBuilder.append(Integer.toString((b & 0x000000ff) + 0x100,
                    16).substring(1));
            stringBuilder.append(" ");
        }
        stringBuilder.delete(len - 1, len);
        return stringBuilder.toString();
    }
    

	private static String hexToAscii(String hexStr) {
	    StringBuilder output = new StringBuilder("");
	    
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}
	
	private static int HexToDecimal(String hexStr) {
	    return Integer.parseInt(hexStr, 16);
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
        
        

    public static byte[] encryptData(String data, String secretKey) throws Exception {
    //public static byte[] encryptData(String data, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");

        // Create a DESede key spec from the secret key
        KeySpec keySpec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = secretKeyFactory.generateSecret(keySpec);

        // Initialize the cipher with the key and set it to encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the data
        return cipher.doFinal(padData(data).getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptData(byte[] arranet_session_key, String arranet_private_key) throws Exception {
    //public static String decryptData(String encryptedData, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");

        // Create a DESede key spec from the secret key
        KeySpec keySpec = new DESedeKeySpec(arranet_private_key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = secretKeyFactory.generateSecret(keySpec);

            // Initialize the cipher with the key and set it to decrypt mode
        
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(arranet_session_key);
        //byte[] encryptedBytes = Base64.getDecoder().decode(arranet_session_key);
        //byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Remove padding and convert the decrypted bytes to a string
        return removePadding(new String(decryptedBytes, StandardCharsets.UTF_8));
        
        // Convert the decrypted bytes to a string
        //return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static String padData(String data) {
        // Add NoPadding manually, as it is not supported by Cipher padding options
        int paddingSize = 8 - (data.length() % 8);
        for (int i = 0; i < paddingSize; i++) {
            data += '\0';
        }
        return data;
    }

    private static String removePadding(String data) {
        // Remove padding manually
        return data.replaceAll("\0", "");
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


}
