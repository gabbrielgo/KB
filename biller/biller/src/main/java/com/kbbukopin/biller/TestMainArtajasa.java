package com.kbbukopin.biller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestMainArtajasa {
	
	public static void main(String[] args)
	{
		
		String sendStr = "0800822000000000000004000000000000000213060925001453001";
		
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
	
	public static void SendServer(String SendMsg) throws UnknownHostException, IOException {
		
		Socket socket = null;
		String ServerIP ="127.0.0.1";
		int ServerPort =8080;
		

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
		System.out.println(line);
		
		
		in.close();
		out.close();
		socket.close();
		
		
		
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
}
