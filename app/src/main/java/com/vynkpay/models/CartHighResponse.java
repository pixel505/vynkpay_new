package com.vynkpay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartHighResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("jsonval")
        @Expose
        private Jsonval jsonval;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;

        public Jsonval getJsonval() {
            return jsonval;
        }

        public void setJsonval(Jsonval jsonval) {
            this.jsonval = jsonval;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class Jsonval {

        @SerializedName("series")
        @Expose
        private List<Series> series = null;
        @SerializedName("colors")
        @Expose
        private List<String> colors = null;
        @SerializedName("chart")
        @Expose
        private Chart chart;
        @SerializedName("title")
        @Expose
        private Title title;
        @SerializedName("legend")
        @Expose
        private Legend legend;
        @SerializedName("xAxis")
        @Expose
        private XAxis xAxis;
        @SerializedName("yAxis")
        @Expose
        private YAxis yAxis;
        @SerializedName("plotOptions")
        @Expose
        private PlotOptions plotOptions;
        @SerializedName("tooltip")
        @Expose
        private Tooltip tooltip;
        @SerializedName("credits")
        @Expose
        private Credits credits;

        public List<Series> getSeries() {
            return series;
        }

        public void setSeries(List<Series> series) {
            this.series = series;
        }

        public List<String> getColors() {
            return colors;
        }

        public void setColors(List<String> colors) {
            this.colors = colors;
        }

        public Chart getChart() {
            return chart;
        }

        public void setChart(Chart chart) {
            this.chart = chart;
        }

        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public Legend getLegend() {
            return legend;
        }

        public void setLegend(Legend legend) {
            this.legend = legend;
        }

        public XAxis getXAxis() {
            return xAxis;
        }

        public void setXAxis(XAxis xAxis) {
            this.xAxis = xAxis;
        }

        public YAxis getYAxis() {
            return yAxis;
        }

        public void setYAxis(YAxis yAxis) {
            this.yAxis = yAxis;
        }

        public PlotOptions getPlotOptions() {
            return plotOptions;
        }

        public void setPlotOptions(PlotOptions plotOptions) {
            this.plotOptions = plotOptions;
        }

        public Tooltip getTooltip() {
            return tooltip;
        }

        public void setTooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
        }

        public Credits getCredits() {
            return credits;
        }

        public void setCredits(Credits credits) {
            this.credits = credits;
        }

    }

    public class Labels {

        @SerializedName("style")
        @Expose
        private Style style;

        public Style getStyle() {
            return style;
        }

        public void setStyle(Style style) {
            this.style = style;
        }

        public class Labels_ {

            @SerializedName("style")
            @Expose
            private Style__ style;

            public Style__ getStyle() {
                return style;
            }

            public void setStyle(Style__ style) {
                this.style = style;
            }

        }

    }

    public class Legend {

        @SerializedName("layout")
        @Expose
        private String layout;
        @SerializedName("align")
        @Expose
        private String align;
        @SerializedName("verticalAlign")
        @Expose
        private String verticalAlign;

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public String getVerticalAlign() {
            return verticalAlign;
        }

        public void setVerticalAlign(String verticalAlign) {
            this.verticalAlign = verticalAlign;
        }

    }

    public class PlotOptions {

        @SerializedName("area")
        @Expose
        private Area area;

        public Area getArea() {
            return area;
        }

        public void setArea(Area area) {
            this.area = area;
        }

    }

    public class Series {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("data")
        @Expose
        private List<String> data = null;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("lineWidth")
        @Expose
        private String lineWidth;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(String lineWidth) {
            this.lineWidth = lineWidth;
        }

    }

    public class Style {

        @SerializedName("fontSize")
        @Expose
        private String fontSize;
        @SerializedName("color")
        @Expose
        private String color;

        public String getFontSize() {
            return fontSize;
        }

        public void setFontSize(String fontSize) {
            this.fontSize = fontSize;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

    }

    public class Style_ {

        @SerializedName("textTransform")
        @Expose
        private String textTransform;

        public String getTextTransform() {
            return textTransform;
        }

        public void setTextTransform(String textTransform) {
            this.textTransform = textTransform;
        }

    }

    public class Style__ {

        @SerializedName("fontSize")
        @Expose
        private String fontSize;
        @SerializedName("color")
        @Expose
        private String color;

        public String getFontSize() {
            return fontSize;
        }

        public void setFontSize(String fontSize) {
            this.fontSize = fontSize;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

    }


    public class Title {

        @SerializedName("text")
        @Expose
        private Object text;

        public Object getText() {
            return text;
        }

        public void setText(Object text) {
            this.text = text;
        }

    }

    public class Title_ {

        @SerializedName("style")
        @Expose
        private Style_ style;
        @SerializedName("text")
        @Expose
        private Object text;

        public Style_ getStyle() {
            return style;
        }

        public void setStyle(Style_ style) {
            this.style = style;
        }

        public Object getText() {
            return text;
        }

        public void setText(Object text) {
            this.text = text;
        }

    }

    public class Tooltip {

        @SerializedName("shared")
        @Expose
        private String shared;
        @SerializedName("valueSuffix")
        @Expose
        private Object valueSuffix;
        @SerializedName("backgroundColor")
        @Expose
        private String backgroundColor;
        @SerializedName("borderWidth")
        @Expose
        private String borderWidth;
        @SerializedName("borderColor")
        @Expose
        private String borderColor;
        @SerializedName("enabled")
        @Expose
        private String enabled;

        public String getShared() {
            return shared;
        }

        public void setShared(String shared) {
            this.shared = shared;
        }

        public Object getValueSuffix() {
            return valueSuffix;
        }

        public void setValueSuffix(Object valueSuffix) {
            this.valueSuffix = valueSuffix;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(String borderWidth) {
            this.borderWidth = borderWidth;
        }

        public String getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

    }

    public class XAxis {

        @SerializedName("categories")
        @Expose
        private List<String> categories = null;
        @SerializedName("gridLineWidth")
        @Expose
        private String gridLineWidth;
        @SerializedName("labels")
        @Expose
        private Labels labels;

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public String getGridLineWidth() {
            return gridLineWidth;
        }

        public void setGridLineWidth(String gridLineWidth) {
            this.gridLineWidth = gridLineWidth;
        }

        public Labels getLabels() {
            return labels;
        }

        public void setLabels(Labels labels) {
            this.labels = labels;
        }

    }

    public class YAxis {

        @SerializedName("minorTickInterval")
        @Expose
        private String minorTickInterval;
        @SerializedName("title")
        @Expose
        private Title_ title;
        @SerializedName("labels")
        @Expose
        private Labels_ labels;
        @SerializedName("gridLineWidth")
        @Expose
        private Double gridLineWidth;
        @SerializedName("gridLineColor")
        @Expose
        private String gridLineColor;
        @SerializedName("minorGridLineWidth")
        @Expose
        private String minorGridLineWidth;
        @SerializedName("lineColor")
        @Expose
        private String lineColor;

        public String getMinorTickInterval() {
            return minorTickInterval;
        }

        public void setMinorTickInterval(String minorTickInterval) {
            this.minorTickInterval = minorTickInterval;
        }

        public Title_ getTitle() {
            return title;
        }

        public void setTitle(Title_ title) {
            this.title = title;
        }

        public Labels_ getLabels() {
            return labels;
        }

        public void setLabels(Labels_ labels) {
            this.labels = labels;
        }

        public Double getGridLineWidth() {
            return gridLineWidth;
        }

        public void setGridLineWidth(Double gridLineWidth) {
            this.gridLineWidth = gridLineWidth;
        }

        public String getGridLineColor() {
            return gridLineColor;
        }

        public void setGridLineColor(String gridLineColor) {
            this.gridLineColor = gridLineColor;
        }

        public String getMinorGridLineWidth() {
            return minorGridLineWidth;
        }

        public void setMinorGridLineWidth(String minorGridLineWidth) {
            this.minorGridLineWidth = minorGridLineWidth;
        }

        public String getLineColor() {
            return lineColor;
        }

        public void setLineColor(String lineColor) {
            this.lineColor = lineColor;
        }

    }


    public class Area {

        @SerializedName("fillOpacity")
        @Expose
        private Double fillOpacity;

        public Double getFillOpacity() {
            return fillOpacity;
        }

        public void setFillOpacity(Double fillOpacity) {
            this.fillOpacity = fillOpacity;
        }

    }

    public class Chart {

        @SerializedName("fontFamily")
        @Expose
        private String fontFamily;
        @SerializedName("backgroundColor")
        @Expose
        private String backgroundColor;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("type")
        @Expose
        private String type;

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class Credits {

        @SerializedName("enabled")
        @Expose
        private String enabled;

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

    }


    public class Labels_ {

        @SerializedName("style")
        @Expose
        private Style__ style;

        public Style__ getStyle() {
            return style;
        }

        public void setStyle(Style__ style) {
            this.style = style;
        }

    }


}
