/*
 * Lingwistyka Matematyczna Zadanie 6 - Analizator składniowy.
 * Szymon Zawadzki 221515.
 */

package parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 *
 * @author Szaman
 */
public class FXMLDocumentController implements Initializable 
{
    //Pictures Stuff
    Image exit = new Image("parser/pictures/Exit.png");
    ImageView exitImage = new ImageView(exit);
    
    //FXML Stuff.
    @FXML
    private Button exitButton;
    
    @FXML
    private TextField userInputField;
    
    @FXML
    private Label validationResult;
    
    //ArrayList of O and C productions' content.
    ArrayList<Character> CList = new ArrayList<>();
    ArrayList<Character> OList = new ArrayList<>();
    
    //ArrayLists of first symbols. Explained in raport.
    ArrayList<Character> firstZ = new ArrayList<>();
    ArrayList<Character> firstWp = new ArrayList<>();
    ArrayList<Character> firstRp = new ArrayList<>();
    ArrayList<Character> firstLp = new ArrayList<>();
        
    int currentIndex;
    
    @FXML
    void handleAccept() throws Exception
    {
        currentIndex = 0;
        String userInput = userInputField.getText();
        int userInputLength = userInput.length();
        System.out.println("User Input: " + userInput + " Length: " + userInputLength);
        try
        {
            startAnalyze(userInput);
            validationResult.setText("Wyrażenie '" + userInput + "'\njest poprawne!");
            validationResult.setTextFill(Color.web("#00CC00"));
        }
        catch(Exception e)
        {
            validationResult.setText("Wyrażenie '" + userInput + "'\njest niepoprawne!");
            validationResult.setTextFill(Color.web("#FF0000"));
        }
    }
    
    void startAnalyze(String userInput) throws Exception
    {
        S(userInput);
    }
    
    //---------------------------------------------------------------------------------------//
    
    // S ::= W ; Z
    void S(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        //if(userInput.charAt(currentIndex) == whitespace)
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        W(userInput);
        
        if(userInput.charAt(currentIndex) == ';')
        {
            currentIndex++;
        }
        else
        {
            throw new Exception();
        }
        
        Z(userInput);
    }   
    
    //Z ::= W ; Z | empty
    void Z(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        if(!firstZ.contains(userInput.charAt(currentIndex)))
        {
            throw new Exception();//return;
        }
        
        W(userInput);
        
        if(userInput.charAt(currentIndex) == ';')
        {
            currentIndex++;
        }
        else
        {
            throw new Exception();
        }
        
        Z(userInput);        
    }

    // W ::= P W ' 
    void W(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
            
        P(userInput);
        
        Wp(userInput);
    }
    
    // W ' ::= O W | empty
    void Wp(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(!firstWp.contains(userInput.charAt(currentIndex)))
        {
            return;
        }
        
        O(userInput);
        
        W(userInput);
    }
    
    // P ::= R | ( W ) 
    void P(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(userInput.charAt(currentIndex) == '(')
        {
            currentIndex++;
            
            W(userInput);
            
            if(userInput.charAt(currentIndex) == ')')
            {
                currentIndex++;
            }
            else
            {
                throw new Exception();
            }
        }
        else
        {
            R(userInput);
        }
    }
    
    // R ::= L R '
    void R(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        L(userInput);
        
        Rp(userInput);
    }
    
    // R ' ::= . L | empty
    void Rp(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(!firstRp.contains(userInput.charAt(currentIndex)))
        {
            return;
        }
        else 
        {
            currentIndex++;
            L(userInput);
        }
    }
    
    // L ::= C L '
    void L(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        C(userInput);
        
        Lp(userInput);
    }
    
    // Lp ::= L | empty
    void Lp(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(!firstLp.contains(userInput.charAt(currentIndex)))
        {
            return;
        }
        
        L(userInput);
    }
    
    // C ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
    void C(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(CList.contains(userInput.charAt(currentIndex)))
        {
            currentIndex++;
        }
        else
        {
            throw new Exception();
        }
    }
    
    // O ::= * | : | + | - | ^ 
    void O(String userInput) throws Exception
    {
        if(currentIndex > userInput.length() - 1)
        {
            return;
        }
        
        if(userInput.charAt(currentIndex) == ' ')
        {
            goToFirstNonWhitespace(userInput);
        }
        
        if(OList.contains(userInput.charAt(currentIndex)))
        {
            currentIndex++;
        }
        else
        {
            throw new Exception();
        }
    }
    
    void goToFirstNonWhitespace(String userInput)
    {
        do
        {
            currentIndex++;
        }
        while(userInput.charAt(currentIndex) == ' ');
    }
    
    //---------------------------------------------------------------------------------------//
    
    //Exit Script.
    @FXML
    public void handleExit()
    {        
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", okButton, cancelButton);
        alert.setTitle("Wyjście");
        alert.setHeaderText(null);
        alert.setContentText("Na pewno?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton)
        {
            System.exit(0);
        } 
        else 
        {
            alert.close();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //Exit initialization
        exitImage.setFitHeight(75);
        exitImage.setFitWidth(75);
        exitButton.setGraphic(exitImage);
        exitButton.setMaxHeight(75);
        exitButton.setMaxWidth(75);
        
        //Lists initialization
        firstZ.addAll(Arrays.asList('(', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        firstWp.addAll(Arrays.asList('*', ':', '+', '-', '^'));
        firstRp.addAll(Arrays.asList('.'));
        firstLp.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        
        CList.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        OList.addAll(Arrays.asList('*', ':', '+', '-', '^'));
                
        validationResult.setText("");
        
        currentIndex = 0;
    }     
}
