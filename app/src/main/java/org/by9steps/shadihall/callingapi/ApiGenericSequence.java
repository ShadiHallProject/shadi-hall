package org.by9steps.shadihall.callingapi;

public interface ApiGenericSequence {
    void SendNewInsertedDataToCloudDB(Object callbackmethod);

    void SendUpdatedDataFromSqliteToCloud(Object callbackmethod);

    void getNewInsertedDataFromServer(Object callbackmethod);

    void getUpdatedDataFromServer(Object callbackmethod);

    void trigerAllMethodInRow(Object finalCallbackmethod);
}
