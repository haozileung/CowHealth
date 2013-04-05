package com.haozileung.scau.client.home.view;

import org.moxieapps.gwt.highcharts.client.Axis;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Chart.ZoomType;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.DateTimeLabelFormats;
import org.moxieapps.gwt.highcharts.client.Highcharts;
import org.moxieapps.gwt.highcharts.client.Lang;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Series.Type;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.Marker;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions.Cursor;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.haozileung.scau.client.CowHealth;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * 类的简介说明
 * </p>
 * 创建时间：2013-3-20 上午9:43:17
 * 
 * @author lianghaopeng
 * @version V1.0
 */
public class SportDataChartView extends HLayout {
	private final VLayout leftPanel = new VLayout();
	private final VLayout rightPanel = new VLayout();
	private final VLayout top = new VLayout();
	private final VLayout buttom = new VLayout();
	private final DynamicForm form = new DynamicForm();
	private final SelectItem selectItem = new SelectItem("cowId", "奶牛");
	private final IButton statusBtn = new IButton("自动更新");
	private Chart chart;
	private boolean running = false;
	private boolean enabled = true;

	private void initRightPanel() {
		Highcharts.setOptions(new Highcharts.Options().setLang(new Lang()
				.setMonths(
						new String[] { "一月", "二月", "三月", "四月", "五月", "六月",
								"七月", "八月", "九月", "十月", "十一月", "十二月" })
				.setWeekdays(
						new String[] { "星期一", "星期二", "星期三", "星期四", "星期五",
								"星期六", "星期天" }).setResetZoom("重置")
				.setResetZoomTitle("重置到初始样式")));
		chart = new Chart();
		chart.setToolTip(new ToolTip().setEnabled(true).setShadow(true)
				.setXDateFormat("%Y年%B%e日%H时 %A"));
		chart.setZoomType(ZoomType.X);
		chart.setSpacingRight(20);
		chart.setChartTitleText("奶牛运动数据曲线图");
		chart.setCredits(new Credits().setHref("http://www.haozileung.com")
				.setText("Haozi Leung 制作"));
		chart.getXAxis()
				.setType(Axis.Type.DATE_TIME)
				.setMaxZoom(24 * 3600000)
				.setDateTimeLabelFormats(
						new DateTimeLabelFormats().setWeek("%B%e日")
								.setDay("%B%e日").setHour("%H:%M")
								.setMonth("%Y年%B").setYear("%Y年"));
		chart.getYAxis().setAxisTitleText("运动量");
		chart.setSeriesPlotOptions(new SeriesPlotOptions().setCursor(
				Cursor.POINTER).setMarker(new Marker().setLineWidth(1)));
		rightPanel.setWidth("80%");
		rightPanel.addMember(chart);
	}

	private void initLeftPanel() {
		selectItem.setValueMap(CowHealth.cowMap);
		selectItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				statusBtn.enable();
				enabled = false;
			}
		});
		form.setItems(selectItem);
		top.addMember(form);
		statusBtn.disable();
		statusBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				((IButton) event.getSource()).disable();
				enabled = true;
				getSportData(selectItem.getValueAsString());
			}
		});
		top.addMember(statusBtn);
		top.setHeight("20%");
		buttom.setHeight("80%");
		leftPanel.setWidth("20%");
		leftPanel.addMember(top);
		leftPanel.addMember(buttom);
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (running == false && enabled == true) {
					getSportData(null);
				}
			}

		};
		timer.scheduleRepeating(1000);
	}

	public SportDataChartView() {
		initLeftPanel();
		initRightPanel();
		addMember(leftPanel);
		addMember(rightPanel);
	}

	public void getSportData(final String cowId) {
		running = true;
		RequestBuilder req = new RequestBuilder(RequestBuilder.GET,
				"data/getSportData.action");
		try {
			String data = null;
			if (cowId != null && !cowId.isEmpty()) {
				data = "cowId=" + cowId;
			}
			req.sendRequest(data, new RequestCallback() {

				@Override
				public void onError(Request arg0, Throwable arg1) {
					SC.say("请求运动数据出错！");
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					String json = response.getText();
					setData(json);
					running = false;
				}
			});
		} catch (RequestException e) {
		}
	}

	public void setData(String json) {
		String cowName = null;
		StringBuffer cowData = new StringBuffer();
		String startDate = null;
		JSONValue jv = JSONParser.parseStrict(json).isObject().get("response");
		if (jv != null) {
			JSONValue jvData = jv.isObject().get("data");
			if (jvData != null) {
				JSONArray ja = jvData.isArray();
				JSONValue jvCow = null;
				for (int i = 0; i < ja.size(); i++) {
					jvCow = ja.get(i);
					if (jvCow != null) {
						cowData.append(jvCow.isObject().get("data").isString()
								.stringValue());
						cowData.append(",");
						if (i == 0) {
							cowName = jvCow.isObject().get("cowName")
									.isString().stringValue();
							startDate = jvCow.isObject().get("currentDate")
									.isString().stringValue();
						}
					}
				}
				String[] dataStr = cowData.toString().split(",");
				if (dataStr.length > 0) {
					Number[] finalData = new Number[dataStr.length];
					for (int i = 0; i < finalData.length; i++) {
						if (!"".equals(dataStr[i])) {
							finalData[i] = Float.valueOf(dataStr[i]);
						}
					}

					chart.removeAllSeries();
					Series series;
					series = chart
							.createSeries()
							.setName("奶牛:" + cowName)
							.setType(Type.LINE)
							.setPlotOptions(
									new LinePlotOptions().setPointInterval(
											3600000).setPointStart(
											(DateTimeFormat
													.getFormat("yyyy-MM-dd")
													.parse(startDate))
													.getTime()))
							.setPoints(finalData, true);
					chart.addSeries(series);
				}
			}
		}
	}
}
