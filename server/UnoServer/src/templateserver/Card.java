/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templateserver;

/**
 *
 * @author pant9060
 */

//this class is used to store information about the card. it makes it easier to get the value, type or color of 
//the card
public class Card {
    private int numberValue;
    private String color;
    private String pathName = "";
    private String type="";
    public Card(int value, String cardColor){
        if(value ==10){
            type= "skip";
        }
        if(value ==11){
            type= "reverse";
        }
        if(value ==12){
            type= "draw";
        }
        if(value ==13){
            type= "wildcard";
        }
        numberValue = value;
        color = cardColor;
        pathName = "resources/"+color+numberValue+".png";
    }
    
    public Card(int value, String cardColor, String path){
        numberValue = value;
        color = cardColor;
        pathName = path;
    }
    
    
    public int returnValue(){
        return numberValue;
    }
    
    public String returnColor(){
        return color;
    }
    
    public String returnType(){
        return type;
    }
    
    
    public String returnPathName(){
        return pathName;
    }
    
}
