/**
 * 
 */
package net.wyun.wm.domain.autoshow;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.wyun.wm.util.CustomDateSerializer;

/**
 * @author Xuecheng
 *
 */
public class LotteryTime {
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date start_t;
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date end_t;
	
	public Date getStart_t() {
		return start_t;
	}
	public void setStart_t(Date start_t) {
		this.start_t = start_t;
	}
	public Date getEnd_t() {
		return end_t;
	}
	public void setEnd_t(Date end_t) {
		this.end_t = end_t;
	}

}
