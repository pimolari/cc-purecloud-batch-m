package com.cc.purecloud.launcher;

import com.cc.purecloud.util.Config;
import com.cipher.CryptoUtil;

public class CryptoLauncher {

  
  public static void main(String args[]) {
    
    if (args.length != 2)
      System.err.println("Usage: CryptoLauncher [value] [key]" );
    
    try {
      String result = CryptoUtil.encrypt(args[1], args[0]);
      
      System.out.println("Encrypting [" + args[0] + "]");
      System.out.println("Using key [" + args[1] + "]");
      System.out.println("Result [" + result + "]");
      
    } catch(Exception e) {
      System.err.println(e.getMessage());
    }
    
    
  }
}
