package com.huntersharpe.BountyHunter.Economy;

import java.io.Serializable;

/**
 * Created by Hunter Sharpe on 7/20/15.
 */
public class Account implements Serializable{

    private static Account instance;

    public static Account getInstance(){
        return instance;
    }

    private static final long serialVersionUID = 4892956202831924502L;

    private String name;
    private Double balance;

    public Account(String name, double balance){
        name = this.name;
        balance = this.balance;
        Util.getInstance().getAccountMap().put(name, balance);
    }

    public double getAccount(String name){
        return balance;
    }

    public boolean hasAccount(String name){
        if(this.name != name){
            return false;
        }else{
            return true;
        }
    }
}
