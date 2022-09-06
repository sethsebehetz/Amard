package com.etz.gh.amard.model;

import java.util.List;

/**
 *
 * @author seth.sebeh
 */
public class ChartJsWrapper {
    private ChartJsData chartJsData;
    private List<ChartJsDatasets> chartJsDataset;

    public ChartJsData getChartJsData() {
        return chartJsData;
    }

    public void setChartJsData(ChartJsData chartJsData) {
        this.chartJsData = chartJsData;
    }

    public List<ChartJsDatasets> getChartJsDataset() {
        return chartJsDataset;
    }

    public void setChartJsDataset(List<ChartJsDatasets> chartJsDataset) {
        this.chartJsDataset = chartJsDataset;
    }

}
