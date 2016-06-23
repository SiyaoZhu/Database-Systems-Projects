import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;


public class LeftoverReducer extends Reducer<IntWritable, Node, IntWritable, Node> {
    public static double alpha = 0.85;
    public void reduce(IntWritable nid, Iterable<Node> Ns, Context context) 
    		throws IOException, InterruptedException {
        //Implement
    	Configuration conf = context.getConfiguration();
    	long leftover = Integer.parseInt(conf.get("leftover"));
    	long size = Integer.parseInt(conf.get("size"));
	System.out.println("LeftoverReducer's leftover: "+leftover + "and size: "+size);
    	double lossRank = leftover/(1000.0*size); 
    	
    	for (Node node: Ns) {
    		double pageRank = node.getPageRank();
    		pageRank = alpha/size + (1-alpha)*(lossRank + pageRank);
    		node.setPageRank(pageRank);
    		context.write(nid, node);
    	}
    	
    	context.getCounter(MyCounter.NODE_COUNT).setValue(0);
    	context.getCounter(MyCounter.LOST_COUNTER).setValue(0);
    }
}
