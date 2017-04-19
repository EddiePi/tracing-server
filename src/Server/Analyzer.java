package Server;

import JsonUtils.AppConstructor;
import ML.GMMAlgorithm;
import ML.GMMParameter;
import Utils.ObjPersistant;
import info.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2017/4/14.
 */
//TODO this class now is only in test package. we need to move it so that it can be reached from main.
public class Analyzer {
    Tracer tracer;
    TracerConf conf;
    ArrayList<ArrayList<Double>> trainingSet;
    String parameterPath;


    public Analyzer() {
        trainingSet = new ArrayList<>();
        this.conf = TracerConf.getInstance();
        parameterPath = conf.getStringOrDefault("tracer.ML.parameter.path", "./parameter");
    }

    public Analyzer(Tracer tracer) {
        this();
        this.tracer = tracer;

    }

    public void doTraining() {
        double[][] initMiu = new double[4][4];
        initMiu[0] = new double[]{1.0, 0.0, 0.0, 0.0};
        initMiu[1] = new double[]{0, 1, 0, 0};
        initMiu[2] = new double[]{0, 0, 1, 0};
        initMiu[3] = new double[]{0, 0, 0, 1};
        GMMAlgorithm trainee = new GMMAlgorithm(trainingSet, initMiu);
        trainee.cluster();
        GMMParameter parameter = trainee.getParameter();
        ObjPersistant.saveObject(parameter, parameterPath);
        printParameter(parameter);
    }

    public void classify() {
        GMMParameter parameter = (GMMParameter)ObjPersistant.readObject(parameterPath);
        GMMAlgorithm classifier = new GMMAlgorithm(trainingSet, parameter);
        classifier.cluster();
        parameter = classifier.getParameter();
        printParameter(parameter);
    }


    public void addFileAppToTraining(String path) {
        App app = AppConstructor.getApp(path);
        trainingSet.addAll(formatApp(app));
    }

    public void addAllMemoryAppToTraining() {
        for(App app: tracer.applications.values()) {
            trainingSet.addAll(formatApp(app));
        }
    }

    private ArrayList<ArrayList<Double>> formatApp(App app) {
        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();
        for(Job job: app.getAllJobs()) {
            for(Stage stage: job.getAllStage()) {
                for(List<ContainerMetrics> metricList: stage.containerMetricsMap.values()) {
                    for(ContainerMetrics metrics: metricList) {
                        ArrayList<Double> data = formatMetrics(metrics);
                        dataSet.add(data);
                    }
                }
            }
        }
        return dataSet;
    }

    private ArrayList<Double> formatMetrics(Metrics metrics) {
        ArrayList<Double> data = new ArrayList<>();
        data.add(metrics.cpuUsage);
        data.add((double)(metrics.storeMemoryUsage + metrics.execMemoryUsage));
        //data.add(metrics.diskReadRate);
        //data.add(metrics.diskWriteRate);
        data.add(metrics.netRecRate);
        data.add(metrics.netTransRate);
        return data;
    }

    public static void printParameter(GMMParameter parameter) {
        int category = parameter.getpPi().length;
        int dime = parameter.getpMiu()[0].length;
        for(int i = 0; i < category; i++) {
            double[] miu = parameter.getpMiu()[i];
            System.out.print(String.format("pi: %f\n", parameter.getpPi()[i]));
            System.out.print(String.format("miu: %.4f, %.4f, %.4f, %.4f\n", miu[0], miu[1], miu[2], miu[3]));
            System.out.print("sigma: \n");
            double[][] sigma = parameter.getpSigma()[i];
            for(int j = 0; j < dime; j++) {
                System.out.print(String.format("%.4f\t%.4f\t%.4f\t%.4f\n", sigma[j][0], sigma[j][1], sigma[j][2], sigma[j][3]));
            }
            System.out.print("\n");
        }
    }
}
