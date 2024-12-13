package Service;

import java.time.Duration;
import java.time.Instant;

public class MyTimer {
    // Duration 분:초:밀리초 로 변환
    public String durationToStringFormat(Duration duration) {
        long minutes = duration.toMinutes() % 60; //분
        long secs = duration.getSeconds() % 60;//초
        long millis = (duration.toMillis() % 1000)/10;//밀리 초

        return String.format("%02d : %02d : %02d", minutes,secs, millis);
    }

    //타이머 레이블에 입력할 텍스트
    public String setTimerText(Instant startTime){
        Instant curTime = Instant.now();
        Duration duration = Duration.between(startTime,curTime);
        return durationToStringFormat(duration);
    }
}
