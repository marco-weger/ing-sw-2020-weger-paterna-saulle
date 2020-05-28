package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.commons.servermessages.CheckBuildServer;
import it.polimi.ingsw.commons.servermessages.CheckMoveServer;
import it.polimi.ingsw.commons.servermessages.CurrentStatusServer;
import it.polimi.ingsw.commons.servermessages.QuestionAbilityServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class BoardController extends DefaultController {

    @FXML
    public Button buttonSelected1;

    @FXML
    public Button buttonSelected2;

    @FXML
    public Button buttonSelected3;

    @FXML
    public Button buttonGod1;

    @FXML
    public Button buttonGod2;

    @FXML
    public Button buttonGod3;

    @FXML
    public Button buttonName1;

    @FXML
    public Button buttonName2;

    @FXML
    public Button buttonName3;

    @FXML
    public Button buttonDescription;

    @FXML
    public Button buttonTimer;

    @FXML
    public Button banner;

    /**
     * Choose size of the Image for Towers Floors
     */
    int towerSize = 80;

    /**
     * Choose size of the Image for Pawns
     */
    int pawnSize = 40;

    private boolean showTimer;

    /**
     * state of the buttons to change the ClientMessage generated once clicked
     * 0 = Worker Initialize
     * 1 = Worker Choose
     * 2 = Move
     * 3 = Build
     * 4 = No Action
     */
    private int state;
    private boolean questionFlag;
    private boolean loserFlag;

    CurrentStatusServer Css;
    CheckMoveServer Cms;
    CheckBuildServer Cbs;
    QuestionAbilityServer Qas;

    public void setCss(CurrentStatusServer css) {
        Css = css;
    }
    public void setCms(CheckMoveServer cms) {
        Cms = cms;
    }
    public void setCbs(CheckBuildServer cbs) {
        Cbs = cbs;
    }
    public void setQas(QuestionAbilityServer qas) { Qas = qas; }

    Image floor1 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png", towerSize, towerSize, true, false);
    Image floor2 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor2.png", towerSize, towerSize, true, false);
    Image floor3 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor3.png", towerSize, towerSize, true, false);
    Image dome = new Image("/it.polimi.ingsw/view/gui/img/tower/dome.png", towerSize, towerSize, true, false);


    @FXML
    Button yes,no;

    @FXML
    Button cell00, cell01, cell02, cell03, cell04,
            cell10, cell11, cell12, cell13, cell14,
            cell20, cell21, cell22, cell23, cell24,
            cell30, cell31, cell32, cell33, cell34,
            cell40, cell41, cell42, cell43, cell44;

    @FXML
    ImageView block00, block01, block02, block03, block04,
            block10, block11, block12, block13, block14,
            block20, block21, block22, block23, block24,
            block30, block31, block32, block33, block34,
            block40, block41, block42, block43, block44;

    @FXML
    ImageView square00, square01, square02, square03, square04,
            square10, square11, square12, square13, square14,
            square20, square21, square22, square23, square24,
            square30, square31, square32, square33, square34,
            square40, square41, square42, square43, square44;


    /**
     * Set the Scene and buttons dimension
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_match.png"));

        double w = 120;
        buttonSelected1.setPrefSize(w,60);
        buttonSelected2.setPrefSize(w,60);
        buttonSelected3.setPrefSize(w,60);

        double h = 130;
        w = 130;
        buttonGod1.setMinSize(w,h);
        buttonGod1.setMaxSize(w,h);
        buttonGod1.setPrefSize(w,h);
        buttonGod2.setMinSize(w,h);
        buttonGod2.setMaxSize(w,h);
        buttonGod2.setPrefSize(w,h);
        buttonGod3.setMinSize(w,h);
        buttonGod3.setMaxSize(w,h);
        buttonGod3.setPrefSize(w,h);

        h = 70;
        w = 120;
        buttonName3.setMinSize(w,h);
        buttonName3.setMaxSize(w,h);
        buttonName3.setPrefSize(w,h);
        buttonName1.setMinSize(w,h);
        buttonName1.setMaxSize(w,h);
        buttonName1.setPrefSize(w,h);
        buttonName2.setMinSize(w,h);
        buttonName2.setMaxSize(w,h);
        buttonName2.setPrefSize(w,h);

        buttonDescription.setMinSize(375,200);
        buttonDescription.setMaxSize(375,200);
        buttonDescription.setPrefSize(375,200);

        h = 50;
        w = 80;
        buttonTimer.setMinSize(w,h);
        buttonTimer.setMaxSize(w,h);
        buttonTimer.setPrefSize(w,h);

        showTimer = true;

        banner.setMinSize(800,60);
        banner.setMaxSize(800,60);
        banner.setPrefSize(800,60);
        banner.setText("");
    }


    /**
     * Initialize every object on the board
     */
    @Override
    public void setup(){
        banner.setLayoutY(bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX((gui.sceneWidth/2)-400);

        int start = 550;
        buttonGod1.getStyleClass().clear();
        buttonGod2.getStyleClass().clear();
        buttonGod3.getStyleClass().clear();

        buttonName1.setLayoutY(10);
        buttonName2.setLayoutY(10);
        buttonName3.setLayoutY(10);

        buttonDescription.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonDescription.getPrefWidth()/2);
        buttonDescription.setLayoutY(gui.sceneHeight-bottom.getPrefHeight()-top.getPrefHeight()-buttonDescription.getPrefHeight()-10);
        buttonTimer.setLayoutX(50);
        buttonTimer.setLayoutY(bottom.getPrefHeight()/2-buttonTimer.getPrefHeight()/2);

        setDescription("");

        if(gui.getClient().getPlayers().size() == 3){
            int offset = 30;
                buttonGod1.getStyleClass().addAll("button",gui.getClient().getPlayers().get(0).card.toString().toLowerCase());
                buttonName1.setText(gui.getClient().getPlayers().get(0).name + "\n" + gui.getClient().getPlayers().get(0).card.toString());
                buttonGod2.getStyleClass().addAll("button",gui.getClient().getPlayers().get(1).card.toString().toLowerCase());
                buttonName2.setText(gui.getClient().getPlayers().get(1).name + "\n" + gui.getClient().getPlayers().get(1).card);
                buttonGod3.getStyleClass().addAll("button",gui.getClient().getPlayers().get(2).card.toString().toLowerCase());
                buttonName3.setText(gui.getClient().getPlayers().get(2).name + "\n" + gui.getClient().getPlayers().get(2).card.toString());

                if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(0).name))
                    buttonSelected1.getStyleClass().addAll("button","podiumGold");
                if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(1).name))
                     buttonSelected2.getStyleClass().addAll("button","podiumGold");
                if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(2).name))
                    buttonSelected3.getStyleClass().addAll("button","podiumGold");




            buttonName1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonName1.getPrefWidth()/2-offset);
            buttonName2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonName2.getPrefWidth()/2);
            buttonName3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonName3.getPrefWidth()/2+offset);

            buttonSelected1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonSelected1.getPrefWidth()/2-offset);
            buttonSelected2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonSelected2.getPrefWidth()/2);
            buttonSelected3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonSelected3.getPrefWidth()/2+offset);

            buttonGod1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonGod1.getPrefWidth()/2-offset);
            buttonGod2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonGod2.getPrefWidth()/2);
            buttonGod3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonGod3.getPrefWidth()/2+offset);

            buttonName2.getStyleClass().add(3,"nameSelected");
        } else if(gui.getClient().getPlayers().size() == 2){
            int offset = 30;

            buttonGod2.getStyleClass().addAll("button",gui.getClient().getPlayers().get(1).card.toString().toLowerCase());
            buttonName2.setText(gui.getClient().getPlayers().get(1).name + "\n" + gui.getClient().getPlayers().get(1).card.toString());

            buttonGod1.getStyleClass().addAll("button",gui.getClient().getPlayers().get(0).card.toString().toLowerCase());
            buttonName1.setText(gui.getClient().getPlayers().get(0).name + "\n" + gui.getClient().getPlayers().get(0).card);


            if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(0).name))
                buttonSelected1.getStyleClass().addAll("button","podiumGold");
            if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(1).name))
                buttonSelected2.getStyleClass().addAll("button","podiumGold");

            buttonName1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonName1.getPrefWidth()/2-offset);
            buttonName2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonName2.getPrefWidth()/2+offset);

            buttonSelected1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonSelected1.getPrefWidth()/2-offset);
            buttonSelected2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonSelected1.getPrefWidth()/2+offset);

            buttonGod1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonGod1.getPrefWidth()/2-offset);
            buttonGod2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonGod2.getPrefWidth()/2+offset);
            buttonGod3.setVisible(false); buttonName3.setVisible(false); buttonSelected3.setVisible(false);

            buttonName1.getStyleClass().add(3,"nameSelected");
        }

        buttonSelected1.setLayoutY(210);
        buttonSelected2.setLayoutY(210);
        buttonSelected3.setLayoutY(210);
        buttonGod1.setLayoutY(100);
        buttonGod2.setLayoutY(100);
        buttonGod3.setLayoutY(100);

        cell00.getStyleClass().add("board");
        cell01.getStyleClass().add("board");
        cell02.getStyleClass().add("board");
        cell03.getStyleClass().add("board");
        cell04.getStyleClass().add("board");
        cell10.getStyleClass().add("board");
        cell11.getStyleClass().add("board");
        cell12.getStyleClass().add("board");
        cell13.getStyleClass().add("board");
        cell14.getStyleClass().add("board");
        cell20.getStyleClass().add("board");
        cell21.getStyleClass().add("board");
        cell22.getStyleClass().add("board");
        cell23.getStyleClass().add("board");
        cell24.getStyleClass().add("board");
        cell30.getStyleClass().add("board");
        cell31.getStyleClass().add("board");
        cell32.getStyleClass().add("board");
        cell33.getStyleClass().add("board");
        cell34.getStyleClass().add("board");
        cell40.getStyleClass().add("board");
        cell41.getStyleClass().add("board");
        cell42.getStyleClass().add("board");
        cell43.getStyleClass().add("board");
        cell44.getStyleClass().add("board");
        yes.getStyleClass().add("cl");
        no.getStyleClass().add("cl");
        //(banner);
        setDescription(gui.getClient().getMyPlayer().card.name());
        state = 4;
        questionFlag = false;
        loserFlag = false;
    }

    public boolean getShowTimer(){ return showTimer; }

    /**
     * Show the god1 description on the board and highlight his button once clicked
     * @param actionEvent
     */
    public void god1OnAction(ActionEvent actionEvent) {
        setDescription(buttonName1.getText().split("\n")[1]);

        while(buttonName3.getStyleClass().size() > 3)
            buttonName3.getStyleClass().remove(3);
        while(buttonName2.getStyleClass().size() > 3)
            buttonName2.getStyleClass().remove(3);

        buttonName1.getStyleClass().add(3,"nameSelected");
    }


    /**
     * Show the god2 description on the board and highlight his button once clicked
     * @param actionEvent
     */
    public void god2OnAction(ActionEvent actionEvent) {
        setDescription(buttonName2.getText().split("\n")[1]);

        while(buttonName1.getStyleClass().size() > 3)
            buttonName1.getStyleClass().remove(3);
        while(buttonName3.getStyleClass().size() > 3)
            buttonName3.getStyleClass().remove(3);

        buttonName2.getStyleClass().add(3,"nameSelected");
    }


    /**
     * Show the god3 description on the board and highlight his button once clicked
     * @param actionEvent
     */
    public void god3OnAction(ActionEvent actionEvent) {
        setDescription(buttonName3.getText().split("\n")[1]);

        while(buttonName1.getStyleClass().size() > 3)
            buttonName1.getStyleClass().remove(3);
        while(buttonName2.getStyleClass().size() > 3)
            buttonName2.getStyleClass().remove(3);

        buttonName3.getStyleClass().add(3,"nameSelected");
    }


    /**
     *Setter for the Card Description on the board
     * @param name the name of the player
     */
    public void setDescription(String name){
        Platform.runLater(() -> {
            try{
                String[] splitted = new String[0];
                for(SnapPlayer p : gui.getClient().getPlayers())
                    if(p.card.toString().equals(name))
                        splitted = p.card.getDescription().split(" ");
                String desc = "";
                int cc = 0;
                for(int i=0; i<splitted.length; i++){
                    cc += splitted[i].length();
                    desc += splitted[i];
                    if(cc > 30){
                        cc = 0;
                        desc += (i+1 == splitted.length ? "" : "\n");
                    } else desc += " ";
                }
                buttonDescription.setText(desc);
            } catch (Exception ex){
                buttonDescription.setText("");
            }
        });
    }


    /**
     *Getter for a floor image of a Tower
     * @param lvl the floor of the tower that you want
     * @return the image of the equivalent floor
     */
    public Image getConstruction(int lvl){
        if(lvl == 1)
            return floor1;
        if(lvl == 2)
            return floor2;
        if(lvl == 3)
            return floor3;
        if(lvl == 4)
            return dome;
        return null;
    }


    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the button (On Stackpane for buttons) with coordinate(x,y)
     */
    public Button getCell(int x, int y){
        if(x == 0 && y == 0)
            return cell00;
        if(x == 0 && y == 1)
            return cell01;
        if(x == 0 && y == 2)
            return cell02;
        if(x == 0 && y == 3)
            return cell03;
        if(x == 0 && y == 4)
            return cell04;
        if(x == 1 && y == 0)
            return cell10;
        if(x == 1 && y == 1)
            return cell11;
        if(x == 1 && y == 2)
            return cell12;
        if(x == 1 && y == 3)
            return cell13;
        if(x == 1 && y == 4)
            return cell14;
        if(x == 2 && y == 0)
            return cell20;
        if(x == 2 && y == 1)
            return cell21;
        if(x == 2 && y == 2)
            return cell22;
        if(x == 2 && y == 3)
            return cell23;
        if(x == 2 && y == 4)
            return cell24;
        if(x == 3 && y == 0)
            return cell30;
        if(x == 3 && y == 1)
            return cell31;
        if(x == 3 && y == 2)
            return cell32;
        if(x == 3 && y == 3)
            return cell33;
        if(x == 3 && y == 4)
            return cell34;
        if(x == 4 && y == 0)
            return cell40;
        if(x == 4 && y == 1)
            return cell41;
        if(x == 4 && y == 2)
            return cell42;
        if(x == 4 && y == 3)
            return cell43;
        if(x == 4 && y == 4)
            return cell44;
        return null;
    }

    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the sqaure (StackPane for Pawns ImageView) with coordinate(x,y)
     */
    public ImageView getSquare(int x, int y){
        if(x == 0 && y == 0)
            return square00;
        if(x == 0 && y == 1)
            return square01;
        if(x == 0 && y == 2)
            return square02;
        if(x == 0 && y == 3)
            return square03;
        if(x == 0 && y == 4)
            return square04;
        if(x == 1 && y == 0)
            return square10;
        if(x == 1 && y == 1)
            return square11;
        if(x == 1 && y == 2)
            return square12;
        if(x == 1 && y == 3)
            return square13;
        if(x == 1 && y == 4)
            return square14;
        if(x == 2 && y == 0)
            return square20;
        if(x == 2 && y == 1)
            return square21;
        if(x == 2 && y == 2)
            return square22;
        if(x == 2 && y == 3)
            return square23;
        if(x == 2 && y == 4)
            return square24;
        if(x == 3 && y == 0)
            return square30;
        if(x == 3 && y == 1)
            return square31;
        if(x == 3 && y == 2)
            return square32;
        if(x == 3 && y == 3)
            return square33;
        if(x == 3 && y == 4)
            return square34;
        if(x == 4 && y == 0)
            return square40;
        if(x == 4 && y == 1)
            return square41;
        if(x == 4 && y == 2)
            return square42;
        if(x == 4 && y == 3)
            return square43;
        if(x == 4 && y == 4)
            return square44;
        return null;
    }

    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the sqaure (StackPane With Tower's ImageView) with coordinate(x,y)
     */
     public ImageView getBLock(int x, int y){
        if(x == 0 && y == 0)
            return block00;
        if(x == 0 && y == 1)
            return block01;
        if(x == 0 && y == 2)
            return block02;
        if(x == 0 && y == 3)
            return block03;
        if(x == 0 && y == 4)
            return block04;
        if(x == 1 && y == 0)
            return block10;
        if(x == 1 && y == 1)
            return block11;
        if(x == 1 && y == 2)
            return block12;
        if(x == 1 && y == 3)
            return block13;
        if(x == 1 && y == 4)
            return block14;
        if(x == 2 && y == 0)
            return block20;
        if(x == 2 && y == 1)
            return block21;
        if(x == 2 && y == 2)
            return block22;
        if(x == 2 && y == 3)
            return block23;
        if(x == 2 && y == 4)
            return block24;
        if(x == 3 && y == 0)
            return block30;
        if(x == 3 && y == 1)
            return block31;
        if(x == 3 && y == 2)
            return block32;
        if(x == 3 && y == 3)
            return block33;
        if(x == 3 && y == 4)
            return block34;
        if(x == 4 && y == 0)
            return block40;
        if(x == 4 && y == 1)
            return block41;
        if(x == 4 && y == 2)
            return block42;
        if(x == 4 && y == 3)
            return block43;
        if(x == 4 && y == 4)
            return block44;
        return null;
    }

    /**
     * refresh the scene, for towers,buttons and pawns update
     */
    public void refresh() {

        for (SnapCell cell : this.gui.getClient().getBoard()) {
            if(cell.level != 0) {
                setLevel(getBLock(cell.row, cell.column), getConstruction(cell.level));
            }
        }
        //clean old workers position
        for (SnapCell cell : this.gui.getClient().getBoard()) {
                getSquare(cell.row, cell.column).setImage(null);
        }
        //get new workers position
        for(SnapPlayer p : this.gui.getClient().getPlayers())
            for (SnapWorker w : this.gui.getClient().getWorkers()){
                if(w.name != null && w.name.equals(p.name)) {
                    Image p2 = new Image(p.color, pawnSize, pawnSize, false, false);
                    getSquare(w.row,w.column).setImage(p2);
                }
            }
    }

    /**
     * Setter for set the image of the floor to a tower
     * @param block Imageview for load towers Image
     * @param floor Images of towers floors
     */
    public void setLevel(ImageView block, Image floor) {
        block.setImage(floor);
    }


    /**
     * Setter fot set the image of the player's pawn into their square Imageview
     * @param square Imageview for load towers Image
     */
    public void setPawn(ImageView square) {
        Image p1 = new Image(this.gui.getClient().getMyPlayer().color, pawnSize,pawnSize, false,false);
                square.setImage(p1);
    }

    /**
     * set the state of the buttons to change sendMessage generated once clicked
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }


    /**
     * Send a WorkerInitializeClient message if the cell is available
     * @param x row coordinate
     * @param y column coordinate
     * @return true if the cell is available, false if the cell is occupied
     */
    public boolean WorkerInitialize(int x, int y) {
         for (SnapWorker sw : this.gui.getClient().getWorkers()) {
             if (sw.row == x && sw.column == y) {
                 return false;
             }
         }
         this.gui.getClient().sendMessage(new WorkerInitializeClient(this.gui.getClient().getUsername(), x, y));
         return true;
    }


    /**
     * Send a WorkerChosenClient message if the cell belongs to the current player
     * @param x row of the chosen worker coordinate
     * @param y column of the chosen worker coordinate
     * @return true if the cell belongs to the current workers, false instead
     */
    public boolean ChoseWorker(int x, int y) {
        for (SnapWorker sw : this.gui.getClient().getWorkers()) {
            if (sw.row == x && sw.column == y && sw.name.equals(this.gui.getClient().getUsername()) && (Css.worker1 && sw.n == 1 || Css.worker2 && sw.n == 2)) {
                this.gui.getClient().sendMessage(new WorkerChoseClient(this.gui.getClient().getUsername(), sw.n));
                return true;
            }
        }
        return false;
    }


    /**
     * Send a MoveClient message if the cell it's available
     * @param x row of the chosen cell
     * @param y column of the chosen cell
     * @return true if the cell is available, false instead
     */
    public boolean Move(int x, int y) {
        for (SnapCell cell : Cms.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new MoveClient(this.gui.getClient().getUsername(), x, y));
                    for (SnapCell cellx : Cms.sc) {
                        lightItDown(getCell(cellx.row,cellx.column));
                    }
                    return true;
            }
        }
        return false;
    }


    /**
     * Send a BuiltClient message if the cell it's available
     * @param x row of the chosen cell
     * @param y column of the chosen cell
     * @return true if the cell is available, false instead
     */
    public boolean Build(int x, int y) {
        for (SnapCell cell : Cbs.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new BuildClient(this.gui.getClient().getUsername(), x, y));
                    for (SnapCell cellx : Cbs.sc) {
                        lightItDown(getCell(cellx.row,cellx.column));
                    }
                    return true;
            }
        }
        return false;
    }


    /**
     * Get the level from the model of the equivalent block and update the floor image
     * @param x row of the block coordinate
     * @param y row of the block coordinate
     */
    public void setFloor(int x,int y) {
        for (SnapCell cell : this.gui.getClient().getBoard()) {
            if (cell.row == x && cell.column == y) {
                if (cell.level == 1) {
                    getBLock(x, y).setImage(floor1);
                }
                if (cell.level == 2) {
                    getBLock(x, y).setImage(floor2);
                }
                if (cell.level == 3) {
                    getBLock(x, y).setImage(floor3);
                }
                if (cell.level == 4) {
                    getBLock(x, y).setImage(dome);
                }
            }

        }
    }

    /**
     * Change the Css to set the yellow color of the active cells (buttons) after a getCheckMove or getCheckBuild
     * @param Cell the cell of the button
     */
    public void lightItUp(Button Cell){
        Cell.getStyleClass().remove("board");
        Cell.getStyleClass().add("boardL");
    }

    /**
     * Change the Css to remove the yellow color of the active cells (buttons) after a getCheckMove or getCheckBuild
     * @param Cell the cell of the button
     */
    public void lightItDown(Button Cell){
        Cell.getStyleClass().remove("boardL");
        Cell.getStyleClass().add("board");
    }

    /**
     * Change the Css to make visible the YES button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void yOn(Button Cell){
        Cell.getStyleClass().remove("cl");
        Cell.getStyleClass().add("yes");
    }

    /**
     * Change the Css to make invisible the YES button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void yOff(Button Cell){
        Cell.getStyleClass().remove("yes");
        Cell.getStyleClass().add("cl");
    }

    /**
     * Change the Css to make visible the NO button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void nOn(Button Cell){
        Cell.getStyleClass().remove("cl");
        Cell.getStyleClass().add("no");
    }

    /**
     * Change the Css to make invisible the NO button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void nOff(Button Cell){
        Cell.getStyleClass().remove("no");
        Cell.getStyleClass().add("cl");
    }


    /**
     * Change the Css to make visible the NewGame button after an SomeoneLose message
     * @param Cell the cell of the button
     */
    public void newGameBOn(Button Cell){
        Cell.getStyleClass().remove("empty");
        Cell.getStyleClass().add("newgame");
    }


    /**
     * Change the Css to make invisible the NewGame button after an SomeoneLose message
     * @param Cell the cell of the button
     */
    public void newGameBOff(Button Cell){
        Cell.getStyleClass().remove("newgame");
        Cell.getStyleClass().add("empty");
    }


    /**
     * Set the yellow color to the available cell get from the CheckMoveServer message
     * @param sc the current CheckMoveServer message
     */
    public void showCheckMove(CheckMoveServer sc) {
        int x, y;
        if (this.gui.getClient().getUsername().equals(this.gui.getClient().getCurrentPlayer())) {
            for (SnapCell cell : Cms.sc) {
                x = cell.row;
                y = cell.column;
                    if(x == 0 && y == 0)
                        lightItUp(cell00);
                    if(x == 0 && y == 1)
                        lightItUp(cell01);
                    if(x == 0 && y == 2)
                        lightItUp(cell02);
                    if(x == 0 && y == 3)
                        lightItUp(cell03);
                    if(x == 0 && y == 4)
                        lightItUp(cell04);
                    if(x == 1 && y == 0)
                        lightItUp(cell10);
                    if(x == 1 && y == 1)
                        lightItUp(cell11);
                    if(x == 1 && y == 2)
                        lightItUp(cell12);
                    if(x == 1 && y == 3)
                        lightItUp(cell13);
                    if(x == 1 && y == 4)
                        lightItUp(cell14);
                    if(x == 2 && y == 0)
                        lightItUp(cell20);
                    if(x == 2 && y == 1)
                        lightItUp(cell21);
                    if(x == 2 && y == 2)
                        lightItUp(cell22);
                    if(x == 2 && y == 3)
                        lightItUp(cell23);
                    if(x == 2 && y == 4)
                        lightItUp(cell24);
                    if(x == 3 && y == 0)
                        lightItUp(cell30);
                    if(x == 3 && y == 1)
                        lightItUp(cell31);
                    if(x == 3 && y == 2)
                        lightItUp(cell32);
                    if(x == 3 && y == 3)
                        lightItUp(cell33);
                    if(x == 3 && y == 4)
                        lightItUp(cell34);
                    if(x == 4 && y == 0)
                        lightItUp(cell40);
                    if(x == 4 && y == 1)
                        lightItUp(cell41);
                    if(x == 4 && y == 2)
                        lightItUp(cell42);
                    if(x == 4 && y == 3)
                        lightItUp(cell43);
                    if(x == 4 && y == 4)
                        lightItUp(cell44);
                    }
           }


    }


    /**
     * Set the yellow color to the available cell get from the CheckBuildServer message
     * @param sb the current CheckBuildServer message
     */
    public void showCheckBuild(CheckBuildServer sb) {
        int x, y;
        if (this.gui.getClient().getUsername().equals(this.gui.getClient().getCurrentPlayer())) {
            for (SnapCell cell : Cbs.sc) {
                x = cell.row;
                y = cell.column;
                    if(x == 0 && y == 0)
                        lightItUp(cell00);
                    if(x == 0 && y == 1)
                        lightItUp(cell01);
                    if(x == 0 && y == 2)
                        lightItUp(cell02);
                    if(x == 0 && y == 3)
                        lightItUp(cell03);
                    if(x == 0 && y == 4)
                        lightItUp(cell04);
                    if(x == 1 && y == 0)
                        lightItUp(cell10);
                    if(x == 1 && y == 1)
                        lightItUp(cell11);
                    if(x == 1 && y == 2)
                        lightItUp(cell12);
                    if(x == 1 && y == 3)
                        lightItUp(cell13);
                    if(x == 1 && y == 4)
                        lightItUp(cell14);
                    if(x == 2 && y == 0)
                        lightItUp(cell20);
                    if(x == 2 && y == 1)
                        lightItUp(cell21);
                    if(x == 2 && y == 2)
                        lightItUp(cell22);
                    if(x == 2 && y == 3)
                        lightItUp(cell23);
                    if(x == 2 && y == 4)
                        lightItUp(cell24);
                    if(x == 3 && y == 0)
                        lightItUp(cell30);
                    if(x == 3 && y == 1)
                        lightItUp(cell31);
                    if(x == 3 && y == 2)
                        lightItUp(cell32);
                    if(x == 3 && y == 3)
                        lightItUp(cell33);
                    if(x == 3 && y == 4)
                        lightItUp(cell34);
                    if(x == 4 && y == 0)
                        lightItUp(cell40);
                    if(x == 4 && y == 1)
                        lightItUp(cell41);
                    if(x == 4 && y == 2)
                        lightItUp(cell42);
                    if(x == 4 && y == 3)
                        lightItUp(cell43);
                    if(x == 4 && y == 4)
                        lightItUp(cell44);
                    }
            }

    }

    /**
     * Shows the NewGame and Exit button in Loser banner
     */
    public void loserbanner(){
        banner.setText("Thanks for playing Santorini");
        newGameBOn(no);
        loserFlag = true;
    }


    /**
     * Shows the YES and NO buttons and change the banner message
     */
    public void question(){
        yOn(yes);
        nOn(no);
        Platform.runLater(() -> {
            //400
            banner.setLayoutX((buttonTimer.getLayoutX()+buttonTimer.getPrefWidth()+yes.getLayoutX())/2-400);
            banner.setText("Do you want to use you God ability?");
        });
        questionFlag = true;
    }

    public void setQuestionFlag(boolean questionFlag) {
        this.questionFlag = questionFlag;
    }

    public boolean getQuestionFlag() {
        return questionFlag;
    }

    public void setLoserFlag(boolean loserFlag) {
        this.loserFlag = loserFlag;
    }

    public boolean isQuestionFlag() {
        return questionFlag;
    }

    public boolean isLoserFlag() {
        return loserFlag;
    }


    /**
     * If the button YES is visible, send a true AnswerAbilityClient when it's clicked
     * and makes the buttons YES and NO invisible and inactive after it
     * @param actionEvent
     */
    public void yes(ActionEvent actionEvent) {
        if(questionFlag) {
            this.gui.getClient().sendMessage(new AnswerAbilityClient(this.gui.getClient().getUsername(), true, Qas.status));
            yOff(yes);
            nOff(no);
            questionFlag = false;
            banner.setLayoutX((gui.sceneWidth/2)-400);
        }
    }


    /**
     * If the button NO is visible, send a false AnswerAbilityClient when it's clicked
     * and makes the buttons YES and NO invisible and inactive after it
     * @param actionEvent
     */
    public void no(ActionEvent actionEvent) {
        if(questionFlag) {
            this.gui.getClient().sendMessage(new AnswerAbilityClient(this.gui.getClient().getUsername(), false, Qas.status));
            yOff(yes);
            nOff(no);
            questionFlag = false;
            banner.setLayoutX((gui.sceneWidth/2)-400);
        }
        if(loserFlag){
            this.gui.getClient().resetMatch();
            Platform.runLater(() -> {
                this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
                this.gui.getPrimaryStage().show();
            });
            loserFlag = false;
            newGameBOff(no);
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell00(ActionEvent actionEvent) {
       int x = 0;
       int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square00);
            setState(4);
            }
        }

        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                block00.setImage(new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png"));
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell01(ActionEvent actionEvent) {
        int x = 0;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square01);
            setState(4);
            }
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){//Move
            if(Move(x, y)){
                setState(4);
            }
        }
        if(state == 3) {  //Build
            if (Build(x, y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell02(ActionEvent actionEvent) {
        int x = 0;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square02);
            setState(4); }
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell03(ActionEvent actionEvent) {
        int x = 0;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square03);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell04(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square04);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell10(ActionEvent actionEvent) {
        int x = 1;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square10);
            setState(4);}
        }

        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}

        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell11(ActionEvent actionEvent) {
        int x = 1;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square11);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell12(ActionEvent actionEvent) {
        int x = 1;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square12);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell13(ActionEvent actionEvent) {
        int x = 1;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square13);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell14(ActionEvent actionEvent) {
        int x = 1;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square14);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell20(ActionEvent actionEvent) {
        int x = 2;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square20);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell21(ActionEvent actionEvent) {
        int x = 2;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square21);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell22(ActionEvent actionEvent) {
        int x = 2;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square22);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell23(ActionEvent actionEvent) {
        int x = 2;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square23);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell24(ActionEvent actionEvent) {
        int x = 2;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square24);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell30(ActionEvent actionEvent) {
        int x = 3;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square30);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell31(ActionEvent actionEvent) {
        int x = 3;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square31);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell32(ActionEvent actionEvent) {
        int x = 3;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square32);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell33(ActionEvent actionEvent) {
        int x = 3;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square33);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell34(ActionEvent actionEvent) {
        int x = 3;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square34);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell40(ActionEvent actionEvent) {
        int x = 4;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square40);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell41(ActionEvent actionEvent) {
        int x = 4;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square41);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell42(ActionEvent actionEvent) {
        int x = 4;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square42);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell43(ActionEvent actionEvent) {
        int x = 4;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square43);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * handler for cell events, change the action of the respective cell
     * depending on the parameter state
     * @param actionEvent
     */
    public void cell44(ActionEvent actionEvent) {
        int x = 4;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square44);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    /**
     * Make possible to answer a request of an QuestionAbilityServer by Keyboard
     * Y to YES, n to NO
     * @param code
     */
    public void activeQuestionIfPossible(KeyCode code) {
        if(questionFlag) {
            this.gui.getClient().sendMessage(new AnswerAbilityClient(this.gui.getClient().getUsername(), code == KeyCode.Y, Qas.status));
            yOff(yes);
            nOff(no);
            questionFlag = false;
        }
    }


    /**
     * Unshows the timer
     */
    public void unShowTimer() {
        showTimer = false;
    }


    /**
     * Shows the Timer
     */
    public void showTimer() {
        showTimer = true;
        buttonTimer.setVisible(true);
    }
}