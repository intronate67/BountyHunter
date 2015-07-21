package com.huntersharpe.BountyHunter.Economy;

import com.huntersharpe.BountyHunter.BountyHunter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hunter Sharpe on 7/20/15.
 */
public class Util {

    private static Util instance;

    public static Util getInstance(){
        return instance;
    }

    public static Map<String, Double> accountMap = new HashMap<String, Double>();

    public static Map getAccountMap(){
        return accountMap;
    }

    public void saveAccounts(){

        try(FileOutputStream fs = new FileOutputStream("/mods/Bounty Hunter/accounts.ser")){

            ObjectOutputStream os = new ObjectOutputStream(fs);

            os.writeObject(accountMap);

            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAccounts(){

        try(FileInputStream fs = new FileInputStream("/mods/Bounty Hunter/accounts.ser")){

            ObjectInputStream os = new ObjectInputStream(fs);

            //Read from accounts.ser
            double balances = os.readDouble();

            for(int i = 0; i < balances; i++){
                try {
                    Account account = (Account)os.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
