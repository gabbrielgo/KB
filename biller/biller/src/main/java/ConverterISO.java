
import static com.kbbukopin.biller.Arranet.ArranetInquiry.arranet_trace_number;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit11;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit12;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit2;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit26;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit32;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit33;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit41;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bit48;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.bitmap;
import static com.kbbukopin.biller.Arranet.ArranetInquiry.mti;
import static com.kbbukopin.biller.util.ConvertUtils.readVariableFromFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Fahmi Work
 */
public class ConverterISO {
    
     public static String mti;
    public static String bit2;
    public static String bit3;
    public static String bit4;
    public static String bit7;
    public static String bit11;
    public static String bit12;
    public static String bit13;
    public static String bit14;
    public static String bit18;
    public static String bit22;
    public static String bit23;
    public static String bit25;
    //public static String bit26;
    //public static String bit32;
    //public static String bit33;
    public static String bit37;
    public static String bit40;
    public static String bit41;
    public static String bit42;
    public static String bit43;
    public static String bit47;
    public static String bit48;
    public static String bit56;
    public static String bit61;
    public static String bit62;
    public static String bitmap;
    private static String dev_arranet_partner_id;
    public static String arranet_trace_number;
    private static String decrypt_session_key;
    private static String arranet_key;

    public static void main(String[] args) throws Exception{
        mti= "2800";
        //key =  "{{arranet_key}}" ; //bit53
        bitmap = "A218060008E300000000000000000000";
        //bit2 =  "99504"; //LLVAR "99501";
        bit3 = "581001";
        //bit4 = "3600000000052750";
        bit7 = "0613165708";
        //bit11 =  "";//arranet_trace_number
        bit12 =  "";//localTime
        bit13 = "0613";
        //bit14 = "";
        //bit18 = "";
        bit22 = "021";
        bit23 = "";
       // bit25 = "";
        //bit26 =  "6021";
        //bit32 =  "4410010"; //LLVAR "4410010"; 074410010
        ///bit33 =  "";//dev_arranet_partner_id
        bit37 = "223061300017";
        //bit40 = "001";
        bit41 =  "WOKEE";
        bit42 = "";
        bit43 = "";
        bit47 = "";
        bit48 =  "5526950000006007"; 
        //bit56 = "2200110218720000202401051110414410010";//    \/ continuedown
        //bit61  = "02200000000050000000";
        //bit62 = "012000000000000";
        
//        Object arranetRequestKey = readVariableFromFile(filePath);
        //System.out.println("readValue : " + arranetRequestKey);

        //String dev_arranet_partner_id = System.getenv("dev_arranet_partner_id_iso");
        dev_arranet_partner_id = "4500001";
        bit33 = dev_arranet_partner_id;
        Date mydate = new Date();
        System.out.println("mydate: " + mydate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String localTime = sdf.format(Calendar.getInstance().getTime());
        bit12 = localTime;
        System.out.println("localTime : " + localTime);

        SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmssSSSSSS");
        arranet_trace_number = sdf2.format(Calendar.getInstance().getTime());
        bit11 = arranet_trace_number;
        bit56 = bit56 + bit11+ bit12 + "4410010"; 
        System.out.println("arranet_trace_number: " + arranet_trace_number);

        String sendStr = mti + bitmap + bit3 + bit7 + bit12 + bit13 + bit22 + bit23 + bit37 + bit41 + bit42 + bit43 + bit47 + bit48 ;
        System.out.println("Hasil TErakhir : "+sendStr);
    }
}
