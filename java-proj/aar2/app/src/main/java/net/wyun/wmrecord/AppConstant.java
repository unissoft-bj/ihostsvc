package net.wyun.wmrecord;

/**
 * Created by guof on 4/25/2015.
 */
public interface AppConstant {


    public class ServiceTag {
        public static final boolean IS_RUNNING = false;

        // long [] pattern = {100,2000,1000,2000};   // ֹͣ ���� ֹͣ ����
        public static final long[] VIBRATOR_PATTERN_START = {100, 400, 100, 400};      // ������ʾ
        public static final long[] VIBRATOR_PATTERN_REMIND = {100, 200, 1, 400};       // ��������
        public static final long[] VIBRATOR_PATTERN_STOP = {100, 2000, 1000, 2000};    // ��ֹ��ʾ
    }
}
