/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j;

@Log4j
public class TrainConfigInfo {
    
    private TrainData4UI.BuyModel model;
    
    private List<SpecificTrainInfo> trains;
    
    public TrainConfigInfo(List<TrainData4UI.UserTrainInfo> userTrains) {
        trains = new ArrayList<SpecificTrainInfo>();
        for (TrainData4UI.UserTrainInfo trainInfo : userTrains) {
            TrainData4UI.SeatOptionType optionType = TrainData4UI.SeatOptionType.getType(trainInfo.getTrainCode().charAt(0));
            if(optionType == null){
                log.error("精确购买车次[" + trainInfo.getTrainCode() + "]不正确，请检查。车次开头只能是D、C、G、Z、T、K、L");
            }else{
                SpecificTrainInfo train = new SpecificTrainInfo(trainInfo.getTrainCode(), optionType, trainInfo.getBestSeatType(), trainInfo.getWorstSeatType());
                trains.add(train);
            }
        }
    }
    
    public TrainData4UI.BuyModel getModel() {
        return model;
    }

    public List<SpecificTrainInfo> getTrains() {
        return trains;
    }

    public static class SpecificTrainInfo{

        private String trainCode;
        
        private List<PassengerData.SeatType> seatPerfers;

        public SpecificTrainInfo(String trainCode, List<PassengerData.SeatType> seatPerfers) {
            super();
            this.trainCode = trainCode;
            this.seatPerfers = seatPerfers;
        }
        
        public SpecificTrainInfo(String trainCode, TrainData4UI.SeatOptionType optionType, String bestSeat, String
                worstSeat) {
            super();
            this.trainCode = trainCode;
            if(optionType == TrainData4UI.SeatOptionType.GAOTIE){
                
            }
            else {
                
            }
        }
        
        public String getTrainCode() {
            return trainCode;
        }

        public void setSeatPerfers(List<PassengerData.SeatType> seatPerfers) {
            this.seatPerfers = seatPerfers;
        }

        public List<PassengerData.SeatType> getSeatPerfers() {
            return seatPerfers;
        }

        public TrainInfo convertToTrainInfo() {
            TrainInfo trainInfo = new TrainInfo();
            trainInfo.setStation_train_code(this.trainCode);
            
            return trainInfo;
        }
        
        public SpecificTrainInfo convertFromTrainInfo(TrainInfo trainInfo) {
            List<PassengerData.SeatType> theSeatPerfers = new ArrayList<PassengerData.SeatType>();
            TrainData4UI.SeatOptionType optionType = TrainData4UI.SeatOptionType.getType(trainInfo.getStation_train_code().charAt(0));
            if (optionType == TrainData4UI.SeatOptionType.GAOTIE) {
                theSeatPerfers.add(PassengerData.SeatType.TWO_SEAT);
                theSeatPerfers.add(PassengerData.SeatType.ONE_SEAT);
                theSeatPerfers.add(PassengerData.SeatType.NONE_SEAT);
            }
            else {
                theSeatPerfers.add(PassengerData.SeatType.HARD_SEAT);
                theSeatPerfers.add(PassengerData.SeatType.HARD_SLEEPER);
                theSeatPerfers.add(PassengerData.SeatType.NONE_SEAT);
            }
            return new SpecificTrainInfo(trainInfo.getStation_train_code(), theSeatPerfers);
        }
        
    }
    
    
}
