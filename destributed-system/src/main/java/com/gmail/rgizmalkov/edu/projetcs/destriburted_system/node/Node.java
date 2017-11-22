package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node;

import com.gmail.rgizmalkov.edu.projects.vo.*;

import java.util.List;

public interface Node {

    /**
     * Записать одно значение на ноде
     * @param val - единица записи
     * @param <T> - тип единицы записи
     * @return - технический ответ сервиса
     */
    <T> ServiceTechRs write(T val);

    /**
     * Прочитать одну запись из ноды
     * @param serviceSingleRq - запрос к ноде с единичным результатлм
     * @param <T> - тип возвращаемого объекта
     * @return - склеенный технический ответ сервиса и объект
     */
    <T> ServiceSingleRs<T> read(ServiceSingleRq serviceSingleRq);

    /**
     * Максимальный обрабатываемый размер батча
     * @return - размер
     */
    int batch();

    /**
     * Запись нескольких записей в ноду
     * @param val - список записываемых значений
     * @param <T> - тип записываемых значений
     * @return - технический ответ сервиса
     */
    <T> ServiceTechRs writeBatch(List<T> val);

    /**
     * Выбор нескольких записей из ноды
     * @param serviceSingleRq - запрос к ноде
     * @param <T> тип возвращаемых записей
     * @return - склеенный серверный ответ со списком возвращаемых объектов
     */
    <T> ServiceManyRs<T> select(ServiceMatchRq serviceSingleRq);


    /**
     * Песистировать данные
     * @return - Да/нет
     */
    boolean persist();
}
