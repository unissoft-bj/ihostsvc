package net.wyun.wm.domain.autoshow;

import static org.junit.Assert.*;

import java.util.Date;

import net.wyun.wm.data.Survey;
import net.wyun.wm.domain.Gender;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SurveyTest {

	@Test
	public void testSurvey2Json() throws JsonProcessingException {
		Survey survey = new Survey();
		
		survey.setAnswers(generateAnswers());
		survey.setSurveyee(generateSurveyee());
		
		ObjectMapper mapper = new ObjectMapper();
		String r_str = mapper.writeValueAsString(survey);
		System.out.println(r_str);
		
	}
	
	private Surveyee generateSurveyee() {
		
		Surveyee s = new Surveyee();
		s.setId("4028a7814cb58e21014cb5901ace0000");
		s.setName("nobody");
		s.setCity("北京");
		s.setAge(38);
		s.setGender(Gender.FEMALE);
		s.setHas_car(true);
		s.setPhone("1388288888");
		s.setShow_location("Boston");
		return s;
	}

	public Answer[] generateAnswers(){
		Answer[] answers = new Answer[2];
		
		Answer ans1 = new Answer();
		ans1.setAvailable_option("{'3':'报刊杂志', '4':'户外广告'}");
		ans1.setQ_id(1);
		
		Answer ans2 = new Answer();
		ans2.setAvailable_option("{'4':'30~40km', '10':'根据目前本市的交通情况，预计平时单程上下班的时间为$20$分钟。'}");
		ans2.setQ_id(4);
		
		answers[0] = ans1;
		answers[1] = ans2;
		return answers;
	}

}
