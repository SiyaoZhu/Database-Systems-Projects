import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class NodeOutputFormat extends FileOutputFormat<IntWritable, Node> {

    public RecordWriter<IntWritable, Node> getRecordWriter(TaskAttemptContext ctxt) throws IOException, InterruptedException {
        
        Path file = FileOutputFormat.getOutputPath(ctxt);
        //Path file = FileOutputFormat.getOutputPath(new JobConf(ctxt.getConfiguration()));//, ctxt.getJobID()));
        //Path file = FileOutputFormat.getOutputPath(ctxt.getJobID());//, ctxt.getJobID()));
        file = new Path(file.toString() + "/output.txt");
        FileSystem fs = FileSystem.get(ctxt.getConfiguration());
        FSDataOutputStream fileOut = fs.create(file);
        return new NodeRecordWriter(fileOut);
    }

}
