/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temclient;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import socketfx.Constants;
import socketfx.FxSocketClient;
import socketfx.SocketListener;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class FXMLDocumentControllerClient implements Initializable {


    @FXML
    private Button sendButton;
    @FXML
    private Label messages,turnLabel;
    @FXML
    private Button connectButton;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private ImageView imgView00,imgView01, imgView02, imgView03, imgView04, imgView05, imgView06, imgView07, imgView08, imgView09, imgView10, imgView11, imgView12, imgView13, imgView14, imgView15;
    @FXML
    private GridPane gpane;
    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private boolean turn=false;

    private boolean isConnected;
    private int numCards;
    ArrayList<Card> clientHand = new ArrayList<>();
    
    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketClient socket;
    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                hostTextField.getText(),
                Integer.valueOf(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
        
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
        clientHand.add(new Card(0,null,"resources/grey.png"));
    }

    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//                connectButton.setDisable(false);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                connectButton.setDisable(true);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case CONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//            case AUTOCONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);


        

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        /*
         * Uncomment to have autoConnect enabled at startup
         */
//        autoConnectCheckBox.setSelected(true);
//        displayState(ConnectionDisplayState.WAITING);
//        connect();
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

         //this method deals with all of the messages sent from the other user. prefixes are used to differentiate the 
        //path names for different imageviews.
        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
               
               String prefix = line.substring(0,3);
               
               if(prefix.equals("dck")){
                   String color = line.substring(3,4);
                   int number = Integer.parseInt(line.substring(4,6));
                   imgView00.setImage(new Image(line.substring(6)));
                   clientHand.set(0,new Card(number,color));
               }
               if(prefix.equals("msg")){
                   messages.setText(line.substring(3));
                   if(line.substring(3).equals("you lost")){
                       turn = false;
                   }
               }
               if(line.equals("changeturn")){
                   turn = true;
                   turnLabel.setText("you");
               }
               if(prefix.equals("new")){
                   String color = line.substring(3,4);
                   int number = Integer.parseInt(line.substring(4,6));
                   if(numCards == 13){
                       System.out.println("no more");
                       messages.setText("you cannot select anymore cards");
                   }else{
                       numOfCards();
                       clientHand.set(numCards+2, new Card(number, color));
                       setImages();
                   }
                    
               }
               if(line.equals("start")){
                   System.out.println("begin start");
                   start();
               }
               if(line.equals("uno")){
                   messages.setText("the other user has an uno!");
               }
               switch(prefix){
                   case "ig2":
                       String color = line.substring(3,4);
                        int number = Integer.parseInt(line.substring(4,6));
                       imgView02.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(2, new Card(number,color));
                       break;
                   case "ig3":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView03.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(3, new Card(number,color));
                       break;
                   case "ig4":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView04.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(4, new Card(number,color));
                       break;
                   case "ig5":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView05.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(5, new Card(number,color));
                       break;
                   case "ig6":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView06.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(6, new Card(number,color));
                       break;
                   case "ig7":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView07.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(7, new Card(number,color));
                       break;
                   case "ig8":
                       color = line.substring(3,4);
                       number = Integer.parseInt(line.substring(4,6));
                       imgView08.setImage(new Image(line.substring(6)));
                       System.out.println(prefix);
                       System.out.println(color);
                       System.out.println(number);
                       clientHand.set(8, new Card(number,color));
                       break;
                   default:
                       break;
               }
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
           
        }
    }


    @FXML
    private void handleConnectButton(ActionEvent event) {
        connectButton.setDisable(true);
        displayState(ConnectionDisplayState.WAITING);
        connect();
    }
    
    //this method finds the selected card, checks if it can be played the deck image view equal lto it
    @FXML
    private void handleSelectedCard(MouseEvent event) {
        if(turn){
        Object source = event.getSource();
        ImageView clickedView = (ImageView) source; 
        String imageId = clickedView.getId(); 
        int index = Integer.parseInt(imageId.substring(imageId.length()-2));
        if(clientHand.get(index).returnPathName().equals("resources/grey.png")){
            messages.setText("please choose a valid card");
        }else{
            numOfCards();
            if(numCards ==0){
                messages.setText("you won!");
                socket.sendMessage("msgyou lost");
                turn = false;
            }else{
                if(check(index)&& !type(index)){         
            imgView00.setImage(new Image(clientHand.get(index).returnPathName()));
            clientHand.set(0,clientHand.get(index));
            String value = Integer.toString(clientHand.get(index).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
            socket.sendMessage("dck"+clientHand.get(index).returnColor()+value+clientHand.get(index).returnPathName());
            clientHand.remove(index);
            clientHand.add(new Card(0,null,"resources/grey.png"));
            System.out.println("new added");
            setImages();
            System.out.println("setImages");
            numOfCards();
            if(numCards==1){
                messages.setText("you have an uno!");
                socket.sendMessage("uno");
            }
            socket.sendMessage("changeturn");
            turnLabel.setText("opponent");
            turn = false;
           }
            if(type(index)){
                    switch(clientHand.get(index).returnValue()){
                        case 10:
                             if(check(index)){
                                  messages.setText("you get to go again");
                                socket.sendMessage("msgyour opponentget to go again");
                                 imgView00.setImage(new Image(clientHand.get(index).returnPathName()));
                            clientHand.set(0,clientHand.get(index));
                            String value = Integer.toString(clientHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+clientHand.get(index).returnColor()+value+clientHand.get(index).returnPathName());
                            clientHand.remove(index);
                            clientHand.add(new Card(0,null,"resources/grey.png"));
                            System.out.println("new added");
                            setImages();
                            System.out.println("setImages");
                            numOfCards();
                            if(numCards==1){
                                messages.setText("you have an uno!");
                                socket.sendMessage("uno");
                            }
                             }else{
                                messages.setText("please choose a valid card");
                            }
                           
                            break;
                        case 11:
                            if(check(index)){
                            messages.setText("you get to go again to reverse order");
                            socket.sendMessage("msgyour opponent gets to go again to reverse order");
                             imgView00.setImage(new Image(clientHand.get(index).returnPathName()));
                            clientHand.set(0,clientHand.get(index));
                            String value = Integer.toString(clientHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+clientHand.get(index).returnColor()+value+clientHand.get(index).returnPathName());
                            clientHand.remove(index);
                            clientHand.add(new Card(0,null,"resources/grey.png"));
                            System.out.println("new added");
                            setImages();
                            System.out.println("setImages");
                            numOfCards();
                            if(numCards==1){
                                messages.setText("you have an uno!");
                                socket.sendMessage("uno");
                            }
                            
                            }else{
                                messages.setText("please choose a valid card");
                            }
                            break;
                        case 12:
                            if(check(index)){
                            messages.setText("your opponent needs to draw two cards");
                             socket.sendMessage("msgyou have to draw two cards");
                            socket.sendMessage("draw");
                             imgView00.setImage(new Image(clientHand.get(index).returnPathName()));
                            clientHand.set(0,clientHand.get(index));
                            String value = Integer.toString(clientHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+clientHand.get(index).returnColor()+value+clientHand.get(index).returnPathName());
                            clientHand.remove(index);
                            clientHand.add(new Card(0,null,"resources/grey.png"));
                            System.out.println("new added");
                            setImages();
                            System.out.println("setImages");
                            numOfCards();
                            if(numCards==1){
                                messages.setText("you have an uno!");
                                socket.sendMessage("uno");
                            }
                            }else{
                                messages.setText("please choose a valid card");
                            }
                            break;
                        case 13:
                            String color = clientHand.get(index).returnColor();
                            socket.sendMessage("changeturn");
                            turnLabel.setText("opponent");
                            turn = false;
                            if(color.equals("B")){
                                messages.setText("you changed color to blue");
                                socket.sendMessage("msgyour opponent changed color to blue");    
                            }
                            if(color.equals("G")){
                                messages.setText("you changed color to green");
                                socket.sendMessage("msgyour opponent changed color to green");
                            }
                            if(color.equals("Y")){
                                messages.setText("you changed color to yellow");
                                socket.sendMessage("msgyour opponent changed color to yellow");
                            }
                            if(color.equals("R")){
                                messages.setText("you changed color to red");
                                socket.sendMessage("msgyour opponent changed color to red");
                            }
                            break;
                        default:
                            break;
                    }
                   
                }
            
            
            }
            
        }
        }else{
            messages.setText("please wait until your opponent responds.");
        }
        
    }
    
    //this method starts the game by setting the back card
    private void start(){
        imgView01.setImage(new Image("resources/Back.png"));
        clientHand.set(1,new Card(0,null,"resources/Back.png"));
        turnLabel.setText("opponent");
    }
    
    //this method redraws all the images in the imageviews.
    private void setImages(){
        imgView00.setImage(new Image(clientHand.get(0).returnPathName()));
        imgView02.setImage(new Image(clientHand.get(2).returnPathName()));
        imgView03.setImage(new Image(clientHand.get(3).returnPathName()));
        imgView04.setImage(new Image(clientHand.get(4).returnPathName()));
        imgView05.setImage(new Image(clientHand.get(5).returnPathName()));
        imgView06.setImage(new Image(clientHand.get(6).returnPathName()));
        imgView07.setImage(new Image(clientHand.get(7).returnPathName()));
        imgView08.setImage(new Image(clientHand.get(8).returnPathName()));
        imgView09.setImage(new Image(clientHand.get(9).returnPathName()));
        imgView10.setImage(new Image(clientHand.get(10).returnPathName()));
        imgView11.setImage(new Image(clientHand.get(11).returnPathName()));
        imgView12.setImage(new Image(clientHand.get(12).returnPathName()));
        imgView13.setImage(new Image(clientHand.get(13).returnPathName()));
        imgView14.setImage(new Image(clientHand.get(14).returnPathName()));
        imgView15.setImage(new Image(clientHand.get(15).returnPathName()));
    }

    //this method gets the number of cards currently being used.
    private void numOfCards(){
        numCards = 0;
            for(int i = 2; i<16;i++){
                if(!(clientHand.get(i).returnPathName().equals("resources/grey.png"))){
                    numCards ++; 
                }
            }
    }
    
    //sends a message to the client to add a new card to the dekc
    @FXML
    private void handleDeal(MouseEvent event){
        if(turn){
            socket.sendMessage("newCard");
            socket.sendMessage("changeturn");
            turnLabel.setText("opponent");
            turn = false;
        }else{
            messages.setText("please wait until your opponent responds.");
        }
        

    }
    
    //this method checks if the card selected can be placed on top of the deck card.
    private boolean check(int index){
        if(clientHand.get(index).returnColor().equals(clientHand.get(0).returnColor())){
            return true;
        }else if(clientHand.get(index).returnValue() == clientHand.get(0).returnValue()){
            return true;
        }
        messages.setText("the card you selected is not valid! please select another card.");
        return false;
    }
   
    //returns true if the card is not a number card
   private boolean type(int index){
        if(clientHand.get(index).returnValue()<10){
            return false;
        }
        return true;
    }

}
