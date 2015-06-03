/**
 * 
 */
package net.wyun.wm.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Xuecheng
 *
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
	
	public static String WMS_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";   //yyyy-MM-dd hh:mm:ss
	
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			WMS_DATE_FORMAT);

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
	}
}
