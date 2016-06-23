import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

public class LeftoverMapper extends Mapper<IntWritable, Node, IntWritable, Node> {

    public void map(IntWritable nid, Node N, Context context) 
    		throws IOException, InterruptedException {
        
        //Implement
	context.write(nid, N);
    }
}
