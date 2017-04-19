package Server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Eddie on 2017/4/18.
 */
public class AnalyzerTest {
    Analyzer analyzer;
    @Before
    public void setUp() throws Exception {
        analyzer = new Analyzer();
        analyzer.addFileAppToTraining("./data/pagerank-huge");
        analyzer.addFileAppToTraining("./data/kmeans-huge");
    }

//    @Test
//    public void addFileAppToTraining() throws Exception {
//
//    }
//
//    @Test
//    public void addAllMemoryAppToTraining() throws Exception {
//
//    }

    @Test
    public void doTrainingTest() throws Exception {
        System.out.print("training test\n");
        analyzer.doTraining();
    }

    @Test
    public void classifyTest() throws Exception {
        System.out.print("classify test\n");
        analyzer.classify();
    }

}