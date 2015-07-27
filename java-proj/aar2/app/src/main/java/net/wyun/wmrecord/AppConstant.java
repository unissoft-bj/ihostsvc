package net.wyun.wmrecord;

/**
 * Created by guof on 4/25/2015.
 */
public interface AppConstant {


    public class ServiceTag {
        // long [] pattern = {100,2000,1000,2000};   // 停止 开启 停止 开启
        public static final long[] VIBRATOR_PATTERN_START = {100, 400, 100, 400};      // 启动提示
        public static final long[] VIBRATOR_PATTERN_REMIND = {100, 200, 1, 400};       // 运行提醒
        public static final long[] VIBRATOR_PATTERN_STOP = {100, 2000, 1000, 2000};    // 终止提示

        public static final String APP_NAME= "职语录";
    }
}
