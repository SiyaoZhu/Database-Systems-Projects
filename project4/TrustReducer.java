import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

public class TrustReducer extends Reducer<IntWritable, NodeOrDouble, IntWritable, Node> {
    public void reduce(IntWritable key, Iterable<NodeOrDouble> values, Context context)
        throws IOException, InterruptedException {
        //Implement
    	Node node = null;
    	double pageRank = 0.;
    	for(NodeOrDouble n : values) {
    		if(n.isNode()) {
    			node = n.getNode();
    		}
    		else {
    			pageRank += n.getDouble();
    		}
    	}
    	node.setPageRank(pageRank);
    	context.write(key, node);
    }
}
