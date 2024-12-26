package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Rank {
    private List<String> rankList = new ArrayList<>();
    private final Vector<String> usersHard = new Vector<>();
    private final Vector<String> usersNormal = new Vector<>();
    private final Vector<String> usersEasy = new Vector<>();
    private Vector<String> setRankResult;

    // 텍스트 파일 읽기
    public List<String> loadRankData(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            return new ArrayList<>(); // 실패 시 빈 리스트 반환
        }
    }

    // 시간(HH:mm:ss)에 따라 오름차순 정렬
    public void sortByTime(Vector<String> users) {
        Collections.sort(users, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                // 분:초:밀리초 값을 추출 및 변환
                int millis1 = parseTimeToMilliseconds(line1.split(",")[1]);
                int millis2 = parseTimeToMilliseconds(line2.split(",")[1]);

                // 총 밀리초 값을 비교
                return Integer.compare(millis1, millis2);
            }

            // 분:초:밀리초 -> 총 밀리초로 변환하는 메소드
            private int parseTimeToMilliseconds(String time) {
                String[] parts = time.split(":"); // "분:초:밀리초" 분리
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                int milliseconds = Integer.parseInt(parts[2]);

                // 초와 밀리초의 범위를 초과해도 정상적으로 계산
                return (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
            }
        });
    }

    // 난이도별로 정렬된 데이터 통합 (최대 10명)
    public void sortByDifficulty() {
        int rankedListNum = 0;

        for (String line : usersHard) {
            if (rankedListNum < 10) {
                setRankResult.add(line);
                rankedListNum++;
            }
        }

        for (String line : usersNormal) {
            if (rankedListNum < 10) {
                setRankResult.add(line);
                rankedListNum++;
            }
        }

        for (String line : usersEasy) {
            if (rankedListNum < 10) {
                setRankResult.add(line);
                rankedListNum++;
            }
        }
    }

    // 전체 데이터 정렬
    public Vector<String> setRankResult(String filePath) {
        rankList = loadRankData(filePath);
        usersHard.clear();
        usersNormal.clear();
        usersEasy.clear();
        setRankResult = new Vector<>();

        // 난이도별로 데이터 분류
        for (String line : rankList) { // line: "이름,시간,난이도"
            switch (line.split(",")[2]) {
                case "HARD":
                    usersHard.add(line);
                    break;
                case "NORMAL":
                    usersNormal.add(line);
                    break;
                case "EASY":
                    usersEasy.add(line);
                    break;
            }
        }

        // 시간에 따라 정렬
        sortByTime(usersHard);
        sortByTime(usersNormal);
        sortByTime(usersEasy);

        // 난이도 따라 정렬
        sortByDifficulty();

        return setRankResult;
    }

    // rank.txt 덮어쓰기
    public void reWriteRankFile(String filePath) {
        try (FileOutputStream fout = new FileOutputStream(filePath, false)) {
            for (String line : setRankResult) {
                fout.write(line.getBytes());
                fout.write("\n".getBytes());
            }
        } catch (IOException e) {
        }
    }


}
