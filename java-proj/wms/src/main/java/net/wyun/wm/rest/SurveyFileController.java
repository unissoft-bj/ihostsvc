/**
 * 
 */
package net.wyun.wm.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.data.SurveyRecord;
import net.wyun.wm.domain.autoshow.Answer;
import net.wyun.wm.domain.autoshow.AnswerRepository;
import net.wyun.wm.domain.autoshow.Surveyee;
import net.wyun.wm.domain.autoshow.SurveyeeRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Xuecheng
 *
 */

@RequestMapping(value = "/secure")
@Controller
public class SurveyFileController {
	
	private static final String[] HEADER = {"名字","年龄","性别","城市", "电话", "有车", "Q1", "Q2", "Q3", "Q4", "Q5"};
	public final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	private static final String NEW_LINE_SEPARATOR = "\r\n";


	@RequestMapping(value = "/survey/file", method = RequestMethod.GET)
	public void getSurveyFile(HttpServletResponse response) throws IOException{
		
		response.setContentType("text/csv;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache, no-store");
		
		String exportFileName = "survey" + dateFormatter.format(new Date());
		
		response.setHeader("Content-Disposition","attachment; filename=\"" + exportFileName + ".csv\"");
		response.setBufferSize(20000);
		
		OutputStream out = response.getOutputStream();
		OutputStreamWriter opsw = new OutputStreamWriter(out, java.nio.charset.Charset.forName("UTF-8"));
		
		createReport(opsw);
		
		opsw.flush();
		
		response.setStatus(HttpServletResponse.SC_OK);
		opsw.close();
		
	}
	
	
	@Autowired
	SurveyeeRepository surveyeeRepo;
	
	@Autowired
	AnswerRepository answerRepo;
	
	
	private void createReport(OutputStreamWriter opsw) throws IOException{
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter('|');

		CSVPrinter csv = new CSVPrinter(opsw, csvFileFormat);
		csv.printRecord(HEADER);
		
		Iterable<Surveyee> surveyees = surveyeeRepo.findAll();
		Iterable<Answer>   anwsers = answerRepo.findAll();
		
		
		for(Surveyee sy:surveyees){
			SurveyRecord sr = new SurveyRecord(sy);
			
			//get q1 to q5
			sr.setQ1(getAnswer(sy.getId(), 1,  anwsers));
			sr.setQ2(getAnswer(sy.getId(), 2,  anwsers));
			sr.setQ3(getAnswer(sy.getId(), 3,  anwsers));
			sr.setQ4(getAnswer(sy.getId(), 4,  anwsers));
			sr.setQ5(getAnswer(sy.getId(), 5,  anwsers));
			
			//write to stream
			List<String> record = new ArrayList<String>();
			record.add(sy.getName());
			record.add(String.valueOf(sy.getAge()));
			record.add(sy.getGender().toString());
			record.add(sy.getCity());
			record.add(sy.getPhone());
			record.add(sy.isHas_car()?"有车":"无车");
            record.add(sr.getQ1());
            record.add(sr.getQ2());
            record.add(sr.getQ3());
            record.add(sr.getQ4());
            record.add(sr.getQ5());
			
            csv.printRecord(record);
		}
		
		
		
	}
	
	private String getAnswer(String surveyee_id, int q_id,  Iterable<Answer> anwsers){
		for(Answer ans:anwsers){
			if(surveyee_id.equals(ans.getSurveyee_id()) &&
					q_id == ans.getQ_id()){
				return ans.getAvailable_option();
			}
		}
		
		return "";
	}
}
