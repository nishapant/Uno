/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templateserver;


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
import socketfx.FxSocketServer;
import socketfx.SocketListener;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class FXMLDocumentControllerServer implements Initializable {


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
    private boolean turn=false;
    
    ArrayList<Card> deck= new ArrayList<>();
    ArrayList<Card> discarded= new ArrayList<>();
    ArrayList<String> pathNames= new ArrayList<>();
    ArrayList<Card> serverHand = new ArrayList<>();
    ArrayList<Card> clientHand = new ArrayList<>();
    int numCards;
    
    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());



    private boolean isConnected;

    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketServer socket;

    private void connect() {
        socket = new FxSocketServer(new FxSocketListener(),
                Integer.valueOf(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
  
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
        serverHand.add(new Card(0,null,"resources/grey.png"));
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
                   serverHand.set(0,new Card(number,color));
               }
               if(line.equals("changeturn")){
                   turn = true;
                   turnLabel.setText("your turn");
               }
               if(prefix.equals("msg")){
                   messages.setText(line.substring(3));
                   if(line.substring(3).equals("you lost")){
                       turn = false;
                   }
               }
                if(line.equals("draw")){
                    dealNew();
                    dealNew();
                }
               if(line.equals("newCard")){
                   if(deck.size() == 0){
                    for(int i = 0; i<discarded.size(); i++){
                        deck.add(discarded.get(i));
                        discarded.remove(i);
                    }
            }
                    int random = (int)(Math.random()* deck.size());
                    String value = Integer.toString(deck.get(random).returnValue());
                    if(!(value.length() == 2)){
                        value = "0"+value;
                    }
                    socket.sendMessage("new"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
                    discarded.add(deck.get(random));
                    deck.remove(random);
               }
               if(line.equals("uno")){
                   messages.setText("the other user has an uno!");
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

    //puts in all the data for the arrays and starts the game.
    @FXML
    private void handleStart(ActionEvent event){
        turnLabel.setText("you");
        randomCard();
        turn = true;
        messages.setText("you will start first");
        socket.sendMessage("msgyour opponent will go first");
        int random = (int)(Math.random()* deck.size());
        while(deck.get(random).returnValue()>=10){
            random = (int)(Math.random()* deck.size());
        }
        imgView00.setImage(new Image(deck.get(random).returnPathName()));
        serverHand.set(0,deck.get(random));
        String value = Integer.toString(deck.get(random).returnValue());
        if(!(value.length() == 2)){
            value = "0"+value;
        }
        socket.sendMessage("dck"+ deck.get(random).returnColor()+value+deck.get(random).returnPathName());
        discarded.add(deck.get(random));
        deck.remove(random);

        imgView01.setImage(new Image("resources/Back.png"));
        serverHand.set(1,new Card(0,null,"resources/Back.png"));
        socket.sendMessage("start");
        
        random = (int)(Math.random()* deck.size());
        imgView02.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(2,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        imgView03.setImage(new Image(deck.get(random).returnPathName()));
        serverHand.set(3,deck.get(random));
        discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        imgView04.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(4,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        imgView05.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(5,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
   
        random = (int)(Math.random()* deck.size());
        imgView06.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(6,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        imgView07.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(7,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        imgView08.setImage(new Image(deck.get(random).returnPathName()));
         serverHand.set(8,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        System.out.println("all server hands work");

        clientHand.set(0,serverHand.get(0));
        clientHand.set(1,serverHand.get(1));
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig2"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
        clientHand.set(2,deck.get(random));
        discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig3"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
        clientHand.set(3,deck.get(random));
        discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig4"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
         clientHand.set(4,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig5"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
         clientHand.set(5,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
   
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig6"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
         clientHand.set(6,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
        value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig7"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
         clientHand.set(7,deck.get(random));
         discarded.add(deck.get(random));
        deck.remove(random);
        
        random = (int)(Math.random()* deck.size());
       value = Integer.toString(deck.get(random).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
        socket.sendMessage("ig8"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
        clientHand.set(8,deck.get(random));
        discarded.add(deck.get(random));
        deck.remove(random);
    }
    
    //gives the user a new card
    @FXML
    private void handleDeal(MouseEvent event){
        if(turn){
            if(deck.size() == 0){
                for(int i = 0; i<discarded.size(); i++){
                    deck.add(discarded.get(i));
                    discarded.remove(i);
                }
            }
            dealNew();
            socket.sendMessage("changeturn");
            turnLabel.setText("opponent");
            turn = false;
        }else{
            messages.setText("please wait until your opponent responds.");
        }
        
    }
    
    private void dealNew(){
        int random = (int)(Math.random()* deck.size());
        if(numCards ==13){
            messages.setText("you cannot select any more cards");
        }else{
            numOfCards();
            System.out.println("number of cards: "+numCards);
            serverHand.set(numCards+2,new Card(deck.get(random).returnValue(),deck.get(random).returnColor()));
            discarded.add(deck.get(random));
            deck.remove(random);
            setImages();
        }
    }
    
    //this method finds the selected card, checks if it can be played the deck image view equal lto it
    @FXML
    private void handleSelectedCard(MouseEvent event) {
        if(turn){
            Object source = event.getSource();
        ImageView clickedView = (ImageView) source; 
        String imageId = clickedView.getId();
        int index = Integer.parseInt(imageId.substring(imageId.length()-2));
        
        if(serverHand.get(index).returnPathName().equals("resources/grey.png")){
            System.out.println("choose another selection");
            messages.setText("please choose a valid card");
        }else{
            numOfCards();
            if(numCards ==0){
                messages.setText("you won!");
                socket.sendMessage("msgyou lost");
            }else{
            if(check(index)&&!type(index)){
                imgView00.setImage(new Image(serverHand.get(index).returnPathName()));
                serverHand.set(0,serverHand.get(index));
                
                String value = Integer.toString(serverHand.get(index).returnValue());
                if(!(value.length() == 2)){
                    value = "0"+value;
                }
                socket.sendMessage("dck"+serverHand.get(index).returnColor()+value+serverHand.get(index).returnPathName());
                discarded.add(serverHand.get(index));
                serverHand.remove(index);
                serverHand.add(new Card(0,null,"resources/grey.png"));
                setImages();
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
                    switch(serverHand.get(index).returnValue()){
                        case 10:
                            if(check(index)){
                            messages.setText("you get to go again");
                            socket.sendMessage("msgyour opponentget to go again");
                            imgView00.setImage(new Image(serverHand.get(index).returnPathName()));
                            serverHand.set(0,serverHand.get(index));
                
                            String value = Integer.toString(serverHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+serverHand.get(index).returnColor()+value+serverHand.get(index).returnPathName());
                            discarded.add(serverHand.get(index));
                            serverHand.remove(index);
                            serverHand.add(new Card(0,null,"resources/grey.png"));
                            setImages();
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
                            imgView00.setImage(new Image(serverHand.get(index).returnPathName()));
                            serverHand.set(0,serverHand.get(index));
                
                            String value = Integer.toString(serverHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+serverHand.get(index).returnColor()+value+serverHand.get(index).returnPathName());
                            discarded.add(serverHand.get(index));
                            serverHand.remove(index);
                            serverHand.add(new Card(0,null,"resources/grey.png"));
                            setImages();
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
                            socket.sendMessage("changeturn");
                            turn = false;
                            turnLabel.setText("opponent");
                            messages.setText("your opponent has to draw two cards");
                            socket.sendMessage("msgyou have to draw two cards");
                            for(int i=0;i<2;i++){
                                int random = (int)(Math.random()* deck.size());
                                String value = Integer.toString(deck.get(random).returnValue());
                                if(!(value.length() == 2)){
                                    value = "0"+value;
                                }
                                socket.sendMessage("new"+deck.get(random).returnColor()+value+deck.get(random).returnPathName());
                                discarded.add(deck.get(random));
                                deck.remove(random);
                                imgView00.setImage(new Image(serverHand.get(index).returnPathName()));
                            serverHand.set(0,serverHand.get(index)); 
                            }
                            String value = Integer.toString(serverHand.get(index).returnValue());
                            if(!(value.length() == 2)){
                                value = "0"+value;
                            }
                            socket.sendMessage("dck"+serverHand.get(index).returnColor()+value+serverHand.get(index).returnPathName());
                            discarded.add(serverHand.get(index));
                            serverHand.remove(index);
                            serverHand.add(new Card(0,null,"resources/grey.png"));
                            setImages();
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
                            socket.sendMessage("changeturn");
                            turnLabel.setText("opponent");
                            turn = false;
                            String color = serverHand.get(index).returnColor();
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
                                socket.sendMessage("msgyour opponentchanged color to red");
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
    
    //this method redraws all the images in the imageviews.
    private void setImages(){
        imgView00.setImage(new Image(serverHand.get(0).returnPathName()));
        imgView02.setImage(new Image(serverHand.get(2).returnPathName()));
        imgView03.setImage(new Image(serverHand.get(3).returnPathName()));
        imgView04.setImage(new Image(serverHand.get(4).returnPathName()));
        imgView05.setImage(new Image(serverHand.get(5).returnPathName()));
        imgView06.setImage(new Image(serverHand.get(6).returnPathName()));
        imgView07.setImage(new Image(serverHand.get(7).returnPathName()));
        imgView08.setImage(new Image(serverHand.get(8).returnPathName()));
        imgView09.setImage(new Image(serverHand.get(9).returnPathName()));
        imgView10.setImage(new Image(serverHand.get(10).returnPathName()));
        imgView11.setImage(new Image(serverHand.get(11).returnPathName()));
        imgView12.setImage(new Image(serverHand.get(12).returnPathName()));
        imgView13.setImage(new Image(serverHand.get(13).returnPathName()));
        imgView14.setImage(new Image(serverHand.get(14).returnPathName()));
        imgView15.setImage(new Image(serverHand.get(15).returnPathName()));
        
    }
    
    //this method gets the number of cards currently being used.
    private void numOfCards(){
        numCards = 0;
            for(int i = 2; i<16;i++){
                if(!(serverHand.get(i).returnPathName().equals("resources/grey.png"))){
                    numCards ++; 
                }
            }
    }
    
    private void randomCard(){
        for(int i =0; i<4; i++){ 
            for(int k=0; k<=13; k++){
                if(i==0){
                    deck.add(new Card(k,"B"));
                }
                if(i==1){
                    deck.add(new Card(k,"G"));
                }
                if(i==2){
                    deck.add(new Card(k,"R"));
                }
                if(i==3){
                    deck.add(new Card(k,"Y"));
                }
               
            }
        }
    }
    
    private boolean check(int index){
        if(serverHand.get(index).returnColor().equals(serverHand.get(0).returnColor())){
            return true;
        }else if(serverHand.get(index).returnValue() == serverHand.get(0).returnValue()){
            return true;
        }
        messages.setText("the card you selected is not valid! please select another card.");
        return false;
    }
    
    private boolean type(int index){
        if(serverHand.get(index).returnType().equals("")){
            return false;
        }
        return true;
    }
}
