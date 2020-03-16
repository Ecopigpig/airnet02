package com.zsc.servicedata.controller;

import com.zsc.servicedata.entity.param.AqiHistoryParam;
import com.zsc.servicedata.service.AirService;
import com.zsc.servicedata.service.PollutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.air.HistoryAqiChart;
import model.pollutant.PollutionEpisode;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(value = "ExportController",tags = "Excel导出控制器")
@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private PollutionService pollutionService;

    @Resource
    private AirService airService;

    //尝试excel的导出
    @ApiOperation(value = "导出全国的历史污染情况")
    @RequestMapping(value = "/pollutantHistory",method = RequestMethod.GET)
    public void exportPollutantHistory(HttpServletResponse response) throws IOException{
        List<PollutionEpisode> pollutionEpisodeList = pollutionService.getPollutantHistory();

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("全国各城市的污染情况历史记录SHEET1");

        HSSFRow row;

        row = sheet.createRow(0);//创建第一个单元格
        row.setHeight((short) (36.25 * 20));
        row.createCell(0).setCellValue("全国各城市的污染情况历史记录");//为第一行单元格设值.

        /*为标题设计空间
         * firstRow从第1行开始
         * lastRow从第0行结束
         *
         *从第1个单元格开始
         * 从第3个单元格结束
         */
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(rowRegion);

      /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
      sheet.addMergedRegion(columnRegion);*/


        /*
         * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where table_name='user' and table_schema='test'
         * 第一个table_name 表名字
         * 第二个table_name 数据库名称
         * */
        row = sheet.createRow(1);
        row.setHeight((short) (22.50 * 20));//设置行高
        row.createCell(0).setCellValue("id");//为第一个单元格设值
        row.createCell(1).setCellValue("城市名称");
        row.createCell(2).setCellValue("空气质量");
        row.createCell(3).setCellValue("发布时间");//为第三个单元格设值
        row.createCell(4).setCellValue("AQI指数");
        row.createCell(5).setCellValue("首要污染物名称");
        row.createCell(6).setCellValue("so2浓度:ug/m3");//为第二个单元格设值
        row.createCell(7).setCellValue("pm2.5浓度:ug/m3");
        row.createCell(8).setCellValue("co浓度:ug/m3");
        row.createCell(9).setCellValue("no2浓度:ug/m3");
        row.createCell(10).setCellValue("pm10浓度:ug/m3");
        row.createCell(11).setCellValue("o3每8小时的平均浓度:ug/m3");



        for (int i = 0; i < pollutionEpisodeList.size(); i++) {
            row = sheet.createRow(i + 2);
            PollutionEpisode episode = pollutionEpisodeList.get(i);
            row.createCell(0).setCellValue(episode.getId()  );//为第一个单元格设值
            row.createCell(1).setCellValue(episode.getArea());
            row.createCell(2).setCellValue(episode.getQuality());
            row.createCell(3).setCellValue(episode.getCt());//为第三个单元格设值
            row.createCell(4).setCellValue(episode.getAqi());
            row.createCell(5).setCellValue(episode.getPrimaryPollutant());
            row.createCell(6).setCellValue(episode.getSo2());//为第二个单元格设值
            row.createCell(7).setCellValue(episode.getPm25());//为第三个单元格设值
            row.createCell(8).setCellValue(episode.getCo());
            row.createCell(9).setCellValue(episode.getNo2());
            row.createCell(10).setCellValue(episode.getPm10());
            row.createCell(11).setCellValue(episode.getO3Per8h());
        }

        sheet.setDefaultRowHeight((short) (16.5 * 20));
        //列宽自适应
        for (int i = 0; i <= 13; i++) {
            sheet.autoSizeColumn(i);
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=全国城市历史污染情况.xls");//默认Excel名称
        wb.write(os);
        os.flush();
        os.close();
    }

    @ApiOperation(value = "按条件导出空气质量历史排行榜")
    @RequestMapping(value = "/airHistory",method = RequestMethod.GET)
    public void exportAirHistory(HttpServletResponse response, @RequestBody AqiHistoryParam param) throws IOException{
        //参数校验
        if(param.getSize()==null||param.getSize()<=0) param.setSize(10L);
        if(param.getOrder()==null||param.getOrder().equals("")){
            param.setOrder("asc");
        }else{
            String order = param.getOrder().toUpperCase();
            if(order.equals("ASC")) param.setOrder("ASC");
            else if(order.equals("DESC")) param.setOrder("DESC");
            else param.setOrder("ASC");
        }
        List<HistoryAqiChart> historyAqiChartList = airService.getAqiHistoryByCondition(param);

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("空气质量历史排行榜SHEET1");

        HSSFRow row;

        row = sheet.createRow(0);//创建第一个单元格
        row.setHeight((short) (36.25 * 20));
        row.createCell(0).setCellValue("空气质量历史排行榜");//为第一行单元格设值.

        /*为标题设计空间
         * firstRow从第1行开始
         * lastRow从第0行结束
         *
         *从第1个单元格开始
         * 从第3个单元格结束
         */
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(rowRegion);

      /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
      sheet.addMergedRegion(columnRegion);*/


        /*
         * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where table_name='user' and table_schema='test'
         * 第一个table_name 表名字
         * 第二个table_name 数据库名称
         * */
        row = sheet.createRow(1);
        row.setHeight((short) (22.50 * 20));//设置行高
        row.createCell(0).setCellValue("id");//为第一个单元格设值
        row.createCell(1).setCellValue("城市名称");
        row.createCell(2).setCellValue("AQI指数");
        row.createCell(3).setCellValue("空气质量");//为第三个单元格设值
        row.createCell(4).setCellValue("全国排名");
        row.createCell(5).setCellValue("记录时间");



        for (int i = 0; i < historyAqiChartList.size(); i++) {
            row = sheet.createRow(i + 2);
            HistoryAqiChart aqiChart = historyAqiChartList.get(i);
            row.createCell(0).setCellValue(aqiChart.getId()  );//为第一个单元格设值
            row.createCell(1).setCellValue(aqiChart.getCity());
            row.createCell(2).setCellValue(aqiChart.getAqi());
            row.createCell(3).setCellValue(aqiChart.getQuality());//为第三个单元格设值
            row.createCell(4).setCellValue(aqiChart.getRank());
            String dateStr = aqiChart.getMarkTime().toString();
            Date time =new Date(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeFormat = sdf.format(time);
            row.createCell(5).setCellValue(timeFormat);
        }

        sheet.setDefaultRowHeight((short) (16.5 * 20));
        //列宽自适应
        for (int i = 0; i <= 13; i++) {
            sheet.autoSizeColumn(i);
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=空气质量历史排行榜.xls");//默认Excel名称
        wb.write(os);
        os.flush();
        os.close();
    }
}
