import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

public class TrustMapper extends Mapper<IntWritable, Node, IntWritable, NodeOrDouble> {
    public void map(IntWritable key, Node value, Context context) 
    		throws IOException, InterruptedException {

        //Implement 
    	double pageRank = value.getPageRank();
    	int outSize = value.outgoingSize();
    	double pass = 0.;
    	try{
    		pass = pageRank/outSize;
    	} catch(ArithmeticException e){
    		System.out.println("No outgoing nodes");
    	}
    	context.write(key, new NodeOrDouble(value));
    	Iterator<Integer>  i = value.iterator();
    	while (i.hasNext()) {
    		Integer integer = i.next();
    		context.write(new IntWritable(integer), new NodeOrDouble(pass));
    	}

    	context.getCounter(MyCounter.NODE_COUNT).increment(1);
    	
    	if(outSize == 0) {
    		long intRank = (long)(pageRank*1000);
		System.out.println(value.nodeid+"'s"+"Loss rank is: "+intRank);
    		context.getCounter(MyCounter.LOST_COUNTER).setValue(intRank);
    	}
    }
}
