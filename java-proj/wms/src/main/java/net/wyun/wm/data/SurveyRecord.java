/**
 * 
 */
package net.wyun.wm.data;

import net.wyun.wm.domain.autoshow.Surveyee;

/**
 * @author Xuecheng
 *
 */
public class SurveyRecord {
	
	/**
	 * @param surveyee
	 */
	public SurveyRecord(Surveyee surveyee) {
		super();
		this.surveyee = surveyee;
	}
	
	
	private Surveyee surveyee;
	
	private String q1;
	private String q2;
	private String q3;
	private String q4;
	private String q5;
	
	public Surveyee getSurveyee() {
		return surveyee;
	}
	public void setSurveyee(Surveyee surveyee) {
		this.surveyee = surveyee;
	}
	public String getQ1() {
		return q1;
	}
	public void setQ1(String q1) {
		this.q1 = q1;
	}
	public String getQ2() {
		return q2;
	}
	public void setQ2(String q2) {
		this.q2 = q2;
	}
	public String getQ3() {
		return q3;
	}
	public void setQ3(String q3) {
		this.q3 = q3;
	}
	public String getQ4() {
		return q4;
	}
	public void setQ4(String q4) {
		this.q4 = q4;
	}
	public String getQ5() {
		return q5;
	}
	public void setQ5(String q5) {
		this.q5 = q5;
	}
	
	

}
