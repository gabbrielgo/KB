package com.kbbukopin.biller;

import java.math.BigInteger;
import java.util.Scanner;

public class BitmapConverter {
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of data elements:");
        int count = scanner.nextInt();

        int[] data = new int[count];
        System.out.println("Enter the data elements (separated by spaces):");
        for (int i = 0; i < count; i++) {
            data[i] = scanner.nextInt();
        }

        // Convert data elements to bitmap
        String bitmap = dataElementsToBitmap(data);

        // Output concatenated bitmap
        System.out.println("Concatenated Bitmap: " + bitmap);

        // Convert binary bitmap to hexadecimal
        String hexRepresentation = binaryToHex(bitmap);
        System.out.println("Hexadecimal Representation: " + hexRepresentation);

        scanner.close();
    }
    
    public static String dataElementsToBitmap(int[] dataElements) {
        StringBuilder primaryBitmap = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000");
        StringBuilder secondaryBitmap = null;
        StringBuilder tertiaryBitmap = null;

        for (int elem : dataElements) {
            if (elem <= 64) {
                primaryBitmap.setCharAt(elem - 1, '1');
            } else if (elem > 64 && elem <= 128) {
                if (secondaryBitmap == null) {
                    secondaryBitmap = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000");
                }
                secondaryBitmap.setCharAt(elem - 65, '1');
            } else if (elem > 128 && elem <= 192) {
                if (tertiaryBitmap == null) {
                    tertiaryBitmap = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000");
                }
                tertiaryBitmap.setCharAt(elem - 129, '1');
            } else {
                System.out.println("Element number exceeds 192: " + elem);
            }
        }

        StringBuilder resultBitmap = new StringBuilder(primaryBitmap);
        if (secondaryBitmap != null) {
            resultBitmap.append(secondaryBitmap);
        }
        if (tertiaryBitmap != null) {
            resultBitmap.append(tertiaryBitmap);
        }

        return resultBitmap.toString();
    }
    
    public static String binaryToHex(String binaryString) {
        // Convert binary string to hexadecimal
        BigInteger decimal = new BigInteger(binaryString, 2);
        return decimal.toString(16).toUpperCase();
    }
}
