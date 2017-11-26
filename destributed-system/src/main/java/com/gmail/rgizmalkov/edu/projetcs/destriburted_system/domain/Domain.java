package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Domain {

    //название ноды - на сет файдов


    /**
     * N - Нод
     * M - Потоков
     *
     * 1 ---- set[uid1,uid2,uid3] <---очередь_1---[ ...]---  |
     * 2 ---- set[uid2,uid3,uid4] <---очередь_2---[ ...]---  |---\ Механизм распределения данных бросает
     * 3 ---- set[uid3,uid4,uid5] <---очередь_3---[ ...]---  |---/ подготовленную запись в n очередей
     * 4 ---- set[uid5,uid6,uid7] <---очередь_4---[ ...]---  |
     * --------------------------
     *  \
     *   \
     *    V
     * Доступ на чтение из сета свободный, блокируем
     */

}
