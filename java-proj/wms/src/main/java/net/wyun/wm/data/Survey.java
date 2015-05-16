/**
 * 
 */
package net.wyun.wm.data;

import net.wyun.wm.domain.autoshow.Answer;
import net.wyun.wm.domain.autoshow.Surveyee;

/**
 * @author Xuecheng
 *
 */
public class Survey {
	
	public Survey() {	}

	Answer[] answers;
	
	Surveyee surveyee;

	public Answer[] getAnswers() {
		return answers;
	}

	public void setAnswers(Answer[] answers) {
		this.answers = answers;
	}

	public Surveyee getSurveyee() {
		return surveyee;
	}

	public void setSurveyee(Surveyee surveyee) {
		this.surveyee = surveyee;
	}
	
	

}
