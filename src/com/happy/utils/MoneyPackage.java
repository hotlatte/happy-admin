package com.happy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoneyPackage {
    private int remainSize;
    private int remainMoney;
    private int size;
    private int money;

    /**
     * @param size  红包个数
     * @param money 红包钱数，单位:分
     */
    public MoneyPackage(int size, int money) throws Exception {
        if (money <= size) {
            throw new Exception("钱太少，不够分的");
        }
        this.remainSize = this.size = size;
        this.remainMoney = this.money = money;
    }

    public boolean hasNext() {
        return this.remainSize > 0;
    }

    public int nextMoney(Random random) {
        if (this.remainSize <= 0) {
            return 0;
        }
        if (this.remainSize == 1) {
            this.remainSize--;
            return this.remainMoney;
        }
        int min = 1; //
        int max = this.remainMoney / this.remainSize * 2;
        int money = (int) Math.floor(max * random.nextFloat());
        money = money <= min ? min : money;
        this.remainSize--;
        this.remainMoney -= money;
        return money;
    }

    public List<Integer> getRandomMoneys() {
        List<Integer> moneys = new ArrayList<>(size);
        Random r = new Random(System.currentTimeMillis());
        while (remainSize > 0) {
            int money = nextMoney(r);
            moneys.add(money);
        }
        return moneys;
    }


    public int getSize() {
        return size;
    }


    public int getMoney() {
        return money;
    }
}
