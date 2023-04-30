package sxv210118;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GraphGenerator {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int NODES = Integer.parseInt(args[0]);
        int EDGES = Integer.parseInt(args[1]);
        Random rand = new Random();
        PrintWriter out = new PrintWriter(new FileWriter("SP9/ip/"+args[2]));
        out.println(NODES + " " + EDGES);
	Set<String> set = new HashSet<>();
        int count = 0;
        // add all edges that connect each vertex to its next vertex in a circular fashion
        /*
	for (int i = 1; i <= NODES; i++) {
            int j = (i % NODES) + 1;

            out.println(i + " " + j + " " + rand.nextInt(100) + 1);
            count+=1;
        }
	*/

        while(count!=EDGES) {
            int u = rand.nextInt(NODES) + 1;
            int v = rand.nextInt(NODES) + 1;
            if(u==v || set.contains(u + " " + v) || set.contains(v + " " + u)) {
                continue;
            }
            out.println(u + " " + v + " " + rand.nextInt(100) + 1);
            count+=1;
	    set.add(u + " " + v);
        }

        out.flush();
        out.close();

    }
}
