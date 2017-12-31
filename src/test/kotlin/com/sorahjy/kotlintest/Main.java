package com.sorahjy.kotlintest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    static int n;
    static final int max = 99999999;
    static int m;
    static boolean take[];

    static class Edge {
        int to, from, len;
    }

    static int ans[];
    static Edge[] edge;

    public static void f() throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        edge = new Edge[m];
        ans = new int[n + 1];
        LinkedList<Integer> list = new LinkedList<>();
        take = new boolean[n + 1];
        for (int i = 0; i < m; i++) {
            edge[i] = new Edge();
            in.nextToken();
            edge[i].from = (int) in.nval;
            in.nextToken();
            edge[i].to = (int) in.nval;
            in.nextToken();
            edge[i].len = (int) in.nval;

        }

        Arrays.fill(ans, max);
        list.add(1);
        ans[1] = 0;
        take[1] = true;
        while (list.size() > 0) {
            int v = list.removeFirst();
            for (int i = 0; i < edge.length; i++) {
                if (edge[i].from == v && ans[edge[i].to] > ans[edge[i].from] + edge[i].len) {
                    ans[edge[i].to] = ans[edge[i].from] + edge[i].len;
                    list.addLast(edge[i].to);
                    if (edge[i].to > n) {
                        //这里表示这个图里有负环路
                        return;
                    }
                    take[edge[i].to] = true;
                }
            }
            take[v] = false;
        }
        for (int i = 2; i <= n; i++) {
            out.println(ans[i]);
        }
        out.flush();
    }//main

}
/*
10
1 2 1
1 3 4
1 6 6
2 4 8
2 5 3
3 6 5
3 5 9
4 5 7
4 6 10
5 6 2
*/


//17


