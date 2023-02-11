package org.olympiad.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.olympiad.model.Answer;
import org.olympiad.model.Map;

import java.util.Collections;
import java.util.List;

public class PathResolver {
    private static final Logger logger = LogManager.getLogger(PathResolver.class);


    // метод находит оптимальный путь для сбора необходимого количества ресурсов и доставки их на базу
    public Answer findAnswer(Map map) {
        //TODO - напишите здесь реализацию метода поиска одного из возможных оптимальных маршрутов на карте
        List<Integer> path = Collections.emptyList();
        Answer answer = new Answer(path);
        logger.info("Response: {}", answer);
        return answer;
    }
}
