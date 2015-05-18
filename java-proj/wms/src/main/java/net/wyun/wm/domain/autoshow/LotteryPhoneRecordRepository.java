/**
 * 
 */
package net.wyun.wm.domain.autoshow;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Xuecheng
 *
 */
public interface LotteryPhoneRecordRepository extends CrudRepository<LotteryPhoneRecord, Integer>{

	 @Query("SELECT lpr FROM LotteryPhoneRecord lpr WHERE (lpr.create_t BETWEEN :start_t AND :end_t) AND lpr.selected = :selected")
	 List<LotteryPhoneRecord> findByBetweenStartTAndEndTAndSelected(
			 @Param("start_t") Date start_t, 
			 @Param("end_t") Date end_t,
			 @Param("selected") boolean selected
			 );
			 
}
