package com.sorahjy.kotlintest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    static class Node {
        HashMap<Integer, Integer> map = new HashMap<>();

        public void add(int key, int val) {
            map.put(key, val);
        }
    }

    static int n;
    static Node node[];

    static class com implements Comparator<Temp> {

        public int compare(Temp a, Temp b) {
            // TODO Auto-generated method stub
            if (a.min < b.min)
                return -1;
            if (a.min > b.min)
                return 1;
            return 0;
        }
    }

    static class Temp {
        int min, node;

        Temp(int a, int b) {
            min = a;
            node = b;
        }
    }

    static int[] ans;

    public  void f(){
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int m = sc.nextInt();
        node = new Node[n];
        ans = new int[n];
        for (int i = 0; i < n; i++) {
            node[i] = new Node();
        }
        int a, b;
        for (int i = 0; i < m; i++) {
            a = sc.nextInt();
            b = sc.nextInt();
            node[a].add(b, sc.nextInt());
        }
        PriorityQueue<Temp> queue = new PriorityQueue<>(new com());
        //准备

        int s = 0;//初始的点
        queue.add(new Temp(0, s));
        Arrays.fill(ans, 999999999);
        ans[s] = 0;

        //
        while (!queue.isEmpty()) {
            Temp t = queue.poll();
            int v = t.node;
            if (ans[v] < t.min)
                continue;
            for (Entry<Integer, Integer> entry : node[v].map.entrySet()) {
                int to = entry.getKey();
                int cost = entry.getValue();

                if (ans[to] > ans[v] + cost) {
                    ans[to] = ans[v] + cost;
                    queue.add(new Temp(ans[to], to));
                }

            }
        }

        for (int i = 0; i < n; i++)
            if (i != s)
                System.out.println(ans[i]);

    }//main


    public void f2()

    {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int m = sc.nextInt();
        node = new Node[n];
        ans = new int[n];
        for (int i = 0; i < n; i++) {
            node[i] = new Node();
        }
        int a, b;
        for (int i = 0; i < m; i++) {
            a = sc.nextInt();
            b = sc.nextInt();
            node[a].add(b, sc.nextInt());
        }
        PriorityQueue<Temp> queue = new PriorityQueue<>(new com());
        //准备

        int s = 0;//初始的点
        queue.add(new Temp(0, s));
        Arrays.fill(ans, 999999999);
        ans[s] = 0;

        //
        while (!queue.isEmpty()) {
            Temp t = queue.poll();
            int v = t.node;
            if (ans[v] < t.min)
                continue;
            for (Entry<Integer, Integer> entry : node[v].map.entrySet()) {
                int to = entry.getKey();
                int cost = entry.getValue();

                if (ans[to] > ans[v] + cost) {
                    ans[to] = ans[v] + cost;
                    queue.add(new Temp(ans[to], to));
                }

            }
        }

        for (int i = 0; i < n; i++)
            if (i != s)
                System.out.println(ans[i]);

    }//main


}
/*
Test Date:
5 7
0 1 3
0 3 8
1 2 5
1 4 4
2 3 4
2 4 7
3 4 2
Out put:
0->1:3
0->2:8
0->3:8
0->4:7
*/
